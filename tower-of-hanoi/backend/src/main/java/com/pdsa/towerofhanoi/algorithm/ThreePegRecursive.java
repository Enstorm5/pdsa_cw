package com.pdsa.towerofhanoi.algorithm;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ThreePegRecursive {
    
    private List<String> moves;
    
    /**
     * Solves Tower of Hanoi using recursive approach
     * Time Complexity: O(2^n)
     * Space Complexity: O(n) for recursion stack
     * 
     * @param n Number of disks
     * @return List of moves in format "A->C"
     */
    public List<String> solve(int n) {
        moves = new ArrayList<>();
        solveRecursive(n, 'A', 'D', 'B');
        return moves;
    }
    
    /**
     * Recursive helper method
     * 
     * @param n Number of disks to move
     * @param source Source peg
     * @param destination Destination peg
     * @param auxiliary Auxiliary peg
     */
    private void solveRecursive(int n, char source, char destination, char auxiliary) {
        if (n == 1) {
            // Base case: move single disk
            moves.add(source + "->" + destination);
            return;
        }
        
        // Step 1: Move n-1 disks from source to auxiliary using destination
        solveRecursive(n - 1, source, auxiliary, destination);
        
        // Step 2: Move the largest disk from source to destination
        moves.add(source + "->" + destination);
        
        // Step 3: Move n-1 disks from auxiliary to destination using source
        solveRecursive(n - 1, auxiliary, destination, source);
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