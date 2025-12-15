// Package declaration - defines the namespace for this service class
package com.pdsa.snakeandladder.service;

// Import all model classes (Board, GameRequest, GameResult) from the model package
import com.pdsa.snakeandladder.model.*;
// Import the repository interface for database operations on GameResult entities
import com.pdsa.snakeandladder.repository.GameResultRepository;
// Import Spring's Service annotation to mark this as a service layer component
import org.springframework.stereotype.Service;

// Import Java utility classes for collections, queues, and arrays
import java.util.*;

// @Service annotation marks this class as a Spring service component for dependency injection
@Service
public class GameService {

    // Repository instance for persisting and retrieving game results from the database
    private final GameResultRepository repo;

    // Constructor-based dependency injection - Spring automatically injects the repository
    public GameService(GameResultRepository repo) {
        // Assign the injected repository to the instance variable
        this.repo = repo;
    }

    // Main method to play a round of the Snake and Ladder game
    public GameResult playRound(GameRequest req) {
        // Create a new game board with the size specified in the request
        Board board = new Board(req.getBoardSize());

        // Record the start time in nanoseconds for BFS algorithm performance measurement
        long t1 = System.nanoTime();
        // Execute BFS (Breadth-First Search) algorithm to find shortest path
        int bfs = bfs(board);
        // Calculate the time taken by BFS in nanoseconds
        long bfsTime = System.nanoTime() - t1;

        // Record the start time in nanoseconds for Dijkstra algorithm performance measurement
        long t2 = System.nanoTime();
        // Execute Dijkstra's algorithm to find shortest path
        int dijkstra = dijkstra(board);
        // Calculate the time taken by Dijkstra in nanoseconds
        long dTime = System.nanoTime() - t2;

        // Determine the correct answer by taking the minimum of both algorithm results
        int correct = Math.min(bfs, dijkstra);

        // Create a new GameResult object to store the outcome
        GameResult g = new GameResult();
        // Set the player's name from the request
        g.setPlayerName(req.getPlayerName());
        // Set the board size used in this game
        g.setBoardSize(req.getBoardSize());
        // Set the correct answer (minimum moves needed)
        g.setCorrectAnswer(correct);
        // Set the player's submitted answer
        g.setPlayerAnswer(req.getPlayerAnswer());
        // Check if the player's answer matches the correct answer and set the boolean flag
        g.setCorrect(req.getPlayerAnswer() == correct);
        // Store the BFS execution time for performance comparison
        g.setBfsTime(bfsTime);
        // Store the Dijkstra execution time for performance comparison
        g.setDijkstraTime(dTime);

        // Persist the game result to the database
        repo.save(g);
        // Return the game result to the caller
        return g;
    }

    // Retrieve all game results from the database to display game history
    public List<GameResult> history() {
        // Query the repository to fetch all GameResult records
        return repo.findAll();
    }

    // BFS (Breadth-First Search) algorithm to find the minimum number of moves to reach the end
    public int bfs(Board b) {
        // Get the final cell number (destination) from the board
        int N = b.getFinalCell();
        // Create a boolean array to track visited cells (index 0 unused, cells numbered 1 to N)
        boolean[] vis = new boolean[N + 1];
        // Initialize a queue to store [position, distance] pairs for BFS traversal
        Queue<int[]> q = new LinkedList<>();
        // Add the starting position (cell 1) with distance 0 to the queue
        q.offer(new int[]{1, 0});
        // Mark the starting cell as visited
        vis[1] = true;

        // Continue processing until the queue is empty
        while (!q.isEmpty()) {
            // Remove and retrieve the front element from the queue
            int[] cur = q.poll();
            // Extract the current position from the array
            int pos = cur[0], dist = cur[1];
            // If we've reached the final cell, return the distance (number of moves)
            if (pos == N) return dist;

            // Try all possible dice rolls (1 to 6)
            for (int d = 1; d <= 6; d++) {
                // Calculate the next position after rolling the dice
                int nxt = pos + d;
                // Skip if the next position exceeds the board size
                if (nxt > N) continue;
                // If there's a ladder at this position, climb up to its destination
                if (b.getLadders().containsKey(nxt)) nxt = b.getLadders().get(nxt);
                // If there's a snake at this position, slide down to its destination
                if (b.getSnakes().containsKey(nxt)) nxt = b.getSnakes().get(nxt);
                // If this cell hasn't been visited yet
                if (!vis[nxt]) {
                    // Mark the cell as visited
                    vis[nxt] = true;
                    // Add the new position to the queue with incremented distance
                    q.offer(new int[]{nxt, dist + 1});
                }
            }
        }
        // Return -1 if no path to the final cell was found (shouldn't happen in valid boards)
        return -1;
    }

    // Dijkstra's algorithm to find the shortest path (minimum moves) to reach the end
    public int dijkstra(Board b) {
        // Get the final cell number (destination) from the board
        int N = b.getFinalCell();
        // Create an array to store the minimum distance to each cell
        int[] dist = new int[N + 1];
        // Initialize all distances to infinity (maximum integer value)
        Arrays.fill(dist, Integer.MAX_VALUE);
        // Set the distance to the starting cell (cell 1) as 0
        dist[1] = 0;

        // Create a min-heap priority queue that orders by distance (second element of the array)
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        // Add the starting position (cell 1) with distance 0 to the priority queue
        pq.offer(new int[]{1, 0});

        // Continue processing until the priority queue is empty
        while (!pq.isEmpty()) {
            // Remove and retrieve the element with the smallest distance
            int[] cur = pq.poll();
            // Extract the current position
            int pos = cur[0];
            // If we've reached the final cell, stop processing (optimization)
            if (pos == N) break;

            // Try all possible dice rolls (1 to 6)
            for (int d = 1; d <= 6; d++) {
                // Calculate the next position after rolling the dice
                int nxt = pos + d;
                // Skip if the next position exceeds the board size
                if (nxt > N) continue;

                // If there's a ladder at this position, climb up to its destination
                if (b.getLadders().containsKey(nxt)) nxt = b.getLadders().get(nxt);
                // If there's a snake at this position, slide down to its destination
                if (b.getSnakes().containsKey(nxt)) nxt = b.getSnakes().get(nxt);

                // If we found a shorter path to the next cell (relaxation step)
                if (dist[pos] + 1 < dist[nxt]) {
                    // Update the distance to the next cell
                    dist[nxt] = dist[pos] + 1;
                    // Add the next cell with its new distance to the priority queue
                    pq.offer(new int[]{nxt, dist[nxt]});
                }
            }
        }
        // Return the minimum distance to the final cell
        return dist[N];
    }
}
