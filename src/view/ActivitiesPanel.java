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

import model.Board;
import model.Path;
import model.Weapon;
import controller.ClientController;
import utils.Utility.Actions;
import utils.Utility.Phases;

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
				Actions[] arr = {};
				option.setModel(new DefaultComboBoxModel(arr));
			}
		});
		phase.setBounds(10, 40, 125, 20);
		add(phase);
		
		option = new JComboBox();
		option.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*if((Actions)option.getSelectedItem() == Actions.MOVE) {
					extraInfo.setVisible(true);
				} else {
					extraInfo.setVisible(false);
				}*/
				switch((Actions)option.getSelectedItem()){
				case MOVE:
					ArrayList<Path> connections = control.model.getPlayer().getLocation().getConnections();
					String [] arr = new String[connections.size()];
					
					for(int i = 0; i < connections.size(); i++){
						arr[i] = control.model.getPlayer().getLocation().parent.getName().toString() + 
								connections.get(i).getDestination(control.model.getPlayer().getLocation()).getClearingNumber();
					}
					extraInfo.setModel(new DefaultComboBoxModel(arr));
					extraInfo.setVisible(true);
					break;
				case ALERT:
					ArrayList<Weapon> weapons = control.model.getPlayer().getWeapons();
					String [] arr2 = new String[weapons.size()];
					for(int i = 0; i < weapons.size(); i++){
						arr2[i] = weapons.get(i).getType().toString() + " " + weapons.get(i).isActive();
					}
					extraInfo.setModel(new DefaultComboBoxModel(arr2));
					extraInfo.setVisible(true);
					break;
				case SEARCH:
					extraInfo.setModel(new DefaultComboBoxModel(SearchTables.values()));
					extraInfo.setVisible(true);
					break;
				case TRADE:
					extraInfo.setVisible(false);
					break;
				case REST:
					extraInfo.setVisible(false);
					break;
				case HIDE:
					extraInfo.setVisible(false);
					break;
				}
			}
		});
		//option.setModel(new DefaultComboBoxModel(Actions.values()));
		option.setBounds(215, 40, 93, 20);
		add(option);
		
		JButton btnRecord = new JButton("Send");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handlePlaySubmit((Phases)phase.getSelectedItem(), (Actions)option.getSelectedItem(), extraInfo.getSelectedItem());
			}
		});
		
		btnRecord.setBounds(220, 145, 89, 23);
		add(btnRecord);
	}

	public void update(){
		//TODO set the phases here?
		/*String[] clearings = new String[95];//had to hardcode the number for this interation
		Board b = control.model.getBoard();

		int count = 0;
		for (int i = 0; i <b.tiles.size(); i++){
			for (int j=0; j < b.tiles.get(i).getClearings().size(); j++){
				clearings[count] = (b.tiles.get(i).getName().toString() + " " + b.tiles.get(i).getClearings().get(j).getClearingNumber());
				count++;
			}
		}

		extraInfo.setModel(new DefaultComboBoxModel(clearings));
		phase.setModel(new DefaultComboBoxModel(clearings));
		
		state = true;*/
		
	}
}
