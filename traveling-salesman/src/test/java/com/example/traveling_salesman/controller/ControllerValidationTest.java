package com.example.traveling_salesman.controller;

import com.example.traveling_salesman.controller.GameController;
import com.example.traveling_salesman.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameController.class)
public class ControllerValidationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GameService gameService;

    @Test
    public void selectCities_missingSessionIdShouldReturnBadRequest() throws Exception {
        String json = "{ \"cities\": [\"A\", \"B\"] }";

        mvc.perform(post("/api/game/select-cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    public void start_emptyPlayerNameShouldReturnBadRequest() throws Exception {
        String json = "{ \"playerName\": \"  \" }";

        mvc.perform(post("/api/game/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }
}
