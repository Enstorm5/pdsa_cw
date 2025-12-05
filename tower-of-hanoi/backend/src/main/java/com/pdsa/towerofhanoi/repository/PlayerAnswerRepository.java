package com.pdsa.towerofhanoi.repository;

import com.pdsa.towerofhanoi.model.PlayerAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlayerAnswerRepository extends JpaRepository<PlayerAnswer, Long> {
    
    // Custom query methods
    List<PlayerAnswer> findByGameRoundId(Long gameRoundId);
    List<PlayerAnswer> findByPlayerName(String playerName);
    List<PlayerAnswer> findByIsCorrect(Boolean isCorrect);
}