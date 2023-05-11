package com.impulsesquare.objects;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class CustomButton extends JButton{
	private static final long serialVersionUID = 1L;

	public CustomButton(Dimension size) {
		setOpaque(false);
		setSize(size);
		setFocusPainted(false);
		setBorder(null);
		setContentAreaFilled(false);
		setBackground(new Color(0,0,0,0));
	}
}
