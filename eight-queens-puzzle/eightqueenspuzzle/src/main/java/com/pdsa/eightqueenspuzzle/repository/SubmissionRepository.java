package com.pdsa.eightqueenspuzzle.repository;

import com.pdsa.eightqueenspuzzle.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    
    // Find all submissions by a specific player
    List<Submission> findByPlayer_PlayerId(Long playerId);
    
    // Count submissions by player
    long countByPlayer_PlayerId(Long playerId);
}