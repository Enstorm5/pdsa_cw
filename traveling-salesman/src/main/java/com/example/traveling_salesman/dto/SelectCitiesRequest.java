package com.example.traveling_salesman.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class SelectCitiesRequest {

    @NotNull(message = "sessionId must be provided")
    private Long sessionId;

    @NotEmpty(message = "cities must contain at least one city")
    private List<String> cities = new ArrayList<>();

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }
}
