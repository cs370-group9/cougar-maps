package com.cougarmaps;

import com.cougarmaps.controller.GraphController;
import com.cougarmaps.controller.RouteController;
import com.cougarmaps.view.PointSelectionMode;
import com.cougarmaps.view.PointSelectionView;

public class CougarMaps {
    private static PointSelectionView selectionView;

    public static void main(String[] args) {
        System.out.println("🟡 Starting CougarMaps...");

        GraphController graphController = new GraphController();
        RouteController routeController = new RouteController(graphController);

        selectionView = new PointSelectionView(
                graphController,
                PointSelectionMode.START,
                null,
                null,
                result -> routeController.handleRoute(result[0], result[1], selectionView)
        );

        selectionView.setVisible(true);
    }
}