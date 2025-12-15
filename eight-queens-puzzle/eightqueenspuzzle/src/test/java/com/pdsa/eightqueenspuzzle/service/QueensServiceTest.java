package com.pdsa.eightqueenspuzzle.service;

import com.pdsa.eightqueenspuzzle.dto.request.SubmissionRequest;
import com.pdsa.eightqueenspuzzle.dto.response.AlgorithmResponse;
import com.pdsa.eightqueenspuzzle.dto.response.GameStatsResponse;
import com.pdsa.eightqueenspuzzle.dto.response.SubmissionResponse;
import com.pdsa.eightqueenspuzzle.exception.ValidationException;
import com.pdsa.eightqueenspuzzle.model.AlgorithmPerformance;
import com.pdsa.eightqueenspuzzle.model.Player;
import com.pdsa.eightqueenspuzzle.model.Solution;
import com.pdsa.eightqueenspuzzle.repository.PerformanceRepository;
import com.pdsa.eightqueenspuzzle.repository.PlayerRepository;
import com.pdsa.eightqueenspuzzle.repository.SolutionRepository;
import com.pdsa.eightqueenspuzzle.repository.SubmissionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Queens Service Tests")
public class QueensServiceTest {
    
    @Mock
    private PlayerRepository playerRepository;
    
    @Mock
    private SolutionRepository solutionRepository;
    
    @Mock
    private SubmissionRepository submissionRepository;
    
    @Mock
    private PerformanceRepository performanceRepository;
    
    @InjectMocks
    private QueensService queensService;
    
    @Test
    @DisplayName("Should run sequential algorithm successfully")
    public void testRunSequentialAlgorithm() {
        // Act
        AlgorithmResponse response = queensService.runSequentialAlgorithm();
        
        // Assert
        assertNotNull(response);
        assertEquals(AlgorithmPerformance.AlgorithmType.SEQUENTIAL, response.getAlgorithmType());
        assertEquals(92, response.getTotalSolutions());
        assertTrue(response.getExecutionTimeMs() > 0);
        assertNotNull(response.getMessage());
        
        // Verify performance was saved
        verify(performanceRepository, times(1)).save(any(AlgorithmPerformance.class));
    }
    
    @Test
    @DisplayName("Should run threaded algorithm successfully")
    public void testRunThreadedAlgorithm() {
        // Act
        AlgorithmResponse response = queensService.runThreadedAlgorithm();
        
        // Assert
        assertNotNull(response);
        assertEquals(AlgorithmPerformance.AlgorithmType.THREADED, response.getAlgorithmType());
        assertEquals(92, response.getTotalSolutions());
        assertTrue(response.getExecutionTimeMs() > 0);
        assertNotNull(response.getMessage());
        
        // Verify performance was saved
        verify(performanceRepository, times(1)).save(any(AlgorithmPerformance.class));
    }
    
    @Test
    @DisplayName("Should accept valid new solution")
    public void testSubmitValidNewSolution() {
        // Arrange
        SubmissionRequest request = new SubmissionRequest(
            "TestPlayer",
            Arrays.asList(0, 4, 7, 5, 2, 6, 1, 3)
        );
        
        Player player = new Player("TestPlayer");
        Solution solution = new Solution("0,4,7,5,2,6,1,3");
        solution.setFound(false);
        
        when(playerRepository.findByPlayerName("TestPlayer")).thenReturn(Optional.of(player));
        when(solutionRepository.findBySolutionData("0,4,7,5,2,6,1,3")).thenReturn(Optional.of(solution));
        when(solutionRepository.countByIsFoundTrue()).thenReturn(1);
        
        // Act
        SubmissionResponse response = queensService.submitSolution(request);
        
        // Assert
        assertTrue(response.isAccepted());
        assertTrue(response.getMessage().contains("accepted"));
        assertEquals(1, response.getFoundCount());
        assertEquals(92, response.getTotalSolutions());
        
        // Verify solution was marked as found
        verify(solutionRepository, times(1)).save(solution);
        assertTrue(solution.isFound());
    }
    
    @Test
    @DisplayName("Should reject duplicate solution")
    public void testRejectDuplicateSolution() {
        // Arrange
        SubmissionRequest request = new SubmissionRequest(
            "TestPlayer",
            Arrays.asList(0, 4, 7, 5, 2, 6, 1, 3)
        );
        
        Solution solution = new Solution("0,4,7,5,2,6,1,3");
        solution.setFound(true); // Already found
        
        when(solutionRepository.findBySolutionData("0,4,7,5,2,6,1,3")).thenReturn(Optional.of(solution));
        when(solutionRepository.countByIsFoundTrue()).thenReturn(5);
        
        // Act
        SubmissionResponse response = queensService.submitSolution(request);
        
        // Assert
        assertFalse(response.isAccepted());
        assertTrue(response.getMessage().contains("already discovered"));
        assertEquals(5, response.getFoundCount());
    }
    
    @Test
    @DisplayName("Should throw exception for invalid solution")
    public void testRejectInvalidSolution() {
        // Arrange - queens on same row
        SubmissionRequest request = new SubmissionRequest(
            "TestPlayer",
            Arrays.asList(0, 0, 7, 5, 2, 6, 1, 3)
        );
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            queensService.submitSolution(request);
        });
        
        assertTrue(exception.getMessage().contains("Invalid solution"));
    }
    
    @Test
    @DisplayName("Should throw exception for empty player name")
    public void testRejectEmptyPlayerName() {
        // Arrange
        SubmissionRequest request = new SubmissionRequest(
            "",
            Arrays.asList(0, 4, 7, 5, 2, 6, 1, 3)
        );
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            queensService.submitSolution(request);
        });
        
        assertTrue(exception.getMessage().contains("Player name is required"));
    }
    
    @Test
    @DisplayName("Should throw exception for wrong number of queens")
    public void testRejectWrongNumberOfQueens() {
        // Arrange - only 5 queens
        SubmissionRequest request = new SubmissionRequest(
            "TestPlayer",
            Arrays.asList(0, 4, 7, 5, 2)
        );
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            queensService.submitSolution(request);
        });
        
        assertTrue(exception.getMessage().contains("exactly 8 queen positions"));
    }
    
    @Test
    @DisplayName("Should get game statistics")
    public void testGetGameStats() {
        // Arrange
        when(solutionRepository.countByIsFoundTrue()).thenReturn(10);
        when(playerRepository.count()).thenReturn(5L);
        
        // Act
        GameStatsResponse stats = queensService.getGameStats();
        
        // Assert
        assertNotNull(stats);
        assertEquals(92, stats.getTotalSolutions());
        assertEquals(10, stats.getFoundSolutions());
        assertEquals(82, stats.getRemainingSolutions());
        assertEquals(5, stats.getTotalPlayers());
    }
    
    @Test
    @DisplayName("Should reset all solutions")
    public void testResetAllSolutions() {
        // Arrange
        Solution sol1 = new Solution("0,4,7,5,2,6,1,3");
        sol1.setFound(true);
        Solution sol2 = new Solution("0,5,7,2,6,3,1,4");
        sol2.setFound(true);
        
        when(solutionRepository.findAll()).thenReturn(Arrays.asList(sol1, sol2));
        
        // Act
        queensService.resetAllSolutions();
        
        // Assert
        assertFalse(sol1.isFound());
        assertFalse(sol2.isFound());
        verify(solutionRepository, times(1)).saveAll(anyList());
    }
}