package com.pdsa.snakeandladder.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> err(Exception e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
