package com.impulsesquare.objects;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class TextureBlock extends JLabel{
	private static final long serialVersionUID = 1L;
	public TextureBlock(ImageIcon texture) {
		setIcon(texture);
	}

	public void addeventclick() {
		addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				
			}
		});
	}
}
