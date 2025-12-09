package com.example.traveling_salesman.dto;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmResultDto {

    private String algorithmName;
    private List<String> path = new ArrayList<>();
    private int totalDistance;
    private long timeTakenNs;

    public AlgorithmResultDto() {}

    public AlgorithmResultDto(String algorithmName, List<String> path, int totalDistance, long timeTakenNs) {
        this.algorithmName = algorithmName;
        this.path = new ArrayList<>(path);
        this.totalDistance = totalDistance;
        this.timeTakenNs = timeTakenNs;
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

    public long getTimeTakenNs() {
        return timeTakenNs;
    }

    public void setTimeTakenNs(long timeTakenNs) {
        this.timeTakenNs = timeTakenNs;
    }
}
