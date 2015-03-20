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

import model.Path;
import model.Phase;
import model.Weapon;
import controller.ClientController;
import utils.Utility.Actions;
import utils.Utility.PhaseType;
import utils.Utility.SearchTables;

@SuppressWarnings({ "rawtypes", "unchecked", "serial"})
public class ActivitiesPanel extends JPanel{
	
	private JComboBox extraInfo;
	private JComboBox phase;
	private JComboBox option;
	private ClientController control;
	public boolean state = false;
	
	public ActivitiesPanel(ClientController c){
		
		this.control = c;
		
		setBounds(750, 500, 524, 192);
		setBorder(new LineBorder(Color.GRAY));
		setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Actions");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 11, 72, 14);
		add(lblNewLabel_1);

		extraInfo = new JComboBox();
		extraInfo.setBounds(389, 40, 125, 20);
		add(extraInfo);
		
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
						//TODO
						arr = new Actions[0];
						break;
					case SUNLIGHT:
						arr = Actions.values();
						break;
					case TREASURE:
						arr = new Actions[1];
						arr[0] = control.model.getPlayer().getPhases().get(phase.getSelectedIndex()).getAction();
						//System.out.println(phase.getSelectedIndex());
						//TODO
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
		
		option = new JComboBox();
		option.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] arr = {};
				
				switch((Actions)option.getSelectedItem()){
					case MOVE:
						ArrayList<Path> connections = control.model.getPlayer().getLocation().getConnections();
						arr = new String[connections.size()];
						
						for(int i = 0; i < connections.size(); i++){
							arr[i] = connections.get(i).getDestination(control.model.getPlayer().getLocation()).parent.getName().toString() +" "+ 
									connections.get(i).getDestination(control.model.getPlayer().getLocation()).getClearingNumber();
						}
						extraInfo.setVisible(true);
						
						break;
					case ALERT:
						ArrayList<Weapon> weapons = control.model.getPlayer().getWeapons();
						arr = new String[weapons.size()];
						for(int i = 0; i < weapons.size(); i++){
							arr[i] = weapons.get(i).getType().toString() + " " + weapons.get(i).isActive();
						}
						extraInfo.setVisible(true);
						break;
					case SEARCH:
						arr = SearchTables.values();
						extraInfo.setVisible(true);
						break;
					case TRADE:
					case REST:
					case HIDE:
					case PASS:
						extraInfo.setVisible(false);
						break;
					default:
						break;
				}
				extraInfo.setModel(new DefaultComboBoxModel(arr));
				if(arr.length > 0){
					extraInfo.setSelectedIndex(0);
				}
			}
		});
		
		option.setBounds(215, 40, 93, 20);
		add(option);
		
		JButton btnRecord = new JButton("Send");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handlePlaySubmit((PhaseType)phase.getSelectedItem(), (Actions)option.getSelectedItem(), extraInfo.getSelectedItem());
			}
		});
		
		btnRecord.setBounds(220, 145, 89, 23);
		add(btnRecord);
	}

	public void update(){
		//TODO set the phases here?
		ArrayList<Phase> phases = this.control.model.getPlayer().getPhases();
		PhaseType[] p = new PhaseType[phases.size()];
		for(int i=0; i<phases.size(); i++){
			p[i] = (phases.get(i).getType());
		}
		phase.setModel(new DefaultComboBoxModel(p));
		if(phases.size() > 0){
			phase.setSelectedIndex(0);
		}
			
	}
}
