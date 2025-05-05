package com.cougarmaps.data;

import com.cougarmaps.Constants;
import com.cougarmaps.model.GraphModel;
import com.cougarmaps.model.graph.*;
import com.cougarmaps.model.graph.enums.NodeStatus;
import com.cougarmaps.model.graph.enums.NodeType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GraphDAO {

    /**
     * Loads the graph from CSV files.
     * @return The GraphModel object containing nodes and edges.
     * @throws IOException If an error occurs while reading the files.
     */
    public static GraphModel loadGraphFromCSV() throws IOException {
        if (Constants.DEBUG) System.out.println("  Loading data elements...");

        // Create a new GraphModel instance
        GraphModel graphModel = new GraphModel();

        // Load nodes from CSV
        loadNodes(graphModel);
        if (Constants.DEBUG) System.out.println("    Nodes: ✅");

        // Load edges from CSV
        loadEdges(graphModel);
        if (Constants.DEBUG) System.out.println("    Edges: ✅");

        if (Constants.DEBUG) System.out.println("    Graph: ✅");
        return graphModel;
    }

    /**
     * Loads nodes from the given CSV file.
     *
     * @param graphModel The GraphModel to which the nodes will be added.
     * @throws IOException If an error occurs while reading the file.
     */
    private static void loadNodes(GraphModel graphModel) throws IOException {

        // Check if the CSV file exists
        File file = new File(Constants.NODE_CSV_PATH);
        if (!file.exists()) {
            System.err.println("Node CSV file not found: " + Constants.NODE_CSV_PATH);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // skip header
            String line; // Read each line of the CSV file

            // Read each line of the CSV file
            while ((line = reader.readLine()) != null) {

                // Split the line by commas, allowing for empty fields
                String[] parts = line.split(",", -1);

                // Skip lines with fewer than 8 parts
                if (parts.length < 7) {
                    System.err.println("⛔ Skipped line (too short): " + line);
                    continue;
                }

                String id = parts[0].trim();        // Node ID
                String campus = parts[1].trim();    // Campus name
                String building = parts[2].trim();  // Building name
                String floor = parts[3].trim();     // Floor name
                String room = parts[4].trim();      // Room name
                NodeType type = NodeType.valueOf(parts[5].trim().toUpperCase());        // Node type
                NodeStatus status = NodeStatus.valueOf(parts[6].trim().toUpperCase());  // Node status


                // Create a new Node object and add it to the graph model
                Node node = new Node(id, campus, building, floor, room, type, status);
                graphModel.addNode(node);
            }
        }
    }

    /**
     * Loads edges from the given CSV file.
     * Each edge is represented by a line with two node IDs and a weight.
     *
     * @param graphModel The GraphModel to which the edges will be added.
     * @throws IOException If an error occurs while reading the file.
     */
    private static void loadEdges(GraphModel graphModel) throws IOException {

        // Check if the CSV file exists
        File file = new File(Constants.EDGE_CSV_PATH);
        if (!file.exists()) {
            System.err.println("Edge CSV file not found: " + Constants.EDGE_CSV_PATH);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int lineNumber = 0; // Line number for debugging
            reader.readLine();  // skip header
            String line;        // Read each line of the CSV file

            // Read each line of the CSV file
            while ((line = reader.readLine()) != null) {
                lineNumber++;  // Increment line number for debugging

                // Skip empty lines
                String[] parts = line.split(",", -1);

                // Skip lines with fewer than 3 parts
                if (parts.length < 3) continue;

                String fromId = parts[0].trim(); // ID of the starting node
                String toId = parts[1].trim();   // ID of the destination node
                double weight = Double.parseDouble(parts[2].trim());  // Weight of the edge

                // Retrieve the nodes from the graph model using their IDs
                Node from = graphModel.getNodeById(fromId);
                Node to = graphModel.getNodeById(toId);

                // If both nodes exist, add an edge between them
                if (from != null && to != null) {
                    graphModel.addEdge(from, to, weight);
                } else {

                    // If either node is null, print an error message
                    System.err.println("Edge skipped: invalid node ID(s) → " + fromId + " or " + toId + ". Line: " + lineNumber);
                }
            }
        }
    }
}