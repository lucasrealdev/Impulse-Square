package com.impulsesquare.game;

import javax.swing.JFrame;

public class Game extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public Game() {
		setTitle("Impulse Square");
		setSize(1000, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game game = new Game();
	}

}
