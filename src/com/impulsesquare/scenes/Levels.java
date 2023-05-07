package com.impulsesquare.scenes;

import javax.swing.JFrame;

public class Levels extends JFrame{
	private static final long serialVersionUID = 1L;

	public Levels() {
		LoadLevels loader = new LoadLevels();
		add(loader);
		setTitle(loader.getSelectedMap().replace(".dat", ""));
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
