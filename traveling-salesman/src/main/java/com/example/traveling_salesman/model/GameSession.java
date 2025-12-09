package com.example.traveling_salesman.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "game_session")
public class GameSession {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "player_name", nullable = false, length = 255)
	private String playerName;

	@Enumerated(EnumType.STRING)
	@Column(name = "home_city", nullable = false, length = 100)
	private City homeCity;

	@Lob
	@Column(name = "distance_matrix", nullable = false, columnDefinition = "LONGTEXT")
	private String distanceMatrixJson;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GameResult> results = new ArrayList<>();

	@OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AlgorithmTimeLog> timeLogs = new ArrayList<>();

	@PrePersist
	void onCreate() {
		if (createdAt == null) {
			createdAt = Instant.now();
		}
	}

	public Long getId() {
		return id;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public City getHomeCity() {
		return homeCity;
	}

	public void setHomeCity(City homeCity) {
		this.homeCity = homeCity;
	}

	public String getDistanceMatrixJson() {
		return distanceMatrixJson;
	}

	public void setDistanceMatrixJson(String distanceMatrixJson) {
		this.distanceMatrixJson = distanceMatrixJson;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public List<GameResult> getResults() {
		return results;
	}

	public List<AlgorithmTimeLog> getTimeLogs() {
		return timeLogs;
	}

	public void addResult(GameResult result) {
		results.add(result);
		result.setSession(this);
	}

	public void addTimeLog(AlgorithmTimeLog timeLog) {
		timeLogs.add(timeLog);
		timeLog.setSession(this);
	}
}
