package com.pdsa.snakeandladder.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Board {

    private int size;
    private int finalCell;
    private Map<Integer, Integer> snakes = new HashMap<>();
    private Map<Integer, Integer> ladders = new HashMap<>();

    public Board(int size) {
        this.size = size;
        this.finalCell = size * size;
        Random r = new Random();
        int count = size - 2;

        while (ladders.size() < count) {
            int s = r.nextInt(finalCell - 2) + 2;
            int e = s + r.nextInt(size) + 3;
            if (e < finalCell && s < e) ladders.putIfAbsent(s, e);
        }
        while (snakes.size() < count) {
            int s = r.nextInt(finalCell - 10) + 10;
            int e = s - (r.nextInt(size) + 3);
            if (e > 1 && e < s) snakes.putIfAbsent(s, e);
        }
    }

    public int getSize() {
        return size;
    }

    public int getFinalCell() {
        return finalCell;
    }

    public Map<Integer, Integer> getSnakes() {
        return snakes;
    }

    public Map<Integer, Integer> getLadders() {
        return ladders;
    }
}
