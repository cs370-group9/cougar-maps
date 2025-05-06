package com.cougarmaps.controller;

import com.cougarmaps.controller.RouteController;
import com.cougarmaps.data.ElevatorCsvDAO;
import com.cougarmaps.model.GraphModel;
import com.cougarmaps.model.graph.Edge;
import com.cougarmaps.model.graph.Node;
import com.cougarmaps.model.graph.enums.NodeStatus;
import com.cougarmaps.model.graph.enums.NodeType;
import com.cougarmaps.routing.RouteStep;
import com.cougarmaps.engine.PathfindingEngine;
import com.cougarmaps.view.DirectionsView;
import com.cougarmaps.view.LocationsView;
import com.cougarmaps.Constants;
import com.cougarmaps.view.RestroomView;

import javax.swing.*;
import java.util.*;

public class LocationsController {
    private final GraphModel model;
    private final LocationsView view;
    private Map<String, Map<String, List<String>>> buildingFloorRoomMap;
    private Node startNode;
    private Node endNode;
    private boolean startMode;

    public LocationsController(GraphModel model, LocationsView view) {
        this.model = model;
        this.view = view;
        this.startNode = null;
        this.endNode = null;
        this.startMode = true;

        buildingFloorRoomMap = model.getBuildingFloorRoomMap();

        List<String> buildingNames = new ArrayList<>(buildingFloorRoomMap.keySet());
        Collections.sort(buildingNames);
        view.getBuildingDropDown().removeAllItems();
        for (String name : buildingNames) {
            view.getBuildingDropDown().addItem(name);
        }
        view.getBuildingDropDown().setSelectedIndex(-1);

        setupListeners();
    }

    private void setupListeners() {
        JComboBox<String> buildingDropDown = view.getBuildingDropDown();
        JComboBox<String> floorDropDown = view.getFloorDropDown();
        JComboBox<String> roomDropDown = view.getRoomDropDown();
        JButton nextButton = view.getNextButton();
        JRadioButton findClassroomRadioButton = view.getFindClassroomRadioButton();
        JRadioButton findRestroomRadioButton = view.getFindRestroomRadioButton();

        nextButton.setEnabled(false);

        Runnable validateForm = () -> {
            boolean allDropDownsSelected = buildingDropDown.getSelectedIndex() > -1 &&
                    floorDropDown.getSelectedIndex() > -1 &&
                    roomDropDown.getSelectedIndex() > -1;
            boolean radioSelected = findClassroomRadioButton.isSelected() || findRestroomRadioButton.isSelected();
            nextButton.setEnabled(allDropDownsSelected && radioSelected);
        };

        buildingDropDown.addActionListener(e -> {
            String selectedBuilding = (String) buildingDropDown.getSelectedItem();
            floorDropDown.removeAllItems();
            roomDropDown.removeAllItems();

            if (selectedBuilding != null && buildingFloorRoomMap.containsKey(selectedBuilding)) {
                List<String> floors = new ArrayList<>(buildingFloorRoomMap.get(selectedBuilding).keySet());
                Collections.sort(floors);
                for (String floor : floors) {
                    floorDropDown.addItem(floor);
                }
                floorDropDown.setSelectedIndex(-1);
            }
        });

        floorDropDown.addActionListener(e -> {
            String selectedBuilding = (String) buildingDropDown.getSelectedItem();
            String selectedFloor = (String) floorDropDown.getSelectedItem();
            roomDropDown.removeAllItems();

            if (selectedBuilding != null && selectedFloor != null &&
                    buildingFloorRoomMap.containsKey(selectedBuilding) &&
                    buildingFloorRoomMap.get(selectedBuilding).containsKey(selectedFloor)) {

                List<String> rooms = new ArrayList<>(buildingFloorRoomMap.get(selectedBuilding).get(selectedFloor));
                Collections.sort(rooms);
                for (String room : rooms) {
                    if ((!startMode && startNode != null && startNode.getBuilding().equals(selectedBuilding) &&
                            startNode.getFloor().equals(selectedFloor) && startNode.getRoom().equals(room)) ||
                            (startMode && endNode != null && endNode.getBuilding().equals(selectedBuilding) &&
                                    endNode.getFloor().equals(selectedFloor) && endNode.getRoom().equals(room))) {
                        continue;
                    }
                    roomDropDown.addItem(room);
                }
                roomDropDown.setSelectedIndex(-1);
            }
        });

        view.getBackButton().addActionListener(e -> {
            if (startMode) {
                int response = JOptionPane.showConfirmDialog(view, "Are you sure you want to quit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) System.exit(0);
            } else {
                view.getBackButton().setText("Exit");
                buildingDropDown.setSelectedItem(startNode.getBuilding());
                floorDropDown.removeAllItems();
                if (buildingFloorRoomMap.containsKey(startNode.getBuilding())) {
                    List<String> floors = new ArrayList<>(buildingFloorRoomMap.get(startNode.getBuilding()).keySet());
                    Collections.sort(floors);
                    for (String floor : floors) floorDropDown.addItem(floor);
                    floorDropDown.setSelectedItem(startNode.getFloor());
                }
                roomDropDown.removeAllItems();
                List<String> rooms = new ArrayList<>(buildingFloorRoomMap.get(startNode.getBuilding()).get(startNode.getFloor()));
                Collections.sort(rooms);
                for (String room : rooms) {
                    if (endNode != null && endNode.getBuilding().equals(buildingDropDown.getSelectedItem()) &&
                            endNode.getFloor().equals(floorDropDown.getSelectedItem()) && endNode.getRoom().equals(room)) continue;
                    roomDropDown.addItem(room);
                }
                roomDropDown.setSelectedItem(startNode.getRoom());
                startMode = true;
            }
        });

        view.getClearButton().addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(view, "Clear all choices and start over?", "Start Over", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                startMode = true;
                startNode = null;
                endNode = null;
                view.getBackButton().setText("Exit");
                view.getStartNodeLabel().setText("Not Selected");
                view.getEndNodeLabel().setText("Not Selected");
                buildingDropDown.setSelectedIndex(-1);
                floorDropDown.removeAllItems();
                roomDropDown.removeAllItems();
                findClassroomRadioButton.setEnabled(true);
                findRestroomRadioButton.setEnabled(true);
                findClassroomRadioButton.setSelected(false);
                findRestroomRadioButton.setSelected(false);
                nextButton.setEnabled(false);
            }
        });

        nextButton.addActionListener(e -> {
            String building = (String) buildingDropDown.getSelectedItem();
            String floor = (String) floorDropDown.getSelectedItem();
            String room = (String) roomDropDown.getSelectedItem();
            Node selectedNode = model.getNode(building, floor, room);
            if (selectedNode == null) {
                JOptionPane.showMessageDialog(view, "Invalid selection or node is not active.");
                return;
            }

            if (findClassroomRadioButton.isSelected()) {
                if (startMode) {
                    startNode = selectedNode;
                    startMode = false;
                    view.getBackButton().setText("Back");
                    view.getStartNodeLabel().setText(startNode.shortLabel());
                    floorDropDown.removeAllItems();
                    roomDropDown.removeAllItems();
                    buildingDropDown.setSelectedIndex(-1);
                    findClassroomRadioButton.setEnabled(false);
                    findRestroomRadioButton.setEnabled(false);
                    nextButton.setEnabled(false);
                } else {
                    endNode = selectedNode;
                    view.getEndNodeLabel().setText(endNode.shortLabel());

                    int response = JOptionPane.showConfirmDialog(
                            view,
                            "Find shortest route between these classrooms?\n" +
                                    "   Start: " + startNode.shortLabel() + "\n" +
                                    "   End:   " + endNode.shortLabel(),
                            "Plan Route to Classroom?",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (response == JOptionPane.YES_OPTION) {
                        view.setVisible(false);
                        RouteController rc = new RouteController(
                                new ElevatorCsvDAO(Constants.ELEVATOR_CSV_PATH),
                                Constants.ELEVATOR_STATUS_URL
                        );
                        List<RouteStep> steps = rc.getRoute(model, startNode, endNode);
                        double dist = steps.stream().mapToDouble(RouteStep::getDistance).sum();
                        SwingUtilities.invokeLater(() -> new DirectionsView(startNode.shortLabel(), endNode.shortLabel(), steps, dist).setVisible(true));
                    }
                }
            }

            if (findRestroomRadioButton.isSelected()) {
                startNode = selectedNode;
                view.getStartNodeLabel().setText(startNode.shortLabel());
                view.getEndNodeLabel().setText("N/A");

                PathfindingEngine engine = new PathfindingEngine();
                List<Node> restrooms = new ArrayList<>();
                for (Node n : model.getAllNodes()) {
                    if (n.getType() == NodeType.RESTROOM && n.getStatus() == NodeStatus.ACTIVE) {
                        restrooms.add(n);
                    }
                }

                List<RestroomView.RestroomPath> restroomPaths = new ArrayList<>();
                for (Node restroom : restrooms) {
                    List<Node> path = engine.findShortestPath(model.getGraph(), startNode, restroom);
                    if (!path.isEmpty()) {
                        double distance = 0;
                        for (int i = 0; i < path.size() - 1; i++) {
                            for (Edge edge : model.getEdgesFrom(path.get(i))) {
                                if (edge.getTo().equals(path.get(i + 1))) {
                                    distance += edge.getWeight();
                                    break;
                                }
                            }
                        }
                        restroomPaths.add(new RestroomView.RestroomPath(restroom, path, distance));
                    }
                }

                restroomPaths.sort(Comparator.comparingDouble(rp -> rp.totalDistance));
                if (restroomPaths.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "No accessible restrooms found.");
                    return;
                }

                view.setVisible(false);
                SwingUtilities.invokeLater(() -> new RestroomView(startNode, restroomPaths, model).setVisible(true));
            }

        });

        buildingDropDown.addActionListener(e -> validateForm.run());
        floorDropDown.addActionListener(e -> validateForm.run());
        roomDropDown.addActionListener(e -> validateForm.run());
        findClassroomRadioButton.addActionListener(e -> validateForm.run());
        findRestroomRadioButton.addActionListener(e -> validateForm.run());
    }

    private static class RestroomDistance {
        Node node;
        double distance;
        RestroomDistance(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }
}
