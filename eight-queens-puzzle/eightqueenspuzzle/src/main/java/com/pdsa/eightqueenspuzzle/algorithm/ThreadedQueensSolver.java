package com.pdsa.eightqueenspuzzle.algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ThreadedQueensSolver {
    private List<List<Integer>> solutions;
    private long executionTime;
    
    // thread-safe list for concurrent access
    public ThreadedQueensSolver() {
        this.solutions = new CopyOnWriteArrayList<>();
    }
    
    public void solveThreaded() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        
        //each thread handles one starting row
        for (int row = 0; row < 8; row++) {
            final int startRow = row;
            executor.submit(() -> {
                int[] board = new int[8];
                board[0] = startRow;
                solveNQueens(board, 1);
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        this.executionTime = System.currentTimeMillis() - startTime;
    }
    
    private void solveNQueens(int[] board, int col) {
        if (col == 8) {
            synchronized(solutions) {
                solutions.add(Arrays.stream(board).boxed()
                    .collect(Collectors.toList()));
            }
            return;
        }
        
        for (int row = 0; row < 8; row++) {
            if (isSafe(board, row, col)) {
                board[col] = row;
                solveNQueens(board, col + 1);
            }
        }
    }
    
    private boolean isSafe(int[] board, int row, int col) {
        for (int i = 0; i < col; i++) {
            if (board[i] == row || 
                Math.abs(board[i] - row) == Math.abs(i - col)) {
                return false;
            }
        }
        return true;
    }
    
    //getters
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