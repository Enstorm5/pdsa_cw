package com.example.traveling_salesman.dto;

import java.util.ArrayList;
import java.util.List;

public class SolveAttemptRequest {

	private Long sessionId;
	private List<String> proposedPath = new ArrayList<>();
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
