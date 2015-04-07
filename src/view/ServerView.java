package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import controller.ServerController;

@SuppressWarnings({"serial", "rawtypes", "unchecked" })
public class ServerView extends JFrame implements ActionListener{
	
	JButton stop = new JButton();
	JButton select = new JButton("SELECT");
	JButton start = new JButton("START GAME");
	JComboBox rolls = new JComboBox();
	ServerController control;
	
	//ChitButtonGrid buttons;

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
		
		if(!c.cheatmode){
			setSize(new Dimension(300, 140));
		}else{
			setSize(new Dimension(300, 140));
		
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
			
			//Causes network shutdown by clicking the close button on the window
			this.addWindowListener( new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent we) {
	                System.out.println("Window Was Closed: Triggering Shutdown");
	                control.network.stop();
	                System.exit(0);
	            }
	        } );	
		}
		
		start.setBounds(110,60,130,20);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.network.ready = true;
				start.setEnabled(false);
				try {
					control.network.server.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		add(start);
		
		
	}

	//STAHPS THE SERVER
	public void actionPerformed(ActionEvent e) {
		if (this.stop == e.getSource()){
			control.network.stop();
			System.exit(0);
		}
	}
}
