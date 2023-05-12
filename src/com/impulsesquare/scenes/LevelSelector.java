package com.impulsesquare.scenes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class LevelSelector extends JFrame{
	private static final long serialVersionUID = 1L;
	String[] maps_array;
	
	public LevelSelector(String[] maps_array) {
		setTitle("Seletor de fase");
		setSize(new Dimension(960, 540));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);
		this.maps_array = maps_array;
		
		ImageIcon background_img = new ImageIcon(getClass().getResource("/com/impulsesquare/images/level_menu.png"));
		background_img.setImage(background_img.getImage().getScaledInstance(960, 580, Image.SCALE_SMOOTH));
		JLabel background = new JLabel(background_img);
		background.setBounds(0,0, 960, 540);
		add(background);
		
		JPanel levelselector_panel = new JPanel();
        levelselector_panel.setLayout(new GridLayout(0, 5)); // 5 colunas, número de linhas é determinado automaticamente
		
		for (int i = 0; i < maps_array.length; i++) {
            JLabel label = new JLabel(Integer.toString(i + 1));
            label.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // Borda dourada de 2px
            label.setBackground(Color.BLUE); // Fundo azul
            label.setForeground(Color.WHITE); // Texto branco
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setOpaque(true); // Permite que o fundo seja pintado
            
            levelselector_panel.add(label);
		}
		levelselector_panel.setBackground(Color.black);
		levelselector_panel.setBounds(0,0,100,100);
		add(levelselector_panel);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] maps = { "Mapa 1", "Mapa 2", "Mapa 3", "Mapa 4", "Mapa 5", "Mapa 6", "Mapa 7", "Mapa 8", "Mapa 9",
                    "Mapa 10", "Mapa 11", "Mapa 12", "Mapa 13", "Mapa 14", "Mapa 15", "Mapa 16", "Mapa 17", "Mapa 18",
                    "Mapa 19", "Mapa 20", "Mapa 21", "Mapa 22", "Mapa 23", "Mapa 24", "Mapa 25" };
            new LevelSelector(maps);
        });
	}
}
