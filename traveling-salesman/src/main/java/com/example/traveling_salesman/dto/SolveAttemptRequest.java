package com.example.traveling_salesman.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class SolveAttemptRequest {

	@NotNull(message = "sessionId must be provided")
	private Long sessionId;

	@NotEmpty(message = "proposedPath must contain at least one city")
	private List<String> proposedPath = new ArrayList<>();

	@Min(value = 0, message = "timeTakenByUserMs must be non-negative")
	private long timeTakenByUserMs;

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public List<String> getProposedPath() {
		return proposedPath;
	}

	public void setProposedPath(List<String> proposedPath) {
		this.proposedPath = proposedPath;
	}

	public long getTimeTakenByUserMs() {
		return timeTakenByUserMs;
	}

	public void setTimeTakenByUserMs(long timeTakenByUserMs) {
		this.timeTakenByUserMs = timeTakenByUserMs;
	}
}
