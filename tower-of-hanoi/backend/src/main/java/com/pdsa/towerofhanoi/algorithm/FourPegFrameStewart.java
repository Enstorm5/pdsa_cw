package com.pdsa.towerofhanoi.algorithm;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class FourPegFrameStewart {
    
    private final char[] pegs = {'A', 'B', 'C', 'D'}; // ✅ Immutable array (safe)
    
    /**
     * Solves Tower of Hanoi with 4 pegs using Frame-Stewart algorithm
     * Moves from A to D using B and C as auxiliary
     * Time Complexity: Better than O(2^n), approximately O(2^(sqrt(2n)))
     * Space Complexity: O(n) for recursion stack
     * 
     * @param n Number of disks
     * @return List of moves in format "A->D"
     */
    public List<String> solve(int n) {
        List<String> moves = new ArrayList<>();  // ✅ Local variable (thread-safe)
        frameStewart(n, 0, 3, 1, 2, moves);  // ✅ Pass as parameter
        return moves;
    }
    
    /**
     * Frame-Stewart algorithm implementation
     * 
     * @param n Number of disks
     * @param source Source peg index
     * @param destination Destination peg index
     * @param aux1 First auxiliary peg index
     * @param aux2 Second auxiliary peg index
     * @param moves List to store moves
     */
    private void frameStewart(int n, int source, int destination, int aux1, int aux2, List<String> moves) {
        if (n == 0) {
            return;
        }
        
        if (n == 1) {
            moves.add(pegs[source] + "->" + pegs[destination]);
            return;
        }
        
        // Find optimal split point k using simplified formula
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
    
    /**
     * Helper method for 3-peg Tower of Hanoi (used in Frame-Stewart)
     */
    private void threePegMove(int n, int source, int destination, int auxiliary, List<String> moves) {
        if (n == 1) {
            moves.add(pegs[source] + "->" + pegs[destination]);
            return;
        }
        
        threePegMove(n - 1, source, auxiliary, destination, moves);
        moves.add(pegs[source] + "->" + pegs[destination]);
        threePegMove(n - 1, auxiliary, destination, source, moves);
    }
    
    /**
     * Get minimum number of moves for n disks with 4 pegs
     * Using dynamic programming to avoid recursion issues
     */
    public int getMinimumMoves(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 3;
        
        // Use dynamic programming array
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        dp[2] = 3;
        
        // Calculate for each number of disks
        for (int i = 3; i <= n; i++) {
            dp[i] = Integer.MAX_VALUE;
            
            // Try all possible split points
            for (int k = 1; k < i; k++) {
                // Cost: move k disks twice (to aux and to dest) + move (i-k) disks once with 3 pegs
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