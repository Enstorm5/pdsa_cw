package com.pdsa.towerofhanoi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameStartResponse {
    
    private Long gameRoundId;
    private Integer numberOfDisks;
    private Integer numberOfPegs;
    private String message;
    
    // Algorithm results for display
    private AlgorithmResult algorithm1Result;
    private AlgorithmResult algorithm2Result;
    private AlgorithmResult algorithm3Result;
    private AlgorithmResult algorithm4Result;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AlgorithmResult {
        private String algorithmName;
        private Integer minimumMoves;
        private Long executionTimeNanos;
        private Double executionTimeMillis;
        private String moveSequence;
    }
}