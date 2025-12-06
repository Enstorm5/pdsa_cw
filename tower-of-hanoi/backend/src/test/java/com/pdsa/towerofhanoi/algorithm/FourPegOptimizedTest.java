package com.pdsa.towerofhanoi.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Four Peg Optimized Algorithm Tests")
class FourPegOptimizedTest {
    
    private FourPegOptimized algorithm;
    
    @BeforeEach
    void setUp() {
        algorithm = new FourPegOptimized();
    }
    
    @Test
    @DisplayName("Test with 1 disk - should return 1 move")
    void testOneDisk() {
        List<String> moves = algorithm.solve(1);
        
        assertEquals(1, moves.size(), "Should have exactly 1 move");
    }
    
    @Test
    @DisplayName("Test with 2 disks - should return 3 moves")
    void testTwoDisks() {
        List<String> moves = algorithm.solve(2);
        
        assertEquals(3, moves.size(), "Should have exactly 3 moves for 2 disks");
    }
    
    @Test
    @DisplayName("Test with 5 disks - should be less than 3-peg solution")
    void testFiveDisks() {
        List<String> moves = algorithm.solve(5);
        
        assertTrue(moves.size() < 31, "4-peg should use fewer moves than 3-peg (31 moves)");
        System.out.println("Optimized with 5 disks: " + moves.size() + " moves");
    }
    
    @Test
    @DisplayName("Test with 7 disks - should be less than 3-peg solution")
    void testSevenDisks() {
        List<String> moves = algorithm.solve(7);
        
        assertTrue(moves.size() < 127, "4-peg should use fewer moves than 3-peg (127 moves)");
        System.out.println("Optimized with 7 disks: " + moves.size() + " moves");
    }
    
    @Test
    @DisplayName("Test with 10 disks - should complete without error")
    void testTenDisks() {
        assertDoesNotThrow(() -> {
            List<String> moves = algorithm.solve(10);
            assertTrue(moves.size() > 0, "Should generate moves");
            System.out.println("Optimized with 10 disks: " + moves.size() + " moves");
        });
    }
    
    @Test
    @DisplayName("Test minimum moves calculation")
    void testMinimumMoves() {
        assertEquals(1, algorithm.getMinimumMoves(1));
        assertEquals(3, algorithm.getMinimumMoves(2));
        assertTrue(algorithm.getMinimumMoves(5) < 31);
        assertTrue(algorithm.getMinimumMoves(10) < 1023);
    }
    
    @Test
    @DisplayName("Test algorithm name")
    void testAlgorithmName() {
        assertEquals("4-Peg Optimized", algorithm.getAlgorithmName());
    }
    
    @Test
    @DisplayName("Test moves format")
    void testMovesFormat() {
        List<String> moves = algorithm.solve(5);
        
        for (String move : moves) {
            assertTrue(move.matches("[A-D]->[A-D]"), 
                "Move should be in format 'X->Y'");
        }
    }
    
    @Test
    @DisplayName("Test efficiency - should be better than 3-peg")
    void testEfficiency() {
        ThreePegRecursive threePeg = new ThreePegRecursive();
        
        for (int n = 5; n <= 10; n++) {
            int threePegMoves = threePeg.getMinimumMoves(n);
            int fourPegMoves = algorithm.getMinimumMoves(n);
            
            assertTrue(fourPegMoves < threePegMoves,
                String.format("4-peg optimized should be better than 3-peg for %d disks", n));
        }
    }
}