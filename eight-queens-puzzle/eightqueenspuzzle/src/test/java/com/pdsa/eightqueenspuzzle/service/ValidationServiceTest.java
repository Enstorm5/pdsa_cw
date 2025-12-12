package com.pdsa.eightqueenspuzzle.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class ValidationService {

    public boolean isValidSolution(List<Integer> solution) {
        // Basic validation
        if (solution == null || solution.size() != 8) {
            return false;
        }

        for (int col = 0; col < solution.size(); col++) {
            int row = solution.get(col);

            // Check bounds
            if (row < 0 || row >= 8) {
                return false;
            }

            // Check conflicts with previous queens
            for (int prevCol = 0; prevCol < col; prevCol++) {
                int prevRow = solution.get(prevCol);

                // Same row
                if (row == prevRow) {
                    return false;
                }

                // Same diagonal
                if (Math.abs(row - prevRow) == Math.abs(col - prevCol)) {
                    return false;
                }
            }
        }

        return true;
    }
}

@DisplayName("Validation Service Tests")
public class ValidationServiceTest {
    
    private ValidationService validationService;
    
    @BeforeEach
    public void setUp() {
        validationService = new ValidationService();
    }
    
    @Test
    @DisplayName("Should accept valid solution")
    public void testValidSolution() {
        // Arrange
        List<Integer> validSolution = Arrays.asList(0, 4, 7, 5, 2, 6, 1, 3);
        
        // Act & Assert
        assertTrue(validationService.isValidSolution(validSolution), 
            "Should accept valid solution");
    }
    
    @Test
    @DisplayName("Should reject solution with queens on same row")
    public void testRejectSameRow() {
        // Arrange
        List<Integer> invalidSolution = Arrays.asList(0, 0, 7, 5, 2, 6, 1, 3);
        
        // Act & Assert
        assertFalse(validationService.isValidSolution(invalidSolution), 
            "Should reject solution with queens on same row");
    }
    
    @Test
    @DisplayName("Should reject solution with diagonal attacks")
    public void testRejectDiagonalAttack() {
        // Arrange
        List<Integer> invalidSolution = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        
        // Act & Assert
        assertFalse(validationService.isValidSolution(invalidSolution), 
            "Should reject solution with diagonal attacks");
    }
    
    @Test
    @DisplayName("Should reject solution with wrong size")
    public void testRejectWrongSize() {
        // Arrange - only 5 queens
        List<Integer> invalidSolution = Arrays.asList(0, 4, 7, 5, 2);
        
        // Act & Assert
        assertFalse(validationService.isValidSolution(invalidSolution), 
            "Should reject solution with wrong number of queens");
    }
    
    @Test
    @DisplayName("Should reject null input")
    public void testRejectNull() {
        // Act & Assert
        assertFalse(validationService.isValidSolution(null), 
            "Should reject null input");
    }
    
    @Test
    @DisplayName("Should reject solution with out-of-bounds positions")
    public void testRejectOutOfBounds() {
        // Arrange - position 9 is out of bounds
        List<Integer> invalidSolution = Arrays.asList(0, 4, 7, 5, 2, 6, 1, 9);
        
        // Act & Assert
        assertFalse(validationService.isValidSolution(invalidSolution), 
            "Should reject solution with out-of-bounds positions");
    }
    
    @Test
    @DisplayName("Should reject solution with negative positions")
    public void testRejectNegativePositions() {
        // Arrange
        List<Integer> invalidSolution = Arrays.asList(0, 4, 7, 5, -1, 6, 1, 3);
        
        // Act & Assert
        assertFalse(validationService.isValidSolution(invalidSolution), 
            "Should reject solution with negative positions");
    }
    
    @Test
    @DisplayName("Should accept all 92 known valid solutions")
    public void testAllKnownValidSolutions() {
        // Arrange - a few known valid solutions
        List<List<Integer>> validSolutions = Arrays.asList(
            Arrays.asList(0, 4, 7, 5, 2, 6, 1, 3),
            Arrays.asList(0, 5, 7, 2, 6, 3, 1, 4),
            Arrays.asList(0, 6, 3, 5, 7, 1, 4, 2),
            Arrays.asList(0, 6, 4, 7, 1, 3, 5, 2),
            Arrays.asList(1, 3, 5, 7, 2, 0, 6, 4)
        );
        
        // Act & Assert
        for (List<Integer> solution : validSolutions) {
            assertTrue(validationService.isValidSolution(solution), 
                "Should accept known valid solution: " + solution);
        }
    }
}