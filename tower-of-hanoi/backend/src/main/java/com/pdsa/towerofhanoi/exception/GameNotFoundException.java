package com.pdsa.towerofhanoi.exception;

public class GameNotFoundException extends RuntimeException {
    
    public GameNotFoundException(Long gameRoundId) {
        super("Game round not found with ID: " + gameRoundId);
    }
    
    public GameNotFoundException(String message) {
        super(message);
    }
}