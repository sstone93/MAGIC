package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.ClientController;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import utils.Utility.Actions;
import javax.swing.JButton;


@SuppressWarnings("serial")
public class View extends JFrame {
	
	private JPanel contentPane;
	private JTextField characterText;
	private JTextField scoreText;
	private JTextField healthText;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View frame = new View(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public View(ClientController control) {
		setResizable(false);
		setSize(new Dimension(1280, 720));
		getContentPane().setLayout(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane BoardPanel = new JScrollPane();
		BoardPanel.setBounds(0, 0, 1000, 500);
		contentPane.add(BoardPanel);
		
		JPanel CharacterInfoPanel = new JPanel();
		CharacterInfoPanel.setBorder(new LineBorder(Color.GRAY));
		CharacterInfoPanel.setBounds(1000, 0, 274, 500);
		contentPane.add(CharacterInfoPanel);
		CharacterInfoPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Character");
		lblNewLabel.setBounds(10, 61, 65, 14);
		CharacterInfoPanel.add(lblNewLabel);
		
		JLabel lblCharacterInfo = new JLabel("Character Info");
		lblCharacterInfo.setBounds(70, 11, 122, 21);
		lblCharacterInfo.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		CharacterInfoPanel.add(lblCharacterInfo);
		
		JLabel lblHealth = new JLabel("Health");
		lblHealth.setBounds(10, 112, 46, 14);
		CharacterInfoPanel.add(lblHealth);
		
		JLabel lblArmour = new JLabel("Armour");
		lblArmour.setBounds(10, 137, 46, 14);
		CharacterInfoPanel.add(lblArmour);
		
		JLabel lblScore = new JLabel("Score");
		lblScore.setBounds(10, 86, 46, 14);
		CharacterInfoPanel.add(lblScore);
		
		JTextArea armourText = new JTextArea();
		armourText.setBounds(10, 169, 254, 93);
		CharacterInfoPanel.add(armourText);
		
		characterText = new JTextField();
		characterText.setBounds(75, 58, 86, 20);
		CharacterInfoPanel.add(characterText);
		characterText.setColumns(10);
		
		scoreText = new JTextField();
		scoreText.setBounds(75, 83, 86, 20);
		CharacterInfoPanel.add(scoreText);
		scoreText.setColumns(10);
		
		healthText = new JTextField();
		healthText.setBounds(75, 109, 86, 20);
		CharacterInfoPanel.add(healthText);
		healthText.setColumns(10);
		
		JLabel lblTreasures = new JLabel("Treasures");
		lblTreasures.setBounds(10, 284, 65, 14);
		CharacterInfoPanel.add(lblTreasures);
		
		JTextArea treasuresText = new JTextArea();
		treasuresText.setBounds(10, 309, 254, 93);
		CharacterInfoPanel.add(treasuresText);
		
		JPanel ButtonPanel1 = new JPanel();
		ButtonPanel1.setBorder(new LineBorder(Color.GRAY));
		ButtonPanel1.setBounds(750, 500, 524, 192);
		contentPane.add(ButtonPanel1);
		ButtonPanel1.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Actions");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 11, 72, 14);
		ButtonPanel1.add(lblNewLabel_1);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(Actions.values()));
		comboBox.setBounds(10, 41, 93, 20);
		ButtonPanel1.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(Actions.values()));
		comboBox_1.setBounds(113, 41, 93, 20);
		ButtonPanel1.add(comboBox_1);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(Actions.values()));
		comboBox_2.setBounds(216, 41, 93, 20);
		ButtonPanel1.add(comboBox_2);
		
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setModel(new DefaultComboBoxModel(Actions.values()));
		comboBox_3.setBounds(319, 41, 93, 20);
		ButtonPanel1.add(comboBox_3);
		
		JButton btnRecord = new JButton("Record");
		btnRecord.setBounds(422, 40, 89, 23);
		ButtonPanel1.add(btnRecord);
		
		JTextArea TextDisplay = new JTextArea();
		TextDisplay.setLineWrap(true);
		TextDisplay.setBounds(0, 500, 750, 192);
		contentPane.add(TextDisplay);
		TextDisplay.setEditable(false);
	}
}
