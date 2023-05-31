package com.impulsesquare.scenes;

import java.awt.Dimension;

import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.impulsesquare.objects.CustomButton;

public class Menu extends JFrame{
	private static final long serialVersionUID = 1L;
	private int WIDTH = 650, HEIGHT = 360;
	private int pWIDTH = WIDTH/100, pHEIGHT = HEIGHT/100;
	private ImageIcon background_img = new ImageIcon(getClass().getResource("/com/impulsesquare/images/background_menu.png"));
	public Menu() throws HeadlessException{
		super("Menu");
		setSize(WIDTH+15, HEIGHT+38);
		setResizable(false);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			Image iconeTitulo = ImageIO.read(getClass().getResource("/com/impulsesquare/images/logo.png"));
			setIconImage(iconeTitulo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		createMenu();
	}
	private void createMenu() {
		background_img.setImage(background_img.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH));
        
		JLabel background = new JLabel(background_img);
		background.setSize(WIDTH, HEIGHT);
		
		CustomButton startButton = new CustomButton(new Dimension(60, 25));
		CustomButton builderButton = new CustomButton(new Dimension(70, 30));
		CustomButton tutorialButton = new CustomButton(new Dimension(60, 25));
		CustomButton closeButton = new CustomButton(new Dimension(20, 20));
		
		background.setBounds(0, 0, WIDTH, HEIGHT);
		
		closeButton.setLocation(pWIDTH*108-closeButton.getSize().width, pHEIGHT*1);
		startButton.setLocation(pWIDTH*36-startButton.getSize().width/2, pHEIGHT*83);
		builderButton.setLocation(pWIDTH*54-builderButton.getSize().width/2, pHEIGHT*82);
		tutorialButton.setLocation(pWIDTH*72-tutorialButton.getSize().width/2, pHEIGHT*83);
		
		closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // BOTAO DE FECHAR
                System.exit(0);
            }
        });
		startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new StartLevel();
                setExtendedState(JFrame.ICONIFIED); //MINIMIZA JANELA
            }
        });
		builderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					new SceneBuilder();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                setExtendedState(JFrame.ICONIFIED);
            }
        });
		tutorialButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Tutorial();
            }
        });
		
		add(tutorialButton);
		add(startButton);
		add(builderButton);
		add(closeButton);
		add(background);
		
		setLayout(null);
		setVisible(true);
		repaint();
	}
	
	public static void main(String[] args) {
		new Menu();
	}
}