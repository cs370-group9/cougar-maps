package com.cougarmaps.controller;

import com.cougarmaps.model.graph.Edge;
import com.cougarmaps.model.graph.Node;
import com.cougarmaps.model.graph.PathfindingEngine;
import com.cougarmaps.model.NodeType;
import com.cougarmaps.view.DirectionsView;
import com.cougarmaps.view.PointSelectionMode;
import com.cougarmaps.view.PointSelectionView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class RouteController {

    private final GraphController graphController;
    private final PathfindingEngine pathfinder;
    private PointSelectionView selectionView;
    private Node startNode;
    private Node endNode;

    public RouteController(GraphController graphController) {
        this.graphController = graphController;
        this.pathfinder = new PathfindingEngine(graphController.getGraph());
    }

    public void handleRoute(Node start, Node end, PointSelectionView selectionView) {
        if (start == null || end == null) {
            JOptionPane.showMessageDialog(null, "Start or destination is not selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.selectionView = selectionView;
        this.startNode = start;
        this.endNode = end;

        List<Node> path = pathfinder.findShortestPath(start, end);
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No valid path could be found.", "No Path", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> steps = new ArrayList<>();
        steps.add(String.format("From: %s, %s, %s", start.getBuilding(), start.getFloor(), start.getName()));
        steps.add(String.format("To: %s, %s, %s", end.getBuilding(), end.getFloor(), end.getName()));
        steps.add("----------------------------------------------------------------------------");

        int stepNum = 1;
        double accumulatedDistance = 0.0;
        double totalDistance = 0.0;
        Node previous = path.getFirst();

        for (int i = 1; i < path.size(); i++) {
            Node current = path.get(i);
            double distance = getEdgeWeightBetween(previous, current);
            accumulatedDistance += distance;
            totalDistance += distance;

            boolean isElevator = current.getType() == NodeType.ELEVATOR;
            boolean isHallway = current.getType() == NodeType.HALLWAY;
            boolean isLast = (i == path.size() - 1);

            if (previous.getType() != NodeType.DOOR && current.getType() == NodeType.DOOR) {
                steps.add(String.format("Step %d: Go %.0f feet and exit %s using %s",
                        stepNum++, accumulatedDistance, previous.getBuilding(), current.getName()));
                accumulatedDistance = 0.0;
            } else if (previous.getType() == NodeType.DOOR && current.getType() == NodeType.DOOR) {
                steps.add(String.format("Step %d: Go %.0f feet to %s at %s",
                        stepNum++, accumulatedDistance, current.getName(), current.getBuilding()));
                accumulatedDistance = 0.0;
            } else if (previous.getType() == NodeType.DOOR && current.getType() != NodeType.DOOR) {
                steps.add(String.format("Step %d: Enter %s and go %.0f feet to %s",
                        stepNum++, current.getBuilding(), accumulatedDistance, current.getName()));
                accumulatedDistance = 0.0;
            }

            if (isElevator) {
                int startFloor = parseFloorNumber(previous.getFloor());
                int endFloor = startFloor;

                int j = i;
                while (j < path.size() && path.get(j).getType() == NodeType.ELEVATOR) {
                    try {
                        endFloor = parseFloorNumber(path.get(j).getFloor());
                    } catch (NumberFormatException e) {
                        break;
                    }
                    j++;
                }

                int floorsTraversed = Math.abs(endFloor - startFloor);
                if (floorsTraversed > 0) {
                    String direction = (endFloor > startFloor) ? "up" : "down";
                    String ordinal = toOrdinal(String.valueOf(endFloor));

                    if (accumulatedDistance > 0) {
                        steps.add(String.format("Step %d: Walk %.0f feet to the elevator", stepNum++, accumulatedDistance));
                    }

                    steps.add(String.format("Step %d: Take the elevator %s %d floor%s to the %s floor",
                            stepNum++, direction, floorsTraversed, (floorsTraversed == 1 ? "" : "s"), ordinal));

                    accumulatedDistance = 0.0;
                    i = j - 1;
                    previous = path.get(i);
                    continue;
                }
            }

            if (isHallway) {
                steps.add(String.format("Step %d: Walk %.0f feet to %s", stepNum++, accumulatedDistance, current.getName()));
                accumulatedDistance = 0.0;
            }

            if (isLast && current.getType() == NodeType.CLASSROOM) {
                steps.add(String.format("Step %d: Walk %.0f feet to %s", stepNum++, accumulatedDistance, current.getName()));
            }

            accumulatedDistance = (isHallway || (isLast && current.getType() == NodeType.CLASSROOM)) ? 0.0 : accumulatedDistance;
            previous = current;
        }

        steps.add("----------------------------------------------------------------------------");
        steps.add(String.format("Total distance: %.0f feet", totalDistance));

        new DirectionsView(steps, this::handleDirectionsAction, selectionView);
    }

    private void handleDirectionsAction(String action, DirectionsView view) {
        view.dispose();
        switch (action) {
            case "back" -> {
                if (selectionView != null) {
                    selectionView.setVisible(true);
                    selectionView.setToEndSelection(); // ⬅️ Custom method to flip view to END mode
                }
            }
            case "start_over" -> {
                if (selectionView != null) {
                    selectionView.setVisible(true);
                    selectionView.resetSelection(); // ⬅️ Another helper you’ll define
                }
            }
            case "exit" -> System.exit(0);
        }
    }

    private double getEdgeWeightBetween(Node from, Node to) {
        for (Edge edge : pathfinder.graph().getEdgesFrom(from)) {
            if (edge.getToNode().equals(to)) {
                return edge.getWeight();
            }
        }
        return 0;
    }

    private int parseFloorNumber(String floorString) {
        try {
            return Integer.parseInt(floorString.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return -1;
        }
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
            return floor;
        }
    }
}
