package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import utils.Config;
import controller.ServerController;

@SuppressWarnings({"serial", "rawtypes", "unchecked" })
public class ServerView extends JFrame implements ActionListener{
	
	JButton stop = new JButton();
	JButton select = new JButton("SELECT");
	JComboBox rolls = new JComboBox();
	ServerController control;

	public ServerView(ServerController c) {
		
		this.control = c;
		setLayout(null);
		
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
			setSize(new Dimension(500, 130));
		
			rolls.setBounds(110,0,140,20);
			add(rolls);
			
			Object[] arr = {0,1,2,3,4,5,6};
			rolls.setModel(new DefaultComboBoxModel(arr));
			rolls.setSelectedIndex(0);
			
			select.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					control.nextRoll = rolls.getSelectedIndex();
					System.out.println(control.nextRoll);
				}
			});

			select.setBounds(110,30,130,20);
			add(select);	
			
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
