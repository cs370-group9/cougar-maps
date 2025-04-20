package com.cougarmaps.model.graph;

import com.cougarmaps.model.NodeStatus;

import java.util.*;

public class PathfindingEngine {

    private final Graph graph;

    public PathfindingEngine(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public List<Node> findShortestPath(Node start, Node end) {
        if (start == null || end == null ||
                start.getStatus() != NodeStatus.ACTIVE ||
                end.getStatus() != NodeStatus.ACTIVE) {
            return Collections.emptyList();
        }

        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        for (Node node : graph.getAllNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
            previous.put(node, null);
        }

        distances.put(start, 0.0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.equals(end)) break;
            if (current.getStatus() != NodeStatus.ACTIVE) continue;

            for (Edge edge : graph.getEdgesFrom(current)) {
                Node neighbor = edge.getToNode();
                if (neighbor.getStatus() != NodeStatus.ACTIVE) continue;

                double newDist = distances.get(current) + edge.getWeight();
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // Reconstruct the path
        List<Node> path = new ArrayList<>();
        Node current = end;
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }
        Collections.reverse(path);

        if (path.size() == 1 && !path.get(0).equals(start)) {
            return Collections.emptyList();
        }

        return path;
    }
}