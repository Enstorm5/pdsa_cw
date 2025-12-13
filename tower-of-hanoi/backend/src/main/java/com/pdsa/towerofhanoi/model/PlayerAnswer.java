//package backend.src.main.java.com.pdsa.towerofhanoi.model;

package com.pdsa.towerofhanoi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "player_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAnswer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String playerName;
    
    @Column(nullable = false)
    private Long gameRoundId;
    
    @Column(nullable = false)
    private Integer playerMinimumMoves;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String playerMoveSequence;
    
    @Column(nullable = false)
    private Boolean isCorrect;
    
    @Column(nullable = false)
    private LocalDateTime submittedAt;
    
    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }
}