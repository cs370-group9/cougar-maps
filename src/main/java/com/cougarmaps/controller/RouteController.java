package com.cougarmaps.controller;

import com.cougarmaps.data.ElevatorCsvDAO;
import com.cougarmaps.engine.PathfindingEngine;
import com.cougarmaps.model.GraphModel;
import com.cougarmaps.model.graph.Node;
import com.cougarmaps.routing.RouteBuilder;
import com.cougarmaps.routing.RouteStep;
import com.cougarmaps.service.ElevatorStatusChecker;

import java.util.List;

/**
 * Responsible for managing route calculation logic between nodes.
 * It automatically checks for out-of-service elevators and updates
 * the graph accordingly before calculating the shortest path.
 */
public class RouteController {

    private final ElevatorStatusChecker elevatorStatusChecker;

    /**
     * Constructs a RouteController with access to elevator status data.
     *
     * @param elevatorDao     DAO used to load elevator name-to-ID mappings.
     * @param elevatorStatusUrl URL of the elevator status page to check.
     */
    public RouteController(ElevatorCsvDAO elevatorDao, String elevatorStatusUrl) {
        this.elevatorStatusChecker = new ElevatorStatusChecker(elevatorDao, elevatorStatusUrl);
    }

    /**
     * Computes the route between the given start and end nodes,
     * applying elevator status updates before routing.
     *
     * @param model Graph model containing all nodes and edges.
     * @param start Starting node.
     * @param end   Destination node.
     * @return A list of RouteSteps representing the computed path.
     */
    public List<RouteStep> getRoute(GraphModel model, Node start, Node end) {

        // Update elevator statuses before computing the route
        elevatorStatusChecker.markOutOfServiceElevators(model.getGraph());

        PathfindingEngine engine = new PathfindingEngine();
        List<Node> path = engine.findShortestPath(model.getGraph(), start, end);

        return RouteBuilder.fromNodeList(model.getGraph(), path);
    }
}
