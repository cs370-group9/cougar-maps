package com.cougarmaps.controller;

import com.cougarmaps.Constants;
import com.cougarmaps.model.graph.Graph;
import com.cougarmaps.model.graph.GraphLoader;
import com.cougarmaps.model.graph.Node;
import com.cougarmaps.model.NodeStatus;
import com.cougarmaps.model.NodeType;
import com.cougarmaps.service.ElevatorStatusChecker;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class GraphController {
    private final Graph graph;

    public GraphController() {
        this.graph = GraphLoader.loadFromCSV(Constants.NODE_CSV_PATH, Constants.EDGE_CSV_PATH);
        updateElevatorStatuses(); // Immediately disable broken elevators after loading
    }

    private void updateElevatorStatuses() {
        List<Integer> outOfServiceNodeIds = ElevatorStatusChecker.getOutOfServiceNodeIds();

        for (Integer nodeId : outOfServiceNodeIds) {
            Node node = graph.getNode(nodeId);
            if (node != null) {
                node.setStatus(NodeStatus.INACTIVE);
                System.out.println("⚠️ Node ID " + nodeId + " (" + node.getName() + ") marked as INACTIVE.");
            }
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public Set<String> getBuildings() {
        return graph.getAllNodes().stream()
                .filter(n -> n.getType() == NodeType.CLASSROOM)
                .map(Node::getBuilding)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<String> getFloors(String building) {
        return graph.getAllNodes().stream()
                .filter(n -> n.getType() == NodeType.CLASSROOM)
                .filter(n -> building.equals(n.getBuilding()))
                .map(Node::getFloor)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<String> getRooms(String building, String floor) {
        return graph.getAllNodes().stream()
                .filter(n -> n.getType() == NodeType.CLASSROOM)
                .filter(n -> building.equals(n.getBuilding()) && floor.equals(n.getFloor()))
                .map(Node::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Node getNode(String building, String floor, String room) {
        return graph.getAllNodes().stream()
                .filter(n ->
                        building.equals(n.getBuilding()) &&
                                floor.equals(n.getFloor()) &&
                                room.equals(n.getName()))
                .findFirst().orElse(null);
    }
}
