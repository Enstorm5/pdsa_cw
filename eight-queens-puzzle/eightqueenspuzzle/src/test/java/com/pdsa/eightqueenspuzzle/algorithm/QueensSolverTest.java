package com.pdsa.eightqueenspuzzle.algorithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DisplayName("Sequential Queens Solver Tests")
public class QueensSolverTest {
    
    private QueensSolver solver;
    
    @BeforeEach
    public void setUp() {
        solver = new QueensSolver();
    }
    
    @Test
    @DisplayName("Should find exactly 92 solutions for 8x8 board")
    public void testFindsAll92Solutions() {
        // Act
        solver.solveSequential();
        
        // Assert
        assertEquals(92, solver.getSolutions().size(), 
            "8-Queens problem should have exactly 92 solutions");
    }
    
    @Test
    @DisplayName("Should record execution time")
    public void testRecordsExecutionTime() {
        // Act
        solver.solveSequential();
        
        // Assert
        assertTrue(solver.getExecutionTime() > 0, 
            "Execution time should be greater than 0");
        assertTrue(solver.getExecutionTime() < 5000, 
            "Execution time should be less than 5 seconds");
    }
    
    @Test
    @DisplayName("All solutions should be unique")
    public void testAllSolutionsAreUnique() {
        // Act
        solver.solveSequential();
        List<List<Integer>> solutions = solver.getSolutions();
        
        // Convert to Set to check uniqueness
        Set<List<Integer>> uniqueSolutions = new HashSet<>(solutions);
        
        // Assert
        assertEquals(solutions.size(), uniqueSolutions.size(), 
            "All solutions should be unique");
    }
    
    @Test
    @DisplayName("Each solution should have exactly 8 queens")
    public void testEachSolutionHas8Queens() {
        // Act
        solver.solveSequential();
        List<List<Integer>> solutions = solver.getSolutions();
        
        // Assert
        for (List<Integer> solution : solutions) {
            assertEquals(8, solution.size(), 
                "Each solution should have exactly 8 queens");
        }
    }
    
    @Test
    @DisplayName("No two queens should be on the same row")
    public void testNoQueensOnSameRow() {
        // Act
        solver.solveSequential();
        List<List<Integer>> solutions = solver.getSolutions();
        
        // Assert
        for (List<Integer> solution : solutions) {
            Set<Integer> rows = new HashSet<>(solution);
            assertEquals(8, rows.size(), 
                "All queens should be on different rows");
        }
    }
    
    @Test
    @DisplayName("No two queens should attack diagonally")
    public void testNoQueensAttackDiagonally() {
        // Act
        solver.solveSequential();
        List<List<Integer>> solutions = solver.getSolutions();
        
        // Assert
        for (List<Integer> solution : solutions) {
            assertTrue(isValidSolution(solution), 
                "Solution should not have diagonal attacks");
        }
    }
    
    @Test
    @DisplayName("Solution count should match getter")
    public void testSolutionCountGetter() {
        // Act
        solver.solveSequential();
        
        // Assert
        assertEquals(solver.getSolutions().size(), solver.getSolutionCount(), 
            "getSolutionCount() should return correct count");
    }
    
    @Test
    @DisplayName("Should handle multiple consecutive runs")
    public void testMultipleRuns() {
        // First run
        solver.solveSequential();
        int firstCount = solver.getSolutionCount();
        
        // Second run (creates new solver)
        QueensSolver solver2 = new QueensSolver();
        solver2.solveSequential();
        int secondCount = solver2.getSolutionCount();
        
        // Assert
        assertEquals(firstCount, secondCount, 
            "Multiple runs should produce same number of solutions");
    }
    
    // Helper method to validate solution
    private boolean isValidSolution(List<Integer> solution) {
        for (int i = 0; i < solution.size(); i++) {
            for (int j = i + 1; j < solution.size(); j++) {
                int rowDiff = Math.abs(solution.get(i) - solution.get(j));
                int colDiff = Math.abs(i - j);
                if (rowDiff == colDiff) {
                    return false; // Diagonal attack
                }
            }
        }
        return true;
    }
}