package com.impulsesquare.objects;

import com.impulsesquare.resources.ImageUtil;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CustomJPanel extends JPanel {
    private ImageIcon background;
    private String pathBackground;

    public CustomJPanel(int width, int height, LayoutManager layout) {
        setPreferredSize(new Dimension(width, height));
        setLayout(layout);
        this.background = ImageUtil.BACKGROUND_WHITE;
        this.pathBackground = ImageUtil.extractImagePath(ImageUtil.BACKGROUND_WHITE);
        initializePanel();
    }

    private void initializePanel() {
        setBorder(null);
        setOpaque(false);
        loadDefaultBackground();
    }

    private void loadDefaultBackground() {
        this.background = ImageUtil.BACKGROUND_WHITE;
        this.pathBackground = ImageUtil.extractImagePath(ImageUtil.BACKGROUND_WHITE);
        scaleBackground();
    }

    public void setBackground(ImageIcon background, String pathBackground) {
        this.background = background;
        this.pathBackground = pathBackground;
        scaleBackground();
        repaint();
    }

    private void scaleBackground() {
        if (background != null) {
            int width = getWidth();
            int height = getHeight();
            if (width <= 0 || height <= 0) {
                width = getPreferredSize().width;
                height = getPreferredSize().height;
            }
            Image scaledImage = background.getImage()
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH);
            background = new ImageIcon(scaledImage);
        }
    }

    public String getPathBackground() {
        return pathBackground;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
}
