package com.example.trafficsim.controller;

import com.example.trafficsim.model.PlayerResult;
import com.example.trafficsim.repository.PlayerResultRepository;
import com.example.trafficsim.service.MaxFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private MaxFlowService maxFlowService;

    @Autowired
    private PlayerResultRepository repo;

    // Returns a random capacity matrix and mapping
    @GetMapping("/new")
    public Map<String,Object> newGame(){
        Random r = new Random();
        int N = 9;
        int[][] cap = new int[N][N];
        // helper to set edge with random cap 5-15 inclusive
        java.util.function.BiConsumer<Integer,Integer> setRand = (u,v) -> {
            cap[u][v] = 5 + r.nextInt(11);
        };
        // Node mapping: A=0, B=1, C=2, D=3, E=4, F=5, G=6, H=7, T=8
        setRand.accept(0,1); // A->B
        setRand.accept(0,2); // A->C
        setRand.accept(0,3); // A->D
        setRand.accept(1,4); // B->E
        setRand.accept(1,5); // B->F
        setRand.accept(2,4); // C->E
        setRand.accept(2,5); // C->F
        setRand.accept(3,5); // D->F
        setRand.accept(4,6); // E->G
        setRand.accept(4,7); // E->H
        setRand.accept(5,7); // F->H
        setRand.accept(6,8); // G->T
        setRand.accept(7,8); // H->T

        List<Map<String,Object>> edges = new ArrayList<>();
        String[] names = new String[]{"A","B","C","D","E","F","G","H","T"};
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                if(cap[i][j]>0){
                    Map<String,Object> e = new HashMap<>();
                    e.put("from", names[i]);
                    e.put("to", names[j]);
                    e.put("cap", cap[i][j]);
                    edges.add(e);
                }
            }
        }
        Map<String,Object> res = new HashMap<>();
        res.put("edges", edges);
        res.put("matrix", cap);
        return res;
    }


    // Solve request: client provides matrix and player's reported value and name
    @PostMapping("/solve")
    public Map<String,Object> solve(@RequestBody Map<String,Object> body){
        // validating inputs
        if(!body.containsKey("matrix") || !(body.get("matrix") instanceof List)){
            throw new IllegalArgumentException("matrix field missing or invalid");
        }
        List<List<Integer>> matrixList = (List<List<Integer>>) body.get("matrix");
        int N = matrixList.size();
        int[][] mat = new int[N][N];
        for(int i=0;i<N;i++){
            List<Integer> row = matrixList.get(i);
            for(int j=0;j<N;j++){
                mat[i][j] = row.get(j);
            }
        }
        int reported = body.getOrDefault("reported", 0) instanceof Number ? ((Number)body.getOrDefault("reported",0)).intValue() : 0;
        String name = (String) body.getOrDefault("name", "");


        //edmondsKarp Algorithm
        long t1 = System.currentTimeMillis();
        int ek = maxFlowService.edmondsKarp(mat);
        long t2 = System.currentTimeMillis();

        //dinic Algorithm
        int dinic = maxFlowService.dinic(mat);
        long t3 = System.currentTimeMillis();

        System.out.println("Calculated correct max flow = "+ek);

        Map<String,Object> res = new HashMap<>();
        res.put("edmondsKarp", ek);
        res.put("dinic", dinic);
        res.put("ekTimeMs", t2 - t1);
        res.put("dinicTimeMs", t3 - t2);
        boolean correct = (reported == ek); // use ek as authoritative
        res.put("reported", reported);
        res.put("correct", correct);

        if(correct && name!=null && !name.trim().isEmpty()){
            PlayerResult pr = new PlayerResult();
            pr.setPlayerName(name);
            pr.setReportedMaxFlow(reported);
            pr.setCorrectMaxFlow(ek);
            pr.setCorrect(true);
            pr.setEkMillis(t2 - t1);
            pr.setDinicMillis(t3 - t2);
            repo.save(pr);
        }
        return res;
    }
}
