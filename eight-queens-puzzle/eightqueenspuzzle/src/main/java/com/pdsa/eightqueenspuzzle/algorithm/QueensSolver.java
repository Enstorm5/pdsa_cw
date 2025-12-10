package com.pdsa.eightqueenspuzzle.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QueensSolver {
    private List<List<Integer>> solutions;
    private long executionTime;
    
    // Why ArrayList? - Dynamic size, fast access for storing solutions
    public QueensSolver() {
        this.solutions = new ArrayList<>();
    }
    
    public void solveSequential() {
        long startTime = System.currentTimeMillis();
        int[] board = new int[8]; // Array to store queen positions
        solveNQueens(board, 0);
        this.executionTime = System.currentTimeMillis() - startTime;
    }
    
    // Backtracking algorithm - explores all possibilities efficiently
    private void solveNQueens(int[] board, int col) {
        if (col == 8) {
            // Found a solution, add it to the list
            solutions.add(Arrays.stream(board).boxed().collect(Collectors.toList()));
            return;
        }
        
        for (int row = 0; row < 8; row++) {
            if (isSafe(board, row, col)) {
                board[col] = row;
                solveNQueens(board, col + 1);
            }
        }
    }
    
    // Check if placing a queen at (row, col) is safe
    private boolean isSafe(int[] board, int row, int col) {
        for (int i = 0; i < col; i++) {
            // Check row and diagonal conflicts
            if (board[i] == row || 
                Math.abs(board[i] - row) == Math.abs(i - col)) {
                return false;
            }
        }
        return true;
    }
    
    // Getters
    public List<List<Integer>> getSolutions() {
        return solutions;
    }
    
    public long getExecutionTime() {
        return executionTime;
    }
    
    public int getSolutionCount() {
        return solutions.size();
    }
}