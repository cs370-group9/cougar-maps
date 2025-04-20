package com.cougarmaps.model.graph;

import com.cougarmaps.model.NodeStatus;
import com.cougarmaps.model.NodeType;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class GraphLoader {

    public static Graph loadFromCSV(String nodeFilePath, String edgeFilePath) {
        Graph graph = new Graph();

        // --- Load Nodes ---
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(nodeFilePath)))) {

            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split(",");

                int id = Integer.parseInt(tokens[0].trim());
                String campus = tokens[1].trim();
                String building = tokens[2].trim();
                String floor = tokens[3].trim();
                String name = tokens[4].trim();
                NodeType type = NodeType.valueOf(tokens[5].trim().toUpperCase());
                NodeStatus status = NodeStatus.valueOf(tokens[6].trim().toUpperCase());

                Node node = new Node(id, campus, building, floor, name, type, status);
                graph.addNode(node);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading nodes file:\n" + e.getMessage(),
                    "File Load Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }

        // --- Load Edges ---
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(edgeFilePath)))) {

            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split(",");

                int fromId = Integer.parseInt(tokens[0].trim());
                int toId = Integer.parseInt(tokens[1].trim());
                double weight = Double.parseDouble(tokens[2].trim());

                Node fromNode = graph.getNode(fromId);
                Node toNode = graph.getNode(toId);

                if (fromNode != null && toNode != null) {
                    graph.addEdge(new Edge(fromNode, toNode, weight));
                    graph.addEdge(new Edge(toNode, fromNode, weight));
                } else {
                    System.err.printf("⚠️ Edge skipped — from_id: %d, to_id: %d (node not found)%n", fromId, toId);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading edges file:\n" + e.getMessage(),
                    "File Load Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }

        return graph;
    }
}