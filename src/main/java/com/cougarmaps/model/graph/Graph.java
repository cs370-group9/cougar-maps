package com.cougarmaps.model.graph;

import java.util.*;

/**
 * Represents a weighted, undirected graph structure using an adjacency list.
 * Nodes and edges are managed independently of metadata like buildings/floors.
 */
public class Graph {
    private final Map<String, Node> nodesById = new HashMap<>();
    private final Map<Node, List<Edge>> adjacencyList = new HashMap<>();

    /**
     * Adds a node to the graph.
     *
     * @param node The node to add.
     */
    public void addNode(Node node) {
        if (node == null || nodesById.containsKey(node.getId())) return;

        nodesById.put(node.getId(), node);
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    /**
     * Adds edges from one node to another.
     *
     * @param from   The starting node.
     * @param to     The destination node.
     * @param weight The distance or cost of the edge.
     */
    public void addEdge(Node from, Node to, double weight) {
        if (from == null || to == null) return;

        // Ensure both nodes exist in the graph
        addNode(from);
        addNode(to);

        // Add the edge to the adjacency list
        adjacencyList.get(from).add(new Edge(from, to, weight));
        adjacencyList.get(to).add(new Edge(to, from, weight));
    }

    /**
     * Retrieves a node by its ID.
     *
     * @param id The unique node ID.
     * @return The corresponding Node or null if not found.
     */
    public Node getNodeById(String id) {
        return nodesById.get(id);
    }

    /**
     * Retrieves all edges originating from a specific node.
     *
     * @param node The node from which to retrieve edges.
     * @return A list of edges originating from the specified node.
     */
    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    /**
     * Gets a read-only view of the adjacency list.
     *
     * @return The full adjacency list.
     */
    public Map<Node, List<Edge>> getAdjacencyList() {
        return Collections.unmodifiableMap(adjacencyList);
    }

    /**
     * Gets all nodes in the graph.
     *
     * @return A collection of all nodes.
     */
    public Collection<Node> getAllNodes() {
        return Collections.unmodifiableCollection(nodesById.values());
    }

    /**
     * Gets the total number of nodes in the graph.
     */
    public int size() {
        return nodesById.size();
    }

    /**
     * Clears the graph (for testing or reloading).
     */
    public void clear() {
        nodesById.clear();
        adjacencyList.clear();
    }
}