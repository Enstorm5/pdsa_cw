package com.pdsa.eightqueenspuzzle.dto.response;

public class SubmissionResponse {
    
    private boolean accepted;
    private String message;
    private int foundCount;
    private int totalSolutions;
    
    // Constructors
    public SubmissionResponse() {}
    
    public SubmissionResponse(boolean accepted, String message, int foundCount, int totalSolutions) {
        this.accepted = accepted;
        this.message = message;
        this.foundCount = foundCount;
        this.totalSolutions = totalSolutions;
    }
    
    // Getters and Setters
    public boolean isAccepted() {
        return accepted;
    }
    
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public int getFoundCount() {
        return foundCount;
    }
    
    public void setFoundCount(int foundCount) {
        this.foundCount = foundCount;
    }
    
    public int getTotalSolutions() {
        return totalSolutions;
    }
    
    public void setTotalSolutions(int totalSolutions) {
        this.totalSolutions = totalSolutions;
    }
}