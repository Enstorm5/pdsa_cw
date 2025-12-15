package com.pdsa.snakeandladder.model;

import jakarta.persistence.*;

@Entity
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName;
    private int boardSize;

    private int correctAnswer;
    private int playerAnswer;
    private boolean correct;

    private long bfsTime;
    private long dijkstraTime;

    private String createdAt = java.time.LocalDateTime.now().toString();

    public GameResult() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getPlayerAnswer() {
        return playerAnswer;
    }

    public void setPlayerAnswer(int playerAnswer) {
        this.playerAnswer = playerAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public long getBfsTime() {
        return bfsTime;
    }

    public void setBfsTime(long bfsTime) {
        this.bfsTime = bfsTime;
    }

    public long getDijkstraTime() {
        return dijkstraTime;
    }

    public void setDijkstraTime(long dijkstraTime) {
        this.dijkstraTime = dijkstraTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
