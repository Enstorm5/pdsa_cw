package com.pdsa.eightqueenspuzzle.repository;

import com.pdsa.eightqueenspuzzle.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    
    // Find solution by data string
    Optional<Solution> findBySolutionData(String solutionData);
    
    // Count how many solutions have been found
    int countByIsFoundTrue();
    
    // Count remaining solutions
    int countByIsFoundFalse();
}