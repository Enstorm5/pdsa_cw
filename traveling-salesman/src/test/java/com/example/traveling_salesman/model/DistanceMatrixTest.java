package com.example.traveling_salesman.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class DistanceMatrixTest {

    @Test
    void rejectsNullMatrix() {
        assertThrows(NullPointerException.class, () -> new DistanceMatrix(null));
    }

    @Test
    void rejectsEmptyMatrix() {
        assertThrows(IllegalArgumentException.class, () -> new DistanceMatrix(new int[][]{}));
    }

    @Test
    void rejectsNonSquare() {
        assertThrows(IllegalArgumentException.class, () -> new DistanceMatrix(new int[][]{{0,1},{1}}));
    }

    @Test
    void rejectsNonZeroDiagonal() {
        assertThrows(IllegalArgumentException.class, () -> new DistanceMatrix(new int[][]{{1,2},{2,0}}));
    }

    @Test
    void rejectsNegativeValues() {
        assertThrows(IllegalArgumentException.class, () -> new DistanceMatrix(new int[][]{{0,-1},{-1,0}}));
    }

    @Test
    void rejectsAsymmetric() {
        assertThrows(IllegalArgumentException.class, () -> new DistanceMatrix(new int[][]{{0,2},{3,0}}));
    }

    @Test
    void acceptsValidSymmetricMatrix() {
        int[][] input = {{0,5,7},{5,0,9},{7,9,0}};
        DistanceMatrix m = new DistanceMatrix(input);
        assertArrayEquals(input, m.toArray());
    }
}
