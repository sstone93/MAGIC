package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import model.Monster;
import model.Player;
import controller.ClientController;
import utils.Utility.CharacterName;
import utils.Utility.MonsterName;

@SuppressWarnings({"rawtypes", "serial", "unchecked"})
public class TargetPanel extends JPanel{
	
	JList playerTargets;
	JList monsterTargets;
	ClientController control;
	
	public TargetPanel(ClientController c){
		
		control = c;
		
		setBounds(750, 500, 524, 192);
		setLayout(null);
		setBorder(new LineBorder(Color.GRAY));
		
		JLabel lblSelectCombatTarget = new JLabel("Select Combat Targets");
		lblSelectCombatTarget.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblSelectCombatTarget.setBounds(10, 11, 154, 14);
		add(lblSelectCombatTarget);
		
		playerTargets = new JList();
		playerTargets.setBounds(10, 36, 154, 140);
		add(playerTargets);
		
		monsterTargets = new JList();
		monsterTargets.setBounds(195, 36, 154, 140);
		add(monsterTargets);
		
		JButton btnSelectTarget = new JButton("Select");
		btnSelectTarget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<CharacterName> chars = new ArrayList<CharacterName>();
				chars.addAll(playerTargets.getSelectedValuesList());
				ArrayList<MonsterName> monsters = new ArrayList<MonsterName>();
				monsters.addAll(monsterTargets.getSelectedValuesList());
				control.handleTargetSelection(chars, monsters);
			}
		});

		btnSelectTarget.setBounds(422, 40, 89, 23);
		add(btnSelectTarget);
		
	}

	public void update(){
		if (control.model.getPlayer() != null) {
			ArrayList<Player> others = control.model.getPlayer().getLocation().getOccupants();
			DefaultListModel p = new DefaultListModel();
			for (int i = 0; i < others.size(); i++){
				p.addElement(others.get(i).getCharacter().getName());
			}
			playerTargets.setModel(p);
			
			ArrayList<Monster> monsters = control.model.getPlayer().getLocation().getMonsters();
			DefaultListModel m = new DefaultListModel();
			for (int i = 0; i < monsters.size(); i++){
				m.addElement(monsters.get(i).getName());
			}
			monsterTargets.setModel(m);
		}
	}
}
