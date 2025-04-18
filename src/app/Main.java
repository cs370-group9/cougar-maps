package app;

import java.util.List;
import model.Edge;
import model.EdgeLoader;
import model.Node;
import model.NodeLoader;

public class Main {
    public static void main(String[] args) {
        // Paths to the CSV files
        String nodesFilePath = "resources/nodes.csv";
        String edgesFilePath = "resources/edges.csv";

        // Create an instance of NodeLoader and EdgeLoader
        NodeLoader nodeLoader = new NodeLoader();
        EdgeLoader edgeLoader = new EdgeLoader();

        // Load nodes from the CSV file
        List<Node> nodes = nodeLoader.loadNodesFromCSV(nodesFilePath);

        // Print the loaded nodes to verify
        System.out.println("Loaded Nodes:");
        for (Node node : nodes) {
            System.out.println(node);
        }

        // Load edges from the CSV file
        List<Edge> edges = edgeLoader.loadEdgesFromCSV(edgesFilePath, nodes);

        // Print the loaded edges to verify
        System.out.println("\nLoaded Edges:");
        for (Edge edge : edges) {
            System.out.println(edge);
        }
    }
}