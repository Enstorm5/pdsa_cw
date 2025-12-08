package com.pdsa.towerofhanoi.algorithm;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ThreePegRecursive {
    
    /**
     * Solves Tower of Hanoi using recursive approach (3 pegs)
     * Moves from A to C using B as auxiliary
     * Time Complexity: O(2^n)
     * Space Complexity: O(n) for recursion stack
     * 
     * @param n Number of disks
     * @return List of moves in format "A->C"
     */
    public List<String> solve(int n) {
        List<String> moves = new ArrayList<>();  // ✅ Local variable (thread-safe)
        solveRecursive(n, 'A', 'C', 'B', moves);  // ✅ Pass as parameter
        return moves;
    }
    
    /**
     * Recursive helper method
     * 
     * @param n Number of disks to move
     * @param source Source peg
     * @param destination Destination peg
     * @param auxiliary Auxiliary peg
     * @param moves List to store moves
     */
    private void solveRecursive(int n, char source, char destination, char auxiliary, List<String> moves) {
        if (n == 1) {
            // Base case: move single disk
            moves.add(source + "->" + destination);
            return;
        }
        
        // Step 1: Move n-1 disks from source to auxiliary using destination
        solveRecursive(n - 1, source, auxiliary, destination, moves);
        
        // Step 2: Move the largest disk from source to destination
        moves.add(source + "->" + destination);
        
        // Step 3: Move n-1 disks from auxiliary to destination using source
        solveRecursive(n - 1, auxiliary, destination, source, moves);
    }
    
    /**
     * Calculate minimum number of moves for n disks
     * Formula: 2^n - 1
     */
    public int getMinimumMoves(int n) {
        return (int) Math.pow(2, n) - 1;
    }
    
    public String getAlgorithmName() {
        return "3-Peg Recursive";
    }
}