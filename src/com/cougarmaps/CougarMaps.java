package com.cougarmaps;

import com.cougarmaps.controller.RouteController;
import com.cougarmaps.model.graph.Graph;
import com.cougarmaps.model.graph.GraphLoader;
import com.cougarmaps.view.LocationSelectorView;

public class CougarMaps {
    public static void main(String[] args) {
        System.out.println("🟡 Starting CougarMaps...");

        // Load graph data from CSV
        Graph graph = GraphLoader.loadFromCSV("resources/nodes.csv", "resources/edges.csv");

        // Initialize the controller with the loaded graph
        RouteController controller = new RouteController(graph);

        // Launch the main view and delegate route logic to the controller
        new LocationSelectorView(graph, result -> controller.handleRoute(result[0], result[1]));

    }
}
