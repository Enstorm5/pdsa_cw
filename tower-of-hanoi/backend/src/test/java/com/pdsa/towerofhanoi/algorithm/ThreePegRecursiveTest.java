package com.pdsa.towerofhanoi.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Three Peg Recursive Algorithm Tests")
class ThreePegRecursiveTest {
    
    private ThreePegRecursive algorithm;
    
    @BeforeEach
    void setUp() {
        algorithm = new ThreePegRecursive();
    }
    
    @Test
    @DisplayName("Test with 1 disk - should return 1 move")
    void testOneDisk() {
        List<String> moves = algorithm.solve(1);
        
        assertEquals(1, moves.size(), "Should have exactly 1 move");
        assertEquals("A->C", moves.get(0), "Move should be from A to C");
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
        assertEquals(31, algorithm.getMinimumMoves(5), "Minimum moves formula should return 31");
    }
    
    @Test
    @DisplayName("Test with 7 disks - should return 127 moves")
    void testSevenDisks() {
        List<String> moves = algorithm.solve(7);
        
        assertEquals(127, moves.size(), "Should have exactly 127 moves for 7 disks");
        assertEquals(127, algorithm.getMinimumMoves(7), "Minimum moves formula should return 127");
    }
    
    @Test
    @DisplayName("Test minimum moves formula - 2^n - 1")
    void testMinimumMovesFormula() {
        assertEquals(1, algorithm.getMinimumMoves(1));
        assertEquals(3, algorithm.getMinimumMoves(2));
        assertEquals(7, algorithm.getMinimumMoves(3));
        assertEquals(15, algorithm.getMinimumMoves(4));
        assertEquals(31, algorithm.getMinimumMoves(5));
        assertEquals(63, algorithm.getMinimumMoves(6));
        assertEquals(127, algorithm.getMinimumMoves(7));
        assertEquals(255, algorithm.getMinimumMoves(8));
        assertEquals(511, algorithm.getMinimumMoves(9));
        assertEquals(1023, algorithm.getMinimumMoves(10));
    }
    
    @Test
    @DisplayName("Test algorithm name")
    void testAlgorithmName() {
        assertEquals("3-Peg Recursive", algorithm.getAlgorithmName());
    }
    
    @Test
    @DisplayName("Test moves format - should contain valid peg names")
    void testMovesFormat() {
        List<String> moves = algorithm.solve(3);
        
        for (String move : moves) {
            assertTrue(move.matches("[ABC]->[ABC]"), 
                "Move should be in format 'X->Y' where X and Y are A, B, or C");
        }
    }
    
    @Test
    @DisplayName("Test first and last moves for 3 disks")
    void testFirstAndLastMoves() {
        List<String> moves = algorithm.solve(3);
        
        // For 3 disks moving from A to C using B as auxiliary:
        // The sequence is: A->C, A->B, C->B, A->C, B->A, B->C, A->C
        assertEquals("A->C", moves.get(0), "First move should be A->C");
        assertEquals("A->C", moves.get(moves.size() - 1), "Last move should be A->C");
    }
}