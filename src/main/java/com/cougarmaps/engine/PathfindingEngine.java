package com.cougarmaps.engine;

import com.cougarmaps.model.graph.*;
import com.cougarmaps.model.graph.enums.NodeStatus;

import java.util.*;

/**
 * Computes the shortest path between nodes using Dijkstra's algorithm,
 * while ignoring nodes that are not ACTIVE.
 */
public class PathfindingEngine {

    /**
     * Finds the shortest path from start to end using Dijkstra's algorithm,
     * considering only ACTIVE nodes.
     *
     * @param graph The graph to search.
     * @param start The starting node.
     * @param end   The destination node.
     * @return A list of nodes representing the path from start to end (inclusive), or empty if no path is found.
     */
    public List<Node> findShortestPath(Graph graph, Node start, Node end) {
        if (start == null || end == null || graph == null) return Collections.emptyList();
        if (start.getStatus() != NodeStatus.ACTIVE || end.getStatus() != NodeStatus.ACTIVE) return Collections.emptyList();

        // Initialize distances and previous nodes
        Map<Node, Double> distance = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(distance::get));

        // Set initial distances to infinity and previous nodes to null
        for (Node node : graph.getAllNodes()) {
            if (node.getStatus() == NodeStatus.ACTIVE) {
                distance.put(node, Double.POSITIVE_INFINITY);   // Set initial distance to infinity
                previous.put(node, null);                       // No previous node
            }
        }

        // Set the distance to the start node to 0 and add it to the queue
        distance.put(start, 0.0);
        queue.add(start);

        // Dijkstra's algorithm
        while (!queue.isEmpty()) {

            // Get the node with the smallest distance
            Node current = queue.poll();

            // If we reached the end node, break
            if (current.equals(end)) break;

            // If the current node is not active, skip it
            for (Edge edge : graph.getEdges(current)) {
                Node neighbor = edge.getTo(); // Get the neighbor node

                // Skip if the neighbor is not active
                if (neighbor.getStatus() != NodeStatus.ACTIVE) continue;

                // Calculate the new distance
                double newDist = distance.get(current) + edge.getWeight();

                // If the new distance is less, update the distance and previous node
                if (newDist < distance.get(neighbor)) {
                    distance.put(neighbor, newDist);    // Update the distance
                    previous.put(neighbor, current);    // Update the previous node
                    queue.remove(neighbor);             // Avoid duplicates
                    queue.add(neighbor);                // Re-add the neighbor to the queue
                }
            }
        }

        // If the end node is unreachable, return an empty list
        return reconstructPath(previous, start, end);
    }

    /**
     * Reconstructs the path from start to end using the previous nodes map.
     *
     * @param previous The map of previous nodes.
     * @param start    The starting node.
     * @param end      The destination node.
     * @return A list of nodes representing the path from start to end (inclusive), or empty if no path is found.
     */
    private List<Node> reconstructPath(Map<Node, Node> previous, Node start, Node end) {

        // Check if the end node is reachable
        List<Node> path = new ArrayList<>();    // Initialize the path list
        Node current = end;                     // Start from the end node

        // Backtrack through the previous nodes to reconstruct the path
        while (current != null && previous.containsKey(current)) {
            path.add(current);                  // Add the current node to the path
            current = previous.get(current);    // Move to the previous node
        }

        // If the path is valid and starts with the start node, reverse it
        if (!path.isEmpty() && path.getLast().equals(start)) {
            Collections.reverse(path);  // Reverse the path to get the correct order
            return path;                // Return the reconstructed path
        }

        // If the path is empty or does not start with the start node, return an empty list
        return Collections.emptyList();
    }
}
