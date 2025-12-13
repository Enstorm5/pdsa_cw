package com.pdsa.towerofhanoi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAnswerRequest {
    
    @NotNull(message = "Game round ID is required")
    private Long gameRoundId;
    
    @NotBlank(message = "Player name is required")
    private String playerName;
    
    @NotNull(message = "Number of moves is required")
    @Min(value = 1, message = "Number of moves must be at least 1")
    private Integer playerMinimumMoves;
    
    @NotBlank(message = "Move sequence is required")
    private String playerMoveSequence;
}