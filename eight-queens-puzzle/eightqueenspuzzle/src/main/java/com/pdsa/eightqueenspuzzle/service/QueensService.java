package com.pdsa.eightqueenspuzzle.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.pdsa.eightqueenspuzzle.repository.PlayerRepository;
import com.pdsa.eightqueenspuzzle.repository.SolutionRepository;
import com.pdsa.eightqueenspuzzle.repository.SubmissionRepository;
import com.pdsa.eightqueenspuzzle.repository.PerformanceRepository;
import com.pdsa.eightqueenspuzzle.model.Player;
import com.pdsa.eightqueenspuzzle.model.Solution;
import com.pdsa.eightqueenspuzzle.model.Submission;
import com.pdsa.eightqueenspuzzle.dto.request.SubmissionRequest;
import com.pdsa.eightqueenspuzzle.dto.response.AlgorithmResponse;
import com.pdsa.eightqueenspuzzle.dto.response.GameStatsResponse;
import com.pdsa.eightqueenspuzzle.dto.response.SubmissionResponse;
import com.pdsa.eightqueenspuzzle.exception.ValidationException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

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
    
    //Save solution with player name
    //Check if solution already found
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
        
        String solutionString = convertToString(request.getQueenPositions());
        
        // Find or create player
        Player player = playerRepository.findByPlayerName(request.getPlayerName())
                .orElseGet(() -> playerRepository.save(new Player(request.getPlayerName())));
        
        // Check if solution exists and if already found
        Solution solution = solutionRepository.findBySolutionData(solutionString)
                .orElseThrow(() -> new ValidationException("This is not a valid 8-queens solution"));
        
        //Check if already found
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
        
        //Check if all solutions found, reset if needed
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
    
    // Validation logic - Why these checks?
    // O(n²) is acceptable for n=8
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
    
    // Reset all solution flags
    public void resetAllSolutions() {
        List<Solution> allSolutions = solutionRepository.findAll();
        allSolutions.forEach(s -> s.setFound(false));
        solutionRepository.saveAll(allSolutions);
    }
    
    private int getFoundCount() {
        return solutionRepository.countByIsFoundTrue();
    }
    
    private String convertToString(List<Integer> positions) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < positions.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(positions.get(i));
        }
        return sb.toString();
    }

    public AlgorithmResponse runSequentialAlgorithm() {
        // Implement sequential algorithm logic
        return null;
    }

    public AlgorithmResponse runThreadedAlgorithm() {
        // Implement threaded algorithm logic
        return null;
    }

    public GameStatsResponse getGameStats() {
        // Implement game stats retrieval logic
        return null;
    }
}