package com.impulsesquare.scenes;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.impulsesquare.objects.Cell;

public class SceneBuilder extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH_CELL = 50;
    private static final int HEIGHT_CELL = 50;
    private static final int WIDTH_SCREEN = 800;
    private static final int HEIGHT_SCREEN = 500;
    private static final int NUM_COLUNMS = WIDTH_SCREEN / WIDTH_CELL;
    private static final int NUM_ROWS = HEIGHT_SCREEN / HEIGHT_CELL; 
    
    File directory = new File("src/com/impulsesquare/textures");
    File[] files = directory.listFiles();
    
    //CRIA MALHA
    JPanel malha = new JPanel(new GridLayout(NUM_ROWS, NUM_COLUNMS));
    
    //CRIA LISTA DE CELULAS	
    ArrayList<Cell> list_cell = new ArrayList<Cell>();
    
    //CRIA LISTA DE BLOCOS DE TEXTURA
    ArrayList<Cell> list_texture_blocks = new ArrayList<Cell>();
    
    //CRIA MENU DE TEXTURAS
    JPanel menu_textures = new JPanel(new GridLayout(10, 1, 4, 4));
	//private Blocks pixels[][];// LINHA/COLUNA
	
	public SceneBuilder() {
		setTitle("Criador de Cenas");
		setSize(WIDTH_SCREEN+200, HEIGHT_SCREEN);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		makebuilder();
	}

	private void makebuilder()
	{
		malha.setPreferredSize(new Dimension(WIDTH_SCREEN, HEIGHT_SCREEN));
		//CRIAR CELULAS DA MALHA
		for (int i = 0; i < NUM_ROWS * NUM_COLUNMS; i++)
		{
	        Cell cell = new Cell();
    		cell.addMouseListener(new MouseAdapter()
    		{
    			public void mouseClicked(MouseEvent e)
    			{
    				clickcell(cell);
    			}
    		});
    		list_cell.add(cell);
	        malha.add(cell);
	    }
		add(malha, BorderLayout.WEST);
		
		//PEGAR AS TEXTURAS DO PACOTE
		menu_textures.setPreferredSize(new Dimension(100, HEIGHT_SCREEN));
		for (File file : files) {
		    if (file.isFile() && file.getName().endsWith(".png")) {
		    	//PEGA O NOME DAS IMAGENS RESGATADAS
		    	String images_name = file.getName();
                ImageIcon textures = new ImageIcon(getClass().getResource("/com/impulsesquare/textures/"+images_name));
                textures.setImage(textures.getImage().getScaledInstance(50, 50, 100));
                Cell texture_block = new Cell(textures);
                texture_block.addMouseListener(new MouseAdapter()
        		{
        			public void mouseClicked(MouseEvent e)
        			{
        				clicktexture(texture_block);
        			}
        		});
                list_texture_blocks.add(texture_block);
                menu_textures.add(texture_block);
		    }
	    }
		add(menu_textures, BorderLayout.EAST);
		pack();
    }
	
	private void clickcell(Cell cell_clicked) {
		boolean find_texture = false;
		for (int i = 0; i < list_texture_blocks.size(); i++) {
			if (list_texture_blocks.get(i).isSelected()) {
				cell_clicked.setIcon(list_texture_blocks.get(i).getTexture());
				find_texture = true;
				return;
			}
		}
		if(!find_texture) JOptionPane.showMessageDialog(null, "Nenhuma textura selecionada");
	}
	
	private void clicktexture(Cell cell_clicked) {
		for (int i = 0; i < list_texture_blocks.size(); i++) {
			if (list_texture_blocks.get(i).isSelected()) {
				list_texture_blocks.get(i).setSelected(false);
			}
		}
		cell_clicked.setSelected(true);
	}
	
	public static void main(String[] args) {
		new SceneBuilder(); 
	}
}
