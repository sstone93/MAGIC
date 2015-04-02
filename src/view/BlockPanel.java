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
import utils.Utility.Actions;
import utils.Utility.PhaseType;
import controller.ClientController;

@SuppressWarnings("serial")
public class BlockPanel extends JPanel{
	
	ClientController control;
	JButton send;
	@SuppressWarnings("rawtypes")
	JComboBox target;
	
	public BlockPanel(ClientController c){
		
this.control = c;
		
		setBounds(700, 0, 750, 50);
		setBorder(new LineBorder(Color.GRAY));
		setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("block someone");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(0, 0, 50, 20);
		add(lblNewLabel_1);

		target = new JComboBox();
		target.setBounds(0, 30, 30, 40);
		add(target);
		
		phase = new JComboBox();
		phase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//somehow get the actions they can perform in that phase.
				//TODO BASED ON THE SELECTED PHASE, GET THE LIST OF ACTIONS
				
				Actions[] arr;
				
				switch((PhaseType)phase.getSelectedItem()){
					case BASIC:
						arr = Actions.values();
						break;
					case SPECIAL:
						arr = new Actions[2];
						arr[0] = control.model.getPlayer().getPhases().get(phase.getSelectedIndex()).getAction();
						arr[1] = Actions.PASS;
						break;
					case SUNLIGHT:
						arr = Actions.values();
						break;
					case TREASURE:
						ArrayList<Actions> actions = control.model.getPlayer().getTreasureActions();
						arr = new Actions[actions.size() + 1];
						for(int i = 0; i < actions.size(); i++){
							arr[i] = actions.get(i);
						}
						arr[actions.size()] = Actions.PASS;
						break;
					default:
						arr = new Actions[0];
						break;
				}
				
				option.setModel(new DefaultComboBoxModel(arr));
				if(arr.length > 0){
					option.setSelectedIndex(0);
				}
			}
		});
		phase.setBounds(10, 40, 125, 20);
		add(phase);
		
		send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handleBlockSubmit((Player)target.getSelectedItem());
			}
		});
		
		send.setBounds(220, 145, 89, 23);
		add(send);
	}
	
	//called when state is true... no...

	change retarded state thing I just did
	
	public void update(){
		
		control.model.getPlayer().getLocation().getOccupants();
		
	}

}
