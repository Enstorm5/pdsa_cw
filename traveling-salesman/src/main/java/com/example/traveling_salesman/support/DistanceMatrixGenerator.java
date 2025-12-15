package com.example.traveling_salesman.support;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.example.traveling_salesman.model.DistanceMatrix;

@Component
public class DistanceMatrixGenerator {

	private static final int CITY_COUNT = 10;
	private static final int MIN_DISTANCE = 50;
	private static final int MAX_DISTANCE = 100;

	private final Random random;

	public DistanceMatrixGenerator() {
		this(new SecureRandom());
	}

	public DistanceMatrixGenerator(Random random) {
		this.random = random;
	}

	public DistanceMatrix generate() {
		int[][] matrix = new int[CITY_COUNT][CITY_COUNT];
		for (int i = 0; i < CITY_COUNT; i++) {
			for (int j = i + 1; j < CITY_COUNT; j++) {
				int distance = nextDistance();
				matrix[i][j] = distance;
				matrix[j][i] = distance;
			}
		}
		return new DistanceMatrix(matrix);
	}

	private int nextDistance() {
		return random.nextInt(MAX_DISTANCE - MIN_DISTANCE + 1) + MIN_DISTANCE;
	}
}
