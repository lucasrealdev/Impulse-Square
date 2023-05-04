package com.impulsesquare.objects;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Cell extends JLabel{
	private static final long serialVersionUID = 1L;
	private boolean selected = false;
	private ImageIcon texture = new ImageIcon(getClass().getResource("/com/impulsesquare/images/transparent.png"));
	private String color;
	public Cell() {
		setPreferredSize(new Dimension(45, 45));
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		addhover();
		setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public Cell(ImageIcon texture, String color) {
		setPreferredSize(new Dimension(45, 45));
		setIcon(texture);
		this.texture = texture;
		this.color = color;
		addhovertexture();
	    setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	private void addhover() {
		addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				setBorder(BorderFactory.createLineBorder(Color.green, 1));
			}
			public void mouseExited(MouseEvent e)
			{
				setBorder(BorderFactory.createLineBorder(Color.black, 1));
			}
		});
	}
	
	private void addhovertexture() {
		addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				setBorder(BorderFactory.createLineBorder(Color.red, 1));
			}
			public void mouseExited(MouseEvent e)
			{
				setBorder(BorderFactory.createLineBorder(Color.black, 0));
			}
		});
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public ImageIcon getTexture() {
		return texture;
	}

	public void setTexture(ImageIcon texture) {
		this.texture = texture;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
