package com.example.trafficsim.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PlayerResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String playerName;
    private int reportedMaxFlow;
    private int correctMaxFlow;
    private boolean correct;
    private long ekMillis;
    private long dinicMillis;
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters and setters
    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}
    public String getPlayerName(){return playerName;}
    public void setPlayerName(String playerName){this.playerName = playerName;}
    public int getReportedMaxFlow(){return reportedMaxFlow;}
    public void setReportedMaxFlow(int reportedMaxFlow){this.reportedMaxFlow = reportedMaxFlow;}
    public int getCorrectMaxFlow(){return correctMaxFlow;}
    public void setCorrectMaxFlow(int correctMaxFlow){this.correctMaxFlow = correctMaxFlow;}
    public boolean isCorrect(){return correct;}
    public void setCorrect(boolean correct){this.correct = correct;}
    public long getEkMillis(){return ekMillis;}
    public void setEkMillis(long ekMillis){this.ekMillis = ekMillis;}
    public long getDinicMillis(){return dinicMillis;}
    public void setDinicMillis(long dinicMillis){this.dinicMillis = dinicMillis;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt = createdAt;}
}
