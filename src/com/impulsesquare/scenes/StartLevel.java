package com.impulsesquare.scenes;

import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class StartLevel extends JFrame{
	private static final long serialVersionUID = 1L;
	LoadLevels loader;
	public StartLevel() {
		loader = new LoadLevels();
		if (loader.getSelectedMap() == null) {
			dispose();
		}
		else{
			add(loader);
			setTitle(loader.getSelectedMap().replace(".dat", ""));
			setSize(781, 536);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setLocationRelativeTo(null);
			setResizable(false);
			setVisible(true);
			try {
				Image iconeTitulo = ImageIO.read(getClass().getResource("/com/impulsesquare/images/logo.png"));
				setIconImage(iconeTitulo);
			} catch (IOException e) {e.printStackTrace();}
		}
		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	loader.reset();
            	System.gc();
            } 
        });
	}
}
