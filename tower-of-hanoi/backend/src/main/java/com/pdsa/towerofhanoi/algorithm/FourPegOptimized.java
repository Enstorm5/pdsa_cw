package com.pdsa.towerofhanoi.algorithm;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class FourPegOptimized {
    
    public List<String> solve(int n) {
        List<String> moves = new ArrayList<>();  
        solveOptimized(n, 'A', 'D', 'B', 'C', moves);  
        return moves;
    }
    
    
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
        
        
        // Move top (n-2) disks to aux1 using all pegs
        solveOptimized(n - 2, source, aux1, aux2, destination, moves);
        
        // Move 2 largest disks directly to destination
        moves.add(source + "->" + aux2);
        moves.add(source + "->" + destination);
        moves.add(aux2 + "->" + destination);
        
        // Move (n-2) disks from aux1 to destination using all pegs
        solveOptimized(n - 2, aux1, destination, source, aux2, moves);
    }
    
    
    //approximate minimum number of moves calculation
    public int getMinimumMoves(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 3;
        
        //  2*T(n-2) + 3
        return 2 * getMinimumMoves(n - 2) + 3;
    }
    
    public String getAlgorithmName() {
        return "4-Peg Optimized";
    }
}