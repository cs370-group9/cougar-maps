package com.cougarmaps.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;

public class DirectionsView extends BaseView {

    public DirectionsView(List<String> stepList, BiConsumer<String, DirectionsView> actionCallback, PointSelectionView previousView) {
        super("Route Directions", 500, 400);

        setTitle("Route Directions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the model and JList
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String step : stepList) {
            model.addElement(step);
        }

        JList<String> directionJList = new JList<>(model);
        directionJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        directionJList.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // VERY IMPORTANT: Set preferred size
        directionJList.setVisibleRowCount(10);
        JScrollPane scrollPane = new JScrollPane(directionJList);
        scrollPane.setPreferredSize(new Dimension(450, 250)); // <-- FIX

        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");
        JButton startOverButton = new JButton("Start Over");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(backButton);
        buttonPanel.add(startOverButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        backButton.addActionListener(e -> {
            previousView.setVisible(true); // 💡 Reactivate selection screen
            actionCallback.accept("back", this);
        });
        startOverButton.addActionListener(e -> actionCallback.accept("start_over", this));
        exitButton.addActionListener(e -> actionCallback.accept("exit", this));

        // Now finalize layout
        pack();
        setVisible(true);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                boolean anyVisible = false;
                for (Window w : Window.getWindows()) {
                    if (w.isVisible()) {
                        anyVisible = true;
                        break;
                    }
                }
                if (!anyVisible) {
                    System.exit(0);
                }
            }
        });
    }
}
