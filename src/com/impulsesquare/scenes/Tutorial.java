package com.impulsesquare.scenes;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Tutorial extends JFrame{
	private static final long serialVersionUID = 1L;
	private int WIDTH = 900, HEIGHT = 500;
	private ImageIcon background_img = new ImageIcon(getClass().getResource("/com/impulsesquare/images/tutorial_menu.png"));
	public Tutorial() {
		super("Tutorial");
		setSize(WIDTH, HEIGHT+20);
		setResizable(false);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		try {
			Image iconeTitulo = ImageIO.read(getClass().getResource("/com/impulsesquare/images/logo.png"));
			setIconImage(iconeTitulo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		background_img.setImage(background_img.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH));
		JLabel background = new JLabel(background_img);
		JButton exitButton = new JButton();
		
		exitButton.setOpaque(false);
		exitButton.setFocusPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setBackground(new Color(0,0,0,0));
		exitButton.setBorder(null);
		
		exitButton.setBounds(WIDTH/2-33,448,65,25);
		background.setBounds(0,0,WIDTH,HEIGHT);
		
		exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
		
		add(exitButton);
		add(background);
		
		setLayout(null);
		setVisible(true);
		repaint();
	}
}
