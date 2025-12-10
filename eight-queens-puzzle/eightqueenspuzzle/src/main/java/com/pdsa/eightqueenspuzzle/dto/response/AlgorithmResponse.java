package com.pdsa.eightqueenspuzzle.dto.response;

import com.pdsa.eightqueenspuzzle.dto.response.AlgorithmResponse;
import com.pdsa.eightqueenspuzzle.model.AlgorithmPerformance;

public class AlgorithmResponse {
    
    private AlgorithmPerformance.AlgorithmType algorithmType;
    private long executionTimeMs;
    private int totalSolutions;
    private String message;
    
    // Constructors
    public AlgorithmResponse() {}
    
    public AlgorithmResponse(AlgorithmPerformance.AlgorithmType algorithmType, 
                            long executionTimeMs, 
                            int totalSolutions, 
                            String message) {
        this.algorithmType = algorithmType;
        this.executionTimeMs = executionTimeMs;
        this.totalSolutions = totalSolutions;
        this.message = message;
    }
    
    // Getters and Setters
    public AlgorithmPerformance.AlgorithmType getAlgorithmType() {
        return algorithmType;
    }
    
    public void setAlgorithmType(AlgorithmPerformance.AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
    
    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
    
    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }
    
    public int getTotalSolutions() {
        return totalSolutions;
    }
    
    public void setTotalSolutions(int totalSolutions) {
        this.totalSolutions = totalSolutions;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}