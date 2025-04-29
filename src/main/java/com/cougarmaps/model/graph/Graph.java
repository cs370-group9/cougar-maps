package com.cougarmaps.model.graph;

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

        // Add edge to an Tadjacency list
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph:\n");
        for (Node node : adjacencyList.keySet()) {
            sb.append("  ").append(node).append(" → ").append(adjacencyList.get(node)).append("\n");
        }
        return sb.toString();
    }
}
