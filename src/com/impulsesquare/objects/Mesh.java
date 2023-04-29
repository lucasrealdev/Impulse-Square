package com.impulsesquare.objects;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Mesh extends JPanel{
	private static final long serialVersionUID = 1L;
	private int id;
	public Mesh(int id) {
		setSize(50, 50); // definindo tamanho do quadrado
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
    	this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public void addeventclick() {
		addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				System.out.println(getId());
			}
		});
	}
}
