package com.pdsa.towerofhanoi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceStatsResponse {
    
    private String algorithmName;
    private Integer numberOfPegs;
    private List<PerformanceRecord> records;
    private Double averageExecutionTimeMillis;
    private Long minExecutionTimeNanos;
    private Long maxExecutionTimeNanos;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PerformanceRecord {
        private Long gameRoundId;
        private Integer numberOfDisks;
        private Integer minimumMoves;
        private Long executionTimeNanos;
        private Double executionTimeMillis;
    }
}