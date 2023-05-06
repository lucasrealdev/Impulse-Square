package com.impulsesquare.scenes;

import javax.swing.JFrame;

public class Levels extends JFrame{
	private static final long serialVersionUID = 1L;

	public Levels() {
		add(new Level1());
		setTitle("Fase 1");
		setSize(781, 536);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Levels();
	}

}
