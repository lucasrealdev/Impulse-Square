package com.impulsesquare.objects;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class Blocks implements Serializable{
	
	private int WIDTH = 10; // define fixed width of every object
	private int HEIGHT = 10; // define fixed height of every object
	private ImageIcon texture; // texture of objects
	private String color; // color of objects
	private int id; // id of each object  
	
	public Blocks(ImageIcon texture, String color) {
		super();
		this.texture = texture;
		this.color = color;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
