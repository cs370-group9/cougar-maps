package com.cougarmaps.view;

import com.cougarmaps.model.graph.Graph;
import com.cougarmaps.model.graph.Node;
import com.cougarmaps.model.NodeType;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LocationSelectorView extends JFrame {
    private final Graph graph;
    private final JLabel titleLabel = new JLabel("Select Starting Location:");
    private final JComboBox<String> buildingDropdown = new JComboBox<>();
    private final JComboBox<String> floorDropdown = new JComboBox<>();
    private final JComboBox<String> roomDropdown = new JComboBox<>();
    private final JRadioButton classroomRadio = new JRadioButton("Find Classroom");
    private final JRadioButton bathroomRadio = new JRadioButton("Find Bathroom");
    private final JPanel radioPanel = new JPanel();
    private final JButton actionButton = new JButton("Next");
    private final JButton backButton = new JButton("Back");

    private boolean selectingStart = true;
    private Node startNode;
    private Consumer<Node[]> onComplete;

    private List<Node> currentFilteredNodes;

    public LocationSelectorView(Graph graph, Consumer<Node[]> onComplete) {
        this.graph = graph;
        this.onComplete = onComplete;

        setTitle("CougarMaps");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(classroomRadio);
        radioGroup.add(bathroomRadio);
        classroomRadio.setSelected(true);

        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        radioPanel.add(classroomRadio);
        radioPanel.add(bathroomRadio);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);

        // ---- Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(titleLabel, gbc);

        // ---- Building label
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Building:"), gbc);

        // ---- Floor label
        gbc.gridx = 1;
        panel.add(new JLabel("Floor:"), gbc);

        // ---- Building dropdown
        buildingDropdown.setPrototypeDisplayValue("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buildingDropdown, gbc);

        // ---- Floor dropdown
        floorDropdown.setPrototypeDisplayValue("00");
        gbc.gridx = 1;
        panel.add(floorDropdown, gbc);

        // ---- Room label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Room:"), gbc);

        // ---- Room dropdown
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(roomDropdown, gbc);

        // ---- Radio buttons
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(radioPanel, gbc);

        // ---- Continue / Next button
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(actionButton, gbc);

        // ---- Back button
        gbc.gridy = 7;
        panel.add(backButton, gbc);


        add(panel);
        pack();

        actionButton.setEnabled(false);
        backButton.setVisible(false);

        initializeStartSelection();

        // ---- Dropdown Listeners
        buildingDropdown.addActionListener(e -> {
            updateFloors();
            updateRooms();
            updateButtonState();
        });

        floorDropdown.addActionListener(e -> {
            updateRooms();
            updateButtonState();
        });

        roomDropdown.addActionListener(e -> updateButtonState());

        // ---- Action Button Logic
        actionButton.addActionListener(e -> {
            String building = (String) buildingDropdown.getSelectedItem();
            String floor = (String) floorDropdown.getSelectedItem();
            String room = (String) roomDropdown.getSelectedItem();

            Node selectedNode = currentFilteredNodes.stream()
                    .filter(n -> n.getBuilding().equals(building))
                    .filter(n -> n.getFloor().equals(floor))
                    .filter(n -> n.getName().equals(room))
                    .findFirst()
                    .orElse(null);

            if (selectedNode == null) return;

            if (selectingStart) {
                if (classroomRadio.isSelected()) {
                    startNode = selectedNode;
                    beginDestinationSelection();
                } else {
                    System.out.println("Bathroom logic not yet implemented.");
                }
            } else {
                Node destinationNode = selectedNode;
                onComplete.accept(new Node[]{startNode, destinationNode});
                dispose();
            }
        });

        // ---- Back Button Logic
        backButton.addActionListener(e -> {
            startNode = null;
            initializeStartSelection();
        });

        setVisible(true);
    }

    private void initializeStartSelection() {
        selectingStart = true;
        titleLabel.setText("Select Starting Location:");
        actionButton.setText("Next");
        radioPanel.setVisible(true);
        backButton.setVisible(false);

        currentFilteredNodes = graph.getAllNodes().stream()
                .filter(n -> n.getType() == NodeType.CLASSROOM)
                .collect(Collectors.toList());

        refreshDropdowns();
    }

    private void beginDestinationSelection() {
        selectingStart = false;
        titleLabel.setText("Select Destination:");
        actionButton.setText("Continue");
        radioPanel.setVisible(false);
        backButton.setVisible(true);

        currentFilteredNodes = graph.getAllNodes().stream()
                .filter(n -> n.getType() == NodeType.CLASSROOM)
                .filter(n -> n.getId() != startNode.getId())
                .collect(Collectors.toList());

        refreshDropdowns();
    }

    private void refreshDropdowns() {
        buildingDropdown.removeAllItems();
        floorDropdown.removeAllItems();
        roomDropdown.removeAllItems();

        floorDropdown.setEnabled(false);
        roomDropdown.setEnabled(false);
        actionButton.setEnabled(false);

        buildingDropdown.addItem("");
        Set<String> buildings = currentFilteredNodes.stream()
                .map(Node::getBuilding)
                .collect(Collectors.toCollection(TreeSet::new));
        for (String b : buildings) {
            buildingDropdown.addItem(b);
        }
        buildingDropdown.setSelectedIndex(0);
    }

    private void updateFloors() {
        floorDropdown.removeAllItems();
        roomDropdown.removeAllItems();
        roomDropdown.setEnabled(false);

        String building = (String) buildingDropdown.getSelectedItem();
        if (building == null || building.isBlank()) {
            floorDropdown.setEnabled(false);
            return;
        }

        floorDropdown.setEnabled(true);
        floorDropdown.addItem("");
        Set<String> floors = currentFilteredNodes.stream()
                .filter(n -> n.getBuilding().equals(building))
                .map(Node::getFloor)
                .collect(Collectors.toCollection(TreeSet::new));
        for (String f : floors) {
            floorDropdown.addItem(f);
        }
        floorDropdown.setSelectedIndex(0);
    }

    private void updateRooms() {
        roomDropdown.removeAllItems();

        String building = (String) buildingDropdown.getSelectedItem();
        String floor = (String) floorDropdown.getSelectedItem();

        if (building == null || building.isBlank() || floor == null || floor.isBlank()) {
            roomDropdown.setEnabled(false);
            return;
        }

        roomDropdown.setEnabled(true);
        roomDropdown.addItem("");
        currentFilteredNodes.stream()
                .filter(n -> n.getBuilding().equals(building))
                .filter(n -> n.getFloor().equals(floor))
                .map(Node::getName)
                .forEach(roomDropdown::addItem);

        roomDropdown.setSelectedIndex(0);
    }

    private void updateButtonState() {
        boolean enabled = buildingDropdown.getSelectedIndex() > 0
                && floorDropdown.getSelectedIndex() > 0
                && roomDropdown.getSelectedIndex() > 0;
        actionButton.setEnabled(enabled);
    }
}
