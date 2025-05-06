package com.cougarmaps.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ElevatorCsvDAO {
    private final String csvPath;

    /**
     * Constructor for ElevatorCsvDAO.
     *
     * @param csvPath Path to the CSV file containing elevator mappings.
     */
    public ElevatorCsvDAO(String csvPath) {
        this.csvPath = csvPath;
    }

    /**
     * Loads elevator mappings from a CSV file.
     * The CSV should have two columns: node ID and elevator name.
     *
     * @return A map where the key is the elevator name and the value is the node ID.
     */
    public Map<String, Integer> loadElevatorMappings() {

        // Initialize an empty map to store elevator mappings
        Map<String, Integer> map = new HashMap<>();

        // Attempt to read the CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            reader.readLine(); // Skip header
            String line; // Empty line for reading each line of the CSV

            // Read each line of the CSV file
            while ((line = reader.readLine()) != null) {

                // Skip empty lines
                if (line.trim().isEmpty()) continue;

                // Split the line into tokens based on the comma delimiter
                String[] tokens = line.split(",", 2);

                // Ensure there are exactly two tokens: node ID and elevator name
                if (tokens.length == 2) {
                    int nodeId = Integer.parseInt(tokens[0].trim());    // Parse the node ID
                    String name = tokens[1].trim();                     // Trim the elevator name
                    map.put(name, nodeId);                              // Add the elevator name and node ID to the map
                }
            }
        } catch (IOException e) {

            // Handle any IO exceptions that occur during file reading
            System.err.println("❌ Failed to load elevator mappings: " + e.getMessage());
        }

        return map;
    }
}
