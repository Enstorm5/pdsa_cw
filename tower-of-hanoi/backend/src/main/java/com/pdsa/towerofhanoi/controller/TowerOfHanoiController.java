package com.pdsa.towerofhanoi.controller;

import com.pdsa.towerofhanoi.dto.*;
import com.pdsa.towerofhanoi.service.TowerOfHanoiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tower")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Allow frontend to access
public class TowerOfHanoiController {
    
    private final TowerOfHanoiService towerOfHanoiService;
    
    /**
     * Start a new game round
     * POST /api/tower/start
     * Body: { "numberOfPegs": 3 }
     */
    @PostMapping("/start")
    public ResponseEntity<GameStartResponse> startGame(
            @Valid @RequestBody GameStartRequest request) {
        
        log.info("Received request to start game with {} pegs", request.getNumberOfPegs());
        
        GameStartResponse response = towerOfHanoiService.startNewGame(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Submit player's answer
     * POST /api/tower/submit-answer
     * Body: {
     *   "gameRoundId": 1,
     *   "playerName": "John",
     *   "playerMinimumMoves": 31,
     *   "playerMoveSequence": "A->C, A->B, ..."
     * }
     */
    @PostMapping("/submit-answer")
    public ResponseEntity<PlayerAnswerResponse> submitAnswer(
            @Valid @RequestBody PlayerAnswerRequest request) {
        
        log.info("Received answer from player {} for game round {}", 
                 request.getPlayerName(), request.getGameRoundId());
        
        PlayerAnswerResponse response = towerOfHanoiService.submitAnswer(request);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get performance statistics for all algorithms
     * GET /api/tower/performance/stats
     * 
     * Used for generating charts in individual report
     */
    @GetMapping("/performance/stats")
    public ResponseEntity<List<PerformanceStatsResponse>> getPerformanceStats() {
        
        log.info("Received request for performance statistics");
        
        List<PerformanceStatsResponse> response = towerOfHanoiService.getPerformanceStats();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get performance statistics for specific algorithm
     * GET /api/tower/performance/stats/{algorithmName}
     * 
     * Example: /api/tower/performance/stats/3-Peg Recursive
     */
    @GetMapping("/performance/stats/{algorithmName}")
    public ResponseEntity<PerformanceStatsResponse> getPerformanceStatsByAlgorithm(
            @PathVariable String algorithmName) {
        
        log.info("Received request for performance statistics of algorithm: {}", algorithmName);
        
        PerformanceStatsResponse response = 
            towerOfHanoiService.getPerformanceStatsByAlgorithm(algorithmName);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Health check endpoint
     * GET /api/tower/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Tower of Hanoi service is running!");
    }
}