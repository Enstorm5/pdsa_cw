package com.pdsa.towerofhanoi.algorithm;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ThreePegRecursive {
    
    
    public List<String> solve(int n) {
        List<String> moves = new ArrayList<>();  
        solveRecursive(n, 'A', 'C', 'B', moves);  
        return moves;
    }
    
    
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
    
    // minimum number of moves calculation (2^n - 1)
    public int getMinimumMoves(int n) {
        return (int) Math.pow(2, n) - 1;
    }
    
    public String getAlgorithmName() {
        return "3-Peg Recursive";
    }
}