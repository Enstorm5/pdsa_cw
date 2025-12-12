package com.pdsa.eightqueenspuzzle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdsa.eightqueenspuzzle.dto.request.SubmissionRequest;
import com.pdsa.eightqueenspuzzle.dto.response.AlgorithmResponse;
import com.pdsa.eightqueenspuzzle.dto.response.GameStatsResponse;
import com.pdsa.eightqueenspuzzle.dto.response.SubmissionResponse;
import com.pdsa.eightqueenspuzzle.model.AlgorithmPerformance;
import com.pdsa.eightqueenspuzzle.service.QueensService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

@WebMvcTest(QueensController.class)
@DisplayName("Queens Controller Tests")
public class QueensControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private QueensService queensService;
    
    @Test
    @DisplayName("POST /api/queens/solve/sequential should return algorithm response")
    public void testSolveSequential() throws Exception {
        // Arrange
        AlgorithmResponse response = new AlgorithmResponse(
            AlgorithmPerformance.AlgorithmType.SEQUENTIAL,
            45L,
            92,
            "Sequential algorithm completed successfully"
        );
        
        when(queensService.runSequentialAlgorithm()).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(post("/api/queens/solve/sequential"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.algorithmType").value("SEQUENTIAL"))
            .andExpect(jsonPath("$.executionTimeMs").value(45))
            .andExpect(jsonPath("$.totalSolutions").value(92))
            .andExpect(jsonPath("$.message").exists());
        
        verify(queensService, times(1)).runSequentialAlgorithm();
    }
    
    @Test
    @DisplayName("POST /api/queens/solve/threaded should return algorithm response")
    public void testSolveThreaded() throws Exception {
        // Arrange
        AlgorithmResponse response = new AlgorithmResponse(
            AlgorithmPerformance.AlgorithmType.THREADED,
            20L,
            92,
            "Threaded algorithm completed successfully"
        );
        
        when(queensService.runThreadedAlgorithm()).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(post("/api/queens/solve/threaded"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.algorithmType").value("THREADED"))
            .andExpect(jsonPath("$.executionTimeMs").value(20))
            .andExpect(jsonPath("$.totalSolutions").value(92));
        
        verify(queensService, times(1)).runThreadedAlgorithm();
    }
    
    @Test
    @DisplayName("POST /api/queens/submit should accept valid solution")
    public void testSubmitSolution() throws Exception {
        // Arrange
        SubmissionRequest request = new SubmissionRequest(
            "TestPlayer",
            Arrays.asList(0, 4, 7, 5, 2, 6, 1, 3)
        );
        
        SubmissionResponse response = new SubmissionResponse(
            true,
            "New solution accepted! Well done!",
            1,
            92
        );
        
        when(queensService.submitSolution(any(SubmissionRequest.class))).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(post("/api/queens/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accepted").value(true))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.foundCount").value(1))
            .andExpect(jsonPath("$.totalSolutions").value(92));
        
        verify(queensService, times(1)).submitSolution(any(SubmissionRequest.class));
    }
    
    @Test
    @DisplayName("GET /api/queens/stats should return game statistics")
    public void testGetStats() throws Exception {
        // Arrange
        GameStatsResponse stats = new GameStatsResponse(92, 10, 82, 5, 45L, 20L);
        
        when(queensService.getGameStats()).thenReturn(stats);
        
        // Act & Assert
        mockMvc.perform(get("/api/queens/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalSolutions").value(92))
            .andExpect(jsonPath("$.foundSolutions").value(10))
            .andExpect(jsonPath("$.remainingSolutions").value(82))
            .andExpect(jsonPath("$.totalPlayers").value(5))
            .andExpect(jsonPath("$.lastSequentialTime").value(45))
            .andExpect(jsonPath("$.lastThreadedTime").value(20));
        
        verify(queensService, times(1)).getGameStats();
    }
    
    @Test
    @DisplayName("POST /api/queens/reset should reset game")
    public void testResetGame() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/queens/reset"))
            .andExpect(status().isOk())
            .andExpect(content().string("Game reset successfully"));
        
        verify(queensService, times(1)).resetAllSolutions();
    }
}