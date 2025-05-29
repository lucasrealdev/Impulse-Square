package com.impulsesquare.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GuiUtils {
    public static JButton createStyledButton(String text, int size, Color bg, Color fg, int fontSize) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setPreferredSize(new Dimension(size, size));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return button;
    }

    public static JPanel createHeaderPanel(String title, String backText, ActionListener backAction, JLabel rightComponent) {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        headerPanel.setBackground(new Color(33, 33, 33));
        JButton backButton = new JButton(backText);
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBackground(new Color(255, 87, 34));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.addActionListener(backAction);
        backButton.setFocusable(false);
        headerPanel.add(backButton, BorderLayout.WEST);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        if (rightComponent != null) {
            headerPanel.add(rightComponent, BorderLayout.EAST);
        }
        return headerPanel;
    }

    public static JPanel createInfoPanel(String infoText) {
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel infoLabel = new JLabel(infoText);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoPanel.add(infoLabel);
        return infoPanel;
    }
} 