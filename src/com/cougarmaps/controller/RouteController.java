package com.cougarmaps.controller;


import com.cougarmaps.model.graph.Edge;
import com.cougarmaps.model.graph.Graph;
import com.cougarmaps.model.graph.Node;
import com.cougarmaps.model.graph.PathfindingEngine;
import com.cougarmaps.model.NodeType;
import com.cougarmaps.view.DirectionsView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class RouteController {

    private final PathfindingEngine pathfinder;

    public RouteController(Graph graph) {
        this.pathfinder = new PathfindingEngine(graph);
    }

    public void handleRoute(Node start, Node destination) {
        if (start == null || destination == null) {
            JOptionPane.showMessageDialog(null, "Start or destination is not selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Node> path = pathfinder.findShortestPath(start, destination);
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No valid path could be found.", "No Path", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> steps = new ArrayList<>();
        steps.add(String.format("From: %s, %s, %s",
                start.getBuilding(), start.getFloor(), start.getName()));
        steps.add(String.format("To: %s, %s, %s",
                destination.getBuilding(), destination.getFloor(), destination.getName()));
        steps.add("----------------------------------------------------------------------------");

        int stepNum = 1;
        double accumulatedDistance = 0.0;
        double totalDistance = 0.0;
        Node previous = path.get(0);

        for (int i = 1; i < path.size(); i++) {
            Node current = path.get(i);
            double distance = getEdgeWeightBetween(previous, current);
            accumulatedDistance += distance;
            totalDistance += distance;

            boolean isLast = (i == path.size() - 1);
            boolean isElevator = current.getType() == NodeType.ELEVATOR;
            boolean isHallway = current.getType() == NodeType.HALLWAY;

            // Handle floor-changing elevator
            if (isElevator && i + 1 < path.size()) {
                Node next = path.get(i + 1);
                boolean floorChange = !current.getFloor().equals(next.getFloor());

                if (floorChange) {
                    if (previous.getType() != NodeType.ELEVATOR) {
                        steps.add(String.format("Step %d: Walk %.0f feet to %s", stepNum++, accumulatedDistance, current.getName()));
                    }
                    try {
                        int from = Integer.parseInt(current.getFloor());
                        int to = Integer.parseInt(next.getFloor());
                        int diff = Math.abs(to - from);
                        String direction = (to > from) ? "up" : "down";
                        String ordinal = toOrdinal(next.getFloor());

                        steps.add(String.format("Step %d: Take the elevator %s %d floor%s to the %s floor",
                                stepNum++, direction, diff, diff == 1 ? "" : "s", ordinal));
                    } catch (NumberFormatException e) {
                        steps.add(String.format("Step %d: Take the elevator to floor %s", stepNum++, next.getFloor()));
                    }

                    accumulatedDistance = 0.0;
                    previous = current;
                    continue;
                }
            }

            // Print hallway junctions
            if (isHallway) {
                steps.add(String.format("Step %d: Walk %.0f feet to %s", stepNum++, accumulatedDistance, current.getName()));
                accumulatedDistance = 0.0;
            }

            // Only print the final classroom
            if (isLast && current.getType() == NodeType.CLASSROOM) {
                steps.add(String.format("Step %d: Walk %.0f feet to %s", stepNum++, accumulatedDistance, current.getName()));
            }

            // Reset accumulated if printed
            accumulatedDistance = (isHallway || (isLast && current.getType() == NodeType.CLASSROOM)) ? 0.0 : accumulatedDistance;
            previous = current;
        }

        steps.add("----------------------------------------------------------------------------");
        steps.add(String.format("Total distance: %.0f feet", totalDistance));

        new DirectionsView(steps);
    }

    private double getEdgeWeightBetween(Node from, Node to) {
        for (Edge edge : pathfinder.getGraph().getEdgesFrom(from)) {
            if (edge.getToNode().equals(to)) {
                return edge.getWeight();
            }
        }
        return 0;
    }

    private String toOrdinal(String floor) {
        try {
            int num = Integer.parseInt(floor.replaceAll("[^0-9]", ""));
            if (num % 100 >= 11 && num % 100 <= 13) return num + "th";
            return switch (num % 10) {
                case 1 -> num + "st";
                case 2 -> num + "nd";
                case 3 -> num + "rd";
                default -> num + "th";
            };
        } catch (NumberFormatException e) {
            return floor; // fallback if floor isn't a number
        }
    }


}
