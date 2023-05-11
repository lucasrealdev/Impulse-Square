package com.impulsesquare.objects;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CustomJPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	ImageIcon background = new ImageIcon(getClass().getResource("/com/impulsesquare/images/background-white.png"));
	public CustomJPanel(GridLayout layout) {
		setLayout(layout);
	}
	
	public void setBackground(ImageIcon background) {
		ImageIcon newBackground = new ImageIcon(background.getImage().getScaledInstance(getSize().width, getSize().height, Image.SCALE_SMOOTH));
		this.background = newBackground;
	}
	
	public ImageIcon getBackgroundImage() {
		return background;
	}
	
	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, null);
	}
}
