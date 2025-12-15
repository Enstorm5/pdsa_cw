package com.example.traveling_salesman.dto;

import jakarta.validation.constraints.NotBlank;

public class StartGameRequest {

	@NotBlank(message = "playerName must not be blank")
	private String playerName;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
