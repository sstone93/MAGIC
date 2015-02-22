package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import utils.Utility;
import utils.Utility.*;
import controller.ClientController;
import model.Board;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class View extends JFrame {
	
	private ClientController control;
	private JPanel contentPane;
	private JLayeredPane boardPanel;
	private JTextField characterText;
	private JTextField vpText;
	private JTextField healthText;
	private JTextField goldText;
	private JTextField fatigueText;
	private JTextField fameText;
	private JTextField notorietyText;
	private JTextField hiddenText;
	private JComboBox play1;
	private JComboBox play2;
	private JComboBox play3;
	private JComboBox play4;
	
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
		
		JTextArea armourText = new JTextArea();
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
		
		JTextArea treasuresText = new JTextArea();
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
		
		JTextArea weaponsText = new JTextArea();
		weaponsText.setLineWrap(true);
		weaponsText.setBounds(224, 167, 161, 142);
		CharacterInfoPanel.add(weaponsText);
		
		JLabel lblChits = new JLabel("Chits");
		lblChits.setBounds(224, 339, 65, 14);
		CharacterInfoPanel.add(lblChits);
		
		JTextArea chitsText = new JTextArea();
		chitsText.setLineWrap(true);
		chitsText.setBounds(224, 364, 161, 128);
		CharacterInfoPanel.add(chitsText);
		
		JPanel ButtonPanel1 = new JPanel();
		ButtonPanel1.setBounds(750, 500, 524, 192);
		ButtonPanel1.setBorder(new LineBorder(Color.GRAY));
		contentPane.add(ButtonPanel1);
		ButtonPanel1.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Actions");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 11, 72, 14);
		ButtonPanel1.add(lblNewLabel_1);
		
		play1 = new JComboBox();
		play1.setModel(new DefaultComboBoxModel(Actions.values()));
		play1.setBounds(10, 41, 93, 20);
		ButtonPanel1.add(play1);
		
		play2 = new JComboBox();
		play2.setModel(new DefaultComboBoxModel(Actions.values()));
		play2.setBounds(113, 41, 93, 20);
		ButtonPanel1.add(play2);
		
		play3 = new JComboBox();
		play3.setModel(new DefaultComboBoxModel(Actions.values()));
		play3.setBounds(216, 41, 93, 20);
		ButtonPanel1.add(play3);
		
		play4 = new JComboBox();
		play4.setModel(new DefaultComboBoxModel(Actions.values()));
		play4.setBounds(319, 41, 93, 20);
		ButtonPanel1.add(play4);
		
		JButton btnRecord = new JButton("Record");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handlePlaysRecorded((Actions)play1.getSelectedItem(), (Actions)play2.getSelectedItem(), (Actions)play3.getSelectedItem(), (Actions)play1.getSelectedItem());
			}
		});
		btnRecord.setBounds(422, 40, 89, 23);
		ButtonPanel1.add(btnRecord);
		
		JTextArea TextDisplay = new JTextArea();
		TextDisplay.setBounds(0, 500, 750, 192);
		TextDisplay.setLineWrap(true);
		contentPane.add(TextDisplay);
		TextDisplay.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, 819, 500);
		contentPane.add(scrollPane);
		
		boardPanel = new JLayeredPane();
		boardPanel.setPreferredSize(new Dimension(800, 1018));
		scrollPane.setViewportView(boardPanel);
		boardPanel.setLayout(null);
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
		this.repaint();
	}
}
