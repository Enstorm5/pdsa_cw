package com.example.traveling_salesman.service.algorithms;

import com.example.traveling_salesman.model.City;
import com.example.traveling_salesman.model.DistanceMatrix;
import java.util.List;

public interface TspAlgorithm {

	String name();

	TspSolution solve(City homeCity, List<City> citiesToVisit, DistanceMatrix matrix);
}
