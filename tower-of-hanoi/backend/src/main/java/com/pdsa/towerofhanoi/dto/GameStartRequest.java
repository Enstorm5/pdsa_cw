package com.pdsa.towerofhanoi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStartRequest {
    
    @NotNull(message = "Number of pegs is required")
    @Min(value = 3, message = "Number of pegs must be 3 or 4")
    @Max(value = 4, message = "Number of pegs must be 3 or 4")
    private Integer numberOfPegs;
}