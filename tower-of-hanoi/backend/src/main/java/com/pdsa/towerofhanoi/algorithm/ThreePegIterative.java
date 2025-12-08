package com.pdsa.towerofhanoi.algorithm;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Component
public class ThreePegIterative {
    
    /**
     * Solves Tower of Hanoi using iterative approach (3 pegs)
     * Moves from A to C using B as auxiliary
     * Time Complexity: O(2^n)
     * Space Complexity: O(n)
     * 
     * @param n Number of disks
     * @return List of moves in format "A->C"
     */
    public List<String> solve(int n) {
        List<String> moves = new ArrayList<>();  // ✅ Already thread-safe
        
        // Calculate total number of moves
        int totalMoves = (int) Math.pow(2, n) - 1;
        
        // Peg naming (3 pegs only: A, B, C)
        char source = 'A';
        char destination = 'C';
        char auxiliary = 'B';
        
        // If number of disks is even, swap destination and auxiliary
        if (n % 2 == 0) {
            char temp = destination;
            destination = auxiliary;
            auxiliary = temp;
        }
        
        // Create stacks for each peg (3 pegs only)
        Stack<Integer> pegA = new Stack<>();
        Stack<Integer> pegB = new Stack<>();
        Stack<Integer> pegC = new Stack<>();
        
        // Initialize source peg with all disks
        for (int i = n; i >= 1; i--) {
            pegA.push(i);
        }
        
        // Perform moves
        for (int i = 1; i <= totalMoves; i++) {
            if (i % 3 == 1) {
                // Move between source and destination
                moveDisk(pegA, pegC, source, destination, moves);
            } else if (i % 3 == 2) {
                // Move between source and auxiliary
                moveDisk(pegA, pegB, source, auxiliary, moves);
            } else {
                // Move between auxiliary and destination
                moveDisk(pegB, pegC, auxiliary, destination, moves);
            }
        }
        
        return moves;
    }
    
    /**
     * Helper method to move disk between two pegs
     */
    private void moveDisk(Stack<Integer> fromPeg, Stack<Integer> toPeg, 
                         char fromName, char toName, List<String> moves) {
        if (fromPeg.isEmpty() && toPeg.isEmpty()) {
            return;
        }
        
        if (fromPeg.isEmpty()) {
            fromPeg.push(toPeg.pop());
            moves.add(toName + "->" + fromName);
        } else if (toPeg.isEmpty()) {
            toPeg.push(fromPeg.pop());
            moves.add(fromName + "->" + toName);
        } else if (fromPeg.peek() > toPeg.peek()) {
            fromPeg.push(toPeg.pop());
            moves.add(toName + "->" + fromName);
        } else {
            toPeg.push(fromPeg.pop());
            moves.add(fromName + "->" + toName);
        }
    }
    
    /**
     * Calculate minimum number of moves for n disks
     * Formula: 2^n - 1
     */
    public int getMinimumMoves(int n) {
        return (int) Math.pow(2, n) - 1;
    }
    
    public String getAlgorithmName() {
        return "3-Peg Iterative";
    }
}