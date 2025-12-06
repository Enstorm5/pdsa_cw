package com.pdsa.towerofhanoi.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Three Peg Iterative Algorithm Tests")
class ThreePegIterativeTest {
    
    private ThreePegIterative algorithm;
    
    @BeforeEach
    void setUp() {
        algorithm = new ThreePegIterative();
    }
    
    @Test
    @DisplayName("Test with 1 disk - should return 1 move")
    void testOneDisk() {
        List<String> moves = algorithm.solve(1);
        
        assertEquals(1, moves.size(), "Should have exactly 1 move");
    }
    
    @Test
    @DisplayName("Test with 3 disks - should return 7 moves")
    void testThreeDisks() {
        List<String> moves = algorithm.solve(3);
        
        assertEquals(7, moves.size(), "Should have exactly 7 moves for 3 disks");
        assertEquals(7, algorithm.getMinimumMoves(3), "Minimum moves formula should return 7");
    }
    
    @Test
    @DisplayName("Test with 5 disks - should return 31 moves")
    void testFiveDisks() {
        List<String> moves = algorithm.solve(5);
        
        assertEquals(31, moves.size(), "Should have exactly 31 moves for 5 disks");
    }
    
    @Test
    @DisplayName("Test with 7 disks - should return 127 moves")
    void testSevenDisks() {
        List<String> moves = algorithm.solve(7);
        
        assertEquals(127, moves.size(), "Should have exactly 127 moves for 7 disks");
    }
    
    @Test
    @DisplayName("Test minimum moves formula matches 3-peg recursive")
    void testMinimumMovesFormula() {
        assertEquals(1, algorithm.getMinimumMoves(1));
        assertEquals(3, algorithm.getMinimumMoves(2));
        assertEquals(7, algorithm.getMinimumMoves(3));
        assertEquals(15, algorithm.getMinimumMoves(4));
        assertEquals(31, algorithm.getMinimumMoves(5));
        assertEquals(127, algorithm.getMinimumMoves(7));
        assertEquals(1023, algorithm.getMinimumMoves(10));
    }
    
    @Test
    @DisplayName("Test algorithm name")
    void testAlgorithmName() {
        assertEquals("3-Peg Iterative", algorithm.getAlgorithmName());
    }
    
    @Test
    @DisplayName("Test moves format")
    void testMovesFormat() {
        List<String> moves = algorithm.solve(3);
        
        for (String move : moves) {
            assertTrue(move.matches("[A-D]->[A-D]"), 
                "Move should be in format 'X->Y'");
        }
    }
    
    @Test
    @DisplayName("Compare with recursive - should have same number of moves")
    void testCompareWithRecursive() {
        ThreePegRecursive recursive = new ThreePegRecursive();
        
        for (int n = 1; n <= 7; n++) {
            List<String> recursiveMoves = recursive.solve(n);
            List<String> iterativeMoves = algorithm.solve(n);
            
            assertEquals(recursiveMoves.size(), iterativeMoves.size(),
                "Iterative and recursive should have same number of moves for " + n + " disks");
        }
    }
}