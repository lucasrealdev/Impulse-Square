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
	private int x=60, y=60; //POSICAO DO PLAYER
	private int dx, dy; //DIRECAO DO PLAYER
	private Image character_img; //IMAGEM DO PLAYER
	private List<Cell> blocks; //MAPA DE BLOCOS
	
	//RECEBE O MAPA
	public Player(List<Cell> blocks) {
		super();
		this.blocks = blocks;
	}
	
	//CARREGA IMAGEM E DEFINE TAMANHO DO PLAYER
	public void load(){
		ImageIcon imageicon = new ImageIcon(getClass().getResource("/com/impulsesquare/images/character-green.png"));
		character_img = imageicon.getImage();
		size = new Dimension(character_img.getWidth(null), character_img.getHeight(null));
	}
	
	//VARIAVEIS DE CONTROLE
	private boolean isMoving;
	private int speed = 16;
	//private boolean isOut = false;
	
	// VERIFICA SE O JOGADOR PODE SE MOVER NAS DIREÇÕES X E Y E MOVE ELE
	public void moviment() {
		
	    // CRIA UM RETÂNGULO QUE REPRESENTA A POSIÇÃO ATUAL DO JOGADOR
	    Rectangle playerRect = new Rectangle(getX() + dx, getY() + dy, size.width, size.height);
	    
	    // PERCORRE TODOS OS BLOCOS DO JOGO
	    for (int i = 0; i < blocks.size()-1; i++) {
	    	
	    	// OBTÉM O RETÂNGULO QUE REPRESENTA A POSIÇÃO DO BLOCO ATUAL
	        Cell block = blocks.get(i);
	        
	        // OBTÉM O RETÂNGULO QUE REPRESENTA OS BLOCOS
	        Rectangle blockRect = new Rectangle(block.getLocation().x, block.getLocation().y, block.getSize().width, block.getSize().height);
	        
	        //DIMINUI HITBOX DOS BLOCOS DE CIMA
	        if (i <= 17) {
	        	blockRect = new Rectangle(block.getLocation().x, block.getLocation().y-4, block.getSize().width, block.getSize().height);
			}
	        
	        //VERIFICA COLISAO
	        if (!new File(block.getTexture().getDescription()).getName().equals("transparent.png") && playerRect.intersects(blockRect)) {
	        	
	        	// CRIA UM NOVO RETÂNGULO QUE REPRESENTA A INTERSECÇÃO ENTRE O JOGADOR E O BLOCO
	            Rectangle intersection = playerRect.intersection(blockRect);
	            if (intersection.width < intersection.height) 
	            {
	            	if (playerRect.x < blockRect.x)
	                {
	            		x = blockRect.x - size.width;
	                }
	                else
	                {
	                	x = blockRect.x + size.width+13;
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
	                    y = blockRect.y + size.height+13;
	                }
	            }
	            isMoving = false;
	            dx=0;
	    		dy=0;
	        }
	    }
	    
	    //COLISAO COM A BORDA
//	    if (x + dx > 781-size.width-9) {
//	        isOut = true;
//	    }
//	    if (x + dx < 0) {
//	    	isOut = true;
//	    }
//	    if (y + dy > 536) {
//	    	isOut = true;
//	    }
//	    if (y + dy < 0) {
//	    	isOut = true;
//	    }
//	    System.out.println(x);
	    
//	    if (!isOut) {
//	    	
//		}
	    
	    x += dx;
        y += dy;
	}
	
	//EVENTO DE TECLA PRESSIONADA
	public void keyPressed(KeyEvent key) {
		if (!isMoving) {
			int code = key.getKeyCode();
			
			if (code == KeyEvent.VK_UP) {
				dy = -speed;
			}
			if (code == KeyEvent.VK_DOWN) {
				dy = speed;
			}
			if (code == KeyEvent.VK_LEFT) {
				dx = -speed;
			}
			if (code == KeyEvent.VK_RIGHT) {
				dx= speed;
			}
			isMoving = true;
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