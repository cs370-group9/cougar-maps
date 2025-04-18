package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NodeLoader {

    public List<Node> loadNodesFromCSV(String filePath) {
        List<Node> nodes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header line

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) continue; // Skip invalid lines

                int id = Integer.parseInt(parts[0].trim());
                String campus = parts[1].trim();
                String building = parts[2].trim();
                String floor = parts[3].trim();
                String name = parts[4].trim();
                NodeType type = NodeType.valueOf(parts[5].trim().toUpperCase());
                NodeStatus status = NodeStatus.valueOf(parts[6].trim().toUpperCase());

                Node node = new Node(id, campus, building, floor, name, type, status);
                nodes.add(node);
            }
        } catch (IOException e) {
            Logger.getLogger(NodeLoader.class.getName()).log(Level.SEVERE, "Error reading the CSV file", e);
        } catch (IllegalArgumentException e) {
            Logger.getLogger(NodeLoader.class.getName()).log(Level.SEVERE, "Error parsing node type or status", e);
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.getLogger(NodeLoader.class.getName()).log(Level.SEVERE, "Error parsing node data", e);
        } catch (Exception e) {
            Logger.getLogger(NodeLoader.class.getName()).log(Level.SEVERE, "Unexpected error", e);
        }

        return nodes;
    }
}