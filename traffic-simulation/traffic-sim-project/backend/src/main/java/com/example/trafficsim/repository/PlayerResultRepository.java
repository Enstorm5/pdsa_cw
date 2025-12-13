package com.example.trafficsim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.trafficsim.model.PlayerResult;

public interface PlayerResultRepository extends JpaRepository<PlayerResult, Long> {
}
