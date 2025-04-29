package com.cougarmaps.view;

import javax.swing.*;

public class BaseView extends JFrame {

    public BaseView(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
