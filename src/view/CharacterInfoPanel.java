package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class CharacterInfoPanel extends JPanel{
	
	public CharacterInfoPanel(){
		setBounds(819, 0, 455, 500);
		setBorder(new LineBorder(Color.GRAY));
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Character");
		lblNewLabel.setBounds(10, 44, 65, 14);
		add(lblNewLabel);
		
		JLabel lblCharacterInfo = new JLabel("Character Info");
		lblCharacterInfo.setBounds(142, 11, 122, 21);
		lblCharacterInfo.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		add(lblCharacterInfo);
		
		JLabel lblHealth = new JLabel("Health");
		lblHealth.setBounds(10, 120, 46, 14);
		add(lblHealth);
		
		JLabel lblArmour = new JLabel("Armour");
		lblArmour.setBounds(10, 145, 46, 14);
		add(lblArmour);
		
		JLabel lblScore = new JLabel("Location"); // TODO : rename. we actually want location
		lblScore.setBounds(10, 69, 81, 14);
		add(lblScore);
		
		JTextArea armourText = new JTextArea();
		armourText.setEditable(false);
		armourText.setLineWrap(true);
		armourText.setBounds(10, 164, 177, 142);
		add(armourText);
		
		JTextField characterText = new JTextField();
		characterText.setBounds(101, 41, 86, 20);
		add(characterText);
		characterText.setColumns(10);
		characterText.setEditable(false);
		
		JTextField vpText = new JTextField();
		vpText.setBounds(101, 66, 86, 20);
		add(vpText);
		vpText.setColumns(10);
		vpText.setEditable(false);
		
		JTextField healthText = new JTextField();
		healthText.setBounds(101, 117, 86, 20);
		add(healthText);
		healthText.setColumns(10);
		healthText.setEditable(false);
		
		JLabel lblTreasures = new JLabel("Treasures");
		lblTreasures.setBounds(10, 317, 65, 14);
		add(lblTreasures);
		
		JTextArea treasuresText = new JTextArea();
		treasuresText.setEditable(false);
		treasuresText.setLineWrap(true);
		treasuresText.setBounds(10, 342, 177, 142);
		add(treasuresText);
		
		JLabel lblGold = new JLabel("Gold");
		lblGold.setBounds(10, 94, 46, 14);
		add(lblGold);
		
		JTextField goldText = new JTextField();
		goldText.setBounds(101, 91, 86, 20);
		add(goldText);
		goldText.setColumns(10);
		goldText.setEditable(false);
		
		JLabel lblFatigue = new JLabel("Fatigue");
		lblFatigue.setBounds(224, 47, 65, 14);
		add(lblFatigue);
		
		JTextField fatigueText = new JTextField();
		fatigueText.setColumns(10);
		fatigueText.setBounds(315, 41, 86, 20);
		fatigueText.setEditable(false);
		add(fatigueText);
		
		JLabel lblFame = new JLabel("Fame");
		lblFame.setBounds(224, 72, 71, 14);
		add(lblFame);
		
		JTextField fameText = new JTextField();
		fameText.setColumns(10);
		fameText.setBounds(315, 66, 86, 20);
		fameText.setEditable(false);
		add(fameText);
		
		JLabel lblNotoriety = new JLabel("Notoriety");
		lblNotoriety.setBounds(224, 97, 65, 14);
		add(lblNotoriety);
		
		JTextField notorietyText = new JTextField();
		notorietyText.setColumns(10);
		notorietyText.setBounds(315, 91, 86, 20);
		notorietyText.setEditable(false);
		add(notorietyText);
		
		JTextField hiddenText = new JTextField();
		hiddenText.setColumns(10);
		hiddenText.setBounds(315, 117, 86, 20);
		hiddenText.setEditable(false);
		add(hiddenText);
		
		JLabel lblHidden = new JLabel("Hidden");
		lblHidden.setBounds(224, 123, 46, 14);
		add(lblHidden);
		
		JLabel lblWeapons = new JLabel("Weapons");
		lblWeapons.setBounds(224, 148, 65, 14);
		add(lblWeapons);
		
		JTextArea weaponsText = new JTextArea();
		weaponsText.setEditable(false);
		weaponsText.setLineWrap(true);
		weaponsText.setBounds(224, 167, 177, 142);
		add(weaponsText);
		
		JLabel lblChits = new JLabel("Chits");
		lblChits.setBounds(224, 317, 65, 14);
		add(lblChits);
		
		JTextArea chitsText = new JTextArea();
		chitsText.setEditable(false);
		chitsText.setLineWrap(true);
		chitsText.setBounds(224, 342, 177, 142);
		add(chitsText);
		
	}
}
