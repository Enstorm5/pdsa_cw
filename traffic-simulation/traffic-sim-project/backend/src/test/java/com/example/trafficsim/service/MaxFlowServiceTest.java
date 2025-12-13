package com.example.trafficsim.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class MaxFlowServiceTest {

    private MaxFlowService maxFlowService;

    @BeforeEach
    void setUp() {
        maxFlowService = new MaxFlowService();
    }

    @Test
    @DisplayName("Test Edmonds-Karp with simple linear flow")
    void testEdmondsKarp_SimpleLinearFlow() {
        // Arrange: A->B->T with capacities 10 and 5
        int[][] cap = createEmptyCapacityMatrix();
        cap[0][1] = 10; // A->B
        cap[1][8] = 5;  // B->T


        int result = maxFlowService.edmondsKarp(cap);


        assertEquals(5, result, "Max flow should be limited by bottleneck (5)");
    }

    @Test
    @DisplayName("Test Edmonds-Karp with multiple paths")
    void testEdmondsKarp_MultiplePaths() {
        // Arrange: Two paths from A to T
        int[][] cap = createEmptyCapacityMatrix();
        cap[0][1] = 10; // A->B
        cap[1][8] = 8;  // B->T
        cap[0][2] = 10; // A->C
        cap[2][8] = 7;  // C->T


        int result = maxFlowService.edmondsKarp(cap);


        assertEquals(15, result, "Max flow should be sum of both paths (8+7)");
    }

    @Test
    @DisplayName("Test Edmonds-Karp with complex network")
    void testEdmondsKarp_ComplexNetwork() {
        // Arrange: Create a more complex graph
        int[][] cap = createEmptyCapacityMatrix();
        cap[0][1] = 10; // A->B
        cap[0][2] = 10; // A->C
        cap[0][3] = 10; // A->D
        cap[1][4] = 4;  // B->E
        cap[1][5] = 8;  // B->F
        cap[2][4] = 9;  // C->E
        cap[2][5] = 6;  // C->F
        cap[3][5] = 10; // D->F
        cap[4][6] = 10; // E->G
        cap[4][7] = 10; // E->H
        cap[5][7] = 10; // F->H
        cap[6][8] = 10; // G->T
        cap[7][8] = 10; // H->T


        int result = maxFlowService.edmondsKarp(cap);


        assertTrue(result > 0, "Max flow should be positive");
        assertTrue(result <= 30, "Max flow cannot exceed total capacity from source");
    }

    @Test
    @DisplayName("Test Edmonds-Karp with no path to sink")
    void testEdmondsKarp_NoPathToSink() {
        // Arrange: Graph with no path from source (A=0) to sink (T=8)
        int[][] cap = createEmptyCapacityMatrix();
        cap[0][1] = 10; // A->B
        cap[2][3] = 10; // C->D (disconnected from A)


        int result = maxFlowService.edmondsKarp(cap);


        assertEquals(0, result, "Max flow should be 0 when no path exists");
    }

    @Test
    @DisplayName("Test Edmonds-Karp with single edge bottleneck")
    void testEdmondsKarp_SingleEdgeBottleneck() {
        // Arrange: Large capacities but one small bottleneck
        int[][] cap = createEmptyCapacityMatrix();
        cap[0][1] = 100; // A->B
        cap[1][2] = 1;   // B->C (bottleneck)
        cap[2][8] = 100; // C->T


        int result = maxFlowService.edmondsKarp(cap);


        assertEquals(1, result, "Max flow should equal the bottleneck capacity");
    }

    @Test
    @DisplayName("Test Dinic with simple linear flow")
    void testDinic_SimpleLinearFlow() {

        int[][] cap = createEmptyCapacityMatrix();
        cap[0][1] = 10; // A->B
        cap[1][8] = 5;  // B->T


        int result = maxFlowService.dinic(cap);


        assertEquals(5, result, "Max flow should be limited by bottleneck (5)");
    }

    @Test
    @DisplayName("Test Dinic with multiple paths")
    void testDinic_MultiplePaths() {

        int[][] cap = createEmptyCapacityMatrix();
        cap[0][1] = 10; // A->B
        cap[1][8] = 8;  // B->T
        cap[0][2] = 10; // A->C
        cap[2][8] = 7;  // C->T


        int result = maxFlowService.dinic(cap);


        assertEquals(15, result, "Max flow should be sum of both paths (8+7)");
    }

    @Test
    @DisplayName("Test Dinic with complex network")
    void testDinic_ComplexNetwork() {

        int[][] cap = createEmptyCapacityMatrix();
        cap[0][1] = 10; // A->B
        cap[0][2] = 10; // A->C
        cap[0][3] = 10; // A->D
        cap[1][4] = 4;  // B->E
        cap[1][5] = 8;  // B->F
        cap[2][4] = 9;  // C->E
        cap[2][5] = 6;  // C->F
        cap[3][5] = 10; // D->F
        cap[4][6] = 10; // E->G
        cap[4][7] = 10; // E->H
        cap[5][7] = 10; // F->H
        cap[6][8] = 10; // G->T
        cap[7][8] = 10; // H->T


        int result = maxFlowService.dinic(cap);


        assertTrue(result > 0, "Max flow should be positive");
        assertTrue(result <= 30, "Max flow cannot exceed total capacity from source");
    }

    @Test
    @DisplayName("Test both algorithms produce same result")
    void testBothAlgorithms_ProduceSameResult() {
        // Arrange: Create random-like capacity matrix
        int[][] cap = createEmptyCapacityMatrix();
        cap[0][1] = 12; // A->B
        cap[0][2] = 8;  // A->C
        cap[0][3] = 9;  // A->D
        cap[1][4] = 7;  // B->E
        cap[1][5] = 10; // B->F
        cap[2][4] = 6;  // C->E
        cap[2][5] = 8;  // C->F
        cap[3][5] = 11; // D->F
        cap[4][6] = 13; // E->G
        cap[4][7] = 9;  // E->H
        cap[5][7] = 14; // F->H
        cap[6][8] = 15; // G->T
        cap[7][8] = 12; // H->T


        int ekResult = maxFlowService.edmondsKarp(cap);
        int dinicResult = maxFlowService.dinic(cap);


        assertEquals(ekResult, dinicResult,
                "Both algorithms should produce the same max flow result");
    }

    @Test
    @DisplayName("Test with zero capacity edges")
    void testWithZeroCapacity() {

        int[][] cap = createEmptyCapacityMatrix();
        cap[0][1] = 10; // A->B
        cap[1][2] = 0;  // B->C (zero capacity)
        cap[2][8] = 10; // C->T


        int ekResult = maxFlowService.edmondsKarp(cap);
        int dinicResult = maxFlowService.dinic(cap);


        assertEquals(0, ekResult, "EK: Max flow should be 0 with zero capacity edge");
        assertEquals(0, dinicResult, "Dinic: Max flow should be 0 with zero capacity edge");
    }

    @Test
    @DisplayName("Test maximum capacity scenario")
    void testMaximumCapacity() {
        // Arrange: All edges at maximum capacity (15)
        int[][] cap = createEmptyCapacityMatrix();
        cap[0][1] = 15; // A->B
        cap[0][2] = 15; // A->C
        cap[0][3] = 15; // A->D
        cap[1][4] = 15; // B->E
        cap[1][5] = 15; // B->F
        cap[2][4] = 15; // C->E
        cap[2][5] = 15; // C->F
        cap[3][5] = 15; // D->F
        cap[4][6] = 15; // E->G
        cap[4][7] = 15; // E->H
        cap[5][7] = 15; // F->H
        cap[6][8] = 15; // G->T
        cap[7][8] = 15; // H->T


        int ekResult = maxFlowService.edmondsKarp(cap);
        int dinicResult = maxFlowService.dinic(cap);


        assertEquals(ekResult, dinicResult);
        assertTrue(ekResult <= 45, "Max flow cannot exceed sum of source edges (3*15)");
    }

    @Test
    @DisplayName("Test minimum capacity scenario")
    void testMinimumCapacity() {
        // Arrange: All edges at minimum capacity (5)
        int[][] cap = createEmptyCapacityMatrix();
        cap[0][1] = 5; // A->B
        cap[0][2] = 5; // A->C
        cap[0][3] = 5; // A->D
        cap[1][4] = 5; // B->E
        cap[1][5] = 5; // B->F
        cap[2][4] = 5; // C->E
        cap[2][5] = 5; // C->F
        cap[3][5] = 5; // D->F
        cap[4][6] = 5; // E->G
        cap[4][7] = 5; // E->H
        cap[5][7] = 5; // F->H
        cap[6][8] = 5; // G->T
        cap[7][8] = 5; // H->T


        int ekResult = maxFlowService.edmondsKarp(cap);
        int dinicResult = maxFlowService.dinic(cap);


        assertEquals(ekResult, dinicResult);
        assertTrue(ekResult > 0 && ekResult <= 15,
                "Max flow should be positive but limited by network structure");
    }

    // Helper method to create empty 9x9 capacity matrix
    private int[][] createEmptyCapacityMatrix() {
        return new int[9][9];
    }
}