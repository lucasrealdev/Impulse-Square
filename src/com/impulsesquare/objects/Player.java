package com.impulsesquare.objects;

import java.awt.Dimension;


import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;

public class Player{
	private Dimension size;
	private int x=60, y=60, dx, dy;
	private Image character_img;
	private List<Cell> blocks;
	
	public Player(List<Cell> blocks) {
		super();
		this.blocks = blocks;
	}
	public void load(){
		ImageIcon imageicon = new ImageIcon(getClass().getResource("/com/impulsesquare/images/character-green.png"));
		character_img = imageicon.getImage();
		size = new Dimension(character_img.getWidth(null), character_img.getHeight(null));
	}
	
	// VERIFICA SE O JOGADOR PODE SE MOVER NAS DIREÇÕES X E Y E MOVE ELE
	public void moviment() {
		// INICIA AS VARIÁVEIS DE CONTROLE COMO VERDADEIRAS
		boolean canMoveX = true;
	    boolean canMoveY = true;
	    
	    // CRIA UM RETÂNGULO QUE REPRESENTA A POSIÇÃO ATUAL DO JOGADOR
	    Rectangle playerRect = new Rectangle(getX() + dx, getY() + dy, size.width, size.height);
	    
	    // PERCORRE TODOS OS BLOCOS DO JOGO
	    for (int i = 0; i < blocks.size()-1; i++) {
	    	
	    	// OBTÉM O RETÂNGULO QUE REPRESENTA A POSIÇÃO DO BLOCO ATUAL
	        Cell block = blocks.get(i);
	        
	        // OBTÉM O RETÂNGULO QUE REPRESENTA OS BLOCOS
	        Rectangle blockRect = new Rectangle(block.getLocation().x, block.getLocation().y, block.getSize().width, block.getSize().height);
	        if (!new File(block.getTexture().getDescription()).getName().equals("transparent.png") && playerRect.intersects(blockRect)) {
	        	// CRIA UM NOVO RETÂNGULO QUE REPRESENTA A INTERSECÇÃO ENTRE O JOGADOR E O BLOCO
	            Rectangle intersection = playerRect.intersection(blockRect);
	            if (intersection.width < intersection.height) 
	            {
	                if (playerRect.x < blockRect.x) // VERIFICA SE A LARGURA DA INTERSECÇÃO É MENOR QUE A ALTURA
	                {
	                    x = blockRect.x - size.width;
	                }
	                else
	                {
	                    x = blockRect.x + blockRect.width;
	                }
	            }
	            else
	            {
	                if (playerRect.y < blockRect.y)
	                {
	                    y = blockRect.y - size.height;
	                }
	                else
	                {
	                    y = blockRect.y + blockRect.height-2;
	                }
	            }
	        }
	    }
	    
	    if (canMoveX) {
	        x += dx;
	    }
	    if (canMoveY) {
	        y += dy;
	    }
	}
	
	public void keyPressed(KeyEvent key) {
		int code = key.getKeyCode();
		
		if (code == KeyEvent.VK_UP) {
			dy = -3;
		}
		if (code == KeyEvent.VK_DOWN) {
			dy = 3;
		}
		if (code == KeyEvent.VK_LEFT) {
			dx = -3;
		}
		if (code == KeyEvent.VK_RIGHT) {
			dx= 3;
		}
	}
	
	public void keyReleased(KeyEvent key) {
		int code = key.getKeyCode();
		
		if (code == KeyEvent.VK_UP) {
			dy = 0;
		}
		if (code == KeyEvent.VK_DOWN) {
			dy = 0;
		}
		if (code == KeyEvent.VK_LEFT) {
			dx = 0;
		}
		if (code == KeyEvent.VK_RIGHT) {
			dx= 0;
		}
	}
	public Image getCharacter_img() {
		return character_img;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
}
