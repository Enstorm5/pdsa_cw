package com.pdsa.towerofhanoi.service;

import com.pdsa.towerofhanoi.algorithm.*;
import com.pdsa.towerofhanoi.dto.*;
import com.pdsa.towerofhanoi.model.*;
import com.pdsa.towerofhanoi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TowerOfHanoiServiceImpl implements TowerOfHanoiService {
    
    private final GameRoundRepository gameRoundRepository;
    private final PlayerAnswerRepository playerAnswerRepository;
    private final AlgorithmPerformanceRepository algorithmPerformanceRepository;
    
    private final ThreePegRecursive threePegRecursive;
    private final ThreePegIterative threePegIterative;
    private final FourPegFrameStewart fourPegFrameStewart;
    private final FourPegOptimized fourPegOptimized;
    
    @Override
    @Transactional
    public GameStartResponse startNewGame(GameStartRequest request) {
        log.info("Starting new game with {} pegs", request.getNumberOfPegs());
        
        // Step 1: Randomly select number of disks between 5 and 10
        Random random = new Random();
        int numberOfDisks = 5 + random.nextInt(6); // 5 to 10 inclusive
        
        // ========== DEBUG LOGGING START ==========
        System.out.println("====================================");
        System.out.println("=== DEBUG: START NEW GAME ===");
        System.out.println("Generated numberOfDisks: " + numberOfDisks);
        System.out.println("Number of pegs: " + request.getNumberOfPegs());
        System.out.println("====================================");
        // ========== DEBUG LOGGING END ==========
        
        log.info("Randomly selected {} disks", numberOfDisks);
        
        // Step 2: Execute appropriate algorithms based on number of pegs
        List<AlgorithmExecutionResult> results;
        if (request.getNumberOfPegs() == 3) {
            
            System.out.println("Executing 3-peg algorithms for " + numberOfDisks + " disks");
            results = executeThreePegAlgorithms(numberOfDisks);
        } else {
            
            System.out.println("Executing 4-peg algorithms for " + numberOfDisks + " disks");
            results = executeFourPegAlgorithms(numberOfDisks);
        }
        
        // Step 3: Get the correct answer (use first algorithm result)
        AlgorithmExecutionResult correctResult = results.get(0);
        
        // ========== DEBUG LOGGING START ==========
        System.out.println("====================================");
        System.out.println("Algorithm 1 Result:");
        System.out.println("  Name: " + correctResult.getAlgorithmName());
        System.out.println("  Minimum Moves: " + correctResult.getMinimumMoves());
        System.out.println("  Actual Moves Generated: " + correctResult.getMoves().size());
        System.out.println("  Expected for " + numberOfDisks + " disks (3-peg): " + ((1 << numberOfDisks) - 1));
        if (results.size() > 1) {
            System.out.println("Algorithm 2 Result:");
            System.out.println("  Name: " + results.get(1).getAlgorithmName());
            System.out.println("  Minimum Moves: " + results.get(1).getMinimumMoves());
            System.out.println("  Actual Moves Generated: " + results.get(1).getMoves().size());
        }
        System.out.println("====================================");
        // ========== DEBUG LOGGING END ==========
        
        // Step 4: Save game round to database
        GameRound gameRound = new GameRound();
        gameRound.setNumberOfDisks(numberOfDisks);
        gameRound.setNumberOfPegs(request.getNumberOfPegs());
        gameRound.setCorrectMinimumMoves(correctResult.getMinimumMoves());
        gameRound.setCorrectMoveSequence(String.join(", ", correctResult.getMoves()));
        
        gameRound = gameRoundRepository.save(gameRound);
        
        // ========== DEBUG LOGGING ==========
        System.out.println("Saved to database:");
        System.out.println("  GameRound ID: " + gameRound.getId());
        System.out.println("  Stored numberOfDisks: " + gameRound.getNumberOfDisks());
        System.out.println("  Stored correctMinimumMoves: " + gameRound.getCorrectMinimumMoves());
        System.out.println("====================================");
        // ===================================
        
        log.info("Saved game round with ID: {}", gameRound.getId());
        
        // Step 5: Save algorithm performance data
        for (AlgorithmExecutionResult result : results) {
            AlgorithmPerformance performance = new AlgorithmPerformance();
            performance.setGameRoundId(gameRound.getId());
            performance.setAlgorithmName(result.getAlgorithmName());
            performance.setNumberOfPegs(request.getNumberOfPegs());
            performance.setExecutionTimeNanos(result.getExecutionTimeNanos());
            performance.setMinimumMoves(result.getMinimumMoves());
            
            algorithmPerformanceRepository.save(performance);
        }
        
        log.info("Saved performance data for {} algorithms", results.size());
        
        // Step 6: Build response
        return buildGameStartResponse(gameRound, results);
    }
    
    /**
     * Execute both 3-peg algorithms
     */
    private List<AlgorithmExecutionResult> executeThreePegAlgorithms(int numberOfDisks) {
        List<AlgorithmExecutionResult> results = new ArrayList<>();
        
        // ========== DEBUG LOGGING ==========
        System.out.println("executeThreePegAlgorithms called with numberOfDisks: " + numberOfDisks);
        // ===================================
        
        // Algorithm 1: Recursive
        AlgorithmExecutionResult result1 = executeAlgorithm(
            () -> threePegRecursive.solve(numberOfDisks),
            threePegRecursive.getMinimumMoves(numberOfDisks),
            threePegRecursive.getAlgorithmName()
        );
        results.add(result1);
        
        // ========== DEBUG LOGGING ==========
        System.out.println("3-Peg Recursive completed:");
        System.out.println("  getMinimumMoves(" + numberOfDisks + ") returned: " + threePegRecursive.getMinimumMoves(numberOfDisks));
        System.out.println("  solve(" + numberOfDisks + ") generated: " + result1.getMoves().size() + " moves");
        // ===================================
        
        // Algorithm 2: Iterative
        AlgorithmExecutionResult result2 = executeAlgorithm(
            () -> threePegIterative.solve(numberOfDisks),
            threePegIterative.getMinimumMoves(numberOfDisks),
            threePegIterative.getAlgorithmName()
        );
        results.add(result2);
        
        // ========== DEBUG LOGGING ==========
        System.out.println("3-Peg Iterative completed:");
        System.out.println("  getMinimumMoves(" + numberOfDisks + ") returned: " + threePegIterative.getMinimumMoves(numberOfDisks));
        System.out.println("  solve(" + numberOfDisks + ") generated: " + result2.getMoves().size() + " moves");
        // ===================================
        
        return results;
    }
    
    /**
     * Execute both 4-peg algorithms
     */
    private List<AlgorithmExecutionResult> executeFourPegAlgorithms(int numberOfDisks) {
        List<AlgorithmExecutionResult> results = new ArrayList<>();
        
        // ========== DEBUG LOGGING ==========
        System.out.println("executeFourPegAlgorithms called with numberOfDisks: " + numberOfDisks);
        // ===================================
        
        // Algorithm 1: Frame-Stewart
        AlgorithmExecutionResult result1 = executeAlgorithm(
            () -> fourPegFrameStewart.solve(numberOfDisks),
            fourPegFrameStewart.getMinimumMoves(numberOfDisks),
            fourPegFrameStewart.getAlgorithmName()
        );
        results.add(result1);
        
        // ========== DEBUG LOGGING ==========
        System.out.println("4-Peg Frame-Stewart completed:");
        System.out.println("  getMinimumMoves(" + numberOfDisks + ") returned: " + fourPegFrameStewart.getMinimumMoves(numberOfDisks));
        System.out.println("  solve(" + numberOfDisks + ") generated: " + result1.getMoves().size() + " moves");
        // ===================================
        
        // Algorithm 2: Optimized
        AlgorithmExecutionResult result2 = executeAlgorithm(
            () -> fourPegOptimized.solve(numberOfDisks),
            fourPegOptimized.getMinimumMoves(numberOfDisks),
            fourPegOptimized.getAlgorithmName()
        );
        results.add(result2);
        
        // ========== DEBUG LOGGING ==========
        System.out.println("4-Peg Optimized completed:");
        System.out.println("  getMinimumMoves(" + numberOfDisks + ") returned: " + fourPegOptimized.getMinimumMoves(numberOfDisks));
        System.out.println("  solve(" + numberOfDisks + ") generated: " + result2.getMoves().size() + " moves");
        // ===================================
        
        return results;
    }
    
    //Execute an algorithm and measure time
    private AlgorithmExecutionResult executeAlgorithm(
            AlgorithmExecutor executor, 
            int minimumMoves, 
            String algorithmName) {
        
        long startTime = System.nanoTime();
        List<String> moves = executor.execute();
        long endTime = System.nanoTime();
        
        long executionTime = endTime - startTime;
        
        log.info("{}: {} moves in {} ns", algorithmName, moves.size(), executionTime);
        
        return AlgorithmExecutionResult.builder()
            .algorithmName(algorithmName)
            .moves(moves)
            .minimumMoves(minimumMoves)
            .executionTimeNanos(executionTime)
            .build();
    }
    
    /**
     * Build response for game start
     */
    private GameStartResponse buildGameStartResponse(
            GameRound gameRound, 
            List<AlgorithmExecutionResult> results) {
        
        GameStartResponse.GameStartResponseBuilder responseBuilder = GameStartResponse.builder()
            .gameRoundId(gameRound.getId())
            .numberOfDisks(gameRound.getNumberOfDisks())
            .numberOfPegs(gameRound.getNumberOfPegs())
            .message("Game started! Try to solve the puzzle.");
        
        // Add algorithm results
        for (int i = 0; i < results.size(); i++) {
            AlgorithmExecutionResult result = results.get(i);
            GameStartResponse.AlgorithmResult algorithmResult = GameStartResponse.AlgorithmResult.builder()
                .algorithmName(result.getAlgorithmName())
                .minimumMoves(result.getMinimumMoves())
                .executionTimeNanos(result.getExecutionTimeNanos())
                .executionTimeMillis(result.getExecutionTimeNanos() / 1_000_000.0)
                .moveSequence(String.join(", ", result.getMoves()))
                .build();
            
            if (i == 0) responseBuilder.algorithm1Result(algorithmResult);
            else if (i == 1) responseBuilder.algorithm2Result(algorithmResult);
            else if (i == 2) responseBuilder.algorithm3Result(algorithmResult);
            else if (i == 3) responseBuilder.algorithm4Result(algorithmResult);
        }
        
        return responseBuilder.build();
    }
    
    @Override
    @Transactional
    public PlayerAnswerResponse submitAnswer(PlayerAnswerRequest request) {
        log.info("Submitting answer for game round {} by player {}", 
                 request.getGameRoundId(), request.getPlayerName());
        
        // Step 1: Get game round
        GameRound gameRound = gameRoundRepository.findById(request.getGameRoundId())
            .orElseThrow(() -> new com.pdsa.towerofhanoi.exception.GameNotFoundException(request.getGameRoundId()));
        
        // ========== DEBUG LOGGING ==========
        System.out.println("====================================");
        System.out.println("=== SUBMIT ANSWER DEBUG ===");
        System.out.println("Player: " + request.getPlayerName());
        System.out.println("Player's moves: " + request.getPlayerMinimumMoves());
        System.out.println("Correct moves (from DB): " + gameRound.getCorrectMinimumMoves());
        System.out.println("Number of disks (from DB): " + gameRound.getNumberOfDisks());
        System.out.println("Number of pegs (from DB): " + gameRound.getNumberOfPegs());
        System.out.println("====================================");
        // ===================================
        
        // Step 2: Check if answer is correct
        boolean isCorrect = gameRound.getCorrectMinimumMoves().equals(request.getPlayerMinimumMoves());
        
        // Step 3: Validate move sequence (basic validation)
        boolean sequenceValid = validateMoveSequence(
            request.getPlayerMoveSequence(), 
            request.getPlayerMinimumMoves()
        );
        
        // Step 4: Determine result
        String result;
        String message;
        
        if (isCorrect && sequenceValid) {
            result = "WIN";
            message = "Congratulations! You solved it correctly!";
            
            // Save player answer
            PlayerAnswer playerAnswer = new PlayerAnswer();
            playerAnswer.setPlayerName(request.getPlayerName());
            playerAnswer.setGameRoundId(request.getGameRoundId());
            playerAnswer.setPlayerMinimumMoves(request.getPlayerMinimumMoves());
            playerAnswer.setPlayerMoveSequence(request.getPlayerMoveSequence());
            playerAnswer.setIsCorrect(true);
            
            playerAnswerRepository.save(playerAnswer);
            log.info("Saved correct answer for player {}", request.getPlayerName());
            
        } else if (request.getPlayerMinimumMoves().equals(gameRound.getCorrectMinimumMoves())) {
            result = "DRAW";
            message = "Correct number of moves, but check your sequence!";
        } else {
            result = "LOSE";
            message = "Incorrect answer. Try again!";
        }
        
        // Step 5: Build response
        return PlayerAnswerResponse.builder()
            .playerName(request.getPlayerName())
            .isCorrect(isCorrect && sequenceValid)
            .result(result)
            .message(message)
            .correctMinimumMoves(gameRound.getCorrectMinimumMoves())
            .correctMoveSequence(gameRound.getCorrectMoveSequence())
            .playerMinimumMoves(request.getPlayerMinimumMoves())
            .playerMoveSequence(request.getPlayerMoveSequence())
            .build();
    }
    
    //move sequence validation
    private boolean validateMoveSequence(String sequence, int expectedMoves) {
        if (sequence == null || sequence.trim().isEmpty()) {
            return false;
        }
        
        String[] moves = sequence.split(",");
        
        // Check if number of moves matches
        if (moves.length != expectedMoves) {
            return false;
        }
        
        // Check format of each move (A->B, etc.)
        for (String move : moves) {
            String trimmed = move.trim();
            if (!trimmed.matches("[A-D]->[A-D]")) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public List<PerformanceStatsResponse> getPerformanceStats() {
        List<AlgorithmPerformance> allPerformances = algorithmPerformanceRepository.findAll();
        
        Map<String, List<AlgorithmPerformance>> groupedByAlgorithm = allPerformances.stream()
            .collect(Collectors.groupingBy(AlgorithmPerformance::getAlgorithmName));
        
        return groupedByAlgorithm.entrySet().stream()
            .map(entry -> buildPerformanceStats(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }
    
    @Override
    public PerformanceStatsResponse getPerformanceStatsByAlgorithm(String algorithmName) {
        List<AlgorithmPerformance> performances = 
            algorithmPerformanceRepository.findByAlgorithmName(algorithmName);
        
        return buildPerformanceStats(algorithmName, performances);
    }
    
    /**
     * Build performance statistics response
     */
    private PerformanceStatsResponse buildPerformanceStats(
            String algorithmName, 
            List<AlgorithmPerformance> performances) {
        
        if (performances.isEmpty()) {
            return PerformanceStatsResponse.builder()
                .algorithmName(algorithmName)
                .records(new ArrayList<>())
                .build();
        }
        
        List<PerformanceStatsResponse.PerformanceRecord> records = new ArrayList<>();
        
        for (AlgorithmPerformance perf : performances) {
            GameRound gameRound = gameRoundRepository.findById(perf.getGameRoundId())
                .orElse(null);
            
            if (gameRound != null) {
                records.add(PerformanceStatsResponse.PerformanceRecord.builder()
                    .gameRoundId(perf.getGameRoundId())
                    .numberOfDisks(gameRound.getNumberOfDisks())
                    .minimumMoves(perf.getMinimumMoves())
                    .executionTimeNanos(perf.getExecutionTimeNanos())
                    .executionTimeMillis(perf.getExecutionTimeNanos() / 1_000_000.0)
                    .build());
            }
        }
        
        // Calculate statistics
        double avgTime = records.stream()
            .mapToDouble(PerformanceStatsResponse.PerformanceRecord::getExecutionTimeMillis)
            .average()
            .orElse(0.0);
        
        long minTime = records.stream()
            .mapToLong(PerformanceStatsResponse.PerformanceRecord::getExecutionTimeNanos)
            .min()
            .orElse(0L);
        
        long maxTime = records.stream()
            .mapToLong(PerformanceStatsResponse.PerformanceRecord::getExecutionTimeNanos)
            .max()
            .orElse(0L);
        
        return PerformanceStatsResponse.builder()
            .algorithmName(algorithmName)
            .numberOfPegs(performances.get(0).getNumberOfPegs())
            .records(records)
            .averageExecutionTimeMillis(avgTime)
            .minExecutionTimeNanos(minTime)
            .maxExecutionTimeNanos(maxTime)
            .build();
    }
    
    /**
     * Functional interface for algorithm execution
     */
    @FunctionalInterface
    private interface AlgorithmExecutor {
        List<String> execute();
    }
    
    /**
     * Internal class to hold algorithm execution results
     */
    @lombok.Data
    @lombok.Builder
    private static class AlgorithmExecutionResult {
        private String algorithmName;
        private List<String> moves;
        private int minimumMoves;
        private long executionTimeNanos;
    }
}