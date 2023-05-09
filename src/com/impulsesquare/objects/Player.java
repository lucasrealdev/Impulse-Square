package com.impulsesquare.objects;

import java.awt.Dimension;


import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Player{
	private Dimension size;
	private int x=60, y=60; //POSICAO DO PLAYER
	private int dx, dy; //DIRECAO DO PLAYER
	private String color;//COR DO PLAYER
	private Image character_img; //IMAGEM DO PLAYER
	private List<Cell> blocks; //MAPA DE BLOCOS
	private File file_animations = new File(getClass().getResource("/com/impulsesquare/animations").getFile());
	private List<Image> list_img_animations = new ArrayList<>();
	
	//RECEBE O MAPA
	public Player(List<Cell> blocks) {
		super();
		this.blocks = blocks;
		loadAnimations();
	}
	
	//CARREGA IMAGEM E DEFINE TAMANHO DO PLAYER
	public void load(){
		ImageIcon imageicon = new ImageIcon(getClass().getResource("/com/impulsesquare/images/character-green.png"));
		color = new File(imageicon.getDescription()).getName().replace(".png", "");
		character_img = imageicon.getImage();
		imageicon.getImage();
		size = new Dimension(character_img.getWidth(null), character_img.getHeight(null));
	}
	
	//VARIAVEIS DE CONTROLE
	private boolean isMoving;
	private int speed = 20;
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
	        if (!new File(block.getTexture().getDescription()).getName().equals("transparent.png")
	        		&& !new File(block.getTexture().getDescription()).getName().contains("bricks.png")
	        		&& playerRect.intersects(blockRect)) {
	        	
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
	    		if (new File(block.getTexture().getDescription()).getName().contains("portal.png")) {
					System.out.println("portal");
					return;
				}
	    		if (!block.getColor().contains(color.replace("character-", ""))) {//VERIFICA COLISAO COM COR DIFERENTE DO PLAYER
					deadAnimation();
				}
	        }
	    }
	    x += dx;
        y += dy;
	}
	
	private void loadAnimations() {
        for (File file : file_animations.listFiles()) {
            if (file.isFile() && file.getName().contains("explosion")) {
            	ImageIcon imageicon = new ImageIcon(getClass().getResource("/com/impulsesquare/animations/"+file.getName()));
            	imageicon.setImage(imageicon.getImage().getScaledInstance(43, 43, Image.SCALE_SMOOTH));
            	list_img_animations.add(imageicon.getImage());
            }
        }
	}
	
	private void deadAnimation() {
	    new Thread(() -> {
	    	isMoving = true;
	    	for (int i = 0; i < list_img_animations.size(); i++) {
	            try {
	                Thread.sleep(25); // espera 1 segundo
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            character_img = list_img_animations.get(i);
	            try {
	                Thread.sleep(25); // espera 1 segundo
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
			}
	    }).start();
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