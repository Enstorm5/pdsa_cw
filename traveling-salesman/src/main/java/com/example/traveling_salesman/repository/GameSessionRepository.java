package com.example.traveling_salesman.repository;

import com.example.traveling_salesman.model.GameSession;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
	List<GameSession> findByPlayerNameOrderByCreatedAtDesc(String playerName);
}
