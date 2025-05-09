package com.cougarmaps.model;

import com.cougarmaps.model.graph.Node;
import com.cougarmaps.model.graph.enums.NodeStatus;
import com.cougarmaps.model.graph.enums.NodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphModelTest {

    private GraphModel graphModel;
    private Node node1;
    private Node node2;
    private Node node3;

    @BeforeEach
    void setUp() {
        graphModel = new GraphModel();

        node1 = new Node("1", "Main", "Science", "1", "101", NodeType.CLASSROOM, NodeStatus.ACTIVE);
        node2 = new Node("2", "Main", "Science", "1", "102", NodeType.CLASSROOM, NodeStatus.ACTIVE);
        node3 = new Node("3", "Main", "Science", "1", "103", NodeType.CLASSROOM, NodeStatus.RETIRED);

        graphModel.addNode(node1);
        graphModel.addNode(node2);
        graphModel.addNode(node3);

        graphModel.addEdge(node1, node2, 10.0);
        graphModel.addEdge(node2, node3, 20.0);
    }

    @Test
    void testAddAndGetNodeById() {
        assertEquals(node1, graphModel.getNodeById("1"));
        assertEquals(node2, graphModel.getNodeById("2"));
        assertEquals(node3, graphModel.getNodeById("3"));
    }

    @Test
    void testGetAllNodes() {
        assertEquals(3, graphModel.getAllNodes().size());
    }

    @Test
    void testGetEdgesFromNode() {
        assertEquals(1, graphModel.getEdgesFrom(node1).size());
        assertEquals(2, graphModel.getEdgesFrom(node2).size());
    }

    @Test
    void testEdgeBidirectionality() {
        boolean hasForward = graphModel.getEdgesFrom(node1)
                .stream().anyMatch(e -> e.getTo().equals(node2));
        boolean hasReverse = graphModel.getEdgesFrom(node2)
                .stream().anyMatch(e -> e.getTo().equals(node1));

        assertTrue(hasForward, "Missing edge from node1 to node2");
        assertTrue(hasReverse, "Missing edge from node2 to node1");
    }
}
