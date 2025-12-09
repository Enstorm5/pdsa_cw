package com.example.traveling_salesman.repository;

import com.example.traveling_salesman.model.GameResult;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {
	List<GameResult> findBySessionIdOrderByIdAsc(Long sessionId);
}
