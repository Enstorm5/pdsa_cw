package com.example.traveling_salesman.algorithms;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.example.traveling_salesman.model.City;
import com.example.traveling_salesman.model.DistanceMatrix;
import com.example.traveling_salesman.service.algorithms.BruteForceAlgorithm;
import com.example.traveling_salesman.service.algorithms.HeldKarpAlgorithm;
import com.example.traveling_salesman.service.algorithms.TspSolution;

public class AlgorithmComparisonTest {

    @Test
    public void bruteForceAndHeldKarpShouldAgreeOn4CityExample() {
        // matrix for cities A,B,C,D (ordinals 0..3)
        int[][] mat = new int[][]{
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };
        DistanceMatrix matrix = new DistanceMatrix(mat);

        BruteForceAlgorithm brute = new BruteForceAlgorithm();
        HeldKarpAlgorithm hk = new HeldKarpAlgorithm();

        TspSolution solBrute = brute.solve(City.A, List.of(City.B, City.C, City.D), matrix);
        TspSolution solHk = hk.solve(City.A, List.of(City.B, City.C, City.D), matrix);

        assertEquals(solBrute.getTotalDistance(), solHk.getTotalDistance());
    }
}
