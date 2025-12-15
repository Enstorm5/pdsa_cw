package com.pdsa.snakeandladder.repository;

import com.pdsa.snakeandladder.model.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {}
