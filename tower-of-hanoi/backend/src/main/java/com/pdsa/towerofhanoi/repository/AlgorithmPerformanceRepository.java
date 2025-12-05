package com.pdsa.towerofhanoi.repository;

import com.pdsa.towerofhanoi.model.AlgorithmPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlgorithmPerformanceRepository extends JpaRepository<AlgorithmPerformance, Long> {
    
    // Custom query methods
    List<AlgorithmPerformance> findByGameRoundId(Long gameRoundId);
    List<AlgorithmPerformance> findByAlgorithmName(String algorithmName);
    List<AlgorithmPerformance> findByNumberOfPegs(Integer numberOfPegs);
}