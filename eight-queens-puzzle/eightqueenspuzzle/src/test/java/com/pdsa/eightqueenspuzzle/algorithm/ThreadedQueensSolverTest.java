package com.pdsa.eightqueenspuzzle.algorithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DisplayName("Threaded Queens Solver Tests")
public class ThreadedQueensSolverTest {
    
    private ThreadedQueensSolver solver;
    
    @BeforeEach
    public void setUp() {
        solver = new ThreadedQueensSolver();
    }
    
    @Test
    @DisplayName("Should find exactly 92 solutions for 8x8 board")
    public void testFindsAll92Solutions() throws InterruptedException {
        // Act
        solver.solveThreaded();
        
        // Assert
        assertEquals(92, solver.getSolutions().size(), 
            "Threaded solver should find exactly 92 solutions");
    }
    
    @Test
    @DisplayName("Should record execution time")
    public void testRecordsExecutionTime() throws InterruptedException {
        // Act
        solver.solveThreaded();
        
        // Assert
        assertTrue(solver.getExecutionTime() > 0, 
            "Execution time should be greater than 0");
        assertTrue(solver.getExecutionTime() < 5000, 
            "Execution time should be less than 5 seconds");
    }
    
    @Test
    @DisplayName("All solutions should be unique (thread-safe)")
    public void testAllSolutionsAreUnique() throws InterruptedException {
        // Act
        solver.solveThreaded();
        List<List<Integer>> solutions = solver.getSolutions();
        
        // Convert to Set to check uniqueness
        Set<List<Integer>> uniqueSolutions = new HashSet<>(solutions);
        
        // Assert
        assertEquals(solutions.size(), uniqueSolutions.size(), 
            "All solutions should be unique (no race conditions)");
    }
    
    @Test
    @DisplayName("Should be faster than sequential (generally)")
    public void testPerformanceComparison() throws InterruptedException {
        // Arrange
        QueensSolver sequential = new QueensSolver();
        
        // Act
        sequential.solveSequential();
        long sequentialTime = sequential.getExecutionTime();
        
        solver.solveThreaded();
        long threadedTime = solver.getExecutionTime();
        
        // Assert - Threaded should generally be faster
        // We use 1.2x threshold to account for variance
        System.out.println("Sequential: " + sequentialTime + "ms");
        System.out.println("Threaded: " + threadedTime + "ms");
        System.out.println("Speedup: " + (double)sequentialTime/threadedTime + "x");
        
        // This assertion might occasionally fail due to system load
        // In production, you might want to run multiple times and average
        assertTrue(threadedTime < sequentialTime * 1.2, 
            "Threaded should generally be faster than sequential");
    }
    
    @Test
    @DisplayName("Each solution should have exactly 8 queens")
    public void testEachSolutionHas8Queens() throws InterruptedException {
        // Act
        solver.solveThreaded();
        List<List<Integer>> solutions = solver.getSolutions();
        
        // Assert
        for (List<Integer> solution : solutions) {
            assertEquals(8, solution.size(), 
                "Each solution should have exactly 8 queens");
        }
    }
    
    @Test
    @DisplayName("All solutions should be valid (no attacks)")
    public void testAllSolutionsAreValid() throws InterruptedException {
        // Act
        solver.solveThreaded();
        List<List<Integer>> solutions = solver.getSolutions();
        
        // Assert
        for (List<Integer> solution : solutions) {
            assertTrue(isValidSolution(solution), 
                "All solutions should be valid");
        }
    }
    
    @Test
    @DisplayName("Should produce same solutions as sequential")
    public void testProducesSameSolutionsAsSequential() throws InterruptedException {
        // Arrange
        QueensSolver sequential = new QueensSolver();
        sequential.solveSequential();
        Set<List<Integer>> sequentialSolutions = new HashSet<>(sequential.getSolutions());
        
        // Act
        solver.solveThreaded();
        Set<List<Integer>> threadedSolutions = new HashSet<>(solver.getSolutions());
        
        // Assert
        assertEquals(sequentialSolutions, threadedSolutions, 
            "Threaded and sequential should produce same set of solutions");
    }
    
    // Helper method
    private boolean isValidSolution(List<Integer> solution) {
        // Check row uniqueness
        Set<Integer> rows = new HashSet<>(solution);
        if (rows.size() != 8) return false;
        
        // Check diagonals
        for (int i = 0; i < solution.size(); i++) {
            for (int j = i + 1; j < solution.size(); j++) {
                int rowDiff = Math.abs(solution.get(i) - solution.get(j));
                int colDiff = Math.abs(i - j);
                if (rowDiff == colDiff) {
                    return false;
                }
            }
        }
        return true;
    }
}