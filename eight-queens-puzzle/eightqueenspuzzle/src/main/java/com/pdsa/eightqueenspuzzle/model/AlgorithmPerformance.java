package com.pdsa.eightqueenspuzzle.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "algorithm_performance")
public class AlgorithmPerformance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_id")
    private Long performanceId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "algorithm_type", nullable = false)
    private AlgorithmType algorithmType;
    
    @Column(name = "execution_time_ms", nullable = false)
    private Long executionTimeMs;
    
    @Column(name = "total_solutions_found", nullable = false)
    private Integer totalSolutionsFound;
    
    @Column(name = "executed_at")
    private LocalDateTime executedAt;
    
    // Constructors
    public AlgorithmPerformance() {
        this.executedAt = LocalDateTime.now();
    }
    
    public AlgorithmPerformance(AlgorithmType algorithmType, Long executionTimeMs, Integer totalSolutionsFound) {
        this.algorithmType = algorithmType;
        this.executionTimeMs = executionTimeMs;
        this.totalSolutionsFound = totalSolutionsFound;
        this.executedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getPerformanceId() {
        return performanceId;
    }
    
    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }
    
    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }
    
    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
    
    public Long getExecutionTimeMs() {
        return executionTimeMs;
    }
    
    public void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }
    
    public Integer getTotalSolutionsFound() {
        return totalSolutionsFound;
    }
    
    public void setTotalSolutionsFound(Integer totalSolutionsFound) {
        this.totalSolutionsFound = totalSolutionsFound;
    }
    
    public LocalDateTime getExecutedAt() {
        return executedAt;
    }
    
    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }
    
    // Enum for algorithm types
    public enum AlgorithmType {
        SEQUENTIAL,
        THREADED
    }
}