package com.example.traveling_salesman.repository;

import com.example.traveling_salesman.model.AlgorithmTimeLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgorithmTimeLogRepository extends JpaRepository<AlgorithmTimeLog, Long> {
	List<AlgorithmTimeLog> findBySessionIdOrderByAlgorithmName(Long sessionId);
}
