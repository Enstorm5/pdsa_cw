package com.pdsa.eightqueenspuzzle.repository;

import com.pdsa.eightqueenspuzzle.model.AlgorithmPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<AlgorithmPerformance, Long> {
    
    // Find all performance records by algorithm type
    List<AlgorithmPerformance> findByAlgorithmType(AlgorithmPerformance.AlgorithmType algorithmType);
    
    // Get average execution time by algorithm type
    @Query("SELECT AVG(ap.executionTimeMs) FROM AlgorithmPerformance ap WHERE ap.algorithmType = :algorithmType")
    Double getAverageExecutionTime(AlgorithmPerformance.AlgorithmType algorithmType);
    
    // Get last 15 records for a specific algorithm
    List<AlgorithmPerformance> findTop15ByAlgorithmTypeOrderByExecutedAtDesc(AlgorithmPerformance.AlgorithmType algorithmType);
}