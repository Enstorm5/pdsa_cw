package com.example.traveling_salesman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.traveling_salesman.model.GameSession;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
	List<GameSession> findByPlayerNameOrderByCreatedAtDesc(String playerName);
}
