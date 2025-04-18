package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EdgeLoader {

    public List<Edge> loadEdgesFromCSV(String filePath, List<Node> nodes) {
        List<Edge> edges = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header line

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue; // Skip invalid lines

                int fromId = Integer.parseInt(parts[0].trim());
                int toId = Integer.parseInt(parts[1].trim());
                double distance = Double.parseDouble(parts[2].trim());

                Node fromNode = findNodeById(nodes, fromId);
                Node toNode = findNodeById(nodes, toId);

                if (fromNode != null && toNode != null) {
                    Edge edge = new Edge(fromNode, toNode, distance);
                    edges.add(edge);
                }
            }
        } catch (IOException e) {
            Logger.getLogger(EdgeLoader.class.getName()).log(Level.SEVERE, "Error reading the CSV file", e);
        } catch (NumberFormatException e) {
            Logger.getLogger(EdgeLoader.class.getName()).log(Level.SEVERE, "Error parsing distance", e);
        } catch (IllegalArgumentException e) {
            Logger.getLogger(EdgeLoader.class.getName()).log(Level.SEVERE, "Error parsing node type or status", e);
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.getLogger(EdgeLoader.class.getName()).log(Level.SEVERE, "Error parsing edge data", e);
        } catch (Exception e) {
            Logger.getLogger(EdgeLoader.class.getName()).log(Level.SEVERE, "Unexpected error", e);
        }

        return edges;
    }

    private Node findNodeById(List<Node> nodes, int id) {
        for (Node node : nodes) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null; // Return null if no node is found
    }
}