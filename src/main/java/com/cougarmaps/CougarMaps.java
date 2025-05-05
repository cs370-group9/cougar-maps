package com.cougarmaps;

import com.cougarmaps.controller.LocationsController;
import com.cougarmaps.data.GraphDAO;
import com.cougarmaps.model.GraphModel;
import com.cougarmaps.view.LocationsView;

import javax.swing.*;

public class CougarMaps {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize model
                GraphModel model = GraphDAO.loadGraphFromCSV();
                //model.addNode(new Node("600", "San Marcos", "Fake Building", "0", "Room 101", NodeType.CLASSROOM, NodeStatus.ACTIVE));


                // Initialize view (the frame is already constructed inside LocationsView)
                LocationsView view = new LocationsView();

                // Initialize controller
                LocationsController controller = new LocationsController(model, view);

                // Show UI
                view.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to load graph data: " + e.getMessage());
            }
        });
    }
}
