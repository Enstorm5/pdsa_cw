package com.example.trafficsim.controller;

import com.example.trafficsim.model.PlayerResult;
import com.example.trafficsim.repository.PlayerResultRepository;
import com.example.trafficsim.service.MaxFlowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MaxFlowService maxFlowService;

    @MockBean
    private PlayerResultRepository playerResultRepository;

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        reset(maxFlowService, playerResultRepository);
    }

    // /api/game/new Endpoint Tests

    @Test
    @DisplayName("GET /new should return game with edges and matrix")
    void testNewGame_ReturnsValidResponse() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/game/new"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.edges").isArray())
                .andExpect(jsonPath("$.matrix").isArray())
                .andExpect(jsonPath("$.edges", hasSize(13))) // 13 edges in the graph
                .andReturn();


        String content = result.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(content, Map.class);

        assertNotNull(response.get("edges"));
        assertNotNull(response.get("matrix"));

        List<Map<String, Object>> edges = (List<Map<String, Object>>) response.get("edges");
        assertEquals(13, edges.size(), "Should have 13 edges");


        for (Map<String, Object> edge : edges) {
            assertTrue(edge.containsKey("from"));
            assertTrue(edge.containsKey("to"));
            assertTrue(edge.containsKey("cap"));

            int capacity = (Integer) edge.get("cap");
            assertTrue(capacity >= 5 && capacity <= 15,
                    "Capacity should be between 5 and 15 inclusive");
        }
    }

    @Test
    @DisplayName("GET /new should return 9x9 matrix")
    void testNewGame_MatrixSize() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/game/new"))
                .andExpect(status().isOk())
                .andReturn();


        String content = result.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(content, Map.class);
        List<List<Integer>> matrix = (List<List<Integer>>) response.get("matrix");

        assertEquals(9, matrix.size(), "Matrix should have 9 rows");
        for (List<Integer> row : matrix) {
            assertEquals(9, row.size(), "Each row should have 9 columns");
        }
    }

    @Test
    @DisplayName("GET /new should have correct node names")
    void testNewGame_CorrectNodeNames() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/game/new"))
                .andExpect(status().isOk())
                .andReturn();


        String content = result.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(content, Map.class);
        List<Map<String, Object>> edges = (List<Map<String, Object>>) response.get("edges");

        String[] validNodes = {"A", "B", "C", "D", "E", "F", "G", "H", "T"};

        for (Map<String, Object> edge : edges) {
            String from = (String) edge.get("from");
            String to = (String) edge.get("to");

            assertTrue(List.of(validNodes).contains(from),
                    "From node should be valid: " + from);
            assertTrue(List.of(validNodes).contains(to),
                    "To node should be valid: " + to);
        }
    }

    @Test
    @DisplayName("GET /new should generate different capacities on multiple calls")
    void testNewGame_RandomCapacities() throws Exception {

        MvcResult result1 = mockMvc.perform(get("/api/game/new"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult result2 = mockMvc.perform(get("/api/game/new"))
                .andExpect(status().isOk())
                .andReturn();


        String content1 = result1.getResponse().getContentAsString();
        String content2 = result2.getResponse().getContentAsString();

        assertNotEquals(content1, content2,
                "Two consecutive calls should likely produce different capacities");
    }

    //  /api/game/solve Endpoint Tests

    @Test
    @DisplayName("POST /solve should return correct flow when player is correct")
    void testSolve_CorrectAnswer() throws Exception {
        // Arrange
        List<List<Integer>> matrix = createTestMatrix();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("matrix", matrix);
        requestBody.put("reported", 10);
        requestBody.put("name", "TestPlayer");

        when(maxFlowService.edmondsKarp(any(int[][].class))).thenReturn(10);
        when(maxFlowService.dinic(any(int[][].class))).thenReturn(10);
        when(playerResultRepository.save(any(PlayerResult.class))).thenReturn(new PlayerResult());


        mockMvc.perform(post("/api/game/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.edmondsKarp").value(10))
                .andExpect(jsonPath("$.dinic").value(10))
                .andExpect(jsonPath("$.reported").value(10))
                .andExpect(jsonPath("$.correct").value(true))
                .andExpect(jsonPath("$.ekTimeMs").exists())
                .andExpect(jsonPath("$.dinicTimeMs").exists());

        // Verify that repository save was called
        verify(playerResultRepository, times(1)).save(any(PlayerResult.class));
    }

    @Test
    @DisplayName("POST /solve should return incorrect when player is wrong")
    void testSolve_IncorrectAnswer() throws Exception {
        // Arrange
        List<List<Integer>> matrix = createTestMatrix();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("matrix", matrix);
        requestBody.put("reported", 5);
        requestBody.put("name", "TestPlayer");

        when(maxFlowService.edmondsKarp(any(int[][].class))).thenReturn(10);
        when(maxFlowService.dinic(any(int[][].class))).thenReturn(10);


        mockMvc.perform(post("/api/game/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.edmondsKarp").value(10))
                .andExpect(jsonPath("$.dinic").value(10))
                .andExpect(jsonPath("$.reported").value(5))
                .andExpect(jsonPath("$.correct").value(false));

        // Verify that repository save was NOT called (incorrect answer)
        verify(playerResultRepository, never()).save(any(PlayerResult.class));
    }

    @Test
    @DisplayName("POST /solve should not save when name is empty even if correct")
    void testSolve_NoSaveWithoutName() throws Exception {
        // Arrange
        List<List<Integer>> matrix = createTestMatrix();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("matrix", matrix);
        requestBody.put("reported", 10);
        requestBody.put("name", "");

        when(maxFlowService.edmondsKarp(any(int[][].class))).thenReturn(10);
        when(maxFlowService.dinic(any(int[][].class))).thenReturn(10);


        mockMvc.perform(post("/api/game/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(true));

        // Verify that repository save was NOT called (empty name)
        verify(playerResultRepository, never()).save(any(PlayerResult.class));
    }

    @Test
    @DisplayName("POST /solve should not save when name is null even if correct")
    void testSolve_NoSaveWithNullName() throws Exception {
        // Arrange
        List<List<Integer>> matrix = createTestMatrix();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("matrix", matrix);
        requestBody.put("reported", 10);


        when(maxFlowService.edmondsKarp(any(int[][].class))).thenReturn(10);
        when(maxFlowService.dinic(any(int[][].class))).thenReturn(10);


        mockMvc.perform(post("/api/game/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(true));

        // Verify that repository save was NOT called
        verify(playerResultRepository, never()).save(any(PlayerResult.class));
    }

    @Test
    @DisplayName("POST /solve should handle missing reported value")
    void testSolve_MissingReportedValue() throws Exception {
        // Arrange
        List<List<Integer>> matrix = createTestMatrix();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("matrix", matrix);


        when(maxFlowService.edmondsKarp(any(int[][].class))).thenReturn(10);
        when(maxFlowService.dinic(any(int[][].class))).thenReturn(10);


        mockMvc.perform(post("/api/game/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reported").value(0))
                .andExpect(jsonPath("$.correct").value(false));
    }

    @Test
    @DisplayName("POST /solve should handle zero flow correctly")
    void testSolve_ZeroFlow() throws Exception {

        List<List<Integer>> matrix = createTestMatrix();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("matrix", matrix);
        requestBody.put("reported", 0);
        requestBody.put("name", "TestPlayer");

        when(maxFlowService.edmondsKarp(any(int[][].class))).thenReturn(0);
        when(maxFlowService.dinic(any(int[][].class))).thenReturn(0);
        when(playerResultRepository.save(any(PlayerResult.class))).thenReturn(new PlayerResult());


        mockMvc.perform(post("/api/game/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.edmondsKarp").value(0))
                .andExpect(jsonPath("$.dinic").value(0))
                .andExpect(jsonPath("$.reported").value(0))
                .andExpect(jsonPath("$.correct").value(true));

        // Verify save was called for correct answer
        verify(playerResultRepository, times(1)).save(any(PlayerResult.class));
    }

    @Test
    @DisplayName("POST /solve should verify both algorithms are called")
    void testSolve_BothAlgorithmsCalled() throws Exception {

        List<List<Integer>> matrix = createTestMatrix();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("matrix", matrix);
        requestBody.put("reported", 15);

        when(maxFlowService.edmondsKarp(any(int[][].class))).thenReturn(15);
        when(maxFlowService.dinic(any(int[][].class))).thenReturn(15);


        mockMvc.perform(post("/api/game/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());


        verify(maxFlowService, times(1)).edmondsKarp(any(int[][].class));
        verify(maxFlowService, times(1)).dinic(any(int[][].class));
    }

    @Test
    @DisplayName("POST /solve should handle name with whitespace")
    void testSolve_NameWithWhitespace() throws Exception {
        // Arrange
        List<List<Integer>> matrix = createTestMatrix();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("matrix", matrix);
        requestBody.put("reported", 10);
        requestBody.put("name", "   ");

        when(maxFlowService.edmondsKarp(any(int[][].class))).thenReturn(10);
        when(maxFlowService.dinic(any(int[][].class))).thenReturn(10);


        mockMvc.perform(post("/api/game/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(true));

        // Verify save was NOT called (whitespace-only name)
        verify(playerResultRepository, never()).save(any(PlayerResult.class));
    }

    @Test
    @DisplayName("POST /solve should save correct PlayerResult data")
    void testSolve_VerifyPlayerResultData() throws Exception {
        // Arrange
        List<List<Integer>> matrix = createTestMatrix();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("matrix", matrix);
        requestBody.put("reported", 20);
        requestBody.put("name", "Alice");

        when(maxFlowService.edmondsKarp(any(int[][].class))).thenReturn(20);
        when(maxFlowService.dinic(any(int[][].class))).thenReturn(20);
        when(playerResultRepository.save(any(PlayerResult.class))).thenAnswer(invocation -> {
            PlayerResult pr = invocation.getArgument(0);
            assertEquals("Alice", pr.getPlayerName());
            assertEquals(20, pr.getReportedMaxFlow());
            assertEquals(20, pr.getCorrectMaxFlow());
            assertTrue(pr.isCorrect());
            assertTrue(pr.getEkMillis() >= 0);
            assertTrue(pr.getDinicMillis() >= 0);
            return pr;
        });


        mockMvc.perform(post("/api/game/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());


        verify(playerResultRepository, times(1)).save(any(PlayerResult.class));
    }

    // Helper method to create a test matrix
    private List<List<Integer>> createTestMatrix() {
        List<List<Integer>> matrix = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                row.add(0);
            }
            matrix.add(row);
        }
        // Add some test edges
        matrix.get(0).set(1, 10); // A->B
        matrix.get(1).set(8, 10); // B->T
        return matrix;
    }
}