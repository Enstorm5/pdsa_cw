package com.pdsa.eightqueenspuzzle.repository;

import com.pdsa.eightqueenspuzzle.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    // Find player by name
    Optional<Player> findByPlayerName(String playerName);
    
    // Check if player exists
    boolean existsByPlayerName(String playerName);
}