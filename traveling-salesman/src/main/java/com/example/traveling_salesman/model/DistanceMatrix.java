package com.example.traveling_salesman.model;

import java.util.Arrays;
import java.util.Objects;

public class DistanceMatrix {
	private final int[][] distances;

	public DistanceMatrix(int[][] distances) {
		this.distances = copyAndValidate(distances);
	}

	public int size() {
		return distances.length;
	}

	public int getDistance(City from, City to) {
		Objects.requireNonNull(from, "from must not be null");
		Objects.requireNonNull(to, "to must not be null");
		return distances[from.ordinal()][to.ordinal()];
	}

	public int[][] toArray() {
		int[][] result = new int[distances.length][distances.length];
		for (int i = 0; i < distances.length; i++) {
			result[i] = Arrays.copyOf(distances[i], distances[i].length);
		}
		return result;
	}

	private int[][] copyAndValidate(int[][] source) {
		Objects.requireNonNull(source, "distance matrix must not be null");
		if (source.length == 0) {
			throw new IllegalArgumentException("distance matrix must not be empty");
		}
		int size = source.length;
		int[][] copy = new int[size][size];
		for (int i = 0; i < size; i++) {
			if (source[i] == null || source[i].length != size) {
				throw new IllegalArgumentException("distance matrix must be square");
			}
			for (int j = 0; j < size; j++) {
				int value = source[i][j];
				if (i == j && value != 0) {
					throw new IllegalArgumentException("diagonal entries must be zero");
				}
				if (i != j && (value < 0)) {
					throw new IllegalArgumentException("distance values must be non-negative");
				}
				copy[i][j] = value;
			}
		}
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				if (copy[i][j] != copy[j][i]) {
					throw new IllegalArgumentException("distance matrix must be symmetric");
				}
			}
		}
		return copy;
	}
}
