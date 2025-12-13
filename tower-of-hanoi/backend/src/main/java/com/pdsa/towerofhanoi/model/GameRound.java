package com.pdsa.towerofhanoi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_rounds")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRound {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer numberOfDisks;
    
    @Column(nullable = false)
    private Integer numberOfPegs;
    
    @Column(nullable = false)
    private Integer correctMinimumMoves;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String correctMoveSequence;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}