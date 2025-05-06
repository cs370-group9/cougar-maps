package com.cougarmaps.view;

import com.cougarmaps.Constants;
import com.cougarmaps.controller.RouteController;
import com.cougarmaps.data.ElevatorCsvDAO;
import com.cougarmaps.model.GraphModel;
import com.cougarmaps.model.graph.Node;
import com.cougarmaps.routing.RouteStep;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class RestroomView extends JFrame {
    private final Node startNode;
    private final List<RestroomPath> restroomPaths;
    private final GraphModel model;

    private final ButtonGroup buttonGroup;
    private final Map<JRadioButton, RestroomPath> buttonMap;

    private JButton backButton;
    private JButton startOverButton;
    private JButton exitButton;


    public RestroomView(Node startNode, List<RestroomPath> restroomPaths, GraphModel model) {
        this.startNode = startNode;
        this.restroomPaths = restroomPaths;
        this.model = model;
        this.buttonGroup = new ButtonGroup();
        this.buttonMap = new HashMap<>();

        setTitle("Nearby Restrooms");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel("Select a restroom near you:");
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        mainPanel.add(header, BorderLayout.NORTH);

        // List Panel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listPanel);

        for (RestroomPath rp : restroomPaths) {
            String label = rp.restroom.shortLabel() + " (" + Math.round(rp.totalDistance) + " ft)";
            JRadioButton radioButton = new JRadioButton(label);
            buttonGroup.add(radioButton);
            buttonMap.put(radioButton, rp);
            listPanel.add(radioButton);
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton showButton = new JButton("Show Directions");
        showButton.addActionListener(this::handleShowDirections);
        mainPanel.add(showButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleShowDirections(ActionEvent e) {
        for (Map.Entry<JRadioButton, RestroomPath> entry : buttonMap.entrySet()) {
            if (entry.getKey().isSelected()) {
                RestroomPath selected = entry.getValue();

                RouteController routeController = new RouteController(
                        new ElevatorCsvDAO(Constants.ELEVATOR_CSV_PATH),
                        Constants.ELEVATOR_STATUS_URL
                );
                List<RouteStep> steps = routeController.getRoute(model, startNode, selected.restroom);
                double totalDistance = steps.stream().mapToDouble(RouteStep::getDistance).sum();

                setVisible(false);
                SwingUtilities.invokeLater(() ->
                        new DirectionsView(
                                startNode.shortLabel(),
                                selected.restroom.shortLabel(),
                                steps,
                                totalDistance
                        ).setVisible(true)
                );
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Please select a restroom.");
    }

    public static class RestroomPath {
        public final Node restroom;
        public final List<Node> path;
        public final double totalDistance;

        public RestroomPath(Node restroom, List<Node> path, double totalDistance) {
            this.restroom = restroom;
            this.path = path;
            this.totalDistance = totalDistance;
        }
    }
}
