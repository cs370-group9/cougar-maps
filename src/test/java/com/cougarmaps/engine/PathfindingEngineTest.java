package com.cougarmaps.engine;

import com.cougarmaps.model.graph.*;
import com.cougarmaps.model.graph.enums.NodeStatus;
import com.cougarmaps.model.graph.enums.NodeType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PathfindingEngineTest {

    private Node createNode(String id, String floor, NodeStatus status) {
        return new Node(id, "Main", "Science", floor, "Room" + id, NodeType.CLASSROOM, status);
    }

    @Test
    void findShortestPath_shouldReturnCorrectPath() {
        Graph graph = new Graph();
        Node a = createNode("A", "1", NodeStatus.ACTIVE);
        Node b = createNode("B", "1", NodeStatus.ACTIVE);
        Node c = createNode("C", "1", NodeStatus.ACTIVE);

        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addEdge(a, b, 2);
        graph.addEdge(b, c, 2);

        PathfindingEngine engine = new PathfindingEngine();
        List<Node> path = engine.findShortestPath(graph, a, c);

        assertEquals(List.of(a, b, c), path);
    }

    @Test
    void findShortestPath_shouldIgnoreInactiveNodes() {
        Graph graph = new Graph();
        Node a = createNode("A", "1", NodeStatus.ACTIVE);
        Node b = createNode("B", "1", NodeStatus.INACTIVE);
        Node c = createNode("C", "1", NodeStatus.ACTIVE);

        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addEdge(a, b, 1);
        graph.addEdge(b, c, 1);
        graph.addEdge(a, c, 10); // Longer direct path

        PathfindingEngine engine = new PathfindingEngine();
        List<Node> path = engine.findShortestPath(graph, a, c);

        // Should skip the faster route (through INACTIVE b)
        assertEquals(List.of(a, c), path);
    }

    @Test
    void findShortestPath_shouldReturnEmptyListIfUnreachable() {
        Graph graph = new Graph();
        Node a = createNode("A", "1", NodeStatus.ACTIVE);
        Node b = createNode("B", "1", NodeStatus.ACTIVE);

        graph.addNode(a);
        graph.addNode(b);
        // No edge between a and b

        PathfindingEngine engine = new PathfindingEngine();
        List<Node> path = engine.findShortestPath(graph, a, b);

        assertTrue(path.isEmpty());
    }
}
