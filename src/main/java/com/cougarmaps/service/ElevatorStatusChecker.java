package com.cougarmaps.service;

import com.cougarmaps.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

public class ElevatorStatusChecker {

    /**
     * Fetches a list of Node IDs corresponding to elevators that are currently out of service.
     * @return List of node IDs to disable
     */
    public static List<Integer> getOutOfServiceNodeIds() {
        List<Integer> outOfServiceNodeIds = new ArrayList<>();
        List<String> outOfServiceElevators = fetchOutOfServiceElevators();
        Map<String, Integer> elevatorNameToNodeId = loadElevatorMappings();

        for (String elevatorName : outOfServiceElevators) {
            Integer nodeId = elevatorNameToNodeId.get(elevatorName);
            if (nodeId != null) {
                outOfServiceNodeIds.add(nodeId);
            } else {
                System.err.println("⚠️ No mapping found for elevator: " + elevatorName);
            }
        }

        return outOfServiceNodeIds;
    }

    /**
     * Fetches the list of elevators currently marked as out of service on the CSUSM website.
     * @return List of elevator names (like "ADM ELEVATOR 5")
     */
    private static List<String> fetchOutOfServiceElevators() {
        List<String> outOfServiceElevators = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(Constants.ELEVATOR_STATUS_URL).get();
            Element header = doc.selectFirst("h1:contains(Out of Service Elevators)");
            if (header != null) {
                Element table = header.nextElementSibling();
                if (table != null && table.tagName().equals("table")) {
                    Elements rows = table.select("tr");
                    for (Element row : rows) {
                        Elements cols = row.select("td");
                        if (!cols.isEmpty()) {
                            String fullDescription = cols.getFirst().text();
                            String elevatorName = extractElevatorName(fullDescription);
                            if (elevatorName != null) {
                                outOfServiceElevators.add(elevatorName);
                            }
                        }
                    }
                } else {
                    System.err.println("⚠️ Could not find the 'Out of Service Elevators' table.");
                }
            } else {
                System.err.println("⚠️ Could not find 'Out of Service Elevators' header.");
            }

        } catch (IOException e) {
            System.err.println("Failed to fetch elevator status: " + e.getMessage());
        }

        return outOfServiceElevators;
    }

    /**
     * Loads elevator name-to-nodeId mappings from elevators.csv
     * @return Map where key = elevator name, value = node ID
     */
    private static Map<String, Integer> loadElevatorMappings() {
        Map<String, Integer> map = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.ELEVATOR_CSV_PATH))) {
            reader.readLine();
            String line; // Skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens.length >= 2) {
                    int nodeId = Integer.parseInt(tokens[0].trim());
                    String elevatorName = tokens[1].trim();
                    map.put(elevatorName, nodeId);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load elevator mappings: " + e.getMessage());
        }

        return map;
    }

    /**
     * Helper to extract the short elevator name after the dash.
     * Example: "Administrative Building - ADM ELEVATOR 5" ➔ "ADM ELEVATOR 5"
     */
    private static String extractElevatorName(String fullDescription) {
        int dashIndex = fullDescription.indexOf("-");
        if (dashIndex >= 0 && dashIndex + 1 < fullDescription.length()) {
            return fullDescription.substring(dashIndex + 1).trim();
        }
        return null;
    }
}