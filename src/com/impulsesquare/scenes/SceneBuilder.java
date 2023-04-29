package com.impulsesquare.scenes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.impulsesquare.objects.Blocks;
import com.impulsesquare.objects.Mesh;

public class SceneBuilder extends JFrame{
	private static final int WIDTH_CELL = 50;
    private static final int HEIGHT_CELL = 50;
    private static final int WIDTH_SCREEN = 800;
    private static final int HEIGHT_SCREEN = 500;
    private static final int NUM_COLUNMS = WIDTH_SCREEN / WIDTH_CELL;
    private static final int NUM_ROWS = HEIGHT_SCREEN / HEIGHT_CELL; 
    
    File directory = new File("src/com/impulsesquare/textures");
    File[] files = directory.listFiles();
    
    JPanel panelimages = new JPanel();
    
	private Blocks pixels[][];// Linha/coluna
    
	public SceneBuilder() {
		setTitle("Criador de Cenas");
		setSize(WIDTH_SCREEN, HEIGHT_SCREEN);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(true);
		setVisible(true);
		makemesh();
		loadtextures();
	}
	
	private void loadtextures() {
		for (File file : files) {
		    if (file.isFile() && file.getName().endsWith(".png")) {
		        // faça o que quiser com o arquivo aqui
		    	String images_name = file.getName();
                ImageIcon icon = new ImageIcon(getClass().getResource(images_name));
                JLabel label = new JLabel(icon);
                add(label);
		    }
	    }
	}

	private void makemesh()
	{
		// Cria Malha
		JPanel malha = new JPanel(new GridLayout(NUM_ROWS, NUM_COLUNMS));
		malha.setPreferredSize(new Dimension(WIDTH_SCREEN, HEIGHT_SCREEN));
		malha.setBackground(Color.black);
		for (int i = 0; i < NUM_ROWS * NUM_COLUNMS; i++)
		{
	    	Mesh celula = new Mesh(i);
	        celula.addeventclick();
	        malha.add(celula);
	        add(malha);
	    }
		 getContentPane().add(malha);
		 pack();
    }
	
	public static void main(String[] args) {
		new SceneBuilder();
	}
}
