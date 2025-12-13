package com.pdsa.towerofhanoi.exception;

public class InvalidMoveSequenceException extends RuntimeException {
    
    public InvalidMoveSequenceException(String message) {
        super(message);
    }
    
    public InvalidMoveSequenceException(String playerName, String reason) {
        super(String.format("Invalid move sequence from player '%s': %s", playerName, reason));
    }
}