package com.pdsa.eightqueenspuzzle.util;

import com.pdsa.eightqueenspuzzle.algorithm.QueensSolver;
import com.pdsa.eightqueenspuzzle.model.Solution;
import com.pdsa.eightqueenspuzzle.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SolutionInitializer implements CommandLineRunner {
    
    @Autowired
    private SolutionRepository solutionRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if solutions already exist
        long count = solutionRepository.count();
        
        if (count == 0) {
            System.out.println("Initializing 92 Eight Queens solutions...");
            
            // Generate all 92 solutions using the algorithm
            QueensSolver solver = new QueensSolver();
            solver.solveSequential();
            
            List<List<Integer>> allSolutions = solver.getSolutions();
            
            // Save each solution to database
            for (List<Integer> solutionPositions : allSolutions) {
                String solutionData = SolutionConverter.listToString(solutionPositions);
                Solution solution = new Solution(solutionData);
                solutionRepository.save(solution);
            }
            
            System.out.println("Successfully initialized " + allSolutions.size() + " solutions!");
        } else {
            System.out.println("Solutions already initialized. Count: " + count);
        }
    }
}