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

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.impulsesquare.objects.Cell;
import com.impulsesquare.objects.Player;

public class Level1 extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private Player player;
    private Timer timer;
    
    private Image background;
    private List<Cell> map;
    
    private File directory;
	@SuppressWarnings("unchecked")
	public Level1() {
		setFocusable(true);
		setDoubleBuffered(true);
		
		addKeyListener(new TecladoAdapter());
		
		directory = new File(System.getProperty("user.dir"));
		
		//ADICIONA TODOS OS ARQUIVOS .DAT AO ARRAY
		
		ArrayList<String> maps = new ArrayList<String>();
        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".dat")) {
                maps.add(file.getName());
            }
        }
        String[] maps_array = maps.toArray(new String[maps.size()]);
        
        String selectedValue = (String)JOptionPane.showInputDialog( null, "Selecione um mapa para jogar abaixo: ", "Mapas...",
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                maps_array,
                maps_array[0]);
        
        if (selectedValue == null) {
			System.exit(0);
		}
        
        //GUARDA O MAPA ESCOLHIDO EM UMA LISTA
		try
		{
            FileInputStream fis = new FileInputStream(selectedValue);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (List<Cell>) ois.readObject();
            ois.close();
            fis.close();
		}
		catch (Exception ex)
		{
            JOptionPane.showMessageDialog(null, ex.getMessage()); 
        }
		
		player = new Player(map);
		player.load();
		
		background = map.get(map.size()-1).getTexture().getImage();
		timer = new Timer(5, this);
		timer.start();
	}

	public void paint(Graphics g) {
		Graphics2D graficos = (Graphics2D) g;
		graficos.drawImage(background, 0, 0, this);
		for (int i = 0; i < map.size()-1; i++) {
			if(i <= 17) {
				graficos.drawImage(map.get(i).getTexture().getImage(), map.get(i).getX(), map.get(i).getY()-2, this);
			}
			else {
				graficos.drawImage(map.get(i).getTexture().getImage(), map.get(i).getX(), map.get(i).getY(), this);
			}
		}
		graficos.drawImage(player.getCharacter_img(), player.getX(), player.getY(), this);
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		player.moviment();
		repaint();
	}
	
	private class TecladoAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			player.keyPressed(e);
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}
	}
}
 