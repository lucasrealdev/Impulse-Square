package com.impulsesquare.scenes;

import com.impulsesquare.objects.Button;

import com.impulsesquare.objects.Cell;
import com.impulsesquare.objects.CustomJPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class SceneBuilder extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH_CELL = 45;
	private static final int HEIGHT_CELL = 45;
	private static final int WIDTH_SCREEN = 800;
	private static final int HEIGHT_SCREEN = 500;
	private static final int NUM_COLUNMS = WIDTH_SCREEN / WIDTH_CELL;
	private static final int NUM_ROWS = HEIGHT_SCREEN / HEIGHT_CELL;

	// CRIA LISTA COM AS TEXTURAS QUE ESTAO NO PACOTE
	private File directory = new File(getClass().getResource("/com/impulsesquare/textures").getFile());
	private File[] files = directory.listFiles();

	// CRIA LISTA DE CELULAS
	private ArrayList<Cell> list_cell = new ArrayList<>();

	// CRIA LISTA DE BLOCOS DE TEXTURA
	private ArrayList<Cell> list_texture_blocks = new ArrayList<>();

	private ImageIcon selected_img = new ImageIcon(getClass().getResource("/com/impulsesquare/images/selected.png"));
	private ImageIcon rotate_img = new ImageIcon(getClass().getResource("/com/impulsesquare/images/rotate.png"));
	private ImageIcon export_img = new ImageIcon(getClass().getResource("/com/impulsesquare/images/export.png"));
	private ImageIcon eraser_img = new ImageIcon(getClass().getResource("/com/impulsesquare/images/eraser.png"));
	private ImageIcon transparent_img = new ImageIcon(
			getClass().getResource("/com/impulsesquare/images/transparent.png"));
	private ImageIcon transparent_bg = new ImageIcon(
			getClass().getResource("/com/impulsesquare/images/background-white.png"));

	// BACKGROUND
	private ImageIcon background_blue = new ImageIcon(
			getClass().getResource("/com/impulsesquare/images/background-blue.jpg"));
	private ImageIcon background_red = new ImageIcon(
			getClass().getResource("/com/impulsesquare/images/background-red.jpg"));
	private ImageIcon background_green = new ImageIcon(
			getClass().getResource("/com/impulsesquare/images/background-green.jpg"));
	private ImageIcon background_yellow = new ImageIcon(
			getClass().getResource("/com/impulsesquare/images/background-yellow.jpg"));

	// BACKGROUND BUTTONS
	private Button background_blue_btn = new Button(background_blue);
	private Button background_red_btn = new Button(background_red);
	private Button background_green_btn = new Button(background_green);
	private Button background_yellow_btn = new Button(background_yellow);
	private JLabel background_transparent = new JLabel("Sem Fundo");

	// CRIA BOTAO DE APAGAR
	private Button eraser_btn = new Button(eraser_img);
	private boolean isEraser = false;

	// CRIA BOTAO DE ROTACIONAR IMAGEM
	private Button rotate_btn = new Button(rotate_img);

	// CRIA BOTAO DE EXPORTAR SCENA
	private Button export_scene_btn = new Button(export_img);

	// GUARDA A ULTIMA CELULA SELECIONADA
	private Cell last_texture = null;

	// GUARDA SE O MOUSE ESTA PRESSIONADO
	private boolean MousePressed = false;

	// CRIA MALHA
	private CustomJPanel malha = new CustomJPanel(new GridLayout(NUM_ROWS, NUM_COLUNMS));

	private ArrayList<Cell> selection;

	public SceneBuilder() {
		setTitle("Criador de Cenas");
		setSize(new Dimension(WIDTH_SCREEN, HEIGHT_SCREEN));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		makebuilder();
		new Thread(this).start();
	}

	// FUNCAO PARA CONSTRUIR A TELA E OS COMPONENTES
	private void makebuilder() {
		// CRIA A BARRA DE MENU
		JMenuBar menuBar = new JMenuBar();

		// CRIA O MENU ARQUIVO E AJUDA
		JMenu arqmenu = new JMenu("Arquivo");
		JMenu helpmenu = new JMenu("Ajuda");

		// CRIA OS ITEMS DO MENU
		JMenuItem newItem = new JMenuItem("Novo");
		JMenuItem openItem = new JMenuItem("Abrir");
		JMenuItem instItem = new JMenuItem("Instruções");

		// ADICIONA OS ITEMS
		arqmenu.add(newItem);
		arqmenu.add(openItem);
		
		helpmenu.add(instItem);

		menuBar.add(arqmenu);
		menuBar.add(helpmenu);

		setJMenuBar(menuBar);

		// BOTAO DE INSTRUCOES
		instItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Esse é o construtor de fases para o jogo\n"
						+ "selecione as texturas na malha para posicionar os blocos\n"
						+ "Quando quiser salvar o arquivo role para baixo e clique na ultima opção\n"
						+ "Se quiser abrir um arquivo já feito clique em arquivo e abrir no menu"
						+ "Para editar uma cena ja feita crie ou edite outra e dê o mesmo nome",
						"Instruções", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// ZERA AS CELULAS SE CLICAR EM NOVO
		newItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < list_cell.size(); i++) {
					list_cell.get(i).setTexture(transparent_img);
					list_cell.get(i).setIcon(transparent_img);
					list_cell.get(i).setColor("");
				}
			}
		});

		// ABRE UM MAPA JA FEITO ANTERIORMENTE
		openItem.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				// CRIA O FILE CHOOSER
				JFileChooser fileChooser = new JFileChooser();

				// EXIBE O DIALOGO DE SELECAO DO ARQUIVO
				int resultado = fileChooser.showOpenDialog(getContentPane());

				// VERIFICA SE O USUARIO SELECIONOU ALGUM ARQUIVO
				if (resultado == JFileChooser.APPROVE_OPTION) {
					// OBTEM O ARQUIVO SELECIONADO
					java.io.File selectedMap = fileChooser.getSelectedFile();
					if (!selectedMap.toString().contains(".dat")) {
						JOptionPane.showMessageDialog(null, "Você deve escolher um arquivo .dat para abrir", "Erro",
								JOptionPane.ERROR_MESSAGE);
					} else {
						// GUARDA O MAPA ESCOLHIDO EM UMA LISTA
						try {
							FileInputStream fis = new FileInputStream(selectedMap);
							ObjectInputStream ois = new ObjectInputStream(fis);
							selection = (ArrayList<Cell>) ois.readObject();
							ois.close();
							fis.close();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage());
						}
						for (int i = 0; i < list_cell.size(); i++) {
							list_cell.get(i).setColor(selection.get(i).getColor());
							list_cell.get(i).setTexture(selection.get(i).getTexture());
							list_cell.get(i).setIcon(selection.get(i).getTexture());
							malha.setBackground(selection.get(list_cell.size()).getTexture());
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado", "Aviso",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		// LAYOUT DO MENU
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		gbc.weighty = 1.0;

		// CRIA MENU DE TEXTURAS
		JPanel menu_textures = new JPanel(new GridLayout(0, 2, 4, 4));
		// CRIA PAINEL QUE GUARDA TEXTURAS
		JPanel leftPane = new JPanel(new GridBagLayout());
		leftPane.add(menu_textures, gbc);

		// CONFIGURA SCROLL DO MENU DE TEXTURAS
		JScrollPane textures_scroll = new JScrollPane(leftPane);
		textures_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		textures_scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textures_scroll.setPreferredSize(new Dimension(150, HEIGHT_SCREEN));

		// CONFIGURA BOTAO DE ROTACIONAR TEXTURA
		rotate_btn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (last_texture != null) {
					ImageIcon resized = rotate_texture(last_texture.getTexture());
					last_texture.setTexture(resized);
					last_texture.setIcon(resized);
				}
			}
		});

		// CONFIGURAS BOTAO DE EXPORTAR MAPA
		export_scene_btn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				makeScene();
			}
		});

		// CONFIGURA BOTAO DE APAGAR TEXTURA
		eraser_btn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				isEraser = true;
				eraser_btn.setBorder(BorderFactory.createLineBorder(Color.green, 1));
				if (last_texture != null)
					last_texture.setIcon(last_texture.getTexture());
			}
		});
		// CRIA BOTOES DE TROCAR FUNDO
		background_blue_btn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				malha.setBackground(background_blue);
				malha.repaint();
			}
		});
		background_red_btn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				malha.setBackground(background_red);
				malha.repaint();
			}
		});
		background_green_btn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				malha.setBackground(background_green);
				malha.repaint();
			}
		});
		background_yellow_btn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				malha.setBackground(background_yellow);
				malha.repaint();
			}
		});
		background_transparent.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				malha.setBackground(transparent_bg);
				malha.repaint();
			}
		});
		// CONFIGURA BOTAO DE FUNDO TRANSPARENT
		background_transparent.setPreferredSize(new Dimension(50, 50));
		background_transparent.setFont(new Font("Serif", Font.PLAIN, 10));
		background_transparent.setBorder(BorderFactory.createLineBorder(Color.gray));

		// CRIAR CELULAS DA MALHA
		for (int i = 0; i < NUM_ROWS * NUM_COLUNMS; i++) {
			Cell cell = new Cell();
			cell.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					clickcell(cell);
				}

				public void mouseEntered(MouseEvent e) {
					if (MousePressed) {
						clickcell(cell);
					}
				}
			});
			list_cell.add(cell);
			malha.add(cell);
		}

		// PEGAR AS TEXTURAS DO PACOTE
		leftPane.setBackground(Color.lightGray);
		menu_textures.setBackground(Color.lightGray);
		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".png")) {
				// PEGA O NOME DAS IMAGENS RESGATADAS
				String images_name = file.getName();
				// CRIA IMAGEM
				ImageIcon textures = new ImageIcon(
						getClass().getResource("/com/impulsesquare/textures/" + images_name));
				// REDIMENSIONA IMAGENS
				textures.setImage(textures.getImage().getScaledInstance(WIDTH_CELL, HEIGHT_CELL, Image.SCALE_SMOOTH));
				// CRIA BLOCOS DE TEXTURA
				Cell texture_block = new Cell(textures, images_name.replace(".png", ""));

				texture_block.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						clicktexture(texture_block);
					}
				});
				list_texture_blocks.add(texture_block);
				menu_textures.add(texture_block, BorderLayout.NORTH);
			}
		}
		leftPane.setBorder(new EmptyBorder(5, 0, 5, 0));
		menu_textures.add(background_blue_btn);
		menu_textures.add(background_green_btn);
		menu_textures.add(background_red_btn);
		menu_textures.add(background_yellow_btn);
		menu_textures.add(background_transparent);
		menu_textures.add(eraser_btn);
		menu_textures.add(rotate_btn);
		menu_textures.add(export_scene_btn);

		getContentPane().add(textures_scroll, BorderLayout.EAST);
		getContentPane().add(malha, BorderLayout.CENTER);
		pack();
	}

	// SALVA A LISTA DE CELULAS EM UM ARQUIVO
	private void makeScene() {
		String name_scene = JOptionPane
				.showInputDialog("Escreva o nome do mapa a ser gerado, Sem espaço ou pontuação!");
		if (name_scene != null && !name_scene.isEmpty()) {
			name_scene.replace(" ", "");
			name_scene.concat(".dat");
			try {
				FileOutputStream fos = new FileOutputStream(name_scene + ".dat");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				Cell fundo = new Cell();
				fundo.setTexture(malha.getBackgroundImage());
				list_cell.add(fundo);
				oos.writeObject(list_cell);
				oos.close();
				fos.close();
				JOptionPane.showMessageDialog(null, "Mapa salvo com sucesso!");
				dispose();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// FUNCAO DE QUANDO CLICA NA CELULA
	private void clickcell(Cell cell_clicked) {
		if (isEraser) {
			cell_clicked.setIcon(transparent_img);
			cell_clicked.setTexture(transparent_img);
			return;
		}
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
		if (!find_texture)
			JOptionPane.showMessageDialog(null, "Nenhuma textura selecionada");
	}

	// FUNCAO DE QUANDO CLICA NA TEXTURA
	private void clicktexture(Cell cell_clicked) {
		for (int i = 0; i < list_texture_blocks.size(); i++) {
			if (list_texture_blocks.get(i).isSelected()) {
				list_texture_blocks.get(i).setSelected(false);
				list_texture_blocks.get(i).setIcon(list_texture_blocks.get(i).getTexture());
			}
		}
		cell_clicked.setIcon(selected_img);
		cell_clicked.setSelected(true);
		last_texture = cell_clicked;
		isEraser = false;
		eraser_btn.setBorder(rotate_btn.getBorder());
	}

	// FUNCAO DE ROTACIONAR IMAGEM
	public ImageIcon rotate_texture(ImageIcon last_texture) {
		Image image = last_texture.getImage();
		BufferedImage rotated = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = rotated.createGraphics();
		g2d.rotate(Math.toRadians(90), image.getWidth(null) / 2, image.getHeight(null) / 2);
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return new ImageIcon(rotated);
	}

	// ABRE UMA THREAD PARA DETECTAR SE PRESSIONOU O MOUSE E ARRASTOU
	public void run() {
		for (int i = 0; i < list_cell.size(); i++) {
			list_cell.get(i).addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					MousePressed = true;
				}

				public void mouseReleased(MouseEvent e) {
					MousePressed = false;
				}
			});
		}
	}
}