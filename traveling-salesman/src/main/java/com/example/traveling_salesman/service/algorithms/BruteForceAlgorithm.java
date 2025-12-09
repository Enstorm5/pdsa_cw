package com.example.traveling_salesman.service.algorithms;

import com.example.traveling_salesman.model.City;
import com.example.traveling_salesman.model.DistanceMatrix;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BruteForceAlgorithm implements TspAlgorithm {

	@Override
	public String name() {
		return "BRUTE_FORCE";
	}

	@Override
	public TspSolution solve(City homeCity, List<City> citiesToVisit, DistanceMatrix matrix) {
		if (homeCity == null) {
			throw new IllegalArgumentException("homeCity must not be null");
		}
		if (matrix == null) {
			throw new IllegalArgumentException("matrix must not be null");
		}
		List<City> remaining = citiesToVisit == null ? new ArrayList<>() : new ArrayList<>(citiesToVisit);
		if (remaining.isEmpty()) {
			return new TspSolution(List.of(homeCity, homeCity), 0);
		}

		List<City>[] bestRoute = new List[] {null};
		int[] bestDistance = new int[] {Integer.MAX_VALUE};

		permute(remaining, 0, matrix, homeCity, (route, distance) -> {
			if (distance < bestDistance[0]) {
				bestDistance[0] = distance;
				bestRoute[0] = new ArrayList<>(route);
			}
		});

		List<City> path = new ArrayList<>();
		path.add(homeCity);
		if (bestRoute[0] != null) {
			path.addAll(bestRoute[0]);
		}
		path.add(homeCity);
		return new TspSolution(path, bestDistance[0]);
	}

	private void permute(List<City> cities, int index, DistanceMatrix matrix, City homeCity, RouteConsumer consumer) {
		if (index == cities.size()) {
			List<City> snapshot = List.copyOf(cities);
			int distance = routeDistance(homeCity, snapshot, matrix);
			consumer.accept(snapshot, distance);
			return;
		}
		for (int i = index; i < cities.size(); i++) {
			Collections.swap(cities, index, i);
			permute(cities, index + 1, matrix, homeCity, consumer);
			Collections.swap(cities, index, i);
		}
	}

	private int routeDistance(City homeCity, List<City> route, DistanceMatrix matrix) {
		int total = 0;
		City current = homeCity;
		for (City next : route) {
			total += matrix.getDistance(current, next);
			current = next;
		}
		total += matrix.getDistance(current, homeCity);
		return total;
	}

	@FunctionalInterface
	private interface RouteConsumer {
		void accept(List<City> route, int distance);
	}
}
