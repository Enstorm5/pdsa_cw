package com.pdsa.towerofhanoi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerAnswerResponse {
    
    private Long answerId;
    private String playerName;
    private Boolean isCorrect;
    private String result; // "WIN", "LOSE", or "DRAW"
    private String message;
    
    // Correct answer details
    private Integer correctMinimumMoves;
    private String correctMoveSequence;
    
    // Player's answer
    private Integer playerMinimumMoves;
    private String playerMoveSequence;
}