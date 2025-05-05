package com.cougarmaps.routing;

import com.cougarmaps.model.graph.Edge;
import com.cougarmaps.model.graph.Graph;
import com.cougarmaps.model.graph.Node;
import com.cougarmaps.model.graph.enums.NodeType;

import java.util.*;

/**
 * Builds a list of RouteSteps from a list of path nodes.
 * Supports consolidation of consecutive CLASSROOM and ELEVATOR nodes.
 */
public class RouteBuilder {

    /**
     * Converts a path of nodes into a list of RouteSteps.
     * Consolidates consecutive CLASSROOMs and ELEVATORs.
     *
     * @param graph The graph used to look up edge weights.
     * @param path  The ordered list of nodes from start to destination.
     * @return A list of RouteSteps with distances and units.
     */
    public static List<RouteStep> fromNodeList(Graph graph, List<Node> path) {
        List<RouteStep> steps = new ArrayList<>();
        int i = 0;

        while (i < path.size() - 1) {
            Node start = path.get(i);
            NodeType type = start.getType();

            // Consolidate consecutive CLASSROOM nodes
            if (type == NodeType.CLASSROOM) {
                int j = i + 1;
                double totalDistance = 0;
                Node end = start;

                while (j < path.size() && path.get(j).getType() == NodeType.CLASSROOM) {
                    totalDistance += getEdgeWeight(graph, path.get(j - 1), path.get(j));
                    end = path.get(j);
                    j++;
                }

                if (!start.equals(end)) {
                    steps.add(new RouteStep(start, end, totalDistance, "Feet"));
                }

                if (j < path.size()) {
                    Node next = path.get(j);
                    double distance = getEdgeWeight(graph, end, next);
                    steps.add(new RouteStep(end, next, distance, "Feet"));
                }

                i = j;

                // Consolidate consecutive ELEVATOR nodes
            } else if (type == NodeType.ELEVATOR) {
                int j = i + 1;
                Node end = start;

                while (j < path.size() && path.get(j).getType() == NodeType.ELEVATOR) {
                    end = path.get(j);
                    j++;
                }

                int floors = j - i - 1; // total elevator transitions = nodes - 1
                if (floors > 0 && !start.equals(end)) {
                    steps.add(new RouteStep(start, end, floors, "Floors"));
                }

                if (j < path.size()) {
                    Node next = path.get(j);
                    double distance = getEdgeWeight(graph, end, next);
                    steps.add(new RouteStep(end, next, distance, "Feet"));
                }

                i = j;

                // All other node transitions
            } else {
                Node next = path.get(i + 1);
                double distance = getEdgeWeight(graph, start, next);

                if (!start.equals(next)) {
                    steps.add(new RouteStep(start, next, distance, "Feet"));
                }

                i++;
            }
        }

        return steps;
    }

    /**
     * Finds the weight between two nodes.
     * Assumes the graph is bidirectional.
     */
    private static double getEdgeWeight(Graph graph, Node from, Node to) {
        for (Edge edge : graph.getEdges(from)) {
            if (edge.getTo().equals(to)) {
                return edge.getWeight();
            }
        }

        System.err.printf("⚠️ No edge found between %s and %s%n", from.getId(), to.getId());
        return Double.POSITIVE_INFINITY;
    }
}