package com.example.traveling_salesman.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.traveling_salesman.dto.AlgorithmEvaluationResponse;
import com.example.traveling_salesman.dto.AlgorithmResultDto;
import com.example.traveling_salesman.dto.GameResponse;
import com.example.traveling_salesman.dto.GameRoundResponse;
import com.example.traveling_salesman.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = GameController.class)
class ControllerSuccessTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameService gameService;

    @Test
    void startReturnsSessionAndMatrix() throws Exception {
        GameRoundResponse resp = new GameRoundResponse();
        resp.setSessionId(99L);
        resp.setPlayerName("alice");
        resp.setHomeCity("A");
        resp.setCityLabels(List.of("A","B"));
        resp.setDistanceMatrix(new int[][]{{0,5},{5,0}});
        when(gameService.startGame(any())).thenReturn(resp);

        mvc.perform(post("/api/game/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"playerName\":\"alice\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(99))
                .andExpect(jsonPath("$.homeCity").value("A"))
                .andExpect(jsonPath("$.distanceMatrix[0][1]").value(5));
    }

    @Test
    void selectCitiesReturnsAlgorithmResults() throws Exception {
        AlgorithmEvaluationResponse resp = new AlgorithmEvaluationResponse();
        resp.setSessionId(101L);
        resp.setHomeCity("A");
        resp.setSelectedCities(List.of("B","C"));
        resp.setAlgorithmResults(List.of(new AlgorithmResultDto("HELD_KARP", List.of("A","B","C","A"), 30, new BigDecimal("1.2345"))));
        when(gameService.evaluateSelection(any())).thenReturn(resp);

        mvc.perform(post("/api/game/select-cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sessionId\":101,\"cities\":[\"B\",\"C\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.algorithmResults[0].algorithmName").value("HELD_KARP"))
                .andExpect(jsonPath("$.algorithmResults[0].totalDistance").value(30));
    }

    @Test
    void solveReturnsOptimalDistanceAndPath() throws Exception {
        GameResponse resp = new GameResponse();
        resp.setSessionId(202L);
        resp.setPlayerName("bob");
        resp.setHomeCity("A");
        resp.setOptimalDistance(22);
        resp.setOptimalPath(List.of("A","B","C","A"));
        resp.setCorrect(true);
        when(gameService.submitAttempt(any())).thenReturn(resp);

        mvc.perform(post("/api/game/solve")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sessionId\":202,\"proposedPath\":[\"A\",\"B\",\"C\",\"A\"],\"timeTakenByUserMs\":5000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.optimalDistance").value(22))
                .andExpect(jsonPath("$.correct").value(true));
    }
}
