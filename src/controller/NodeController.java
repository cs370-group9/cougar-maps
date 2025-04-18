package controller;

import model.Node;
import model.NodeLoader;
import model.NodeType;
import model.NodeStatus;

import java.util.ArrayList;
import java.util.List;

public class NodeController {
    private List<Node> nodes;

    public NodeController() {
        this.nodes = new ArrayList<>();
    }

    // Load nodes from a CSV file
    public void loadNodesFromCSV(String filePath) {
        NodeLoader loader = new NodeLoader();
        this.nodes = loader.loadNodesFromCSV(filePath);
    }

    // Add a new node
    public void addNode(int id, String campus, String building, String floor, String name, NodeType type, NodeStatus status) {
        Node node = new Node(id, campus, building, floor, name, type, status);
        nodes.add(node);
    }

    // Find a node by ID
    public Node findNodeById(int id) {
        for (Node node : nodes) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null; // Return null if no node is found
    }

    // Update a node's status
    public boolean updateNodeStatus(int id, NodeStatus newStatus) {
        Node node = findNodeById(id);
        if (node != null) {
            node.setStatus(newStatus);
            return true;
        }
        return false; // Return false if the node is not found
    }
}