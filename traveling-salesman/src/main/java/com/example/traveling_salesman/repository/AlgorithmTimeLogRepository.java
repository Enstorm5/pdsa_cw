package com.example.traveling_salesman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.traveling_salesman.model.AlgorithmTimeLog;

@Repository
public interface AlgorithmTimeLogRepository extends JpaRepository<AlgorithmTimeLog, Long> {
	List<AlgorithmTimeLog> findBySessionIdOrderByAlgorithmName(Long sessionId);
}
