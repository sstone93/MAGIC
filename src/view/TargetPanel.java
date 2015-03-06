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
import controller.ClientController;
import utils.Utility.CharacterName;

@SuppressWarnings({"rawtypes", "serial", "unchecked"})
public class TargetPanel extends JPanel{
	
	JComboBox target;
	
	public TargetPanel(ClientController c){
		
		ClientController control = c;
		
		setVisible(false);
		setBounds(750, 500, 524, 192);
		setLayout(null);
		setBorder(new LineBorder(Color.GRAY));
		
		JLabel lblSelectCombatTarget = new JLabel("Select Combat Target");
		lblSelectCombatTarget.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblSelectCombatTarget.setBounds(10, 11, 154, 14);
		add(lblSelectCombatTarget);
		
		target = new JComboBox();
		target.setBounds(10, 41, 93, 20);
		add(target);
		
		JButton btnSelectTarget = new JButton("Select");
		btnSelectTarget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handleTargetSelection((CharacterName)target.getSelectedItem());
			}
		});

		btnSelectTarget.setBounds(422, 40, 89, 23);
		add(btnSelectTarget);
		
	}

	public void update(Player p){
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
