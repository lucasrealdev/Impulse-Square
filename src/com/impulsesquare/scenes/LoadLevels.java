package com.impulsesquare.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.GridLayout;
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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
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
    
    private File directory;
    private String selectedMap;
    private String[] maps_array;
    
    private JPanel levelselector_panel;
    
	@SuppressWarnings("unchecked")
	//CONSTRUTOR
	public LoadLevels() {
		setLayout(null);
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
        maps_array = maps.toArray(new String[maps.size()]);
        
        //ESCOLHA DE MAPA
        LevelSelector levelselector = new LevelSelector(maps_array);
        
    	//GUARDA O MAPA ESCOLHIDO EM UMA LISTA
		try
		{
            FileInputStream fis = new FileInputStream(selectedMap);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (List<Cell>) ois.readObject();
            ois.close();
            fis.close();
		}
		catch (Exception ex)
		{
			
        }
		
		//CRIA UM PLAYER E CARREGA AS TEXTURAS
		player = new Player(map);
		player.load();
		
		background = map.get(map.size()-1).getTexture().getImage();
		
		//INICIA LOOP
		timer = new Timer(5, this);
		timer.start();
		
		
		levelselector_panel = new JPanel();
		levelselector_panel.setLayout(new GridLayout(5, maps_array.length/5));
		for (int i = 0; i < maps_array.length; i++) {
            JLabel label = new JLabel(Integer.toString(i + 1));
            label.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // Borda dourada de 2px
            label.setBackground(Color.BLUE); // Fundo azul
            label.setForeground(Color.WHITE); // Texto branco
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setOpaque(true); // Permite que o fundo seja pintado
            // Estilizando a fonte do JLabel
            Font font = new Font("", Font.BOLD, 20); // Define a fonte em negrito com tamanho 20
            levelselector_panel.add(label);
        }
		levelselector_panel.setBounds(10, 10, 500, (int) Math.ceil(maps_array.length / 5.0) * 50);
        add(levelselector_panel);
	}

	//FUNCAO QUE DESENHA NA TELA
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
	
	//CRIA LEITORES DE TECLADO
	@Override
	public void actionPerformed(ActionEvent e) {
		//CHAMA A FUNCAO DE MOVIMENTO
		player.moviment();
		repaint();
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
 