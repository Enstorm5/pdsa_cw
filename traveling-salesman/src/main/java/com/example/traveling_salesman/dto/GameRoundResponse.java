package com.example.traveling_salesman.dto;

import java.util.ArrayList;
import java.util.List;

public class GameRoundResponse {

	private Long sessionId;
	private String playerName;
	private String homeCity;
	private List<String> citiesToVisit = new ArrayList<>();
	private List<String> cityLabels = new ArrayList<>();
	private int[][] distanceMatrix;
	private List<AlgorithmResult> algorithmResults = new ArrayList<>();

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getHomeCity() {
		return homeCity;
	}

	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}

	public List<String> getCitiesToVisit() {
		return citiesToVisit;
	}

	public void setCitiesToVisit(List<String> citiesToVisit) {
		this.citiesToVisit = citiesToVisit;
	}

	public List<String> getCityLabels() {
		return cityLabels;
	}

	public void setCityLabels(List<String> cityLabels) {
		this.cityLabels = cityLabels;
	}

	public int[][] getDistanceMatrix() {
		return distanceMatrix;
	}

	public void setDistanceMatrix(int[][] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}

	public List<AlgorithmResult> getAlgorithmResults() {
		return algorithmResults;
	}

	public void setAlgorithmResults(List<AlgorithmResult> algorithmResults) {
		this.algorithmResults = algorithmResults;
	}

	public static class AlgorithmResult {
		private String algorithmName;
		private List<String> path;
		private int totalDistance;
		private long timeTakenMs;

		public AlgorithmResult() {}

		public AlgorithmResult(String algorithmName, List<String> path, int totalDistance, long timeTakenMs) {
			this.algorithmName = algorithmName;
			this.path = path;
			this.totalDistance = totalDistance;
			this.timeTakenMs = timeTakenMs;
		}

		public String getAlgorithmName() {
			return algorithmName;
		}

		public void setAlgorithmName(String algorithmName) {
			this.algorithmName = algorithmName;
		}

		public List<String> getPath() {
			return path;
		}

		public void setPath(List<String> path) {
			this.path = path;
		}

		public int getTotalDistance() {
			return totalDistance;
		}

		public void setTotalDistance(int totalDistance) {
			this.totalDistance = totalDistance;
		}

		public long getTimeTakenMs() {
			return timeTakenMs;
		}

		public void setTimeTakenMs(long timeTakenMs) {
			this.timeTakenMs = timeTakenMs;
		}
	}
}
