package com.example.traveling_salesman.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "algorithm_time_log")
public class AlgorithmTimeLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id", nullable = false)
	private GameSession session;

	@Column(name = "algorithm_name", nullable = false, length = 50)
	private String algorithmName;

	@Column(name = "time_taken_ms", nullable = false, precision = 15, scale = 4)
	private java.math.BigDecimal timeTakenMs;

	public Long getId() {
		return id;
	}

	public GameSession getSession() {
		return session;
	}

	public void setSession(GameSession session) {
		this.session = session;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public java.math.BigDecimal getTimeTakenMs() {
		return timeTakenMs;
	}

	public void setTimeTakenMs(java.math.BigDecimal timeTakenMs) {
		this.timeTakenMs = timeTakenMs;
	}
}
