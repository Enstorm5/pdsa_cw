package com.pdsa.towerofhanoi.algorithm;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class FourPegOptimized {
    
    /**
     * Solves Tower of Hanoi with 4 pegs using optimized heuristic approach
     * Moves from A to D using B and C as auxiliary
     * Time Complexity: Better than 3-peg but not as optimal as Frame-Stewart
     * Space Complexity: O(n) for recursion stack
     * 
     * @param n Number of disks
     * @return List of moves in format "A->D"
     */
    public List<String> solve(int n) {
        List<String> moves = new ArrayList<>();  // ✅ Local variable (thread-safe)
        solveOptimized(n, 'A', 'D', 'B', 'C', moves);  // ✅ Pass as parameter
        return moves;
    }
    
    /**
     * Optimized recursive approach for 4 pegs
     * Strategy: Use simple heuristic to split work between auxiliary pegs
     * 
     * @param n Number of disks
     * @param source Source peg
     * @param destination Destination peg
     * @param aux1 First auxiliary peg
     * @param aux2 Second auxiliary peg
     * @param moves List to store moves
     */
    private void solveOptimized(int n, char source, char destination, char aux1, char aux2, List<String> moves) {
        if (n == 0) {
            return;
        }
        
        if (n == 1) {
            moves.add(source + "->" + destination);
            return;
        }
        
        if (n == 2) {
            moves.add(source + "->" + aux1);
            moves.add(source + "->" + destination);
            moves.add(aux1 + "->" + destination);
            return;
        }
        
        // For n > 2, use heuristic split
        // Move top (n-2) disks to aux1 using all pegs
        solveOptimized(n - 2, source, aux1, aux2, destination, moves);
        
        // Move 2 largest disks directly to destination
        moves.add(source + "->" + aux2);
        moves.add(source + "->" + destination);
        moves.add(aux2 + "->" + destination);
        
        // Move (n-2) disks from aux1 to destination using all pegs
        solveOptimized(n - 2, aux1, destination, source, aux2, moves);
    }
    
    /**
     * Calculate approximate minimum moves (heuristic)
     * Not as optimal as Frame-Stewart but better than 3-peg
     */
    public int getMinimumMoves(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 3;
        
        // Heuristic formula: 2*T(n-2) + 3
        return 2 * getMinimumMoves(n - 2) + 3;
    }
    
    public String getAlgorithmName() {
        return "4-Peg Optimized";
    }
}