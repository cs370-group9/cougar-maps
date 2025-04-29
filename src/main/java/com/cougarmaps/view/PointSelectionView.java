package com.cougarmaps.view;

import com.cougarmaps.controller.GraphController;
import com.cougarmaps.model.graph.Node;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class PointSelectionView extends BaseView {

    private final GraphController controller;
    private PointSelectionMode mode;
    private final Consumer<Node[]> onComplete;
    private Node startNode;
    private Node endNode;

    private JComboBox<String> buildingDropdown;
    private JComboBox<String> floorDropdown;
    private JComboBox<String> roomDropdown;
    private JRadioButton classroomRadio;
    private JRadioButton bathroomRadio;
    private JButton backButton;
    private JButton clearButton;
    private JButton nextButton;

    public PointSelectionView(GraphController controller, PointSelectionMode mode, Node startNode, Node endNode, Consumer<Node[]> onComplete) {
        super(mode == PointSelectionMode.START ? "Select Starting Point" : "Select Destination", 300, 400);
        this.controller = controller;
        this.mode = mode;
        this.startNode = startNode;
        this.endNode = endNode;
        this.onComplete = onComplete;

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        buildingDropdown = new JComboBox<>();
        floorDropdown = new JComboBox<>();
        roomDropdown = new JComboBox<>();

        panel.add(new JLabel("Building:"), gbc);
        gbc.gridy++;
        panel.add(buildingDropdown, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Floor:"), gbc);
        gbc.gridy++;
        panel.add(floorDropdown, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Room:"), gbc);
        gbc.gridy++;
        panel.add(roomDropdown, gbc);

        // Radio Buttons
        classroomRadio = new JRadioButton("Find Classroom");
        bathroomRadio = new JRadioButton("Find Bathroom");
        ButtonGroup group = new ButtonGroup();
        group.add(classroomRadio);
        group.add(bathroomRadio);

        classroomRadio.setSelected(true);

        gbc.gridy++;
        JPanel radioPanel = new JPanel(new GridLayout(2, 1));
        radioPanel.setBorder(BorderFactory.createTitledBorder("Search Type"));
        radioPanel.add(classroomRadio);
        radioPanel.add(bathroomRadio);

        panel.add(radioPanel, gbc);

        // Buttons
        backButton = new JButton("Back");
        clearButton = new JButton("Clear");
        nextButton = new JButton("Next");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(nextButton);

        gbc.gridy++;
        panel.add(buttonPanel, gbc);

        add(panel);

        // Populate dropdowns
        populateBuildingDropdown();

        setupListeners();
        updateNextButtonState();

        // Enable/disable controls based on mode
        backButton.setEnabled(mode != PointSelectionMode.START);
        clearButton.setEnabled(true);
        nextButton.setEnabled(false);

        setSearchTypeEnabled(mode == PointSelectionMode.START);
    }

    private void setupListeners() {
        buildingDropdown.addActionListener(e -> {
            populateFloorDropdown();
            updateNextButtonState();
        });

        floorDropdown.addActionListener(e -> {
            populateRoomDropdown();
            updateNextButtonState();
        });

        roomDropdown.addActionListener(e -> updateNextButtonState());

        backButton.addActionListener(e -> handleBackButton());
        clearButton.addActionListener(e -> handleClearButton());
        nextButton.addActionListener(e -> handleNextButton());
    }

    private void populateBuildingDropdown() {
        buildingDropdown.removeAllItems();
        buildingDropdown.addItem("");
        controller.getBuildings().forEach(buildingDropdown::addItem);

        if (endNode != null) {
            buildingDropdown.setSelectedItem(endNode.getBuilding());
            populateFloorDropdown();
            populateRoomDropdown();
        } else {
            buildingDropdown.setSelectedIndex(0);
            floorDropdown.removeAllItems();
            floorDropdown.addItem("");
            roomDropdown.removeAllItems();
            roomDropdown.addItem("");
        }
    }

    private void populateFloorDropdown() {
        floorDropdown.removeAllItems();
        floorDropdown.addItem("");

        String building = (String) buildingDropdown.getSelectedItem();
        if (building != null && !building.isEmpty()) {
            controller.getFloors(building).forEach(floorDropdown::addItem);

            if (endNode != null && building.equals(endNode.getBuilding())) {
                floorDropdown.setSelectedItem(endNode.getFloor());
            }
        }
    }

    private void populateRoomDropdown() {
        roomDropdown.removeAllItems();
        roomDropdown.addItem("");

        String building = (String) buildingDropdown.getSelectedItem();
        String floor = (String) floorDropdown.getSelectedItem();

        if (building != null && !building.isEmpty() && floor != null && !floor.isEmpty()) {
            controller.getRooms(building, floor).forEach(room -> {
                if (mode == PointSelectionMode.START && endNode != null && room.equals(endNode.getName())) {
                    return; // Skip endNode's name
                }
                if (mode == PointSelectionMode.END && startNode != null && room.equals(startNode.getName())) {
                    return; // Skip startNode's name
                }
                roomDropdown.addItem(room);
            });

            if (endNode != null &&
                    building.equals(endNode.getBuilding()) &&
                    floor.equals(endNode.getFloor())) {
                roomDropdown.setSelectedItem(endNode.getName());
            }
        }
    }

    private void handleBackButton() {
        if (mode == PointSelectionMode.END) {
            if (buildingDropdown.getSelectedItem() != null &&
                    floorDropdown.getSelectedItem() != null &&
                    roomDropdown.getSelectedItem() != null) {
                endNode = getSelectedNode();
            }

            mode = PointSelectionMode.START;

            setTitle("Select Start");
            populateFloorDropdown();
            populateRoomDropdown();
            buildingDropdown.setSelectedItem(startNode.getBuilding());
            floorDropdown.setSelectedItem(startNode.getFloor());
            roomDropdown.setSelectedItem(startNode.getName());
            backButton.setEnabled(false);
            clearButton.setEnabled(true);
            nextButton.setEnabled(true);
            setSearchTypeEnabled(true);
        }
    }

    private void handleClearButton() {
        buildingDropdown.setSelectedIndex(0);
        floorDropdown.removeAllItems();
        floorDropdown.addItem("");
        roomDropdown.removeAllItems();
        roomDropdown.addItem("");
        nextButton.setEnabled(false);
    }

    private void handleNextButton() {
        if (mode == PointSelectionMode.START) {
            mode = PointSelectionMode.END;
            startNode = getSelectedNode();
            setTitle("Select Destination");
            if (endNode == null) {
                buildingDropdown.setSelectedIndex(0);
                floorDropdown.removeAllItems();
                floorDropdown.addItem("");
                roomDropdown.removeAllItems();
                roomDropdown.addItem("");
            } else {
                buildingDropdown.setSelectedItem(endNode.getBuilding());
                floorDropdown.setSelectedItem(endNode.getFloor());
                roomDropdown.setSelectedItem(endNode.getName());
                roomDropdown.removeItem(startNode.getName());

            }
            backButton.setEnabled(true);
            clearButton.setEnabled(true);
            nextButton.setEnabled(false);
            setSearchTypeEnabled(false);
        } else if (mode == PointSelectionMode.END) {
            endNode = getSelectedNode();
            if (startNode != null && endNode != null) {
                this.setVisible(false);
                onComplete.accept(new Node[]{ startNode, endNode });
            }
        }
    }

    private void updateNextButtonState() {
        String building = (String) buildingDropdown.getSelectedItem();
        String floor = (String) floorDropdown.getSelectedItem();
        String room = (String) roomDropdown.getSelectedItem();

        boolean allSelected = building != null && !building.isEmpty()
                && floor != null && !floor.isEmpty()
                && room != null && !room.isEmpty();

        nextButton.setEnabled(allSelected);
    }

    private Node getSelectedNode() {
        String building = (String) buildingDropdown.getSelectedItem();
        String floor = (String) floorDropdown.getSelectedItem();
        String room = (String) roomDropdown.getSelectedItem();

        if (building != null && floor != null && room != null) {
            return controller.getNode(building, floor, room);
        }
        return null;
    }

    private void setSearchTypeEnabled(boolean enabled) {
        classroomRadio.setEnabled(enabled);
        bathroomRadio.setEnabled(enabled);
    }

    public void setToEndSelection() {
        this.mode = PointSelectionMode.END;
        setTitle("Select Destination");
        populateBuildingDropdown();
        populateFloorDropdown();
        populateRoomDropdown();
        backButton.setEnabled(true);
        nextButton.setEnabled(false);
        setSearchTypeEnabled(false);
    }

    public void resetSelection() {
        this.mode = PointSelectionMode.START;
        this.startNode = null;
        this.endNode = null;
        setTitle("Select Starting Point");
        populateBuildingDropdown();
        floorDropdown.removeAllItems();
        floorDropdown.addItem("");
        roomDropdown.removeAllItems();
        roomDropdown.addItem("");
        backButton.setEnabled(false);
        nextButton.setEnabled(false);
        setSearchTypeEnabled(true);
    }

}
