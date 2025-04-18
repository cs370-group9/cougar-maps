package controller;

import java.util.ArrayList;
import java.util.List;
import model.Edge;
import model.EdgeLoader;
import model.Node;
import model.NodeStatus;

public class EdgeController {
    private List<Edge> edges;

    public EdgeController() {
        this.edges = new ArrayList<>();
    }

    // Load edges from a CSV file
    public void loadEdgesFromCSV(String filePath, List<Node> nodes) {
        EdgeLoader loader = new EdgeLoader();
        this.edges = loader.loadEdgesFromCSV(filePath, nodes);
    }

    // Get all edges
    public List<Edge> getAllEdges() {
        return edges;
    }

    // Add a new edge
    public void addEdge(Node fromNode, Node toNode, double distance) {
        Edge edge = new Edge(fromNode, toNode, distance);
        Edge reverseEdge = new Edge(toNode, fromNode, distance);
        edges.add(edge);
        edges.add(reverseEdge);
    }

    // Find edges connected to a specific node, ignoring inactive nodes
    public List<Edge> findEdgesByNode(Node node) {
        if (node.getStatus() != NodeStatus.ACTIVE) {
            return new ArrayList<>(); // Return an empty list if the node is inactive
        }

        List<Edge> connectedEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if ((edge.getFromNode().equals(node) || edge.getToNode().equals(node)) &&
                edge.getFromNode().getStatus() ==  NodeStatus.ACTIVE && edge.getToNode().getStatus() == NodeStatus.ACTIVE) {
                connectedEdges.add(edge);
            }
        }
        return connectedEdges;
    }
}