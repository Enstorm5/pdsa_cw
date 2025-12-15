package com.example.traveling_salesman.algorithms;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.example.traveling_salesman.model.City;
import com.example.traveling_salesman.model.DistanceMatrix;
import com.example.traveling_salesman.service.algorithms.NearestNeighborAlgorithm;
import com.example.traveling_salesman.service.algorithms.TspSolution;

class NearestNeighborAlgorithmTest {

    private final NearestNeighborAlgorithm algorithm = new NearestNeighborAlgorithm();

    @Test
    void findsGreedyRouteOnThreeCities() {
        int[][] mat = {
                {0, 10, 20},
                {10, 0, 12},
                {20, 12, 0}
        };
        DistanceMatrix matrix = new DistanceMatrix(mat);

        TspSolution solution = algorithm.solve(City.A, List.of(City.B, City.C), matrix);

        assertEquals(42, solution.getTotalDistance());
        assertEquals(List.of(City.A, City.B, City.C, City.A), solution.getOrderedPath());
    }

    @Test
    void breaksTiesByOrdinal() {
        int[][] mat = {
                {0, 10, 10},
                {10, 0, 5},
                {10, 5, 0}
        };
        DistanceMatrix matrix = new DistanceMatrix(mat);

        TspSolution solution = algorithm.solve(City.A, List.of(City.B, City.C), matrix);

        // Tie between B and C from A (both 10). Should pick B (lower ordinal), then C.
        assertEquals(List.of(City.A, City.B, City.C, City.A), solution.getOrderedPath());
        assertEquals(25, solution.getTotalDistance());
    }

    @Test
    void rejectsNullHomeOrMatrix() {
        DistanceMatrix matrix = new DistanceMatrix(new int[][] {{0,1},{1,0}});
        assertThrows(IllegalArgumentException.class, () -> algorithm.solve(null, List.of(City.B), matrix));
        assertThrows(IllegalArgumentException.class, () -> algorithm.solve(City.A, List.of(City.B), null));
    }
}
