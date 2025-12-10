package com.pdsa.eightqueenspuzzle.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class SubmissionRequest {
    
    @NotBlank(message = "Player name is required")
    private String playerName;
    
    @NotNull(message = "Queen positions are required")
    @Size(min = 8, max = 8, message = "Must provide exactly 8 queen positions")
    private List<Integer> queenPositions;
    
    // Constructors
    public SubmissionRequest() {}
    
    public SubmissionRequest(String playerName, List<Integer> queenPositions) {
        this.playerName = playerName;
        this.queenPositions = queenPositions;
    }
    
    // Getters and Setters
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public List<Integer> getQueenPositions() {
        return queenPositions;
    }
    
    public void setQueenPositions(List<Integer> queenPositions) {
        this.queenPositions = queenPositions;
    }
}