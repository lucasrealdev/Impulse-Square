package com.impulsesquare.scenes;

import javax.swing.JFrame;

public class StartLevel extends JFrame{
	private static final long serialVersionUID = 1L;

	public StartLevel() {
		LoadLevels loader = new LoadLevels();
		if (loader.getSelectedMap() == null) {
			dispose();//FECHA SE NAO SELECIONAR MAPA
		}
		else{
			add(loader);
			setTitle(loader.getSelectedMap().replace(".dat", ""));
			setSize(781, 536);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setLocationRelativeTo(null);
			setResizable(false);
			setVisible(true);
		}
	}
}
