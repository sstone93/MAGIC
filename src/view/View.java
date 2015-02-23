package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import utils.Utility;
import utils.Utility.*;
import controller.ClientController;
import model.Board;
import model.Clearing;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class View extends JFrame {
	
	private ClientController control;
	private JPanel contentPane;
	private JPanel playsPanel;
	private JPanel combatPanel;
	private JLayeredPane boardPanel;
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
	private JComboBox attack;
	private JComboBox defense;
	private JComboBox maneuvers;
	private JPanel movesPanel;
	private JLabel lblSelectMoveLocation;
	private JButton btnSelectMoves;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private ButtonGroup movesGroup;
	
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
		
		JLabel lblScore = new JLabel("Victory Points");
		lblScore.setBounds(10, 69, 71, 14);
		CharacterInfoPanel.add(lblScore);
		
		armourText = new JTextArea();
		armourText.setLineWrap(true);
		armourText.setBounds(10, 164, 161, 142);
		CharacterInfoPanel.add(armourText);
		
		characterText = new JTextField();
		characterText.setBounds(85, 41, 86, 20);
		CharacterInfoPanel.add(characterText);
		characterText.setColumns(10);
		
		vpText = new JTextField();
		vpText.setBounds(85, 66, 86, 20);
		CharacterInfoPanel.add(vpText);
		vpText.setColumns(10);
		
		healthText = new JTextField();
		healthText.setBounds(85, 120, 86, 20);
		CharacterInfoPanel.add(healthText);
		healthText.setColumns(10);
		
		JLabel lblTreasures = new JLabel("Treasures");
		lblTreasures.setBounds(10, 336, 65, 14);
		CharacterInfoPanel.add(lblTreasures);
		
		treasuresText = new JTextArea();
		treasuresText.setLineWrap(true);
		treasuresText.setBounds(10, 361, 161, 128);
		CharacterInfoPanel.add(treasuresText);
		
		JLabel lblGold = new JLabel("Gold");
		lblGold.setBounds(10, 94, 46, 14);
		CharacterInfoPanel.add(lblGold);
		
		goldText = new JTextField();
		goldText.setBounds(85, 91, 86, 20);
		CharacterInfoPanel.add(goldText);
		goldText.setColumns(10);
		
		JLabel lblFatigue = new JLabel("Fatigue");
		lblFatigue.setBounds(224, 47, 65, 14);
		CharacterInfoPanel.add(lblFatigue);
		
		fatigueText = new JTextField();
		fatigueText.setColumns(10);
		fatigueText.setBounds(299, 44, 86, 20);
		CharacterInfoPanel.add(fatigueText);
		
		JLabel lblFame = new JLabel("Fame");
		lblFame.setBounds(224, 72, 71, 14);
		CharacterInfoPanel.add(lblFame);
		
		fameText = new JTextField();
		fameText.setColumns(10);
		fameText.setBounds(299, 69, 86, 20);
		CharacterInfoPanel.add(fameText);
		
		JLabel lblNotoriety = new JLabel("Notoriety");
		lblNotoriety.setBounds(224, 97, 46, 14);
		CharacterInfoPanel.add(lblNotoriety);
		
		notorietyText = new JTextField();
		notorietyText.setColumns(10);
		notorietyText.setBounds(299, 94, 86, 20);
		CharacterInfoPanel.add(notorietyText);
		
		hiddenText = new JTextField();
		hiddenText.setColumns(10);
		hiddenText.setBounds(299, 123, 86, 20);
		CharacterInfoPanel.add(hiddenText);
		
		JLabel lblHidden = new JLabel("Hidden");
		lblHidden.setBounds(224, 123, 46, 14);
		CharacterInfoPanel.add(lblHidden);
		
		JLabel lblWeapons = new JLabel("Weapons");
		lblWeapons.setBounds(224, 148, 46, 14);
		CharacterInfoPanel.add(lblWeapons);
		
		weaponsText = new JTextArea();
		weaponsText.setLineWrap(true);
		weaponsText.setBounds(224, 167, 161, 142);
		CharacterInfoPanel.add(weaponsText);
		
		JLabel lblChits = new JLabel("Chits");
		lblChits.setBounds(224, 339, 65, 14);
		CharacterInfoPanel.add(lblChits);
		
		chitsText = new JTextArea();
		chitsText.setLineWrap(true);
		chitsText.setBounds(224, 364, 161, 128);
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
		play1.setModel(new DefaultComboBoxModel(Actions.values()));
		play1.setBounds(10, 41, 93, 20);
		playsPanel.add(play1);
		
		play2 = new JComboBox();
		play2.setModel(new DefaultComboBoxModel(Actions.values()));
		play2.setBounds(113, 41, 93, 20);
		playsPanel.add(play2);
		
		play3 = new JComboBox();
		play3.setModel(new DefaultComboBoxModel(Actions.values()));
		play3.setBounds(216, 41, 93, 20);
		playsPanel.add(play3);
		
		play4 = new JComboBox();
		play4.setModel(new DefaultComboBoxModel(Actions.values()));
		play4.setBounds(319, 41, 93, 20);
		playsPanel.add(play4);
		
		JButton btnRecord = new JButton("Record");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handlePlaysRecorded((Actions)play1.getSelectedItem(), (Actions)play2.getSelectedItem(), (Actions)play3.getSelectedItem(), (Actions)play1.getSelectedItem());
			}
		});
		btnRecord.setBounds(422, 40, 89, 23);
		playsPanel.add(btnRecord);
		
		textDisplay = new JTextArea();
		textDisplay.setBounds(0, 500, 750, 192);
		textDisplay.setLineWrap(true);
		contentPane.add(textDisplay);
		textDisplay.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, 819, 500);
		contentPane.add(scrollPane);
		
		boardPanel = new JLayeredPane();
		boardPanel.setPreferredSize(new Dimension(800, 1018));
		scrollPane.setViewportView(boardPanel);
		boardPanel.setLayout(null);
		
		movesPanel = new JPanel();
		movesPanel.setBorder(new LineBorder(Color.GRAY));
		movesPanel.setBounds(750, 500, 524, 192);
		contentPane.add(movesPanel);
		movesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblSelectMoveLocation = new JLabel("Select Move Location");
		lblSelectMoveLocation.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		movesPanel.add(lblSelectMoveLocation);
		
		movesGroup = new ButtonGroup();
		
		btnSelectMoves = new JButton("Select");
		btnSelectMoves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO: add event handler
			}
		});
		movesPanel.add(btnSelectMoves);
		
		combatPanel = new JPanel();
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
				control.handleCombatMoves((Attacks)attack.getSelectedItem(), (Defenses)defense.getSelectedItem(), (Maneuvers)maneuvers.getSelectedItem());
			}
		});
		btnSelect.setBounds(422, 40, 89, 23);
		combatPanel.add(btnSelect);
	}
	
	public void update(){
		Board b = control.model.getBoard();
		boardPanel.removeAll();
		BufferedImage pic;
		for (int i = 0; i < b.tiles.length; i++){
			try {
				TileName name = b.tiles[i].getName();
				pic = ImageIO.read(this.getClass().getResource(Utility.getTileImage(name)));
				JLabel tile = new JLabel(new ImageIcon(pic));
				tile.setBounds(b.tiles[i].getX() - 100, b.tiles[i].getY() - 86, 200, 173);
				boardPanel.add(tile);
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
		
		switch(control.state){
			case CHOOSE_CHARACTER:
				break;
			case CHOOSE_PLAYS:
				combatPanel.setVisible(false);
				movesPanel.setVisible(false);
				playsPanel.setVisible(true);
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
				combatPanel.setVisible(false);
				movesPanel.setVisible(true);
				playsPanel.setVisible(false);
				break;
			case ALERT:
				break;
			case REST:
				break;
			case CHOOSE_COMBAT:
				combatPanel.setVisible(true);
				movesPanel.setVisible(false);
				playsPanel.setVisible(false);
				break;
		}
		
		this.repaint();
	}
}
