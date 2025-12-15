package com.example.traveling_salesman.service.algorithms;

import com.example.traveling_salesman.model.City;
import java.util.Collections;
import java.util.List;

public class TspSolution {
    private final List<City> orderedPath;
    private final int totalDistance;

    public TspSolution(List<City> orderedPath, int totalDistance) {
        this.orderedPath = List.copyOf(orderedPath);
        this.totalDistance = totalDistance;
    }

    public List<City> getOrderedPath() {
        return Collections.unmodifiableList(orderedPath);
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
