package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import utils.Config;
import controller.ServerController;

@SuppressWarnings("serial")
public class ServerView extends JFrame implements ActionListener{
	
	JButton stop = new JButton();
	ServerController control;

	public ServerView(ServerController c) {
		
		this.control = c;
		
		stop.setSize(100,100);
		stop.setSelected(false);
		stop.setBackground(Color.white);
		stop.setEnabled(true);
		
		try {
			stop.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/images/stop.png"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		stop.addActionListener(this);  
		this.add(stop); 
		setResizable(false);
		
		if(!Config.CHEAT_MODE){
			setSize(new Dimension(100, 140));
		}else{
			setSize(new Dimension(500, 300));
		}
	}

	//STAHPS THE SERVER
	public void actionPerformed(ActionEvent e) {
		if (this.stop == e.getSource()){
			control.network.stop();
			System.exit(0);
		}
	}
}
