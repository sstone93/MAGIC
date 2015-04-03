package view;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import controller.ClientController;
import model.Armour;
import model.Player;
import model.Treasure;
import model.Weapon;

@SuppressWarnings("serial")
public class CharacterInfoPanel extends JPanel{
	
	private JTextField characterText;
	private JTextField vpText;
	private JTextField healthText;
	private JTextField goldText;
	private JTextField fatigueText;
	private JTextField fameText;
	private JTextField notorietyText;
	private JTextField hiddenText;
	private JTextArea armourText;
	private JTextArea treasuresText;
	private JTextArea weaponsText;
	private JTextArea chitsText;
	private ClientController control;
	private JTextField blockedText;
	private JTextField deadText;
	
	public CharacterInfoPanel(ClientController c){
		this.control = c;
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
		lblArmour.setBounds(10, 171, 46, 14);
		add(lblArmour);
		
		JLabel lblScore = new JLabel("Location"); // TODO : rename. we actually want location
		lblScore.setBounds(10, 69, 81, 14);
		add(lblScore);
		
		this.armourText = new JTextArea();
		armourText.setEditable(false);
		armourText.setLineWrap(true);
		armourText.setBounds(10, 188, 177, 142);
		add(armourText);
		
		this.characterText = new JTextField();
		characterText.setBounds(101, 41, 86, 20);
		add(characterText);
		characterText.setColumns(10);
		characterText.setEditable(false);
		
		this.vpText = new JTextField();
		vpText.setBounds(101, 66, 86, 20);
		add(vpText);
		vpText.setColumns(10);
		vpText.setEditable(false);
		
		this.healthText = new JTextField();
		healthText.setBounds(101, 117, 86, 20);
		add(healthText);
		healthText.setColumns(10);
		healthText.setEditable(false);
		
		JLabel lblTreasures = new JLabel("Treasures");
		lblTreasures.setBounds(10, 332, 65, 14);
		add(lblTreasures);
		
		this.treasuresText = new JTextArea();
		treasuresText.setEditable(false);
		treasuresText.setLineWrap(true);
		treasuresText.setBounds(10, 347, 177, 142);
		add(treasuresText);
		
		JLabel lblGold = new JLabel("Gold");
		lblGold.setBounds(10, 94, 46, 14);
		add(lblGold);
		
		this.goldText = new JTextField();
		goldText.setBounds(101, 91, 86, 20);
		add(goldText);
		goldText.setColumns(10);
		goldText.setEditable(false);
		
		JLabel lblFatigue = new JLabel("Fatigue");
		lblFatigue.setBounds(224, 47, 65, 14);
		add(lblFatigue);
		
		this.fatigueText = new JTextField();
		fatigueText.setColumns(10);
		fatigueText.setBounds(315, 41, 86, 20);
		fatigueText.setEditable(false);
		add(fatigueText);
		
		JLabel lblFame = new JLabel("Fame");
		lblFame.setBounds(224, 72, 71, 14);
		add(lblFame);
		
		this.fameText = new JTextField();
		fameText.setColumns(10);
		fameText.setBounds(315, 66, 86, 20);
		fameText.setEditable(false);
		add(fameText);
		
		JLabel lblNotoriety = new JLabel("Notoriety");
		lblNotoriety.setBounds(224, 97, 65, 14);
		add(lblNotoriety);
		
		this.notorietyText = new JTextField();
		notorietyText.setColumns(10);
		notorietyText.setBounds(315, 91, 86, 20);
		notorietyText.setEditable(false);
		add(notorietyText);
		
		this.hiddenText = new JTextField();
		hiddenText.setColumns(10);
		hiddenText.setBounds(315, 117, 86, 20);
		hiddenText.setEditable(false);
		add(hiddenText);
		
		JLabel lblHidden = new JLabel("Hidden");
		lblHidden.setBounds(224, 123, 46, 14);
		add(lblHidden);
		
		JLabel lblWeapons = new JLabel("Weapons");
		lblWeapons.setBounds(224, 171, 65, 14);
		add(lblWeapons);
		
		this.weaponsText = new JTextArea();
		weaponsText.setEditable(false);
		weaponsText.setLineWrap(true);
		weaponsText.setBounds(224, 188, 177, 142);
		add(weaponsText);
		
		JLabel lblChits = new JLabel("Discoveries");
		lblChits.setBounds(224, 332, 86, 14);
		add(lblChits);
		
		this.chitsText = new JTextArea();
		chitsText.setEditable(false);
		chitsText.setLineWrap(true);
		chitsText.setBounds(224, 347, 177, 142);
		add(chitsText);
		
		JLabel lblBlocked = new JLabel("Blocked");
		lblBlocked.setBounds(10, 148, 46, 14);
		add(lblBlocked);
		
		blockedText = new JTextField();
		blockedText.setEditable(false);
		blockedText.setColumns(10);
		blockedText.setBounds(101, 145, 86, 20);
		add(blockedText);
		
		JLabel lblDead = new JLabel("Dead");
		lblDead.setBounds(224, 151, 46, 14);
		add(lblDead);
		
		deadText = new JTextField();
		deadText.setEditable(false);
		deadText.setColumns(10);
		deadText.setBounds(315, 145, 86, 20);
		add(deadText);
	}
	
	public void update(){
		
		Player p = control.model.getPlayer();
		
		characterText.setText(p.getCharacter().getName().toString());
		vpText.setText(p.getLocation().parent.getName().toString() + String.valueOf(p.getLocation().getClearingNumber())); // TODO: change later
		healthText.setText(Integer.toString(p.getHealth()));
		goldText.setText(Integer.toString(p.getGold()));
		fatigueText.setText(Integer.toString(p.getFatigue()));
		fameText.setText(Integer.toString(p.getFame()));
		notorietyText.setText(Integer.toString(p.getNotoriety()));
		hiddenText.setText(String.valueOf(p.isHidden()));
		blockedText.setText(String.valueOf(p.isBlocked()));
		deadText.setText(String.valueOf(p.isDead()));
		
		ArrayList<Armour> armour = p.getArmour();
		String armourS = "";
		if (armour != null) {
			for(int i = 0; i < armour.size(); i++){
				if(armour.get(i) != null) {
					armourS += armour.get(i).getType().toString() + " weight - " + armour.get(i).getWeight().toString() + 
							" damaged - " + String.valueOf(armour.get(i).isDamaged()) + 
							" active - " + String.valueOf(armour.get(i).isActive()) + "\n";
				}
			}
		}
		armourText.setText(armourS);
		
		ArrayList<Treasure> treasures = p.getTreasures();
		String treasuresS = "";
		if (treasures != null) {
			for(int i = 0; i < treasures.size(); i++){
				if ( treasures.get(i) != null) {
					treasuresS += treasures.get(i).getName() + "\n";
				}
			}
		}
		treasuresText.setText(treasuresS);
		
		ArrayList<Weapon> weapons = p.getWeapons();
		String weaponsS = "";
		if (weapons != null) {
			for(int i = 0; i < weapons.size(); i++){
				if (weapons.get(i) != null) {
					weaponsS += weapons.get(i).getType().toString() + " weight - " + weapons.get(i).getWeight().toString()+ 
							" length - " + Integer.toString(weapons.get(i).getLength()) + 
							" speed - " + Integer.toString(weapons.get(i).getSpeed()) + 
							" ranged - " + String.valueOf(weapons.get(i).isRanged()) + 
							" active - " + String.valueOf(weapons.get(i).isActive()) + "\n";
				}
			}
		}
		weaponsText.setText(weaponsS);
		
		String discoveries = "";
		for(int i = 0; i < p.getDiscoveries().size(); i++){
			discoveries += p.getDiscoveries().get(i).toString()+"\n";
		}
		chitsText.setText(discoveries);
	}
}
