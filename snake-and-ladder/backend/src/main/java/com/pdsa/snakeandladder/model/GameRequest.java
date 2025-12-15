package com.pdsa.snakeandladder.model;

import jakarta.validation.constraints.*;

public class GameRequest {
    @NotBlank
    private String playerName;

    @Min(6)
    @Max(12)
    private int boardSize;

    @Min(1)
    private int playerAnswer;

    public GameRequest() {}

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

    public int getPlayerAnswer() {
        return playerAnswer;
    }

    public void setPlayerAnswer(int playerAnswer) {
        this.playerAnswer = playerAnswer;
    }
}
