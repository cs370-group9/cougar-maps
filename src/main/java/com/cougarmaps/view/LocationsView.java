package com.cougarmaps.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LocationsView extends JFrame {

    private JLabel startNodeLabel;
    private JLabel endNodeLabel;
    private JComboBox<String> buildingDropDown;
    private JComboBox<String> floorDropDown;
    private JComboBox<String> roomDropDown;
    private ButtonGroup radioGroup;
    private JRadioButton findClassroomRadioButton;
    private JRadioButton findRestroomRadioButton;
    private JButton backButton;
    private JButton clearButton;
    private JButton nextButton;

    public LocationsView() {
        setTitle("CougarMaps");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(362, 545);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null); // Use absolute positioning

        // Logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/com/cougarmaps/view/logo.PNG"));
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(((362 - 270) / 2)-5, 10, 270, 161); // centered at top
        mainPanel.add(logoLabel);

        // Start label and node
        JLabel startLabel = new JLabel("Start:");
        startLabel.setBounds(logoLabel.getX(), logoLabel.getY() + logoLabel.getHeight() + 10, 50, 20);
        mainPanel.add(startLabel);

        startNodeLabel = new JLabel("Not Selected");
        startNodeLabel.setFont(startNodeLabel.getFont().deriveFont(Font.PLAIN));
        startNodeLabel.setBounds(startLabel.getX() + 40, startLabel.getY(), 250, 20);
        mainPanel.add(startNodeLabel);

        // End label and node
        JLabel endLabel = new JLabel("End:");
        endLabel.setBounds(startLabel.getX(), startLabel.getY() + 25, 200, 20);
        mainPanel.add(endLabel);

        endNodeLabel = new JLabel("Not Selected");
        endNodeLabel.setFont(endNodeLabel.getFont().deriveFont(Font.PLAIN));
        endNodeLabel.setBounds(endLabel.getX() + 40, endLabel.getY(), 250, 20);
        mainPanel.add(endNodeLabel);

        // Building and Floor labels side by side
        JLabel buildingLabel = new JLabel("Building:");
        buildingLabel.setBounds(endLabel.getX(), endLabel.getY() + 30, 100, 20);
        mainPanel.add(buildingLabel);

        JLabel floorLabel = new JLabel("Floor:");
        floorLabel.setBounds(buildingLabel.getX() + 190, buildingLabel.getY(), 100, 20);
        mainPanel.add(floorLabel);

        // Building and Floor dropdowns
        buildingDropDown = new JComboBox<>();
        buildingDropDown.setBounds(buildingLabel.getX(), buildingLabel.getY() + 25, 160, 25);
        mainPanel.add(buildingDropDown);

        floorDropDown = new JComboBox<>();
        floorDropDown.setBounds(floorLabel.getX(), floorLabel.getY() + 25, 70, 25);
        mainPanel.add(floorDropDown);

        // Room label
        JLabel roomLabel = new JLabel("Room:");
        roomLabel.setBounds(buildingLabel.getX(), buildingLabel.getY() + 55, 100, 20);
        mainPanel.add(roomLabel);

        // Room dropdowns
        roomDropDown = new JComboBox<>();
        roomDropDown.setBounds(roomLabel.getX(), roomLabel.getY() + 25, 160, 25);
        mainPanel.add(roomDropDown);

        // Radio button panel with border
        JPanel radioPanel = new JPanel(null);
        radioPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        radioPanel.setBounds(buildingLabel.getX(), roomDropDown.getY() + 40, 200, 75);

        findClassroomRadioButton = new JRadioButton("Find Classroom");
        findClassroomRadioButton.setBounds(10, 20, 150, 20);
        radioPanel.add(findClassroomRadioButton);

        findRestroomRadioButton = new JRadioButton("Find Restroom");
        findRestroomRadioButton.setBounds(10, findClassroomRadioButton.getY() + 25, 150, 20);
        radioPanel.add(findRestroomRadioButton);

        radioGroup = new ButtonGroup();
        radioGroup.add(findClassroomRadioButton);
        radioGroup.add(findRestroomRadioButton);

        mainPanel.add(radioPanel);

        // Back button
        backButton = new JButton("Exit");
        backButton.setBounds(roomLabel.getX(), radioPanel.getHeight() + radioPanel.getY() + 15, 80, 30);
        backButton.addActionListener(e -> {
        });

        // Clear button
        clearButton = new JButton("Clear");
        clearButton.setBounds(backButton.getX() + 90, backButton.getY(), 80, 30);
        clearButton.addActionListener(e -> {
        });

        // Next button
        nextButton = new JButton("Next");
        nextButton.setBounds(clearButton.getX() + 90, backButton.getY(), 80, 30);
        nextButton.addActionListener(e -> {
        });

        mainPanel.add(backButton);
        mainPanel.add(clearButton);
        mainPanel.add(nextButton);

        setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        EventQueue.invokeLater(() -> new LocationsView().setVisible(true));
    }

    public JLabel getStartNodeLabel() {
        return startNodeLabel;
    }

    public JLabel getEndNodeLabel() {
        return endNodeLabel;
    }

    public void setBuildingOptions(java.util.List<String> buildingNames) {
        buildingDropDown.removeAllItems();
        for (String name : buildingNames) {
            buildingDropDown.addItem(name);
        }
    }

    public JComboBox<String> getBuildingDropDown() {
        return buildingDropDown;
    }

    public JComboBox<String> getFloorDropDown() {
        return floorDropDown;
    }

    public JComboBox<String> getRoomDropDown() {
        return roomDropDown;
    }
    public void setFloorDropDown(List<String> floors) {
        floorDropDown.removeAllItems();
        for (String floor : floors) {
            floorDropDown.addItem(floor);
        }
    }

    public void setRoomDropDown(List<String> rooms) {
        roomDropDown.removeAllItems();
        for (String room : rooms) {
            roomDropDown.addItem(room);
        }
    }

    public JRadioButton getFindClassroomRadioButton() {
        return findClassroomRadioButton;
    }

    public JRadioButton getFindRestroomRadioButton() {
        return findRestroomRadioButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public ButtonGroup getRadioGroup() {
        return radioGroup;
    }

}
