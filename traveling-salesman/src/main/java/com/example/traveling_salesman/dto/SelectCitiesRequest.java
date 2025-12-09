package com.example.traveling_salesman.dto;

import java.util.ArrayList;
import java.util.List;

public class SelectCitiesRequest {

    private Long sessionId;
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
