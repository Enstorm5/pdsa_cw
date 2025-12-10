package com.pdsa.eightqueenspuzzle.service;

import com.pdsa.eightqueenspuzzle.algorithm.QueensSolver;
import com.pdsa.eightqueenspuzzle.algorithm.ThreadedQueensSolver;
import com.pdsa.eightqueenspuzzle.dto.request.SubmissionRequest;
import com.pdsa.eightqueenspuzzle.dto.response.AlgorithmResponse;
import com.pdsa.eightqueenspuzzle.dto.response.GameStatsResponse;
import com.pdsa.eightqueenspuzzle.dto.response.SubmissionResponse;
import com.pdsa.eightqueenspuzzle.exception.ValidationException;
import com.pdsa.eightqueenspuzzle.model.AlgorithmPerformance;
import com.pdsa.eightqueenspuzzle.model.Player;
import com.pdsa.eightqueenspuzzle.model.Solution;
import com.pdsa.eightqueenspuzzle.model.Submission;
import com.pdsa.eightqueenspuzzle.repository.PerformanceRepository;
import com.pdsa.eightqueenspuzzle.repository.PlayerRepository;
import com.pdsa.eightqueenspuzzle.repository.SolutionRepository;
import com.pdsa.eightqueenspuzzle.repository.SubmissionRepository;
import com.pdsa.eightqueenspuzzle.util.SolutionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QueensService {
    
    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private SolutionRepository solutionRepository;
    
    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Autowired
    private PerformanceRepository performanceRepository;
    
    // 5.1.1 - Run Sequential Algorithm
    public AlgorithmResponse runSequentialAlgorithm() {
        try {
            QueensSolver solver = new QueensSolver();
            solver.solveSequential();
            
            // Save performance record
            AlgorithmPerformance performance = new AlgorithmPerformance(
                AlgorithmPerformance.AlgorithmType.SEQUENTIAL,
                solver.getExecutionTime(),
                solver.getSolutionCount()
            );
            performanceRepository.save(performance);
            
            return new AlgorithmResponse(
                AlgorithmPerformance.AlgorithmType.SEQUENTIAL,
                solver.getExecutionTime(),
                solver.getSolutionCount(),
                "Sequential algorithm completed successfully"
            );
        } catch (Exception e) {
            throw new ValidationException("Error running sequential algorithm: " + e.getMessage());
        }
    }
    
    // 5.1.2 - Run Threaded Algorithm
    public AlgorithmResponse runThreadedAlgorithm() {
        try {
            ThreadedQueensSolver solver = new ThreadedQueensSolver();
            solver.solveThreaded();
            
            // Save performance record
            AlgorithmPerformance performance = new AlgorithmPerformance(
                AlgorithmPerformance.AlgorithmType.THREADED,
                solver.getExecutionTime(),
                solver.getSolutionCount()
            );
            performanceRepository.save(performance);
            
            return new AlgorithmResponse(
                AlgorithmPerformance.AlgorithmType.THREADED,
                solver.getExecutionTime(),
                solver.getSolutionCount(),
                "Threaded algorithm completed successfully"
            );
        } catch (Exception e) {
            throw new ValidationException("Error running threaded algorithm: " + e.getMessage());
        }
    }
    
    // 5.1.3 & 5.1.4 - Submit Solution
    public SubmissionResponse submitSolution(SubmissionRequest request) {
        // Validation
        if (request.getPlayerName() == null || request.getPlayerName().trim().isEmpty()) {
            throw new ValidationException("Player name is required");
        }
        
        if (request.getQueenPositions() == null || request.getQueenPositions().size() != 8) {
            throw new ValidationException("Must provide exactly 8 queen positions");
        }
        
        // Validate solution correctness
        if (!isValidSolution(request.getQueenPositions())) {
            throw new ValidationException("Invalid solution - queens attack each other");
        }
        
        String solutionString = SolutionConverter.listToString(request.getQueenPositions());
        
        // Find or create player
        Player player = playerRepository.findByPlayerName(request.getPlayerName())
                .orElseGet(() -> playerRepository.save(new Player(request.getPlayerName())));
        
        // Check if solution exists
        Solution solution = solutionRepository.findBySolutionData(solutionString)
                .orElseThrow(() -> new ValidationException("This is not a valid 8-queens solution"));
        
        // 5.1.4 - Check if already found
        if (solution.isFound()) {
            return new SubmissionResponse(false, 
                "This solution was already discovered! Try to find a different one.", 
                getFoundCount(), 92);
        }
        
        // Mark as found and save submission
        solution.setFound(true);
        solutionRepository.save(solution);
        
        Submission submission = new Submission(player, solution);
        submissionRepository.save(submission);
        
        int foundCount = getFoundCount();
        
        // 5.1.5 - Check if all solutions found, reset if needed
        if (foundCount == 92) {
            resetAllSolutions();
            return new SubmissionResponse(true, 
                "Congratulations! You found the last solution! Game has been reset.", 
                0, 92);
        }
        
        return new SubmissionResponse(true, 
            "New solution accepted! Well done!", 
            foundCount, 92);
    }
    
    // Get Game Statistics
    public GameStatsResponse getGameStats() {
        int totalSolutions = 92;
        int foundSolutions = getFoundCount();
        int remainingSolutions = totalSolutions - foundSolutions;
        long totalPlayers = playerRepository.count();
        
        // Get last execution times
        List<AlgorithmPerformance> sequentialPerfs = performanceRepository
            .findTop15ByAlgorithmTypeOrderByExecutedAtDesc(AlgorithmPerformance.AlgorithmType.SEQUENTIAL);
        List<AlgorithmPerformance> threadedPerfs = performanceRepository
            .findTop15ByAlgorithmTypeOrderByExecutedAtDesc(AlgorithmPerformance.AlgorithmType.THREADED);
        
        Long lastSequentialTime = sequentialPerfs.isEmpty() ? null : sequentialPerfs.get(0).getExecutionTimeMs();
        Long lastThreadedTime = threadedPerfs.isEmpty() ? null : threadedPerfs.get(0).getExecutionTimeMs();
        
        return new GameStatsResponse(
            totalSolutions,
            foundSolutions,
            remainingSolutions,
            totalPlayers,
            lastSequentialTime,
            lastThreadedTime
        );
    }
    
    // 5.1.5 - Reset all solution flags
    public void resetAllSolutions() {
        List<Solution> allSolutions = solutionRepository.findAll();
        allSolutions.forEach(s -> s.setFound(false));
        solutionRepository.saveAll(allSolutions);
    }
    
    // Validation logic
    private boolean isValidSolution(List<Integer> positions) {
        Set<Integer> rows = new HashSet<>();
        Set<Integer> cols = new HashSet<>();
        
        for (int col = 0; col < positions.size(); col++) {
            int row = positions.get(col);
            
            // Check bounds
            if (row < 0 || row >= 8) return false;
            
            // Check row/col uniqueness
            if (rows.contains(row) || cols.contains(col)) return false;
            rows.add(row);
            cols.add(col);
            
            // Check diagonals with previous queens
            for (int prevCol = 0; prevCol < col; prevCol++) {
                int prevRow = positions.get(prevCol);
                if (Math.abs(prevRow - row) == Math.abs(prevCol - col)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private int getFoundCount() {
        return solutionRepository.countByIsFoundTrue();
    }
}