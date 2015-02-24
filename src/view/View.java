package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import utils.Utility;
import utils.Utility.CharacterName;
import utils.Utility.*;
import controller.ClientController;
import model.Armour;
import model.Board;
import model.Chit;
import model.Clearing;
import model.Player;
import model.Treasure;
import model.Weapon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class View extends JFrame {
	
	private ClientController control;
	private JPanel contentPane;
	private JPanel playsPanel;
	private JPanel combatPanel;
	private JPanel movesPanel;
	private JPanel alertPanel;
	private JPanel characterSelectPanel;
	private JPanel targetPanel;
	private JLayeredPane boardPanel;
	private JScrollPane scrollPanel;
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
	private JTextArea textDisplay;
	private JComboBox play1;
	private JComboBox play2;
	private JComboBox play3;
	private JComboBox play4;
	private JComboBox play1Location;
	private JComboBox play2Location;
	private JComboBox play3Location;
	private JComboBox play4Location;
	private JComboBox attack;
	private JComboBox defense;
	private JComboBox maneuvers;
	private JComboBox attackFatigue;
	private JComboBox maneuversFatigue;
	private JComboBox target;
	private ButtonGroup movesGroup;
	private ButtonGroup alertGroup;
	
	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public View(final ClientController control) {
		this.control = control;
		setResizable(false);
		setSize(new Dimension(1280, 720));
		getContentPane().setLayout(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel CharacterInfoPanel = new JPanel();
		CharacterInfoPanel.setBounds(819, 0, 455, 500);
		CharacterInfoPanel.setBorder(new LineBorder(Color.GRAY));
		contentPane.add(CharacterInfoPanel);
		CharacterInfoPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Character");
		lblNewLabel.setBounds(10, 44, 65, 14);
		CharacterInfoPanel.add(lblNewLabel);
		
		JLabel lblCharacterInfo = new JLabel("Character Info");
		lblCharacterInfo.setBounds(142, 11, 122, 21);
		lblCharacterInfo.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		CharacterInfoPanel.add(lblCharacterInfo);
		
		JLabel lblHealth = new JLabel("Health");
		lblHealth.setBounds(10, 120, 46, 14);
		CharacterInfoPanel.add(lblHealth);
		
		JLabel lblArmour = new JLabel("Armour");
		lblArmour.setBounds(10, 145, 46, 14);
		CharacterInfoPanel.add(lblArmour);
		
		JLabel lblScore = new JLabel("Location"); // TODO : rename. we actually want location
		lblScore.setBounds(10, 69, 81, 14);
		CharacterInfoPanel.add(lblScore);
		
		armourText = new JTextArea();
		armourText.setLineWrap(true);
		armourText.setBounds(10, 164, 177, 142);
		CharacterInfoPanel.add(armourText);
		
		characterText = new JTextField();
		characterText.setBounds(101, 41, 86, 20);
		CharacterInfoPanel.add(characterText);
		characterText.setColumns(10);
		characterText.setEditable(false);
		
		vpText = new JTextField();
		vpText.setBounds(101, 66, 86, 20);
		CharacterInfoPanel.add(vpText);
		vpText.setColumns(10);
		vpText.setEditable(false);
		
		healthText = new JTextField();
		healthText.setBounds(101, 117, 86, 20);
		CharacterInfoPanel.add(healthText);
		healthText.setColumns(10);
		healthText.setEditable(false);
		
		JLabel lblTreasures = new JLabel("Treasures");
		lblTreasures.setBounds(10, 317, 65, 14);
		CharacterInfoPanel.add(lblTreasures);
		
		treasuresText = new JTextArea();
		treasuresText.setLineWrap(true);
		treasuresText.setBounds(10, 342, 177, 142);
		CharacterInfoPanel.add(treasuresText);
		
		JLabel lblGold = new JLabel("Gold");
		lblGold.setBounds(10, 94, 46, 14);
		CharacterInfoPanel.add(lblGold);
		
		goldText = new JTextField();
		goldText.setBounds(101, 91, 86, 20);
		CharacterInfoPanel.add(goldText);
		goldText.setColumns(10);
		goldText.setEditable(false);
		
		JLabel lblFatigue = new JLabel("Fatigue");
		lblFatigue.setBounds(224, 47, 65, 14);
		CharacterInfoPanel.add(lblFatigue);
		
		fatigueText = new JTextField();
		fatigueText.setColumns(10);
		fatigueText.setBounds(315, 41, 86, 20);
		fatigueText.setEditable(false);
		CharacterInfoPanel.add(fatigueText);
		
		JLabel lblFame = new JLabel("Fame");
		lblFame.setBounds(224, 72, 71, 14);
		CharacterInfoPanel.add(lblFame);
		
		fameText = new JTextField();
		fameText.setColumns(10);
		fameText.setBounds(315, 66, 86, 20);
		fameText.setEditable(false);
		CharacterInfoPanel.add(fameText);
		
		JLabel lblNotoriety = new JLabel("Notoriety");
		lblNotoriety.setBounds(224, 97, 65, 14);
		CharacterInfoPanel.add(lblNotoriety);
		
		notorietyText = new JTextField();
		notorietyText.setColumns(10);
		notorietyText.setBounds(315, 91, 86, 20);
		notorietyText.setEditable(false);
		CharacterInfoPanel.add(notorietyText);
		
		hiddenText = new JTextField();
		hiddenText.setColumns(10);
		hiddenText.setBounds(315, 117, 86, 20);
		hiddenText.setEditable(false);
		CharacterInfoPanel.add(hiddenText);
		
		JLabel lblHidden = new JLabel("Hidden");
		lblHidden.setBounds(224, 123, 46, 14);
		CharacterInfoPanel.add(lblHidden);
		
		JLabel lblWeapons = new JLabel("Weapons");
		lblWeapons.setBounds(224, 148, 65, 14);
		CharacterInfoPanel.add(lblWeapons);
		
		weaponsText = new JTextArea();
		weaponsText.setLineWrap(true);
		weaponsText.setBounds(224, 167, 177, 142);
		CharacterInfoPanel.add(weaponsText);
		
		JLabel lblChits = new JLabel("Chits");
		lblChits.setBounds(224, 317, 65, 14);
		CharacterInfoPanel.add(lblChits);
		
		chitsText = new JTextArea();
		chitsText.setLineWrap(true);
		chitsText.setBounds(224, 342, 177, 142);
		CharacterInfoPanel.add(chitsText);
		
		playsPanel = new JPanel();
		playsPanel.setBounds(750, 500, 524, 192);
		playsPanel.setBorder(new LineBorder(Color.GRAY));
		contentPane.add(playsPanel);
		playsPanel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Actions");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 11, 72, 14);
		playsPanel.add(lblNewLabel_1);
		
		play1 = new JComboBox();
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
		playsPanel.add(play1);
		
		play2 = new JComboBox();
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
		playsPanel.add(play2);
		
		play3 = new JComboBox();
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
		playsPanel.add(play3);
		
		play4 = new JComboBox();
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
		playsPanel.add(play4);
		
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
		playsPanel.add(btnRecord);
		
		play1Location = new JComboBox();
		play1Location.setBounds(10, 84, 111, 20);
		playsPanel.add(play1Location);
		
		play2Location = new JComboBox();
		play2Location.setBounds(131, 84, 111, 20);
		playsPanel.add(play2Location);
		
		play3Location = new JComboBox();
		play3Location.setBounds(262, 84, 111, 20);
		playsPanel.add(play3Location);
		
		play4Location = new JComboBox();
		play4Location.setDoubleBuffered(true);
		play4Location.setBounds(393, 84, 111, 20);
		playsPanel.add(play4Location);
		
		textDisplay = new JTextArea();
		textDisplay.setBounds(0, 500, 750, 192);
		textDisplay.setLineWrap(true);
		contentPane.add(textDisplay);
		textDisplay.setEditable(false);
		
		scrollPanel = new JScrollPane();
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.setBounds(0, 0, 819, 500);
		contentPane.add(scrollPanel);
		
		boardPanel = new JLayeredPane();
		boardPanel.setPreferredSize(new Dimension(800, 1018));
		scrollPanel.setViewportView(boardPanel);
		boardPanel.setLayout(null);
		
		characterSelectPanel = new JPanel();
		characterSelectPanel.setPreferredSize(new Dimension(800, 1200));
		characterSelectPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		try{
			BufferedImage pic;
			pic = ImageIO.read(this.getClass().getResource("amazonDetail.jpg"));
			JButton btnAmazon = new JButton(new ImageIcon(pic));
			btnAmazon.setSize(360, 284);
			btnAmazon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					control.handleCharacterSelection(CharacterName.AMAZON);
				}
			});
			characterSelectPanel.add(btnAmazon);
		} catch (IOException e){
			
		}
		
		try{
			BufferedImage pic;
			pic = ImageIO.read(this.getClass().getResource("berserkerDetail.jpg"));
			JButton btnBerserker = new JButton(new ImageIcon(pic));
			btnBerserker.setSize(360, 284);
			btnBerserker.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					control.handleCharacterSelection(CharacterName.BERSERKER);
				}
			});
			characterSelectPanel.add(btnBerserker);
		} catch (IOException e){
			
		}
		
		try{
			BufferedImage pic;
			pic = ImageIO.read(this.getClass().getResource("black_knightDetail.jpg"));
			JButton btnBlack = new JButton(new ImageIcon(pic));
			btnBlack.setSize(360, 284);
			btnBlack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					control.handleCharacterSelection(CharacterName.BLACK_KNIGHT);
				}
			});
			characterSelectPanel.add(btnBlack);
		} catch (IOException e){
			
		}
		
		try{
			BufferedImage pic;
			pic = ImageIO.read(this.getClass().getResource("captainDetail.jpg"));
			JButton btnCaptain = new JButton(new ImageIcon(pic));
			btnCaptain.setSize(360, 284);
			btnCaptain.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					control.handleCharacterSelection(CharacterName.CAPTAIN);
				}
			});
			characterSelectPanel.add(btnCaptain);
		} catch (IOException e){
			
		}
		
		try{
			BufferedImage pic;
			pic = ImageIO.read(this.getClass().getResource("dwarfDetail.jpg"));
			JButton btnDwarf = new JButton(new ImageIcon(pic));
			btnDwarf.setSize(360, 284);
			btnDwarf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					control.handleCharacterSelection(CharacterName.DWARF);
				}
			});
			characterSelectPanel.add(btnDwarf);
		} catch (IOException e){
			
		}
		
		try{
			BufferedImage pic;
			pic = ImageIO.read(this.getClass().getResource("elfDetail.jpg"));
			JButton btnElf = new JButton(new ImageIcon(pic));
			btnElf.setSize(360, 284);
			btnElf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					control.handleCharacterSelection(CharacterName.ELF);
				}
			});
			characterSelectPanel.add(btnElf);
		} catch (IOException e){
			
		}
		
		try{
			BufferedImage pic;
			pic = ImageIO.read(this.getClass().getResource("swordsmanDetail.jpg"));
			JButton btnSwordsman = new JButton(new ImageIcon(pic));
			btnSwordsman.setSize(360, 284);
			btnSwordsman.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					control.handleCharacterSelection(CharacterName.SWORDSMAN);
				}
			});
			characterSelectPanel.add(btnSwordsman);
		} catch (IOException e){
			
		}
		
		try{
			BufferedImage pic;
			pic = ImageIO.read(this.getClass().getResource("white_knightDetail.jpg"));
			JButton btnWhite = new JButton(new ImageIcon(pic));
			btnWhite.setSize(360, 284);
			btnWhite.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					control.handleCharacterSelection(CharacterName.WHITE_KNIGHT);
				}
			});
			characterSelectPanel.add(btnWhite);
		} catch (IOException e){
			
		}
		movesPanel = new JPanel();
		movesPanel.setVisible(false);
		movesPanel.setBorder(new LineBorder(Color.GRAY));
		movesPanel.setBounds(750, 500, 524, 192);
		contentPane.add(movesPanel);
		movesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblSelectMoveLocation = new JLabel("Select Move Location");
		lblSelectMoveLocation.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		movesPanel.add(lblSelectMoveLocation);
		
		movesGroup = new ButtonGroup();
		
		JButton btnSelectMoves = new JButton("Select");
		btnSelectMoves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(Enumeration<AbstractButton> buttons = movesGroup.getElements(); buttons.hasMoreElements();){
					AbstractButton button = buttons.nextElement();
					if (button.isSelected()) {
						control.handleMoveSelection(button.getText());
					}
				}
			}
		});
		movesPanel.add(btnSelectMoves);
		
		alertPanel = new JPanel();
		alertPanel.setVisible(false);
		alertPanel.setBorder(new LineBorder(Color.GRAY));
		alertPanel.setBounds(750, 500, 524, 192);
		contentPane.add(alertPanel);
		alertPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblAlert= new JLabel("Select Weapon to Alert");
		lblAlert.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		movesPanel.add(lblAlert);
		
		alertGroup = new ButtonGroup();
		
		JButton btnSelectAlert = new JButton("Select");
		btnSelectAlert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(Enumeration<AbstractButton> buttons = movesGroup.getElements(); buttons.hasMoreElements();){
					AbstractButton button = buttons.nextElement();
					if (button.isSelected()) {
						control.handleAlertSelection(button.getText());
					}
				}
			}
		});
		movesPanel.add(btnSelectAlert);
		
		combatPanel = new JPanel();
		combatPanel.setVisible(false);
		combatPanel.setBounds(750, 500, 524, 192);
		contentPane.add(combatPanel);
		combatPanel.setLayout(null);
		combatPanel.setBorder(new LineBorder(Color.GRAY));
		
		JLabel lblSelectCombatActions = new JLabel("Select Combat Actions");
		lblSelectCombatActions.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblSelectCombatActions.setBounds(10, 11, 154, 14);
		combatPanel.add(lblSelectCombatActions);
		
		attack = new JComboBox();
		attack.setModel(new DefaultComboBoxModel(Attacks.values()));
		attack.setBounds(10, 41, 93, 20);
		combatPanel.add(attack);
		
		maneuvers = new JComboBox();
		maneuvers.setModel(new DefaultComboBoxModel(Maneuvers.values()));
		maneuvers.setBounds(113, 41, 93, 20);
		combatPanel.add(maneuvers);
		
		defense = new JComboBox();
		defense.setModel(new DefaultComboBoxModel(Defenses.values()));
		defense.setBounds(216, 41, 93, 20);
		combatPanel.add(defense);
		
		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handleCombatMoves((Attacks)attack.getSelectedItem(), (Defenses)defense.getSelectedItem(), 
						(Maneuvers)maneuvers.getSelectedItem(), (int)attackFatigue.getSelectedItem(), (int)maneuversFatigue.getSelectedItem());
			}
		});
		
		attackFatigue = new JComboBox();
		attackFatigue.setModel(new DefaultComboBoxModel(new Integer[] {0, 1, 2}));
		attackFatigue.setBounds(10, 70, 50, 20);
		combatPanel.add(attackFatigue);
		
		maneuversFatigue = new JComboBox();
		maneuversFatigue.setModel(new DefaultComboBoxModel(new Integer[] {0, 1, 2}));
		maneuversFatigue.setBounds(113, 70, 50, 20);
		combatPanel.add(maneuversFatigue);
		btnSelect.setBounds(422, 40, 89, 23);
		combatPanel.add(btnSelect);
		
		targetPanel = new JPanel();
		targetPanel.setVisible(false);
		targetPanel.setBounds(750, 500, 524, 192);
		contentPane.add(targetPanel);
		targetPanel.setLayout(null);
		targetPanel.setBorder(new LineBorder(Color.GRAY));
		
		JLabel lblSelectCombatTarget = new JLabel("Select Combat Target");
		lblSelectCombatTarget.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblSelectCombatTarget.setBounds(10, 11, 154, 14);
		targetPanel.add(lblSelectCombatTarget);
		
		target = new JComboBox();
		target.setBounds(10, 41, 93, 20);
		targetPanel.add(target);
		
		JButton btnSelectTarget = new JButton("Select");
		btnSelectTarget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handleTargetSelection((CharacterName)target.getSelectedItem());
			}
		});

		btnSelectTarget.setBounds(422, 40, 89, 23);
		targetPanel.add(btnSelectTarget);
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void update(){
		Board b = control.model.getBoard();
		if (b != null) {
			boardPanel.removeAll();
			BufferedImage pic;
			for (int i = 0; i < b.tiles.length; i++){
				try {
					TileName name = b.tiles[i].getName();
					pic = ImageIO.read(this.getClass().getResource(Utility.getTileImage(name)));
					JLabel tile = new JLabel(new ImageIcon(pic));
					tile.setBounds(b.tiles[i].getX() - 100, b.tiles[i].getY() - 86, 200, 173);
					boardPanel.add(tile);
					
					Clearing[] clearings = b.tiles[i].getClearings();
					if (clearings != null) {
						for(int j = 0; j < clearings.length; j++){
							Player[] occupants = clearings[j].occupants;
							if(occupants != null){
								for(int k = 0; k < occupants.length; k++){
									if (occupants[k] != null){
										CharacterName character = occupants[k].getCharacter().getName();
										pic = ImageIO.read(this.getClass().getResource(Utility.getCharacterImage(character)));
										JLabel c = new JLabel(new ImageIcon(pic));
										c.setBounds(b.tiles[i].getX() - 25, b.tiles[i].getY() - 25, 50, 50);
										boardPanel.add(c, new Integer(5), 0);
										c.repaint();
									}
								}
							}
						}
					}
				} catch (IOException e){
					
				}
			}
			for (int i = 0; i < b.garrisons.length; i++){
				try {
					GarrisonName name = b.garrisons[i].getName();
					pic = ImageIO.read(this.getClass().getResource(Utility.getGarrisonImage(name)));
					JLabel garrison = new JLabel(new ImageIcon(pic));
					garrison.setBounds(b.garrisons[i].getLocation().parent.getX() - 25, b.garrisons[i].getLocation().parent.getY() - 21, 50, 43);
					boardPanel.add(garrison, new Integer(5), 0);
					garrison.repaint();
				} catch (IOException e){
					
				}
			}
		}
		Player p = control.model.getPlayer();
		if (p != null){
			characterText.setText(p.getCharacter().getName().toString());
//			vpText.setText(Integer.toString(p.getVictoryPoints()));
			vpText.setText(p.getLocation().parent.getName().toString() + String.valueOf(p.getLocation().getClearingNumber())); // TODO: change later
			healthText.setText(Integer.toString(p.getHealth()));
			goldText.setText(Integer.toString(p.getGold()));
			fatigueText.setText(Integer.toString(p.getFatigue()));
			fameText.setText(Integer.toString(p.getFame()));
			notorietyText.setText(Integer.toString(p.getNotoriety()));
			hiddenText.setText(String.valueOf(p.isHidden()));
			
			Armour[] armour = p.getArmour();
			String armourS = "";
			if (armour != null) {
				for(int i = 0; i < armour.length; i++){
					if(armour[i] != null) {
						armourS += armour[i].getType().toString() + " weight - " + armour[i].getWeight().toString() + 
								" damaged - " + String.valueOf(armour[i].isDamaged()) + 
								" active - " + String.valueOf(armour[i].isActive()) + "\n";
					}
				}
			}
			armourText.setText(armourS);
			
			Treasure[] treasures = p.getTreasures();
			String treasuresS = "";
			if (treasures != null) {
				for(int i = 0; i < treasures.length; i++){
					if ( treasures[i] != null) {
						treasuresS += treasures[i].getType() + " gold - " + Integer.toString(treasures[i].getGold()) + "\n";
					}
				}
			}
			treasuresText.setText(treasuresS);
			
			Weapon[] weapons = p.getWeapons();
			String weaponsS = "";
			if (weapons != null) {
				for(int i = 0; i < weapons.length; i++){
					if (weapons[i] != null) {
						weaponsS += weapons[i].getType().toString() + " weight - " + weapons[i].getWeight().toString()+ 
								" length - " + Integer.toString(weapons[i].getLength()) + 
								" speed - " + Integer.toString(weapons[i].getSpeed()) + 
								" ranged - " + String.valueOf(weapons[i].isRanged()) + 
								" active - " + String.valueOf(weapons[i].isActive()) + "\n";
					}
				}
			}
			weaponsText.setText(weaponsS);
			
			Chit[] chits = p.getChits();
			String chitsS = "";
			if (chits != null) {
				for(int i = 0; i < chits.length; i++){
					if (chits[i] != null) {
						chitsS += chits[i].getType() + " name - " + chits[i].getName() + "\n";
					}
				}
			}
			chitsText.setText(chitsS);
			textDisplay.setText(control.model.getMessages());
		}
		
		switch(control.state){
			case CHOOSE_CHARACTER:
				scrollPanel.setViewportView(characterSelectPanel);
				break;
			case CHOOSE_PLAYS:
				if (b != null){
					String[] clearings = new String[95];//had to hardcode the number for this interation
					int count = 0;
					for (int i = 0; i < b.tiles.length; i++){
						for (int j=0; j < b.tiles[i].getClearings().length; j++){
							clearings[count] = b.tiles[i].getName().toString() + " clearing " + b.tiles[i].getClearings()[j].getClearingNumber();
							count++;
						}
					}
					play1Location.setModel(new DefaultComboBoxModel(clearings));
					play2Location.setModel(new DefaultComboBoxModel(clearings));
					play3Location.setModel(new DefaultComboBoxModel(clearings));
					play4Location.setModel(new DefaultComboBoxModel(clearings));
				}
				alertPanel.setVisible(false);
				combatPanel.setVisible(false);
				movesPanel.setVisible(false);
				playsPanel.setVisible(true);
				targetPanel.setVisible(false);
				scrollPanel.setViewportView(boardPanel);
				break;
			case MOVE:
				for(Enumeration<AbstractButton> buttons = movesGroup.getElements(); buttons.hasMoreElements();){
					AbstractButton button = buttons.nextElement();
					movesGroup.remove(button);
					movesPanel.remove(button);
				}
				Clearing[] connections = this.control.model.getPlayer().getLocation().connections;
				for(int i = 0; i < connections.length; i++){
					String label = connections[i].parent.getName().toString() + " clearing " + connections[i].getClearingNumber();
					JRadioButton button = new JRadioButton(label);
					movesGroup.add(button);
					movesPanel.add(button, movesPanel.getComponents().length - 2);
				}
				alertPanel.setVisible(false);
				combatPanel.setVisible(false);
				movesPanel.setVisible(true);
				playsPanel.setVisible(false);
				targetPanel.setVisible(false);
				scrollPanel.setViewportView(boardPanel);
				break;
			case ALERT:
				for(Enumeration<AbstractButton> buttons = alertGroup.getElements(); buttons.hasMoreElements();){
					AbstractButton button = buttons.nextElement();
					alertGroup.remove(button);
					alertPanel.remove(button);
				}
				Weapon[] weapons = this.control.model.getPlayer().getWeapons();
				for(int i = 0; i < weapons.length; i++){
					String label = weapons[i].getType().toString() + " " + weapons[i].isActive();
					JRadioButton button = new JRadioButton(label);
					alertGroup.add(button);
					alertPanel.add(button, alertPanel.getComponents().length - 2);
				}
				alertPanel.setVisible(false);
				combatPanel.setVisible(false);
				movesPanel.setVisible(true);
				playsPanel.setVisible(false);
				targetPanel.setVisible(false);
				scrollPanel.setViewportView(boardPanel);
				break;
			case CHOOSE_COMBATMOVES:
				alertPanel.setVisible(false);
				combatPanel.setVisible(true);
				movesPanel.setVisible(false);
				playsPanel.setVisible(false);
				targetPanel.setVisible(false);
				scrollPanel.setViewportView(boardPanel);
				break;
			case CHOOSE_COMBATTARGET:
				if (p != null) {
					Player[] others = p.getLocation().getOccupants();
					if (others != null) {
						CharacterName[] targets = new CharacterName[others.length];
						for (int i = 0; i < others.length; i++){
							targets[i] = others[i].getCharacter().getName();
						}

						target.setModel(new DefaultComboBoxModel(targets));
					}
				}
				alertPanel.setVisible(false);
				combatPanel.setVisible(false);
				movesPanel.setVisible(false);
				playsPanel.setVisible(false);
				targetPanel.setVisible(true);
				scrollPanel.setViewportView(boardPanel);
				break;
		}
		
		this.repaint();
	}
}
