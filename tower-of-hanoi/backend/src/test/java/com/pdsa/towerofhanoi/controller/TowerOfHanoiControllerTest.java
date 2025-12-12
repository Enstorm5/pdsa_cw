package com.pdsa.towerofhanoi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdsa.towerofhanoi.dto.*;
import com.pdsa.towerofhanoi.service.TowerOfHanoiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TowerOfHanoiController.class)
@DisplayName("Tower of Hanoi Controller Tests")
class TowerOfHanoiControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private TowerOfHanoiService service;
    
    @Test
    @DisplayName("Test health check endpoint")
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/tower/health"))
            .andExpect(status().isOk())
            .andExpect(content().string("Tower of Hanoi service is running!"));
    }
    
    @Test
    @DisplayName("Test start game with valid 3-peg request")
    void testStartGameWithThreePegs() throws Exception {
        // Arrange
        GameStartRequest request = new GameStartRequest();
        request.setNumberOfPegs(3);
        
        GameStartResponse response = GameStartResponse.builder()
            .gameRoundId(1L)
            .numberOfDisks(7)
            .numberOfPegs(3)
            .message("Game started! Try to solve the puzzle.")
            .algorithm1Result(GameStartResponse.AlgorithmResult.builder()
                .algorithmName("3-Peg Recursive")
                .minimumMoves(127)
                .executionTimeNanos(100000L)
                .executionTimeMillis(0.1)
                .moveSequence("A->D, A->B, ...")
                .build())
            .build();
        
        when(service.startNewGame(any(GameStartRequest.class))).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(post("/api/tower/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.gameRoundId").value(1))
            .andExpect(jsonPath("$.numberOfDisks").value(7))
            .andExpect(jsonPath("$.numberOfPegs").value(3))
            .andExpect(jsonPath("$.algorithm1Result.algorithmName").value("3-Peg Recursive"));
    }
    
    @Test
    @DisplayName("Test start game with valid 4-peg request")
    void testStartGameWithFourPegs() throws Exception {
        // Arrange
        GameStartRequest request = new GameStartRequest();
        request.setNumberOfPegs(4);
        
        GameStartResponse response = GameStartResponse.builder()
            .gameRoundId(2L)
            .numberOfDisks(8)
            .numberOfPegs(4)
            .message("Game started! Try to solve the puzzle.")
            .build();
        
        when(service.startNewGame(any(GameStartRequest.class))).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(post("/api/tower/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.numberOfPegs").value(4));
    }
    
    @Test
    @DisplayName("Test start game with invalid pegs - should return 400")
    void testStartGameWithInvalidPegs() throws Exception {
        // Arrange
        GameStartRequest request = new GameStartRequest();
        request.setNumberOfPegs(5);  // Invalid! Must be 3 or 4
        
        // Act & Assert
        mockMvc.perform(post("/api/tower/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400));
    }
    
    @Test
    @DisplayName("Test start game with null pegs - should return 400")
    void testStartGameWithNullPegs() throws Exception {
        // Arrange
        GameStartRequest request = new GameStartRequest();
        // numberOfPegs is null
        
        // Act & Assert
        mockMvc.perform(post("/api/tower/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("Test submit answer with valid data")
    void testSubmitAnswer() throws Exception {
        // Arrange
        PlayerAnswerRequest request = new PlayerAnswerRequest();
        request.setGameRoundId(1L);
        request.setPlayerName("Test Player");
        request.setPlayerMinimumMoves(7);
        request.setPlayerMoveSequence("A->D, A->B, D->B, A->D, B->A, B->D, A->D");
        
        PlayerAnswerResponse response = PlayerAnswerResponse.builder()
            .playerName("Test Player")
            .isCorrect(true)
            .result("WIN")
            .message("Congratulations! You solved it correctly!")
            .correctMinimumMoves(7)
            .playerMinimumMoves(7)
            .build();
        
        when(service.submitAnswer(any(PlayerAnswerRequest.class))).thenReturn(response);
        
        
        mockMvc.perform(post("/api/tower/submit-answer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.playerName").value("Test Player"))
            .andExpect(jsonPath("$.isCorrect").value(true))
            .andExpect(jsonPath("$.result").value("WIN"));
    }
    
    @Test
    @DisplayName("Test submit answer with empty player name - should return 400")
    void testSubmitAnswerWithEmptyPlayerName() throws Exception {
        // Arrange
        PlayerAnswerRequest request = new PlayerAnswerRequest();
        request.setGameRoundId(1L);
        request.setPlayerName("");  // Empty!
        request.setPlayerMinimumMoves(7);
        request.setPlayerMoveSequence("A->D");
        
        // Act & Assert
        mockMvc.perform(post("/api/tower/submit-answer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("Test get performance stats")
    void testGetPerformanceStats() throws Exception {
        // Arrange
        when(service.getPerformanceStats()).thenReturn(new ArrayList<>());
        
        // Act & Assert
        mockMvc.perform(get("/api/tower/performance/stats"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    @DisplayName("Test get performance stats by algorithm name")
    void testGetPerformanceStatsByAlgorithm() throws Exception {
        // Arrange
        PerformanceStatsResponse response = PerformanceStatsResponse.builder()
            .algorithmName("3-Peg Recursive")
            .numberOfPegs(3)
            .records(new ArrayList<>())
            .build();
        
        when(service.getPerformanceStatsByAlgorithm(any(String.class))).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(get("/api/tower/performance/stats/3-Peg Recursive"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.algorithmName").value("3-Peg Recursive"));
    }
}