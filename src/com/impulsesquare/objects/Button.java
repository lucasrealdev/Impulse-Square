package com.impulsesquare.objects;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Button extends JButton{
	private static final long serialVersionUID = 1L;

	public Button(ImageIcon image) {
		setPreferredSize(new Dimension(50, 50));
		image.setImage(image.getImage().getScaledInstance(40, 40, 100));
		setIcon(image);
	    setOpaque(false);
	    setFocusPainted(false);
	    setBackground(new Color(0,0,0,0));
	    setToolTipText("muda o fundo");
	}
}
