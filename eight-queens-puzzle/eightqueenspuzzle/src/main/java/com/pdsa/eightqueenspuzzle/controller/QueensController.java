package com.pdsa.eightqueenspuzzle.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import com.pdsa.eightqueenspuzzle.service.QueensService;
import com.pdsa.eightqueenspuzzle.dto.request.SubmissionRequest;
import com.pdsa.eightqueenspuzzle.dto.response.SubmissionResponse;
import com.pdsa.eightqueenspuzzle.dto.response.AlgorithmResponse;
import com.pdsa.eightqueenspuzzle.dto.response.GameStatsResponse;

@RestController
@RequestMapping("/api/queens")
@CrossOrigin(origins = "http://localhost:5175")
public class QueensController {
    
    @Autowired
    private QueensService queensService;
    
    // 5.1.1 & 5.1.6 - Run sequential algorithm
    @PostMapping("/solve/sequential")
    public ResponseEntity<AlgorithmResponse> solveSequential() {
        return ResponseEntity.ok(queensService.runSequentialAlgorithm());
    }
    
    // 5.1.2 & 5.1.6 - Run threaded algorithm
    @PostMapping("/solve/threaded")
    public ResponseEntity<AlgorithmResponse> solveThreaded() {
        return ResponseEntity.ok(queensService.runThreadedAlgorithm());
    }
    
    // 5.1.3 & 5.1.4 - Submit player solution
    @PostMapping("/submit")
    public ResponseEntity<SubmissionResponse> submitSolution(
            @RequestBody @Valid SubmissionRequest request) {
        return ResponseEntity.ok(queensService.submitSolution(request));
    }
    
    // Get game statistics
    @GetMapping("/stats")
    public ResponseEntity<GameStatsResponse> getStats() {
        return ResponseEntity.ok(queensService.getGameStats());
    }
    
    // 5.1.5 - Reset game (clear all found flags)
    @PostMapping("/reset")
    public ResponseEntity<String> resetGame() {
        queensService.resetAllSolutions();
        return ResponseEntity.ok("Game reset successfully");
    }
}