package com.example.traveling_salesman.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmResultDto {

    private String algorithmName;
    private List<String> path = new ArrayList<>();
    private int totalDistance;
    private BigDecimal timeTakenMs;

    public AlgorithmResultDto() {}

    public AlgorithmResultDto(String algorithmName, List<String> path, int totalDistance, BigDecimal timeTakenMs) {
        this.algorithmName = algorithmName;
        this.path = new ArrayList<>(path);
        this.totalDistance = totalDistance;
        this.timeTakenMs = timeTakenMs;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public BigDecimal getTimeTakenMs() {
        return timeTakenMs;
    }

    public void setTimeTakenMs(BigDecimal timeTakenMs) {
        this.timeTakenMs = timeTakenMs;
    }
}
