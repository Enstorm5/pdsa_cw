package com.pdsa.towerofhanoi.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Four Peg Frame-Stewart Algorithm Tests")
class FourPegFrameStewartTest {
    
    private FourPegFrameStewart algorithm;
    
    @BeforeEach
    void setUp() {
        algorithm = new FourPegFrameStewart();
    }
    
    @Test
    @DisplayName("Test with 1 disk - should return 1 move")
    void testOneDisk() {
        List<String> moves = algorithm.solve(1);
        
        assertEquals(1, moves.size(), "Should have exactly 1 move");
    }
    
    @Test
    @DisplayName("Test with 3 disks - should be less than 3-peg solution")
    void testThreeDisks() {
        List<String> moves = algorithm.solve(3);
        
        assertTrue(moves.size() <= 7, "4-peg should use 7 or fewer moves for 3 disks");
    }
    
    @Test
    @DisplayName("Test with 5 disks - should be significantly less than 31")
    void testFiveDisks() {
        List<String> moves = algorithm.solve(5);
        
        assertTrue(moves.size() < 31, "4-peg should use fewer moves than 3-peg (31 moves)");
        System.out.println("Frame-Stewart with 5 disks: " + moves.size() + " moves (vs 31 for 3-peg)");
    }
    
    @Test
    @DisplayName("Test with 7 disks - should be significantly less than 127")
    void testSevenDisks() {
        List<String> moves = algorithm.solve(7);
        
        assertTrue(moves.size() < 127, "4-peg should use fewer moves than 3-peg (127 moves)");
        System.out.println("Frame-Stewart with 7 disks: " + moves.size() + " moves (vs 127 for 3-peg)");
    }
    
    @Test
    @DisplayName("Test with 10 disks - should complete without error")
    void testTenDisks() {
        assertDoesNotThrow(() -> {
            List<String> moves = algorithm.solve(10);
            assertTrue(moves.size() > 0, "Should generate moves");
            assertTrue(moves.size() < 1023, "Should be less than 3-peg solution (1023 moves)");
            System.out.println("Frame-Stewart with 10 disks: " + moves.size() + " moves (vs 1023 for 3-peg)");
        });
    }
    
    @Test
    @DisplayName("Test minimum moves calculation")
    void testMinimumMoves() {
        assertEquals(1, algorithm.getMinimumMoves(1));
        assertTrue(algorithm.getMinimumMoves(5) < 31);
        assertTrue(algorithm.getMinimumMoves(7) < 127);
        assertTrue(algorithm.getMinimumMoves(10) < 1023);
    }
    
    @Test
    @DisplayName("Test algorithm name")
    void testAlgorithmName() {
        assertEquals("4-Peg Frame-Stewart", algorithm.getAlgorithmName());
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
    @DisplayName("Test efficiency - 4-peg should be better than 3-peg")
    void testEfficiencyComparison() {
        ThreePegRecursive threePeg = new ThreePegRecursive();
        
        for (int n = 5; n <= 10; n++) {
            int threePegMoves = threePeg.getMinimumMoves(n);
            int fourPegMoves = algorithm.getMinimumMoves(n);
            
            assertTrue(fourPegMoves < threePegMoves,
                String.format("4-peg (%d moves) should be more efficient than 3-peg (%d moves) for %d disks",
                    fourPegMoves, threePegMoves, n));
        }
    }
}