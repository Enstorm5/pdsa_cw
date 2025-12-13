package com.pdsa.towerofhanoi.algorithm;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class FourPegFrameStewart {
    
    private final char[] pegs = {'A', 'B', 'C', 'D'}; 
    
    
    public List<String> solve(int n) {
        List<String> moves = new ArrayList<>();  
        frameStewart(n, 0, 3, 1, 2, moves);  
        return moves;
    }
    
    
    private void frameStewart(int n, int source, int destination, int aux1, int aux2, List<String> moves) {
        if (n == 0) {
            return;
        }
        
        if (n == 1) {
            moves.add(pegs[source] + "->" + pegs[destination]);
            return;
        }
        
        // Find optimal split point k 
        int k = n - (int) Math.round(Math.sqrt(2 * n));
        if (k < 1) k = 1;
        if (k >= n) k = n - 1;
        
        // Step 1: Move top k disks from source to aux1 using all 4 pegs
        frameStewart(k, source, aux1, aux2, destination, moves);
        
        // Step 2: Move remaining (n-k) disks from source to destination using 3 pegs
        threePegMove(n - k, source, destination, aux2, moves);
        
        // Step 3: Move k disks from aux1 to destination using all 4 pegs
        frameStewart(k, aux1, destination, source, aux2, moves);
    }
    
    
    //3 peg method
    private void threePegMove(int n, int source, int destination, int auxiliary, List<String> moves) {
        if (n == 1) {
            moves.add(pegs[source] + "->" + pegs[destination]);
            return;
        }
        
        threePegMove(n - 1, source, auxiliary, destination, moves);
        moves.add(pegs[source] + "->" + pegs[destination]);
        threePegMove(n - 1, auxiliary, destination, source, moves);
    }
    
    
    //minimum number of moves calculation using dynamic programming
    public int getMinimumMoves(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 3;
        
        // dynamic programming array
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        dp[2] = 3;
        
        // Calculate for each number of disks
        for (int i = 3; i <= n; i++) {
            dp[i] = Integer.MAX_VALUE;
            
            // Try all possible split points
            for (int k = 1; k < i; k++) {
                // move k disks twice (to aux and to dest) + move (i-k) disks once with 3 pegs
                int movesCount = 2 * dp[k] + ((1 << (i - k)) - 1);
                dp[i] = Math.min(dp[i], movesCount);
            }
        }
        
        return dp[n];
    }
    
    public String getAlgorithmName() {
        return "4-Peg Frame-Stewart";
    }
}