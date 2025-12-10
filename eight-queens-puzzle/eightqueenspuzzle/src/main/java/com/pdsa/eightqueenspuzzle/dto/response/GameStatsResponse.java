package com.pdsa.eightqueenspuzzle.dto.response;
public class GameStatsResponse {
    
    private int totalSolutions;
    private int foundSolutions;
    private int remainingSolutions;
    private long totalPlayers;
    private Long lastSequentialTime;
    private Long lastThreadedTime;
    
    // Constructors
    public GameStatsResponse() {}
    
    public GameStatsResponse(int totalSolutions, int foundSolutions, int remainingSolutions, long totalPlayers, Long lastSequentialTime, Long lastThreadedTime) {
        this.totalSolutions = totalSolutions;
        this.foundSolutions = foundSolutions;
        this.remainingSolutions = remainingSolutions;
        this.totalPlayers = totalPlayers;
        this.lastSequentialTime = lastSequentialTime;
        this.lastThreadedTime = lastThreadedTime;
    }
    
    // Getters and Setters
    public int getTotalSolutions() {
        return totalSolutions;
    }
    
    public void setTotalSolutions(int totalSolutions) {
        this.totalSolutions = totalSolutions;
    }
    
    public int getFoundSolutions() {
        return foundSolutions;
    }
    
    public void setFoundSolutions(int foundSolutions) {
        this.foundSolutions = foundSolutions;
    }
    
    public int getRemainingSolutions() {
        return remainingSolutions;
    }
    
    public void setRemainingSolutions(int remainingSolutions) {
        this.remainingSolutions = remainingSolutions;
    }
    
    public long getTotalPlayers() {
        return totalPlayers;
    }
    
    public void setTotalPlayers(long totalPlayers) {
        this.totalPlayers = totalPlayers;
    }
    
    public Long getLastSequentialTime() {
        return lastSequentialTime;
    }
    
    public void setLastSequentialTime(Long lastSequentialTime) {
        this.lastSequentialTime = lastSequentialTime;
    }
    
    public Long getLastThreadedTime() {
        return lastThreadedTime;
    }
    
    public void setLastThreadedTime(Long lastThreadedTime) {
        this.lastThreadedTime = lastThreadedTime;
    }
}