//package backend.src.main.java.com.pdsa.towerofhanoi.model;

package com.pdsa.towerofhanoi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "algorithm_performance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmPerformance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long gameRoundId;
    
    @Column(nullable = false)
    private String algorithmName;
    
    @Column(nullable = false)
    private Integer numberOfPegs;
    
    @Column(nullable = false)
    private Long executionTimeNanos;
    
    @Column(nullable = false)
    private Integer minimumMoves;
    
    @Column(nullable = false)
    private LocalDateTime recordedAt;
    
    @PrePersist
    protected void onCreate() {
        recordedAt = LocalDateTime.now();
    }
}