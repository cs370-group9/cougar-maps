package com.cougarmaps.service;

import com.cougarmaps.Constants;
import com.cougarmaps.data.ElevatorCsvDAO;
import com.cougarmaps.model.graph.Graph;
import com.cougarmaps.model.graph.Node;
import com.cougarmaps.model.graph.enums.NodeStatus;
import com.cougarmaps.model.graph.enums.NodeType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * Checks the current status of elevators from a remote source and updates the graph
 * by marking out-of-service elevators as INACTIVE.
 */
public class ElevatorStatusChecker {
    private final ElevatorCsvDAO elevatorCsvDAO;
    private final String elevatorStatusUrl;

    public ElevatorStatusChecker(ElevatorCsvDAO dao, String statusUrl) {
        this.elevatorCsvDAO = dao;
        this.elevatorStatusUrl = statusUrl;
    }

    /**
     * Updates the provided graph by resetting all elevators to ACTIVE and marking
     * the ones found on the out-of-service list as INACTIVE.
     *
     * @param graph The graph containing elevator nodes to be updated.
     */
    public void markOutOfServiceElevators(Graph graph) {
        List<String> outOfServiceNames = fetchOutOfServiceElevators();
        Map<String, Integer> nameToId = elevatorCsvDAO.loadElevatorMappings();

        // Set all elevator nodes to ACTIVE
        for (Node node : graph.getAllNodes()) {
            if (node.getType() == NodeType.ELEVATOR) {
                node.setStatus(NodeStatus.ACTIVE);
            }
        }

        // Mark out-of-service elevators as INACTIVE
        for (String name : outOfServiceNames) {
            Integer nodeId = nameToId.get(name);
            if (nodeId != null) {
                Node node = graph.getNodeById(String.valueOf(nodeId));
                if (node != null) {
                    node.setStatus(NodeStatus.INACTIVE);
                }
            }
        }

        // Debug output
        if (Constants.DEBUG) {
            System.out.println("  Loading Elevators Status:");
            for (String name : outOfServiceNames) {
                System.out.println("    " + name + ":⚠️");
            }
        }
    }

    /**
     * Scrapes the elevator status webpage and extracts the names of elevators currently out of service.
     *
     * @return A list of elevator names (as shown on the website) that are out of service.
     */
    private List<String> fetchOutOfServiceElevators() {
        List<String> names = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(elevatorStatusUrl).get();
            Element header = doc.selectFirst("h1:contains(Out of Service Elevators)");
            if (header != null) {
                Element table = header.nextElementSibling();
                if (table != null && table.tagName().equals("table")) {
                    for (Element row : table.select("tr")) {
                        Elements cols = row.select("td");
                        if (!cols.isEmpty()) {
                            String full = cols.get(0).text();
                            String name = extractElevatorName(full);
                            if (name != null) names.add(name);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Failed to fetch elevator status: " + e.getMessage());
        }

        return names;
    }

    /**
     * Extracts the elevator name from a full string.
     *
     * @param full The full string containing the elevator information.
     * @return The extracted elevator name, or null if not found.
     */
    private String extractElevatorName(String full) {
        int dash = full.indexOf("-");
        return (dash >= 0 && dash + 1 < full.length()) ? full.substring(dash + 1).trim() : null;
    }
}
