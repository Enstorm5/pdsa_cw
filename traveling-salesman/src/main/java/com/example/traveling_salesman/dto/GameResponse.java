package com.example.traveling_salesman.dto;

import java.util.ArrayList;
import java.util.List;

public class GameResponse {

    private Long sessionId;
    private String playerName;
    private String homeCity;
    private boolean correct;
    private int submittedDistance;
    private int optimalDistance;
    private List<String> optimalPath = new ArrayList<>();
    private String message;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public int getSubmittedDistance() {
        return submittedDistance;
    }

    public void setSubmittedDistance(int submittedDistance) {
        this.submittedDistance = submittedDistance;
    }

    public int getOptimalDistance() {
        return optimalDistance;
    }

    public void setOptimalDistance(int optimalDistance) {
        this.optimalDistance = optimalDistance;
    }

    public List<String> getOptimalPath() {
        return optimalPath;
    }

    public void setOptimalPath(List<String> optimalPath) {
        this.optimalPath = optimalPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
