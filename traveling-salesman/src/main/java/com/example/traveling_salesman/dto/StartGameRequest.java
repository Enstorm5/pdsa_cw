package com.example.traveling_salesman.dto;

import java.util.ArrayList;
import java.util.List;

public class StartGameRequest {

	private String playerName;
	private String homeCity;
	private List<String> citiesToVisit = new ArrayList<>();

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
}
