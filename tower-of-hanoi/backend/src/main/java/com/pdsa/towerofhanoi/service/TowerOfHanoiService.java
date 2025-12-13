package com.pdsa.towerofhanoi.service;

import com.pdsa.towerofhanoi.dto.*;
import java.util.List;

public interface TowerOfHanoiService {
    
    /**
     * Start a new game round
     * - Randomly selects number of disks (5-10)
     * - Executes all 4 algorithms
     * - Saves performance data to database
     */
    GameStartResponse startNewGame(GameStartRequest request);
    
    /**
     * Submit player's answer and validate
     * - Compares with correct answer
     * - Saves to database if correct
     * - Returns WIN/LOSE/DRAW result
     */
    PlayerAnswerResponse submitAnswer(PlayerAnswerRequest request);
    
    /**
     * Get performance statistics for all algorithms
     * Used for generating charts in reports
     */
    List<PerformanceStatsResponse> getPerformanceStats();
    
    /**
     * Get performance statistics for specific algorithm
     */
    PerformanceStatsResponse getPerformanceStatsByAlgorithm(String algorithmName);
}