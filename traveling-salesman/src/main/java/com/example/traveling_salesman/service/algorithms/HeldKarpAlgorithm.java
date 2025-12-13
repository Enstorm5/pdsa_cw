package com.example.traveling_salesman.service.algorithms;

import com.example.traveling_salesman.model.City;
import com.example.traveling_salesman.model.DistanceMatrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HeldKarpAlgorithm implements TspAlgorithm {

	@Override
	public String name() {
		return "HELD_KARP";
	}

	@Override
	public TspSolution solve(City homeCity, List<City> citiesToVisit, DistanceMatrix matrix) {
		if (homeCity == null) {
			throw new IllegalArgumentException("homeCity must not be null");
		}
		if (matrix == null) {
			throw new IllegalArgumentException("matrix must not be null");
		}

		List<City> cities = deduplicate(citiesToVisit, homeCity);
		if (cities.isEmpty()) {
			return new TspSolution(List.of(homeCity, homeCity), 0);
		}

		int n = cities.size();
		int subsetCount = 1 << n;
		int[][] dp = new int[subsetCount][n];
		int[][] parent = new int[subsetCount][n];
		int inf = Integer.MAX_VALUE / 4;
		for (int subset = 0; subset < subsetCount; subset++) {
			Arrays.fill(dp[subset], inf);
			Arrays.fill(parent[subset], -1);
		}

		for (int i = 0; i < n; i++) {
			int mask = 1 << i;
			dp[mask][i] = matrix.getDistance(homeCity, cities.get(i));
		}

		for (int subset = 1; subset < subsetCount; subset++) {
			for (int end = 0; end < n; end++) {
				int endMask = 1 << end;
				if ((subset & endMask) == 0) {
					continue;
				}
				int prevSubset = subset ^ endMask;
				if (prevSubset == 0) {
					continue;
				}
				for (int prev = 0; prev < n; prev++) {
					if ((prevSubset & (1 << prev)) == 0) {
						continue;
					}
					int candidate = dp[prevSubset][prev] + matrix.getDistance(cities.get(prev), cities.get(end));
					if (candidate < dp[subset][end]) {
						dp[subset][end] = candidate;
						parent[subset][end] = prev;
					}
				}
			}
		}

		int fullSet = subsetCount - 1;
		int bestDistance = inf;
		int lastIndex = -1;
		for (int end = 0; end < n; end++) {
			int routeDistance = dp[fullSet][end];
			if (routeDistance >= inf) {
				continue;
			}
			int candidate = routeDistance + matrix.getDistance(cities.get(end), homeCity);
			if (candidate < bestDistance) {
				bestDistance = candidate;
				lastIndex = end;
			}
		}

		if (lastIndex == -1) {
			return new TspSolution(List.of(homeCity, homeCity), 0);
		}

		List<City> ordered = reconstructPath(cities, parent, fullSet, lastIndex);
		List<City> path = new ArrayList<>();
		path.add(homeCity);
		path.addAll(ordered);
		path.add(homeCity);
		return new TspSolution(path, bestDistance);
	}

	private List<City> deduplicate(List<City> citiesToVisit, City homeCity) {
		List<City> unique = new ArrayList<>();
		if (citiesToVisit == null) {
			return unique;
		}
		for (City city : citiesToVisit) {
			if (city != null && city != homeCity && !unique.contains(city)) {
				unique.add(city);
			}
		}
		return unique;
	}

	private List<City> reconstructPath(List<City> cities, int[][] parent, int subset, int currentIndex) {
		List<City> ordered = new ArrayList<>();
		int mask = subset;
		int index = currentIndex;
		while (index != -1) {
			ordered.add(cities.get(index));
			int nextMask = mask ^ (1 << index);
			int nextIndex = parent[mask][index];
			mask = nextMask;
			index = nextIndex;
		}
		Collections.reverse(ordered);
		return ordered;
	}
}


