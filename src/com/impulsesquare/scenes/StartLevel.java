package com.impulsesquare.scenes;

import javax.swing.JFrame;

public class StartLevel extends JFrame{
	private static final long serialVersionUID = 1L;

	public StartLevel() {
		LoadLevels loader = new LoadLevels() {
		private static final long serialVersionUID = 1L;};
		add(loader);
		setTitle(loader.getSelectedMap().replace(".dat", ""));
		setSize(781, 536);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new StartLevel();
	}
}
