package com.example.traveling_salesman.service.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.traveling_salesman.model.City;
import com.example.traveling_salesman.model.DistanceMatrix;

@Component
public class NearestNeighborAlgorithm implements TspAlgorithm {

	@Override
	public String name() {
		return "NEAREST_NEIGHBOR";
	}

	@Override
	public TspSolution solve(City homeCity, List<City> citiesToVisit, DistanceMatrix matrix) {
		if (homeCity == null) {
			throw new IllegalArgumentException("homeCity must not be null");
		}
		if (matrix == null) {
			throw new IllegalArgumentException("matrix must not be null");
		}

		List<City> remaining = new ArrayList<>();
		if (citiesToVisit != null) {
			for (City city : citiesToVisit) {
				if (city != null && city != homeCity && !remaining.contains(city)) {
					remaining.add(city);
				}
			}
		}

		if (remaining.isEmpty()) {
			return new TspSolution(List.of(homeCity, homeCity), 0);
		}

		List<City> path = new ArrayList<>();
		path.add(homeCity);
		City current = homeCity;
		int totalDistance = 0;

		while (!remaining.isEmpty()) {
			City next = selectNearestCity(current, remaining, matrix);
			totalDistance += matrix.getDistance(current, next);
			path.add(next);
			remaining.remove(next);
			current = next;
		}

		totalDistance += matrix.getDistance(current, homeCity);
		path.add(homeCity);
		return new TspSolution(path, totalDistance);
	}

	private City selectNearestCity(City current, List<City> remaining, DistanceMatrix matrix) {
		City best = null;
		int bestDistance = Integer.MAX_VALUE;
		for (City candidate : remaining) {
			int distance = matrix.getDistance(current, candidate);
			if (distance < bestDistance) {
				bestDistance = distance;
				best = candidate;
			} else if (distance == bestDistance && best != null && candidate.ordinal() < best.ordinal()) {
				best = candidate;
			}
		}
		if (best == null) {
			throw new IllegalStateException("No candidate city available for nearest neighbor step");
		}
		return best;
	}
}
