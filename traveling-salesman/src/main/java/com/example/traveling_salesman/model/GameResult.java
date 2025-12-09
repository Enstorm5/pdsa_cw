package com.example.traveling_salesman.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "game_result")
public class GameResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id", nullable = false)
	private GameSession session;

	@Lob
	@Column(name = "cities_selected", nullable = false)
	private String citiesSelected;

	@Lob
	@Column(name = "calculated_path", nullable = false)
	private String calculatedPath;

	@Column(name = "total_distance", nullable = false)
	private int totalDistance;

	@Column(name = "time_taken_by_user_ms", nullable = false)
	private long timeTakenByUserMs;

	public Long getId() {
		return id;
	}

	public GameSession getSession() {
		return session;
	}

	public void setSession(GameSession session) {
		this.session = session;
	}

	public String getCitiesSelected() {
		return citiesSelected;
	}

	public void setCitiesSelected(String citiesSelected) {
		this.citiesSelected = citiesSelected;
	}

	public String getCalculatedPath() {
		return calculatedPath;
	}

	public void setCalculatedPath(String calculatedPath) {
		this.calculatedPath = calculatedPath;
	}

	public int getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(int totalDistance) {
		this.totalDistance = totalDistance;
	}

	public long getTimeTakenByUserMs() {
		return timeTakenByUserMs;
	}

	public void setTimeTakenByUserMs(long timeTakenByUserMs) {
		this.timeTakenByUserMs = timeTakenByUserMs;
	}
}
