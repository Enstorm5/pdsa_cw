package com.pdsa.eightqueenspuzzle.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionConverter {
    
    // Convert List<Integer> to String (e.g., [0,4,7,5,2,6,1,3] -> "0,4,7,5,2,6,1,3")
    public static String listToString(List<Integer> positions) {
        return positions.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
    
    // Convert String to List<Integer> (e.g., "0,4,7,5,2,6,1,3" -> [0,4,7,5,2,6,1,3])
    public static List<Integer> stringToList(String solutionData) {
        return Arrays.stream(solutionData.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
    
    // Check if two solutions are the same (rotation/reflection)
    public static boolean areSolutionsEquivalent(List<Integer> solution1, List<Integer> solution2) {
        // For simplicity, we'll just check exact match
        // You could implement rotation/reflection logic here if needed
        return solution1.equals(solution2);
    }
}