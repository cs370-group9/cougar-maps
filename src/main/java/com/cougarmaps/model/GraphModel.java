package com.cougarmaps.model;

import com.cougarmaps.model.graph.Node;
import com.cougarmaps.model.graph.Edge;
import com.cougarmaps.model.graph.Graph;
import com.cougarmaps.model.graph.enums.NodeStatus;
import com.cougarmaps.model.graph.enums.NodeType;
import com.cougarmaps.model.location.Building;


import java.util.*;

/**
 * The GraphModel class represents a model for managing a graph of nodes and edges.
 * It provides methods to add nodes, create edges, and retrieve information about the graph.
 * The graph is used to represent the layout of buildings and their rooms.
 */
public class GraphModel {
    private final Graph graph;
    private final Map<String, Building> buildingsByName;

    /**
     * Constructor for the GraphModel.
     * Initializes the graph and the building hierarchy.
     */
    public GraphModel() {
        this.graph = new Graph();
        this.buildingsByName = new HashMap<>();
    }

    /**
     * Retrieves the underlying graph.
     *
     * @return The Graph instance.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Adds a Node to the graph and updates the building.
     * @param node The node to be added.
     */
    public void addNode(Node node) {
        graph.addNode(node);
    }

    /**
     * Adds an edge between two nodes in the graph.
     *
     * @param from   The starting node.
     * @param to     The destination node.
     * @param weight The distance or cost of the edge.
     */
    public void addEdge(Node from, Node to, double weight) {
        graph.addEdge(from, to, weight);
    }

    /**
     * Retrieves a node by its ID.
     *
     * @param id The unique node ID.
     * @return The corresponding Node or null if not found.
     */
    public Node getNodeById(String id) {
        return graph.getNodeById(id);
    }

    /**
     * Retrieves all edges from a specific node.
     *
     * @param node The node from which to retrieve edges.
     * @return A list of edges originating from the specified node.
     */
    public List<Edge> getEdgesFrom(Node node) {
        return graph.getEdges(node);
    }

    /**
     * Retrieves all nodes in the graph.
     *
     * @return A collection of all nodes.
     */
    public Collection<Node> getAllNodes() {
        return graph.getAllNodes();
    }

    /**
     * Retrieves all buildings in the graph.
     *
     * @return A collection of all buildings.
     */
    public Map<String, Map<String, List<String>>> getBuildingFloorRoomMap() {
        Map<String, Map<String, List<String>>> structure = new HashMap<>();

        for (Node node : graph.getAllNodes()) {
            if (node.getType() == NodeType.CLASSROOM) {
                String building = node.getBuilding();
                String floor = node.getFloor();
                String room = node.getRoom();

                structure
                        .computeIfAbsent(building, b -> new HashMap<>())
                        .computeIfAbsent(floor, f -> new ArrayList<>())
                        .add(room);
            }
        }

        return structure;
    }

    public Node getNode(String building, String floor, String room) {
        for (Node node : graph.getAllNodes()) {
            if (node.getBuilding().equals(building)
                    && node.getFloor().equals(floor)
                    && node.getRoom().equals(room)
                    && node.getStatus() == NodeStatus.ACTIVE) {
                return node;
            }
        }
        return null;
    }



}
