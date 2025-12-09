package com.example.traveling_salesman.dto;

import java.util.ArrayList;
import java.util.List;

public class GameRoundResponse {

	private Long sessionId;
	private String playerName;
	private String homeCity;
	private List<String> cityLabels = new ArrayList<>();
	private int[][] distanceMatrix;
	private List<AlgorithmResultDto> algorithmResults = new ArrayList<>();

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

	public List<AlgorithmResultDto> getAlgorithmResults() {
		return algorithmResults;
	}

	public void setAlgorithmResults(List<AlgorithmResultDto> algorithmResults) {
		this.algorithmResults = algorithmResults;
	}
}
