package com.pdsa.eightqueenspuzzle.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "players")
public class Player {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long playerId;
    
    @Column(name = "player_name", nullable = false, unique = true, length = 100)
    private String playerName;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public Player() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Player(String playerName) {
        this.playerName = playerName;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}