package com.example.traveling_salesman.service;

import com.example.traveling_salesman.dto.AlgorithmEvaluationResponse;
import com.example.traveling_salesman.dto.AlgorithmResultDto;
import com.example.traveling_salesman.dto.GameResponse;
import com.example.traveling_salesman.dto.GameRoundResponse;
import com.example.traveling_salesman.dto.SelectCitiesRequest;
import com.example.traveling_salesman.dto.SolveAttemptRequest;
import com.example.traveling_salesman.dto.StartGameRequest;
import com.example.traveling_salesman.model.AlgorithmTimeLog;
import com.example.traveling_salesman.model.City;
import com.example.traveling_salesman.model.DistanceMatrix;
import com.example.traveling_salesman.model.GameResult;
import com.example.traveling_salesman.model.GameSession;
import com.example.traveling_salesman.repository.GameResultRepository;
import com.example.traveling_salesman.repository.GameSessionRepository;
import com.example.traveling_salesman.service.algorithms.TspAlgorithm;
import com.example.traveling_salesman.service.algorithms.TspSolution;
import com.example.traveling_salesman.support.DistanceMatrixGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

	private final DistanceMatrixGenerator distanceMatrixGenerator;
	private final GameSessionRepository gameSessionRepository;
	private final GameResultRepository gameResultRepository;
	private final List<TspAlgorithm> algorithms;
	private final ObjectMapper objectMapper;

	public GameService(
			DistanceMatrixGenerator distanceMatrixGenerator,
			GameSessionRepository gameSessionRepository,
			GameResultRepository gameResultRepository,
			List<TspAlgorithm> algorithms,
			ObjectMapper objectMapper) {
		this.distanceMatrixGenerator = distanceMatrixGenerator;
		this.gameSessionRepository = gameSessionRepository;
		this.gameResultRepository = gameResultRepository;
		this.algorithms = algorithms;
		this.objectMapper = objectMapper;
	}

	@Transactional
	public GameRoundResponse startGame(StartGameRequest request) {
		DistanceMatrix matrix = distanceMatrixGenerator.generate();
		City homeCity = randomHomeCity();

		GameSession session = new GameSession();
		session.setPlayerName(request.getPlayerName());
		session.setHomeCity(homeCity);
		session.setDistanceMatrixJson(writeMatrix(matrix));

		gameSessionRepository.save(session);

		GameRoundResponse response = new GameRoundResponse();
		response.setSessionId(session.getId());
		response.setPlayerName(request.getPlayerName());
		response.setHomeCity(homeCity.name());
		response.setCityLabels(Arrays.stream(City.values()).map(Enum::name).toList());
		response.setDistanceMatrix(matrix.toArray());
		return response;
	}

	@Transactional
	public AlgorithmEvaluationResponse evaluateSelection(SelectCitiesRequest request) {
		if (algorithms.isEmpty()) {
			throw new IllegalStateException("No TSP algorithms registered");
		}
		GameSession session = gameSessionRepository
				.findById(request.getSessionId())
				.orElseThrow(() -> new IllegalArgumentException("Game session not found"));

		DistanceMatrix matrix = readMatrix(session.getDistanceMatrixJson());
		City homeCity = session.getHomeCity();
		List<City> visitCities = toCityList(request.getCities());
		visitCities.removeIf(city -> city == homeCity);

		List<AlgorithmResultDto> algorithmResults = new ArrayList<>();
		for (TspAlgorithm algorithm : algorithms) {
		    long start = System.nanoTime();
		    TspSolution solution = algorithm.solve(homeCity, visitCities, matrix);
		    long elapsedNs = System.nanoTime() - start;

		    BigDecimal elapsedMs = BigDecimal.valueOf(elapsedNs)
			    .divide(BigDecimal.valueOf(1_000_000L), 4, RoundingMode.HALF_UP);

		    session.addTimeLog(createTimeLog(algorithm.name(), elapsedMs));

		    algorithmResults.add(new AlgorithmResultDto(
			    algorithm.name(),
			    toCityStrings(solution.getOrderedPath()),
			    solution.getTotalDistance(),
			    elapsedMs));
		}

		gameSessionRepository.save(session);

		AlgorithmEvaluationResponse response = new AlgorithmEvaluationResponse();
		response.setSessionId(session.getId());
		response.setHomeCity(homeCity.name());
		response.setSelectedCities(toCityStrings(visitCities));
		response.setAlgorithmResults(algorithmResults);
		return response;
	}

	@Transactional
	public GameResponse submitAttempt(SolveAttemptRequest request) {
		GameSession session = gameSessionRepository
				.findById(request.getSessionId())
				.orElseThrow(() -> new IllegalArgumentException("Game session not found"));

		DistanceMatrix matrix = readMatrix(session.getDistanceMatrixJson());
		City homeCity = session.getHomeCity();

		List<City> submittedPath = toCityPath(request.getProposedPath());
		if (submittedPath.isEmpty()) {
			throw new IllegalArgumentException("Submitted path must include at least the home city");
		}

		int submittedDistance = calculateDistance(submittedPath, matrix);

		List<City> visitCities = deriveVisitCities(submittedPath, homeCity);
		TspSolution optimalSolution = resolveBestSolution(homeCity, visitCities, matrix);

		boolean correct = submittedDistance == optimalSolution.getTotalDistance()
				&& normalizePath(submittedPath).equals(normalizePath(optimalSolution.getOrderedPath()));

		if (correct) {
			persistSuccessfulAttempt(session, visitCities, optimalSolution, request.getTimeTakenByUserMs());
		}

		GameResponse response = new GameResponse();
		response.setSessionId(session.getId());
		response.setPlayerName(session.getPlayerName());
		response.setHomeCity(homeCity.name());
		response.setSubmittedDistance(submittedDistance);
		response.setOptimalDistance(optimalSolution.getTotalDistance());
		response.setOptimalPath(toCityStrings(optimalSolution.getOrderedPath()));
		response.setCorrect(correct);
		response.setMessage(correct ? "Correct route identified." : "Submitted route is not optimal.");
		return response;
	}

	private void persistSuccessfulAttempt(
			GameSession session, List<City> visitCities, TspSolution optimalSolution, long timeTakenMs) {
		GameResult result = new GameResult();
		session.addResult(result);
		result.setCitiesSelected(writeJson(toCityStrings(visitCities)));
		result.setCalculatedPath(writeJson(toCityStrings(optimalSolution.getOrderedPath())));
		result.setTotalDistance(optimalSolution.getTotalDistance());
		result.setTimeTakenByUserMs(timeTakenMs);
		gameResultRepository.save(result);
	}

	private AlgorithmTimeLog createTimeLog(String algorithmName, BigDecimal elapsedMs) {
		AlgorithmTimeLog timeLog = new AlgorithmTimeLog();
		timeLog.setAlgorithmName(algorithmName);
		timeLog.setTimeTakenMs(elapsedMs);
		return timeLog;
	}

	private TspSolution resolveBestSolution(City homeCity, List<City> visitCities, DistanceMatrix matrix) {
		return algorithms.stream()
				.map(algorithm -> algorithm.solve(homeCity, visitCities, matrix))
				.min((a, b) -> Integer.compare(a.getTotalDistance(), b.getTotalDistance()))
				.orElseThrow(() -> new IllegalStateException("No algorithms available"));
	}

	private DistanceMatrix readMatrix(String json) {
		try {
			int[][] values = objectMapper.readValue(json, int[][].class);
			return new DistanceMatrix(values);
		} catch (JsonProcessingException ex) {
			throw new IllegalStateException("Unable to read stored distance matrix", ex);
		}
	}

	private String writeMatrix(DistanceMatrix matrix) {
		return writeJson(matrix.toArray());
	}

	private String writeJson(Object value) {
		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException ex) {
			throw new IllegalStateException("Failed to serialize value", ex);
		}
	}

	private City randomHomeCity() {
		City[] cities = City.values();
		return cities[ThreadLocalRandom.current().nextInt(cities.length)];
	}

	private List<City> toCityList(List<String> names) {
		if (names == null) {
			return new ArrayList<>();
		}
		return names.stream()
				.filter(Objects::nonNull)
				.map(name -> City.valueOf(name.trim().toUpperCase(Locale.ROOT)))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private List<City> toCityPath(List<String> path) {
		if (path == null) {
			return new ArrayList<>();
		}
		return path.stream()
				.filter(Objects::nonNull)
				.map(name -> City.valueOf(name.trim().toUpperCase(Locale.ROOT)))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private List<String> toCityStrings(List<City> cities) {
		return cities.stream().map(Enum::name).toList();
	}

	private int calculateDistance(List<City> path, DistanceMatrix matrix) {
		if (path.size() < 2) {
			return 0;
		}
		int sum = 0;
		City previous = path.get(0);
		for (int i = 1; i < path.size(); i++) {
			City next = path.get(i);
			sum += matrix.getDistance(previous, next);
			previous = next;
		}
		return sum;
	}

	private List<City> deriveVisitCities(List<City> path, City homeCity) {
		if (path.isEmpty()) {
			return List.of();
		}
		int start = 0;
		int end = path.size();
		if (path.get(0) == homeCity) {
			start = 1;
		}
		if (!path.isEmpty() && path.get(path.size() - 1) == homeCity) {
			end = path.size() - 1;
		}
		Set<City> visitSet = new LinkedHashSet<>(path.subList(start, end));
		visitSet.remove(homeCity);
		return new ArrayList<>(visitSet);
	}

	private List<City> normalizePath(List<City> path) {
		return path.stream().filter(Objects::nonNull).toList();
	}
}
