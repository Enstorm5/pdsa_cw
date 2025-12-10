package com.pdsa.eightqueenspuzzle.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long submissionId;
    
    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
    
    @ManyToOne
    @JoinColumn(name = "solution_id", nullable = false)
    private Solution solution;
    
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    // Constructors
    public Submission() {
        this.submittedAt = LocalDateTime.now();
    }
    
    public Submission(Player player, Solution solution) {
        this.player = player;
        this.solution = solution;
        this.submittedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getSubmissionId() {
        return submissionId;
    }
    
    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public Solution getSolution() {
        return solution;
    }
    
    public void setSolution(Solution solution) {
        this.solution = solution;
    }
    
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}