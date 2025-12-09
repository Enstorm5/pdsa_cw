package com.example.traveling_salesman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.traveling_salesman.model.GameResult;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {
	List<GameResult> findBySessionIdOrderByIdAsc(Long sessionId);
}
