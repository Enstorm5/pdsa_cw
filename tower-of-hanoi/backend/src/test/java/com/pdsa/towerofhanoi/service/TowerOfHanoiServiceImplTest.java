package com.pdsa.towerofhanoi.service;

import com.pdsa.towerofhanoi.algorithm.*;
import com.pdsa.towerofhanoi.dto.*;
import com.pdsa.towerofhanoi.model.*;
import com.pdsa.towerofhanoi.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tower of Hanoi Service Tests")
class TowerOfHanoiServiceImplTest {
    
    @Mock
    private GameRoundRepository gameRoundRepository;
    
    @Mock
    private PlayerAnswerRepository playerAnswerRepository;
    
    @Mock
    private AlgorithmPerformanceRepository algorithmPerformanceRepository;
    
    @Mock
    private ThreePegRecursive threePegRecursive;
    
    @Mock
    private ThreePegIterative threePegIterative;
    
    @Mock
    private FourPegFrameStewart fourPegFrameStewart;
    
    @Mock
    private FourPegOptimized fourPegOptimized;
    
    @InjectMocks
    private TowerOfHanoiServiceImpl service;
    
    @BeforeEach
    void setUp() {
        // Initialize real algorithms for testing
        threePegRecursive = new ThreePegRecursive();
        threePegIterative = new ThreePegIterative();
        fourPegFrameStewart = new FourPegFrameStewart();
        fourPegOptimized = new FourPegOptimized();
        
        service = new TowerOfHanoiServiceImpl(
            gameRoundRepository,
            playerAnswerRepository,
            algorithmPerformanceRepository,
            threePegRecursive,
            threePegIterative,
            fourPegFrameStewart,
            fourPegOptimized
        );
    }
    
    @Test
    @DisplayName("Test start new game with 3 pegs")
    void testStartNewGameWithThreePegs() {
        // Arrange
        GameStartRequest request = new GameStartRequest();
        request.setNumberOfPegs(3);
        
        GameRound savedGameRound = new GameRound();
        savedGameRound.setId(1L);
        savedGameRound.setNumberOfDisks(7);
        savedGameRound.setNumberOfPegs(3);
        
        when(gameRoundRepository.save(any(GameRound.class))).thenReturn(savedGameRound);
        when(algorithmPerformanceRepository.save(any(AlgorithmPerformance.class)))
            .thenReturn(new AlgorithmPerformance());
        
        // Act
        GameStartResponse response = service.startNewGame(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getGameRoundId());
        assertTrue(response.getNumberOfDisks() >= 5 && response.getNumberOfDisks() <= 10,
            "Number of disks should be between 5 and 10");
        assertEquals(3, response.getNumberOfPegs());
        assertNotNull(response.getAlgorithm1Result());
        assertNotNull(response.getAlgorithm2Result());
        
        // Verify database operations
        verify(gameRoundRepository, times(1)).save(any(GameRound.class));
        verify(algorithmPerformanceRepository, times(2)).save(any(AlgorithmPerformance.class));
    }
    
    @Test
    @DisplayName("Test start new game with 4 pegs")
    void testStartNewGameWithFourPegs() {
        // Arrange
        GameStartRequest request = new GameStartRequest();
        request.setNumberOfPegs(4);
        
        GameRound savedGameRound = new GameRound();
        savedGameRound.setId(2L);
        savedGameRound.setNumberOfDisks(8);
        savedGameRound.setNumberOfPegs(4);
        
        when(gameRoundRepository.save(any(GameRound.class))).thenReturn(savedGameRound);
        when(algorithmPerformanceRepository.save(any(AlgorithmPerformance.class)))
            .thenReturn(new AlgorithmPerformance());
        
        // Act
        GameStartResponse response = service.startNewGame(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(2L, response.getGameRoundId());
        assertEquals(4, response.getNumberOfPegs());
        assertNotNull(response.getAlgorithm1Result());
        assertNotNull(response.getAlgorithm2Result());
        
        // Verify 4-peg algorithms used fewer moves than 3-peg would
        assertTrue(response.getAlgorithm1Result().getMinimumMoves() < Math.pow(2, response.getNumberOfDisks()) - 1,
            "4-peg should be more efficient than 3-peg");
    }
    
    @Test
    @DisplayName("Test random disk selection is within range")
    void testRandomDiskSelection() {
        // Arrange
        GameStartRequest request = new GameStartRequest();
        request.setNumberOfPegs(3);
        
        when(gameRoundRepository.save(any(GameRound.class))).thenAnswer(invocation -> {
            GameRound gr = invocation.getArgument(0);
            gr.setId(1L);
            return gr;
        });
        when(algorithmPerformanceRepository.save(any(AlgorithmPerformance.class)))
            .thenReturn(new AlgorithmPerformance());
        
        // Act - run multiple times to test randomness
        for (int i = 0; i < 10; i++) {
            GameStartResponse response = service.startNewGame(request);
            
            // Assert
            assertTrue(response.getNumberOfDisks() >= 5, 
                "Number of disks should be at least 5");
            assertTrue(response.getNumberOfDisks() <= 10, 
                "Number of disks should be at most 10");
        }
    }
    
    @Test
    @DisplayName("Test submit correct answer")
    void testSubmitCorrectAnswer() {
        // Arrange
        GameRound gameRound = new GameRound();
        gameRound.setId(1L);
        gameRound.setNumberOfDisks(3);
        gameRound.setNumberOfPegs(3);
        gameRound.setCorrectMinimumMoves(7);
        gameRound.setCorrectMoveSequence("A->D, A->B, D->B, A->D, B->A, B->D, A->D");
        
        PlayerAnswerRequest request = new PlayerAnswerRequest();
        request.setGameRoundId(1L);
        request.setPlayerName("Test Player");
        request.setPlayerMinimumMoves(7);
        request.setPlayerMoveSequence("A->D, A->B, D->B, A->D, B->A, B->D, A->D");
        
        when(gameRoundRepository.findById(1L)).thenReturn(Optional.of(gameRound));
        when(playerAnswerRepository.save(any(PlayerAnswer.class)))
            .thenReturn(new PlayerAnswer());
        
        // Act
        PlayerAnswerResponse response = service.submitAnswer(request);
        
        // Assert
        assertNotNull(response);
        assertEquals("Test Player", response.getPlayerName());
        assertTrue(response.getIsCorrect());
        assertEquals("WIN", response.getResult());
        assertEquals(7, response.getCorrectMinimumMoves());
        
        // Verify player answer was saved
        verify(playerAnswerRepository, times(1)).save(any(PlayerAnswer.class));
    }
    
    @Test
    @DisplayName("Test submit incorrect answer - wrong number of moves")
    void testSubmitIncorrectAnswer() {
        // Arrange
        GameRound gameRound = new GameRound();
        gameRound.setId(1L);
        gameRound.setCorrectMinimumMoves(7);
        gameRound.setCorrectMoveSequence("A->D, A->B, D->B, A->D, B->A, B->D, A->D");
        
        PlayerAnswerRequest request = new PlayerAnswerRequest();
        request.setGameRoundId(1L);
        request.setPlayerName("Test Player");
        request.setPlayerMinimumMoves(10);  // Wrong!
        request.setPlayerMoveSequence("A->D, A->B, D->B, A->D, B->A, B->D, A->D, A->B, A->D, B->D");
        
        when(gameRoundRepository.findById(1L)).thenReturn(Optional.of(gameRound));
        
        // Act
        PlayerAnswerResponse response = service.submitAnswer(request);
        
        // Assert
        assertFalse(response.getIsCorrect());
        assertEquals("LOSE", response.getResult());
        
        // Verify player answer was NOT saved (only correct answers saved)
        verify(playerAnswerRepository, never()).save(any(PlayerAnswer.class));
    }
    
    @Test
    @DisplayName("Test algorithm execution time is recorded")
    void testAlgorithmExecutionTimeRecorded() {
        // Arrange
        GameStartRequest request = new GameStartRequest();
        request.setNumberOfPegs(3);
        
        when(gameRoundRepository.save(any(GameRound.class))).thenAnswer(invocation -> {
            GameRound gr = invocation.getArgument(0);
            gr.setId(1L);
            return gr;
        });
        when(algorithmPerformanceRepository.save(any(AlgorithmPerformance.class)))
            .thenReturn(new AlgorithmPerformance());
        
        // Act
        GameStartResponse response = service.startNewGame(request);
        
        // Assert
        assertNotNull(response.getAlgorithm1Result().getExecutionTimeNanos());
        assertNotNull(response.getAlgorithm2Result().getExecutionTimeNanos());
        assertTrue(response.getAlgorithm1Result().getExecutionTimeNanos() > 0);
        assertTrue(response.getAlgorithm2Result().getExecutionTimeNanos() > 0);
    }
}