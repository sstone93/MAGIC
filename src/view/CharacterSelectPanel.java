package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import utils.Utility;
import utils.Utility.Actions;
import utils.Utility.CharacterName;
import utils.Utility.GarrisonName;
import utils.Utility.PhaseType;
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
				if(control.view != null)
					control.view.getCharacterDetailsPanel().updatePic((CharacterName)character.getSelectedItem());
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
	
	
}
