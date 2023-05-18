 package com.impulsesquare.scenes;

import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.impulsesquare.objects.Cell;
import com.impulsesquare.objects.Player;

public class LoadLevels extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	//CRIA OBJETOS
	private Player player;
    private Timer timer;
    
    private Image background;
    private List<Cell> map;
    private List<Cell> newMap;
    
    private File directory;
    private String selectedMap;
    
    // VARIAVEIS DE FPS
    private int fpsCount = 0;
    private double fps = 0;
    private long lastTime = System.currentTimeMillis();
    
	@SuppressWarnings("unchecked")
	//CONSTRUTOR
	public LoadLevels() {
		setFocusable(true);
		setDoubleBuffered(true);
		addKeyListener(new TecladoAdapter());
		
		directory = new File(System.getProperty("user.dir"));
		
		//ADICIONA TODOS OS ARQUIVOS .DAT AO ARRAY
		ArrayList<String> maps = new ArrayList<String>();
        for (File file : directory.listFiles()) {
        	try {
        		if (file.getName().endsWith(".dat")) {
                    maps.add(file.getName());
                }
			} catch (Exception e) {e.printStackTrace();}
        }
        
        String[] maps_array = maps.toArray(new String[maps.size()]);
        
        //ESCOLHA DE MAPA
        selectedMap = (String)JOptionPane.showInputDialog( null, "Selecione um mapa para jogar abaixo: ", "Mapas...",
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                maps_array,
                maps_array[0]);
        
        if (selectedMap == null) {
        	return;
        }
        
    	//GUARDA O MAPA ESCOLHIDO EM UMA LISTA
		try
		{
            FileInputStream fis = new FileInputStream(selectedMap);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (List<Cell>) ois.readObject();
            ois.close();
            fis.close();
		}
		catch (Exception ex){JOptionPane.showMessageDialog(null, ex.getMessage()); }
		
		//CRIA UM PLAYER E CARREGA AS TEXTURAS
		player = new Player(map);
		player.load();
		
		//PEGA A IMAGEM DE FUNDO
		background = map.get(map.size()-1).getTexture().getImage();
		
		newMap = new ArrayList<>(map);
		
		//REMOVE O PLAYER DO MAPA
		for (int i = 0; i < map.size(); i++) {
			if (map.get(i).getColor() != null && map.get(i).getColor().contains("character")) {
				newMap.get(i).setColor("");
				newMap.get(i).setTexture(new ImageIcon(getClass().getResource("/com/impulsesquare/images/transparent.png")));
				newMap.get(i).setIcon(new ImageIcon(getClass().getResource("/com/impulsesquare/images/transparent.png")));
			}
		}
		//INICIA LOOP
		timer = new Timer(10, this);
		timer.start();
	}

	//FUNCAO QUE DESENHA NA TELA
	public void paint(Graphics g) {
		Graphics2D graficos = (Graphics2D) g;
		
        graficos.drawImage(background, 0, 0, this);
	    
		for (int i = 0; i < newMap.size()-1; i++) {
			graficos.drawImage(newMap.get(i).getTexture().getImage(), newMap.get(i).getX(), newMap.get(i).getY()-1, this);
		}
		
		graficos.drawImage(player.getCharacter_img(), player.getX(), player.getY(), this);
		
		// DESENHA FPS
	    graficos.drawString("FPS: " + String.format("%.2f", fps), 10, 20);
	    
		g.dispose();
	}
	
	public void reset() {
		player.setClose(true);
		player = null;
		timer.stop();
	}
	
	//CRIA LEITORES DE TECLADO
	@Override
	public void actionPerformed(ActionEvent e) {
		//CHAMA A FUNCAO DE MOVIMENTO
		player.moviment();
		repaint();
		
		// CALCULAR FPS
	    long currentTime = System.currentTimeMillis();
	    long elapsedTime = currentTime - lastTime;
	    fpsCount++;

	    if (elapsedTime >= 1000) {
	        fps = (double) fpsCount / (elapsedTime / 1000.0);
	        fpsCount = 0;
	        lastTime = currentTime;
	    }
	}
	
	private class TecladoAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			player.keyPressed(e);
		}
	}

	//RETORNA NOME DO MAPA ESCOLHIDO
	public String getSelectedMap() {
		return selectedMap;
	}
}
 