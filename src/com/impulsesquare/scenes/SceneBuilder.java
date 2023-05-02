package com.impulsesquare.scenes;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.impulsesquare.objects.Cell;

public class SceneBuilder extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH_CELL = 50;
    private static final int HEIGHT_CELL = 50;
    private static final int WIDTH_SCREEN = 800;
    private static final int HEIGHT_SCREEN = 500;
    private static final int NUM_COLUNMS = WIDTH_SCREEN / WIDTH_CELL;
    private static final int NUM_ROWS = HEIGHT_SCREEN / HEIGHT_CELL; 
    
    //CRIA LISTA COM AS TEXTURAS QUE ESTAO NO PACOTE
    File directory = new File("src/com/impulsesquare/textures");
    File[] files = directory.listFiles();
    
    //CRIA LISTA DE CELULAS	
    ArrayList<Cell> list_cell = new ArrayList<Cell>();
    
    //CRIA LISTA DE BLOCOS DE TEXTURA
    ArrayList<Cell> list_texture_blocks = new ArrayList<Cell>();
    
	public SceneBuilder() {
		setTitle("Criador de Cenas");
		setSize(new Dimension(WIDTH_SCREEN, HEIGHT_SCREEN));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(true);
		setVisible(true);
		makebuilder();
	}

	private void makebuilder()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.weightx = 0.3;
	    gbc.weighty = 1.0;
	    
		//CRIA MENU DE TEXTURAS
	    JPanel menu_textures = new JPanel(new GridLayout(0, 2, 4, 4));
	    
	    //CRIA MALHA
	    JPanel malha = new JPanel(new GridLayout(NUM_ROWS, NUM_COLUNMS));
	    
	    JPanel leftPane = new JPanel(new GridBagLayout());
	    leftPane.add(menu_textures, gbc);
	    
	    JScrollPane textures_scroll = new JScrollPane(leftPane);
	    textures_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    textures_scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    textures_scroll.setPreferredSize(new Dimension(150, HEIGHT_SCREEN));
	    
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
		
		//PEGAR AS TEXTURAS DO PACOTE
		menu_textures.setBackground(Color.LIGHT_GRAY);
		for (File file : files) {
		    if (file.isFile() && file.getName().endsWith(".png")) {
		    	//PEGA O NOME DAS IMAGENS RESGATADAS
		    	String images_name = file.getName();
		    	//CRIA IMAGEM
                ImageIcon textures = new ImageIcon(getClass().getResource("/com/impulsesquare/textures/"+images_name));
                //REDIMENSIONA IMAGENS
                textures.setImage(textures.getImage().getScaledInstance(50, 50, 100));
                //CRIA BLOCOS DE TEXTURA
                Cell texture_block = new Cell(textures, images_name.replace(".png", ""));
                
                texture_block.addMouseListener(new MouseAdapter()
        		{
        			public void mouseClicked(MouseEvent e)
        			{
        				clicktexture(texture_block);
        			}
        		});
                list_texture_blocks.add(texture_block);
                menu_textures.add(texture_block, BorderLayout.NORTH);
                menu_textures.setBorder(new EmptyBorder(1, 5, 10, 10));
		    }
	    }
		getContentPane().add(textures_scroll, BorderLayout.EAST);
		getContentPane().add(malha, BorderLayout.CENTER);
		pack();
    }
	
	private void makeScene() {
		for (int i = 0; i < list_cell.size()-1; i++) {
			System.out.println(list_cell.get(i).getColor() + " " + list_cell.get(i).getTexture());
		}
	}
	
	private void clickcell(Cell cell_clicked) {
		boolean find_texture = false;
		for (int i = 0; i < list_texture_blocks.size(); i++) {
			if (list_texture_blocks.get(i).isSelected()) {
				cell_clicked.setIcon(list_texture_blocks.get(i).getTexture());
				cell_clicked.setTexture(list_texture_blocks.get(i).getTexture());
				cell_clicked.setColor(list_texture_blocks.get(i).getColor());
				find_texture = true;
				return;
			}
		}
		if(!find_texture) JOptionPane.showMessageDialog(null, "Nenhuma textura selecionada");
	}
	
	private void clicktexture(Cell cell_clicked) {
		for (int i = 0; i < list_texture_blocks.size(); i++)
		{
			if (list_texture_blocks.get(i).isSelected())
			{
				list_texture_blocks.get(i).setSelected(false);
				list_texture_blocks.get(i).setIcon(list_texture_blocks.get(i).getTexture());
			}
		}
		cell_clicked.setIcon(new ImageIcon(getClass().getResource("/com/impulsesquare/images/selected.png")));
		cell_clicked.setSelected(true);
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(SceneBuilder::new);
	}
}