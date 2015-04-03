package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import model.Player;
import utils.Utility.CharacterName;
import controller.ClientController;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class BlockPanel extends JPanel{
	
	ClientController control;
	JButton send;
	JComboBox target;
	
	public BlockPanel(ClientController c){
		
		this.control = c;
		
		setBounds(600, 0, 750, 150);
		setBorder(new LineBorder(Color.GRAY));
		setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("block someone");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(0, 0, 50, 20);
		add(lblNewLabel_1);

		target = new JComboBox();
		target.setBounds(0, 30, 100, 40);
		add(target);

		send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handleBlockSubmit((Player)target.getSelectedItem());
			}
		});
		
		send.setBounds(0, 60, 30, 90);
		add(send);
	}
	
	//called when state is true... no...
	public void update(){
		Player p = control.model.getPlayer();
		if (p != null) {
			ArrayList<Player> others = p.getLocation().getOccupants();
			if (others != null) {
				CharacterName[] targets = new CharacterName[others.size()];
				for (int i = 0; i < others.size(); i++){
					if (others.get(i) != null)
						targets[i] = others.get(i).getCharacter().getName();
				}

				target.setModel(new DefaultComboBoxModel(targets));
			}
		}
	}

}
