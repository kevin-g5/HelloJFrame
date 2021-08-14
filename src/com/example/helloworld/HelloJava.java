package com.example.helloworld;

import javax.swing.*;
import java.awt.*;

public class HelloJava {
    public static void main(String[] args) {
        System.out.println("Hello, JFrame!");

        JFrame frame = new JFrame("Overlay");

        initialize(frame);
        setSize(frame);
    }

    private static void initialize(JFrame frame) {
            frame.setUndecorated(true);
            frame.setBackground(new Color(0, 0, 0, 0));
            frame.setAlwaysOnTop(true);

            // Without this, the window is draggable from any non transparent
            // point, including points  inside textboxes.
            //frame.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", false);

            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(new JTextField("text field north"), BorderLayout.NORTH);
            frame.getContentPane().add(new JTextField("text field south"), BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.pack();
    }

    private static void setSize(JFrame frame) {
        final Rectangle rect = new Rectangle(10, 10, 500, 100); //needs to be final or effectively final for lambda
        //WindowUtils.getAllWindows(true).forEach(desktopWindow -> {
        //    if (desktopWindow.getTitle().contains("Lobby")) {
        //        rect.setRect(desktopWindow.getLocAndSize());
        //    }
        //);
        frame.setSize(rect.width, rect.height);
        frame.setLocation(rect.x, rect.y);
    }
}
