package com.example.trafficsim;

import com.example.trafficsim.service.MaxFlowService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MaxFlowServiceTest {

    @Test
    public void testSimpleNetwork(){
        MaxFlowService s = new MaxFlowService();
        int[][] cap = new int[9][9];
        // set a simple deterministic network (all edges = 10)
        int[] edgesFrom = {0,0,0,1,1,2,2,3,4,4,5,6,7};
        int[] edgesTo   = {1,2,3,4,5,4,5,5,6,7,7,8,8};
        for(int i=0;i<edgesFrom.length;i++){
            cap[edgesFrom[i]][edgesTo[i]] = 10;
        }
        int ek = s.edmondsKarp(cap);
        int dinic = s.dinic(cap);
        assertEquals(20, ek); // expected in this setup
        assertEquals(20, dinic);
    }
}
