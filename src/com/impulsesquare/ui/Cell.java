package com.impulsesquare.objects;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Cell extends JLabel {
    private ImageIcon texture = null;
    private String pathTexture = null;
    private String name = null;
    private boolean flipped = false;
    private int rotation = 0;

    public Cell(int cellSize) {
        setPreferredSize(new Dimension(cellSize, cellSize));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        addhover();
    }

    public Cell(ImageIcon texture, String name, Integer cellSize, String pathTexture) {
        setPreferredSize(new Dimension(cellSize, cellSize));
        this.texture = texture;
        this.name = name;
        this.pathTexture = pathTexture;
        setIcon(texture);
        addhovertexture();
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    private void addhover() {
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.green, 1));
            }

            public void mouseExited(MouseEvent e) {
                setBorder(null);
            }
        });
    }

    private void addhovertexture() {
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.red, 1));
            }

            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.black, 0));
            }
        });
    }

    public void clear() {
        texture = null;
        pathTexture = null;
        name = null;
        rotation = 0;
        flipped = false;
        setIcon(null);
    }

    // Getters e Setters para as novas propriedades
    public ImageIcon getTexture() {
        return texture;
    }

    public void setTexture(ImageIcon texture) {
        this.texture = texture;
        setIcon(texture);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation % 360; // Garante que fique entre 0-359
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public String getPathTexture() {
        return pathTexture;
    }

    public void setPathTexture(String pathTexture) {
        this.pathTexture = pathTexture;
    }
}
