package com.cougarmaps.routing;

import com.cougarmaps.model.graph.Graph;
import com.cougarmaps.model.graph.Node;
import com.cougarmaps.model.graph.enums.NodeStatus;
import com.cougarmaps.model.graph.enums.NodeType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteBuilderTest {

    private Node makeNode(String id, String building, String floor, String room, NodeType type) {
        return new Node(id, "Main", building, floor, room, type, NodeStatus.ACTIVE);
    }

    @Test
    public void testConsecutiveClassroomsAreConsolidated() {
        Graph graph = new Graph();
        Node n1 = makeNode("1", "A", "1", "101", NodeType.CLASSROOM);
        Node n2 = makeNode("2", "A", "1", "102", NodeType.CLASSROOM);
        Node n3 = makeNode("3", "A", "1", "103", NodeType.CLASSROOM);

        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);

        graph.addEdge(n1, n2, 5);
        graph.addEdge(n2, n3, 5);

        List<Node> path = List.of(n1, n2, n3);
        List<RouteStep> steps = RouteBuilder.fromNodeList(graph, path);

        assertEquals(1, steps.size());
        RouteStep step = steps.get(0);
        assertEquals(n1, step.getFrom());
        assertEquals(n3, step.getTo());
        assertEquals(10.0, step.getDistance());
    }

    @Test
    public void testElevatorsAreConsolidatedAsFloors() {
        Graph graph = new Graph();
        Node e1 = makeNode("E1", "A", "1", "E1", NodeType.ELEVATOR);
        Node e2 = makeNode("E2", "A", "2", "E2", NodeType.ELEVATOR);
        Node e3 = makeNode("E3", "A", "3", "E3", NodeType.ELEVATOR);

        graph.addNode(e1);
        graph.addNode(e2);
        graph.addNode(e3);

        graph.addEdge(e1, e2, 1);
        graph.addEdge(e2, e3, 1);

        List<Node> path = List.of(e1, e2, e3);
        List<RouteStep> steps = RouteBuilder.fromNodeList(graph, path);

        assertEquals(1, steps.size());
        RouteStep step = steps.get(0);
        assertEquals(e1, step.getFrom());
        assertEquals(e3, step.getTo());
        assertEquals(2.0, step.getDistance());
    }

    @Test
    public void testSingleStepPath() {
        Graph graph = new Graph();
        Node n1 = makeNode("1", "A", "1", "101", NodeType.CLASSROOM);
        Node n2 = makeNode("2", "A", "1", "102", NodeType.CLASSROOM);

        graph.addNode(n1);
        graph.addNode(n2);
        graph.addEdge(n1, n2, 7);

        List<Node> path = List.of(n1, n2);
        List<RouteStep> steps = RouteBuilder.fromNodeList(graph, path);

        assertEquals(1, steps.size());
        RouteStep step = steps.get(0);
        assertEquals(7.0, step.getDistance());
    }

    @Test
    public void testMixedPath() {
        Graph graph = new Graph();
        Node n1 = makeNode("1", "A", "1", "101", NodeType.CLASSROOM);
        Node n2 = makeNode("2", "A", "1", "102", NodeType.CLASSROOM);
        Node n3 = makeNode("3", "A", "1", "103", NodeType.CLASSROOM);
        Node e1 = makeNode("4", "A", "1", "Elevator", NodeType.ELEVATOR);
        Node e2 = makeNode("5", "A", "2", "Elevator", NodeType.ELEVATOR);
        Node n4 = makeNode("6", "A", "2", "201", NodeType.CLASSROOM);

        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(e1);
        graph.addNode(e2);
        graph.addNode(n4);

        graph.addEdge(n1, n2, 3);
        graph.addEdge(n2, n3, 3);
        graph.addEdge(n3, e1, 2);
        graph.addEdge(e1, e2, 1);
        graph.addEdge(e2, n4, 4);

        List<Node> path = List.of(n1, n2, n3, e1, e2, n4);
        List<RouteStep> steps = RouteBuilder.fromNodeList(graph, path);

        assertEquals(2, steps.size());

        RouteStep step1 = steps.get(0);
        assertEquals(n1, step1.getFrom());
        assertEquals(n3, step1.getTo());
        assertEquals(6.0, step1.getDistance());

        RouteStep step2 = steps.get(1);
        assertEquals(e1, step2.getFrom());
        assertEquals(e2, step2.getTo());
        assertEquals(1.0, step2.getDistance());
    }
}
