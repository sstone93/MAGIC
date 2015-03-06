package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import model.Board;
import controller.ClientController;
import utils.Utility.Actions;

@SuppressWarnings({ "rawtypes", "unchecked", "serial"})
public class ActivitiesPanel extends JPanel{
	
	private JComboBox play1Location;
	private JComboBox play2Location;
	private JComboBox play3Location;
	private JComboBox play4Location;
	private ClientController control;
	
	public ActivitiesPanel(ClientController c){
		
		this.control = c;
		
		setBounds(750, 500, 524, 192);
		setBorder(new LineBorder(Color.GRAY));
		setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Actions");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 11, 72, 14);
		add(lblNewLabel_1);

		play1Location = new JComboBox();
		play1Location.setBounds(10, 84, 111, 20);
		add(play1Location);
		
		play2Location = new JComboBox();
		play2Location.setBounds(131, 84, 111, 20);
		add(play2Location);
		
		play3Location = new JComboBox();
		play3Location.setBounds(262, 84, 111, 20);
		add(play3Location);
		
		play4Location = new JComboBox();
		play4Location.setBounds(393, 84, 111, 20);
		add(play4Location);
		
		JComboBox play1 = new JComboBox();
		play1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if((Actions)play1.getSelectedItem() == Actions.MOVE) {
					play1Location.setVisible(true);
				} else {
					play1Location.setVisible(false);
				}
			}
		});
		play1.setModel(new DefaultComboBoxModel(Actions.values()));
		play1.setBounds(10, 41, 93, 20);
		add(play1);
		
		JComboBox play2 = new JComboBox();
		play2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if((Actions)play2.getSelectedItem() == Actions.MOVE) {
					play2Location.setVisible(true);
				} else {
					play2Location.setVisible(false);
				}
			}
		});
		play2.setModel(new DefaultComboBoxModel(Actions.values()));
		play2.setBounds(131, 41, 93, 20);
		add(play2);
		
		JComboBox play3 = new JComboBox();
		play3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if((Actions)play3.getSelectedItem() == Actions.MOVE) {
					play3Location.setVisible(true);
				} else {
					play3Location.setVisible(false);
				}
			}
		});
		play3.setModel(new DefaultComboBoxModel(Actions.values()));
		play3.setBounds(262, 41, 93, 20);
		add(play3);
		
		JComboBox play4 = new JComboBox();
		play4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if((Actions)play4.getSelectedItem() == Actions.MOVE) {
					play4Location.setVisible(true);
				} else {
					play4Location.setVisible(false);
				}
			}
		});
		play4.setModel(new DefaultComboBoxModel(Actions.values()));
		play4.setBounds(393, 41, 93, 20);
		add(play4);
		
		JButton btnRecord = new JButton("Record");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handlePlaysRecorded((Actions)play1.getSelectedItem(), (String)play1Location.getSelectedItem(), 
						(Actions)play2.getSelectedItem(), (String)play2Location.getSelectedItem(), 
						(Actions)play3.getSelectedItem(), (String)play3Location.getSelectedItem(), 
						(Actions)play1.getSelectedItem(), (String)play4Location.getSelectedItem());
			}
		});
		
		btnRecord.setBounds(220, 145, 89, 23);
		add(btnRecord);
	}
	
	public void update(){
		//TODO MOVE THIS INTO ACTIVITIES, DOESN NEED THE VIEW
				String[] clearings = new String[95];//had to hardcode the number for this interation
				Board b = control.model.getBoard();

				int count = 0;
				for (int i = 0; i <b.tiles.size(); i++){
					for (int j=0; j < b.tiles.get(i).getClearings().size(); j++){
						clearings[count] = (b.tiles.get(i).getName().toString() + " " + b.tiles.get(i).getClearings().get(j).getClearingNumber());
						count++;
					}
				}
				
				play1Location.setModel(new DefaultComboBoxModel(clearings));
				play2Location.setModel(new DefaultComboBoxModel(clearings));
				play3Location.setModel(new DefaultComboBoxModel(clearings));
				play4Location.setModel(new DefaultComboBoxModel(clearings));
	}
}
