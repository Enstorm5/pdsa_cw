package com.example.traveling_salesman.dto;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmEvaluationResponse {

    private Long sessionId;
    private String homeCity;
    private List<String> selectedCities = new ArrayList<>();
    private List<AlgorithmResultDto> algorithmResults = new ArrayList<>();

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public List<String> getSelectedCities() {
        return selectedCities;
    }

    public void setSelectedCities(List<String> selectedCities) {
        this.selectedCities = selectedCities;
    }

    public List<AlgorithmResultDto> getAlgorithmResults() {
        return algorithmResults;
    }

    public void setAlgorithmResults(List<AlgorithmResultDto> algorithmResults) {
        this.algorithmResults = algorithmResults;
    }
}
