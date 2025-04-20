package com.cougarmaps.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DirectionsView extends JFrame {

    public DirectionsView(List<String> stepList) {
        setTitle("Route Directions");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String step : stepList) {
            model.addElement(step);
        }

        JList<String> directionJList = new JList<>(model);
        directionJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        directionJList.setVisibleRowCount(-1);
        directionJList.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(directionJList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Check if any windows are still visible
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
