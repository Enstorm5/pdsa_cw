package com.pdsa.eightqueenspuzzle.intergration;

import com.pdsa.eightqueenspuzzle.dto.request.SubmissionRequest;
import com.pdsa.eightqueenspuzzle.dto.response.AlgorithmResponse;
import com.pdsa.eightqueenspuzzle.dto.response.GameStatsResponse;
import com.pdsa.eightqueenspuzzle.dto.response.SubmissionResponse;
import com.pdsa.eightqueenspuzzle.service.QueensService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("Full Game Flow Integration Test")
public class FullGameFlowTest {
    
    @Autowired
    private QueensService queensService;
    
    @Test
    @DisplayName("Should complete full game flow successfully")
    public void testCompleteGameFlow() {
        // Step 1: Run Sequential Algorithm
        AlgorithmResponse seqResponse = queensService.runSequentialAlgorithm();
        assertNotNull(seqResponse);
        assertEquals(92, seqResponse.getTotalSolutions());
        assertTrue(seqResponse.getExecutionTimeMs() > 0);
        System.out.println("Sequential completed in: " + seqResponse.getExecutionTimeMs() + "ms");
        
        // Step 2: Run Threaded Algorithm
        AlgorithmResponse threadResponse = queensService.runThreadedAlgorithm();
        assertNotNull(threadResponse);
        assertEquals(92, threadResponse.getTotalSolutions());
        assertTrue(threadResponse.getExecutionTimeMs() > 0);
        System.out.println("Threaded completed in: " + threadResponse.getExecutionTimeMs() + "ms");
        
        // Step 3: Check Initial Stats
        GameStatsResponse initialStats = queensService.getGameStats();
        assertEquals(92, initialStats.getTotalSolutions());
        assertEquals(0, initialStats.getFoundSolutions());
        
        // Step 4: Submit First Solution
        SubmissionRequest request1 = new SubmissionRequest(
            "Player1",
            Arrays.asList(0, 4, 7, 5, 2, 6, 1, 3)
        );
        SubmissionResponse submission1 = queensService.submitSolution(request1);
        assertTrue(submission1.isAccepted());
        assertEquals(1, submission1.getFoundCount());
        
        // Step 5: Try Duplicate (Should Reject)
        SubmissionRequest request2 = new SubmissionRequest(
            "Player2",
            Arrays.asList(0, 4, 7, 5, 2, 6, 1, 3)
        );
        SubmissionResponse submission2 = queensService.submitSolution(request2);
        assertFalse(submission2.isAccepted());
        assertTrue(submission2.getMessage().contains("already discovered"));
        
        // Step 6: Submit Different Solution
        SubmissionRequest request3 = new SubmissionRequest(
            "Player2",
            Arrays.asList(0, 5, 7, 2, 6, 3, 1, 4)
        );
        SubmissionResponse submission3 = queensService.submitSolution(request3);
        assertTrue(submission3.isAccepted());
        assertEquals(2, submission3.getFoundCount());
        
        // Step 7: Check Updated Stats
        GameStatsResponse updatedStats = queensService.getGameStats();
        assertEquals(2, updatedStats.getFoundSolutions());
        assertEquals(90, updatedStats.getRemainingSolutions());
        assertNotNull(updatedStats.getLastSequentialTime());
        assertNotNull(updatedStats.getLastThreadedTime());
        
        // Step 8: Reset Game
        queensService.resetAllSolutions();
        
        // Step 9: Verify Reset
        GameStatsResponse resetStats = queensService.getGameStats();
        assertEquals(0, resetStats.getFoundSolutions());
        assertEquals(92, resetStats.getRemainingSolutions());
        
        System.out.println("✓ Full game flow completed successfully!");
    }
}