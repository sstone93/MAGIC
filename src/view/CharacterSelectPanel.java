package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import utils.Utility;
import utils.Utility.CharacterName;
import utils.Utility.GarrisonName;
import controller.ClientController;

@SuppressWarnings("serial")
public class CharacterSelectPanel extends JPanel{
	ClientController control;
	@SuppressWarnings("rawtypes")
	JComboBox character;
	@SuppressWarnings("rawtypes")
	JComboBox starting;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CharacterSelectPanel(ClientController c){
		this.control = c;
		
		setBounds(750, 500, 524, 192);
		setBorder(new LineBorder(Color.GRAY));
		setLayout(null);
		
		starting = new JComboBox();
		starting.setBounds(309, 40, 93, 20);
		add(starting);
		
		character = new JComboBox();
		character.setModel(new DefaultComboBoxModel(CharacterName.values()));
		character.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				starting.setModel(new DefaultComboBoxModel(
						Utility.getCharacterStartingLocations((CharacterName)character.getSelectedItem())));
						update();
			}
		});
		
		character.setSelectedIndex(0);
		character.setBounds(137, 40, 93, 20);
		add(character);
		
		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handleCharacterSelection((CharacterName)character.getSelectedItem(), 
						(GarrisonName)starting.getSelectedItem());
			}
		});
		
		btnSelect.setBounds(220, 145, 89, 23);
		add(btnSelect);
	}
	
	public void update(){
		if(control.view != null)
			control.view.getCharacterDetailsPanel().updatePic((CharacterName)character.getSelectedItem());
	}
	
}
