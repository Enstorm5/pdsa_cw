package com.pdsa.towerofhanoi.repository;

import com.pdsa.towerofhanoi.model.GameRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoundRepository extends JpaRepository<GameRound, Long> {
    // JpaRepository provides: save, findById, findAll, delete, etc.
    // No need to write implementation - Spring handles it!
}