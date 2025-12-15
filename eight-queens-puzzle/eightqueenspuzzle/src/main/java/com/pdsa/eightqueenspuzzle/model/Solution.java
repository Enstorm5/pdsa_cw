package com.pdsa.eightqueenspuzzle.model;

import jakarta.persistence.*;

@Entity
@Table(name = "solutions")
public class Solution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solution_id")
    private Long solutionId;
    
    @Column(name = "solution_data", nullable = false, unique = true)
    private String solutionData; // e.g., "0,4,7,5,2,6,1,3"
    
    @Column(name = "is_found")
    private boolean isFound = false;
    
    // Constructors
    public Solution() {}
    
    public Solution(String solutionData) {
        this.solutionData = solutionData;
        this.isFound = false;
    }
    
    // Getters and Setters
    public Long getSolutionId() {
        return solutionId;
    }
    
    public void setSolutionId(Long solutionId) {
        this.solutionId = solutionId;
    }
    
    public String getSolutionData() {
        return solutionData;
    }
    
    public void setSolutionData(String solutionData) {
        this.solutionData = solutionData;
    }
    
    public boolean isFound() {
        return isFound;
    }
    
    public void setFound(boolean found) {
        isFound = found;
    }
}