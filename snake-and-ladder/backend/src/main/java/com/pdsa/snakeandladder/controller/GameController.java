package com.pdsa.snakeandladder.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdsa.snakeandladder.model.GameRequest;
import com.pdsa.snakeandladder.model.GameResult;
import com.pdsa.snakeandladder.service.GameService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/snake")
@CrossOrigin(origins = "http://localhost:5174")
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @PostMapping("/play")
    public GameResult play(@Valid @RequestBody GameRequest req) {
        return service.playRound(req);
    }

    @GetMapping("/history")
    public List<GameResult> history() {
        return service.history();
    }
}
