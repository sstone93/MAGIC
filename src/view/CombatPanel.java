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

import controller.ClientController;
import utils.Utility.Attacks;
import utils.Utility.Defenses;
import utils.Utility.Maneuvers;

@SuppressWarnings("serial")
public class CombatPanel extends JPanel{
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CombatPanel(ClientController c){
		
		ClientController control = c;

		setBounds(750, 500, 524, 192);
		setLayout(null);
		setBorder(new LineBorder(Color.GRAY));
		
		JLabel lblSelectCombatActions = new JLabel("Select Combat Actions");
		lblSelectCombatActions.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblSelectCombatActions.setBounds(10, 11, 154, 14);
		add(lblSelectCombatActions);
		
		JComboBox attack = new JComboBox();
		attack.setModel(new DefaultComboBoxModel(Attacks.values()));
		attack.setBounds(10, 41, 93, 20);
		add(attack);
		
		JComboBox maneuvers = new JComboBox();
		maneuvers.setModel(new DefaultComboBoxModel(Maneuvers.values()));
		maneuvers.setBounds(113, 41, 93, 20);
		add(maneuvers);
		
		JComboBox defense = new JComboBox();
		defense.setModel(new DefaultComboBoxModel(Defenses.values()));
		defense.setBounds(216, 41, 93, 20);
		add(defense);
		
		JComboBox attackFatigue = new JComboBox();
		attackFatigue.setModel(new DefaultComboBoxModel(new Integer[] {0, 1, 2}));
		attackFatigue.setBounds(10, 70, 50, 20);
		add(attackFatigue);
		
		JComboBox maneuversFatigue = new JComboBox();
		maneuversFatigue.setModel(new DefaultComboBoxModel(new Integer[] {0, 1, 2}));
		maneuversFatigue.setBounds(113, 70, 50, 20);
		add(maneuversFatigue);
		
		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handleCombatMoves((Attacks)attack.getSelectedItem(), (Defenses)defense.getSelectedItem(), 
						(Maneuvers)maneuvers.getSelectedItem(), (int)attackFatigue.getSelectedItem(), (int)maneuversFatigue.getSelectedItem());
			}
		});
		
		btnSelect.setBounds(422, 40, 89, 23);
		add(btnSelect);
	}
}
