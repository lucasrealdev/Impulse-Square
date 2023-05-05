package com.impulsesquare.scenes;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import javax.swing.JOptionPane;

import com.impulsesquare.objects.Cell;

public class Level1 {
	
	public Level1() {
		
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		List<Cell> map = null;
		try
		{
            FileInputStream fis = new FileInputStream("mapa1.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (List<Cell>) ois.readObject();
            ois.close();
            fis.close();
		}
		catch (Exception ex)
		{
            JOptionPane.showMessageDialog(null, ex.getMessage()); 
        }
	}
}
 