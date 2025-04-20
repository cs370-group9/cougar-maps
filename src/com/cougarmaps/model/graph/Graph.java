package com.cougarmaps.model.graph;

import com.cougarmaps.model.NodeStatus;

import java.util.*;

public class Graph {
    private final Map<Integer, Node> nodes;
    private final Map<Node, List<Edge>> adjacencyList;

    public Graph() {
        this.nodes = new HashMap<>();
        this.adjacencyList = new HashMap<>();
    }

    public void addNode(Node node) {
        nodes.putIfAbsent(node.getId(), node);
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(Edge edge) {
        Node from = edge.getFromNode();
        Node to = edge.getToNode();

        // Add nodes if they don't already exist
        addNode(from);
        addNode(to);

        // Add edge to adjacency list
        adjacencyList.get(from).add(edge);
    }

    public Node getNode(int id) {
        return nodes.get(id);
    }

    public Collection<Node> getAllNodes() {
        return nodes.values();
    }

    public List<Edge> getEdgesFrom(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    public Map<Node, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public List<Node> findShortestPath(Node start, Node end) {
        if (start == null || end == null || start.getStatus() != NodeStatus.ACTIVE || end.getStatus() != NodeStatus.ACTIVE) {
            return Collections.emptyList();
        }

        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        for (Node node : nodes.values()) {
            distances.put(node, Double.POSITIVE_INFINITY);
            previous.put(node, null);
        }

        distances.put(start, 0.0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.equals(end)) break;
            if (current.getStatus() != NodeStatus.ACTIVE) continue;

            for (Edge edge : adjacencyList.getOrDefault(current, Collections.emptyList())) {
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

        // Reconstruct path
        List<Node> path = new ArrayList<>();
        Node current = end;
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }
        Collections.reverse(path);

        // If path only contains the end and it's not reachable
        if (path.size() == 1 && !path.get(0).equals(start)) {
            return Collections.emptyList();
        }

        return path;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph:\n");
        for (Node node : adjacencyList.keySet()) {
            sb.append("  ").append(node).append(" → ").append(adjacencyList.get(node)).append("\n");
        }
        return sb.toString();
    }
}
