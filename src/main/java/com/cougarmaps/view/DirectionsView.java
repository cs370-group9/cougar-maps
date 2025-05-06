package com.cougarmaps.view;

import com.cougarmaps.CougarMaps;
import com.cougarmaps.routing.RouteStep;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.List;

public class DirectionsView extends JFrame {
    public DirectionsView(String fromLabel, String toLabel, List<RouteStep> steps, double totalDistance) {
        setTitle("Directions");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JEditorPane htmlPane = new JEditorPane();
        htmlPane.setContentType("text/html");
        htmlPane.setEditable(false);
        htmlPane.setBackground(Color.WHITE);

        StringBuilder html = new StringBuilder(
                "<html><body style='font-family: sans-serif; font-size: 14pt; line-height: 1.2;'>"
        );
        html.append("<p><strong>From:</strong> ").append(fromLabel).append("<br>");
        html.append("<strong>To:</strong> ").append(toLabel).append("</p>");
        html.append("<hr>");

        int stepNum = 1;
        for (RouteStep step : steps) {
            html.append("<p><strong>Step ").append(stepNum++).append(":</strong> ")
                    .append(step.toString())
                    .append("</p>");
        }

        html.append("<hr>");
        html.append("<p><strong>Total Distance:</strong> ")
                .append(String.format("%.1f feet", totalDistance))
                .append("</p>");
        html.append("</body></html>");

        htmlPane.setText(html.toString());

        JScrollPane scrollPane = new JScrollPane(htmlPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton startOverButton = new JButton("Start Over");
        startOverButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> CougarMaps.main(new String[]{}));
        });

        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(e -> {
            String htmlContent = htmlPane.getText();

            Transferable htmlTransferable = new Transferable() {
                private final DataFlavor htmlFlavor = new DataFlavor("text/html;class=java.lang.String", "HTML Format");

                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[] {
                            DataFlavor.stringFlavor,
                            htmlFlavor
                    };
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return flavor.equals(DataFlavor.stringFlavor) || flavor.equals(htmlFlavor);
                }

                @Override
                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                    if (isDataFlavorSupported(flavor)) {
                        return htmlContent;
                    } else {
                        throw new UnsupportedFlavorException(flavor);
                    }
                }
            };

            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(htmlTransferable, null);

            JOptionPane.showMessageDialog(this, "Directions copied to clipboard with formatting.");
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(startOverButton);
        buttonPanel.add(copyButton);
        buttonPanel.add(exitButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(panel);
    }
}
