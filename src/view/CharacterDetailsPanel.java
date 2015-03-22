package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Utility;
import utils.Utility.CharacterName;
import controller.ClientController;

@SuppressWarnings("serial")
public class CharacterDetailsPanel extends JPanel{
	ClientController control;
	JLabel picture;
	public CharacterDetailsPanel(ClientController c){
		this.control = c;
		
		setPreferredSize(new Dimension(800, 480));
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		picture = new JLabel();

		picture.setSize(610, 480);
		add(picture);
	}
	
	public void updatePic(CharacterName n){
		try{
			BufferedImage pic;
			pic = ImageIO.read(this.getClass().getResource(Utility.getCharacterDetailImage(n)));
			picture.setIcon(new ImageIcon(pic));
		} catch (IOException e){	
		}
	}
}
