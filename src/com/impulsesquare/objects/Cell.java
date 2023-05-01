package com.impulsesquare.objects;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Cell extends JLabel{
	private static final long serialVersionUID = 1L;
	private boolean selected = false;
	private ImageIcon texture;
	public Cell() {
		setSize(50, 50); // DEFINE O TAMANHO DA CELULA
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		addhover();
	}
	
	public Cell(ImageIcon texture) {
		setSize(50, 50);
		setIcon(texture);
		this.texture = texture;
		addhovertexture();
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
}
