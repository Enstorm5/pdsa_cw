package com.example.traveling_salesman.controller;

import com.example.traveling_salesman.dto.ErrorResponse;
import com.example.traveling_salesman.dto.GameResponse;
import com.example.traveling_salesman.dto.GameRoundResponse;
import com.example.traveling_salesman.dto.SolveAttemptRequest;
import com.example.traveling_salesman.dto.StartGameRequest;
import com.example.traveling_salesman.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8089")
@RequestMapping("/api/game")
public class GameController {

	private final GameService gameService;

	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	@PostMapping("/start")
	public GameRoundResponse start(@RequestBody StartGameRequest request) {
		return gameService.startGame(request);
	}

	@PostMapping("/solve")
	public GameResponse solve(@RequestBody SolveAttemptRequest request) {
		return gameService.submitAttempt(request);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(ex.getMessage()));
	}
}
