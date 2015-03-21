package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Utility.CharacterName;
import controller.ClientController;

@SuppressWarnings("serial")
public class CharacterDetailsPanel extends JPanel{
	ClientController control;
	JLabel picture;
	public CharacterDetailsPanel(ClientController c){
		this.control = c;
		
		setPreferredSize(new Dimension(800, 1200));
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		picture = new JLabel();

		picture.setSize(360, 284);
		add(picture);
		/*for(CharacterName n: CharacterName.values()){
			createClassButton(n);
		}*/
	}
	
	/*private void createClassButton(CharacterName n){
		try{
			BufferedImage pic;
			pic = ImageIO.read(this.getClass().getResource("/images/"+n.toString().toLowerCase()+"Detail.jpg"));
			JButton btn = new JButton(new ImageIcon(pic));
			btn.setSize(360, 284);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					control.handleCharacterSelection(n);
				}
			});
			add(btn);
		} catch (IOException e){	
		}
	}*/
	
	public void updatePic(CharacterName n){
		try{
			BufferedImage pic;
			pic = ImageIO.read(this.getClass().getResource("/images/"+n.toString().toLowerCase()+"Detail.jpg"));
			picture.setIcon(new ImageIcon(pic));
		} catch (IOException e){	
		}
	}
}