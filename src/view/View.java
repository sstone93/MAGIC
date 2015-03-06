package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import utils.Utility;
import utils.Utility.CharacterName;
import utils.Utility.*;
import controller.ClientController;
import model.Board;
import model.Clearing;
import model.Player;
import model.Tile;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

@SuppressWarnings("serial")
public class View extends JFrame {
	
	private ClientController control;
	private boolean boardMade;
	private JPanel contentPane;
	private JLayeredPane boardPanel;
	private JScrollPane scrollPanel;
	private JTextArea textDisplay;
	private JPanel	blankPanel;

	private HashMap<Tile, JPanel> iconPanels = new HashMap<Tile, JPanel>();
	private JLabel [] tileLbls;
	private JLabel [] garrisonLbls;
	private JLabel [] charLbls;
	
	private CharacterInfoPanel characterInfoPanel;
	private ActivitiesPanel playsPanel;
	private CombatPanel combatPanel;
	private CharacterSelectPanel characterSelectPanel;
	private TargetPanel targetPanel;

	public View(final ClientController control) {
		
		this.control = control;
		this.boardMade = false;
		setResizable(false);
		setSize(new Dimension(1280, 720));
		getContentPane().setLayout(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Causes netowrk shutdown by clicking the close button on the window
		this.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.out.println("Window Was Closed: Triggering Shutdown");
                control.network.stop();
                System.exit(0);
            }
        } );

		//Creates the characterinfo panel and adds it to the view
		characterInfoPanel = new CharacterInfoPanel();
		contentPane.add(characterInfoPanel);
		
		//creates the plays/activities panel
		playsPanel = new ActivitiesPanel(control);
		contentPane.add(playsPanel);
		
		//adds the text box
		textDisplay = new JTextArea();
		textDisplay.setBounds(0, 500, 750, 192);
		textDisplay.setLineWrap(true);
		contentPane.add(textDisplay);
		textDisplay.setEditable(false);
		
		//adds the scroll bar
		scrollPanel = new JScrollPane();
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.setBounds(0, 0, 819, 500);
		contentPane.add(scrollPanel);
		
		//adds the board panel
		boardPanel = new JLayeredPane();
		boardPanel.setPreferredSize(new Dimension(800, 1018));
		scrollPanel.setViewportView(boardPanel);
		boardPanel.setLayout(null);
		
		//adds the blank no actions panel
		blankPanel = new JPanel();
		blankPanel.setBounds(750, 500, 524, 192);
		blankPanel.setBorder(new LineBorder(Color.GRAY));
		blankPanel.setLayout(null);
		JLabel lblNewLabel_1 = new JLabel("Waiting on Server or other Players");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 11, 275, 18);
		blankPanel.add(lblNewLabel_1);
		contentPane.add(blankPanel);
		
		//creates the character select panel and all its buttons
		characterSelectPanel = new CharacterSelectPanel(control);
		
		//creates a new combatpanel and adds it
		combatPanel = new CombatPanel(control);
		contentPane.add(combatPanel);
		
		//creates a new targetpanel, exact same functionality as before, but in a seperate class to reduce clutter
		targetPanel = new TargetPanel(control);
		contentPane.add(targetPanel);
		
		//adds moves panel
		/*movesPanel = new JPanel();
		contentPane.add(movesPanel);
		movesPanel.setVisible(false);
		movesPanel.setBorder(new LineBorder(Color.GRAY));
		movesPanel.setBounds(750, 500, 524, 192);
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
		*/
		
		//Alert panel
		/*alertPanel = new JPanel();
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
		alertPanel.add(btnSelectAlert);*/	
	}
	
	private void makeBoard(){
		Board b = control.model.getBoard();
		if (b != null) {
			tileLbls = new JLabel[b.tiles.size()];
			garrisonLbls = new JLabel[b.garrisons.size()];
			charLbls = new JLabel[control.model.getNumPlayers()];
			
			BufferedImage pic;
			for (int i = 0; i < b.tiles.size(); i++){
				try {
					TileName name = b.tiles.get(i).getName();
					pic = ImageIO.read(this.getClass().getResource(Utility.getTileImage(name)));
					tileLbls[i] = new JLabel(new ImageIcon(pic));
					tileLbls[i].setBounds(b.tiles.get(i).getX() - 100, b.tiles.get(i).getY() - 86, 200, 173);
					boardPanel.add(tileLbls[i]);
				}catch (IOException e){
					
				}
			}
			for (int i = 0; i < b.garrisons.size(); i++){
				try {
					GarrisonName name = b.garrisons.get(i).getName();
					pic = ImageIO.read(this.getClass().getResource(Utility.getGarrisonImage(name)));
					garrisonLbls[i] = new JLabel(new ImageIcon(pic));
					garrisonLbls[i].setBounds(b.garrisons.get(i).getLocation().parent.getX() - 25, b.garrisons.get(i).getLocation().parent.getY() - 21, 50, 43);
					boardPanel.add(garrisonLbls[i], new Integer(5), 0);
					//garrison.repaint();
				} catch (IOException e){
					
				}
			}
		}
		
		this.boardMade = true;
	}
	
	public void updateMessageBox(){
		textDisplay.setText(control.model.getMessages());
	}
	
	@SuppressWarnings({"rawtypes"})
	public void update(){
		final Board b = control.model.getBoard();
		if (b != null) {
			
			//ENSURE TH BOARD IS ONLY DRAWN ONCE
			if (!this.boardMade){
				makeBoard();
			}
			
			Iterator it = iconPanels.entrySet().iterator();

			//TODO 
			//TODO Concurrent thread access, is the View thread and the Client control thread both calling update at the same time? possible.
			//TODO CONTROL THIS!!!!
			//TODO
			
			while (it.hasNext()) {
				JPanel panel = (JPanel)((HashMap.Entry)it.next()).getValue();
				if(panel != null){
					boardPanel.remove(panel);
				}
			}

			iconPanels.clear();
			
			for(int i = 0; i < charLbls.length; i++){
				if(charLbls[i] != null){
					boardPanel.remove(charLbls[i]);
				}
			}
			
			charLbls = new JLabel[control.model.getNumPlayers()];
			
			BufferedImage pic;
			
			//1. Iterates through every single tile in the board (always 20 TBH), (tile i)
			for (int i = 0; i < b.tiles.size(); i++){
				try {
					ArrayList<Clearing> clearings = b.tiles.get(i).getClearings();
					if (clearings != null) {//should never be null
						int chars = 0;
						//2. Gets all clearings on a given tile, then cycles through all of them, (clearing j)
						for(int j = 0; j < clearings.size(); j++){
							ArrayList<Player> occupants = clearings.get(j).getOccupants();
							if(occupants != null){
								//3. Gets all Occupants of a clearing, and cycles through them. (occupant k)
								for(int k = 0; k < occupants.size(); k++){
									if (occupants.get(k) != null){
										
										CharacterName character = occupants.get(k).getCharacter().getName();
										System.out.println("	adding Character " + character.toString() + " to " + b.tiles.get(i).getName().toString());
										pic = ImageIO.read(this.getClass().getResource(Utility.getCharacterImage(character)));
										charLbls[chars] = new JLabel(new ImageIcon(pic));
										charLbls[chars].setBounds(b.tiles.get(i).getX() - 25, b.tiles.get(i).getY() - 25, 50, 50);
										boardPanel.add(charLbls[chars], new Integer(5), 0);
										
										if (!iconPanels.containsKey(b.tiles.get(i))){
											JPanel newPane = new JPanel();
											newPane.setBounds(b.tiles.get(i).getX(), b.tiles.get(i).getY(), 200, 300);
											newPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
											newPane.setVisible(false);
											boardPanel.add(newPane, new Integer(10), 0);
											iconPanels.put(b.tiles.get(i), newPane);
										}
										
										JPanel panel = new JPanel();
										panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
										panel.setSize(65, 75);
										JLabel img = new JLabel(new ImageIcon(pic));
										img.setSize(50, 50);
										panel.add(img);
										
										JLabel lbl = new JLabel(Integer.toString(clearings.get(j).getClearingNumber()));
										lbl.setSize(10, 15);
										panel.add(lbl);
										iconPanels.get(b.tiles.get(i)).add(panel);
										
										final int index = i;
										
										charLbls[chars].addMouseListener(new MouseAdapter() {
											@Override
											public void mouseEntered(MouseEvent e) {
												iconPanels.get(b.tiles.get(index)).setVisible(true);
											}
										});
										charLbls[chars].addMouseListener(new MouseAdapter() {
											@Override
											public void mouseExited(MouseEvent e) {
												iconPanels.get(b.tiles.get(index)).setVisible(false);
											}
										});
										chars++;
									}
								}
							}
						}
					}
				} catch (IOException e){
					
				}
			}
			for (int i = 0; i < b.garrisons.size(); i++){
				try {
					GarrisonName name = b.garrisons.get(i).getName();
					pic = ImageIO.read(this.getClass().getResource(Utility.getGarrisonImage(name)));
					final Tile parent = b.garrisons.get(i).getLocation().parent;
					
					if (!iconPanels.containsKey(parent)){
						JPanel newPane = new JPanel();
						newPane.setBounds(parent.getX(), parent.getY(), 200, 300);
						newPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
						newPane.setVisible(false);
						boardPanel.add(newPane, new Integer(10), 0);
						iconPanels.put(parent, newPane);
					}
					
					JPanel panel = new JPanel();
					panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
					panel.setSize(65, 75);
					JLabel img = new JLabel(new ImageIcon(pic));
					img.setSize(50, 50);
					panel.add(img);
					
					JLabel lbl = new JLabel(Integer.toString(b.garrisons.get(i).getLocation().getClearingNumber()));
					lbl.setSize(10, 15);
					panel.add(lbl);
					iconPanels.get(parent).add(panel);
					
					garrisonLbls[i].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							iconPanels.get(parent).setVisible(true);
						}
					});
					garrisonLbls[i].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseExited(MouseEvent e) {
							iconPanels.get(parent).setVisible(false);
						}
					});
				} catch (IOException e){
					
				}
			}
		}
		
		//UPDATES THE PLAYER PANEL
		Player p = control.model.getPlayer();
		if (p != null){
			characterInfoPanel.update(p);
		}
		updateNonBoardGUI(p);
	}
	
	public void updateNonBoardGUI(Player p){

		//UPDATES THE TEXTBOX
		textDisplay.setText(control.model.getMessages());

		//UPDATES THE INPUT PANEL BASED ON IT'S TYPE
		switch(control.state){
		case CHOOSE_CHARACTER:
			makePanelVisible(blankPanel);
			scrollPanel.setViewportView(characterSelectPanel);//this must be called AFTER makePanelVisible
			break;
		case CHOOSE_PLAYS:
			playsPanel.update();
			makePanelVisible(playsPanel);
			break;
			/*case MOVE:
						for(Enumeration<AbstractButton> buttons = movesGroup.getElements(); buttons.hasMoreElements();){
							AbstractButton button = buttons.nextElement();
							movesGroup.remove(button);
							movesPanel.remove(button);
						}

						ArrayList<Path> connections = this.control.model.getPlayer().getLocation().getConnections();

						for(int i = 0; i < connections.size(); i++){
							String label = this.control.model.getPlayer().getLocation().parent.getName().toString() + " clearing " + connections.get(i).getDestination(this.control.model.getPlayer().getLocation()).getClearingNumber();
							JRadioButton button = new JRadioButton(label);
							movesGroup.add(button);
							movesPanel.add(button, movesPanel.getComponents().length - 2);
						}
						makePanelVisible(movesPanel);
						break;
					case ALERT:
						for(Enumeration<AbstractButton> buttons = alertGroup.getElements(); buttons.hasMoreElements();){
							AbstractButton button = buttons.nextElement();
							alertGroup.remove(button);
							alertPanel.remove(button);
						}
						ArrayList<Weapon> weapons = this.control.model.getPlayer().getWeapons();
						for(int i = 0; i < weapons.size(); i++){
							String label = weapons.get(i).getType().toString() + " " + weapons.get(i).isActive();
							JRadioButton button = new JRadioButton(label);
							alertGroup.add(button);
							alertPanel.add(button, alertPanel.getComponents().length - 2);
						}
						makePanelVisible(movesPanel);
						break;*/
		case CHOOSE_COMBATMOVES:
			makePanelVisible(combatPanel);
			break;
		case CHOOSE_COMBATTARGET:
			if (p != null){
				targetPanel.update(p);
			}
			makePanelVisible(targetPanel);
			break;
		case NULL:
			makePanelVisible(blankPanel);
		default:
			break;
		}
	}
	
	public void makePanelVisible(JPanel newPanel){
		newPanel.setVisible(true);
		if(newPanel != combatPanel)
			combatPanel.setVisible(false);
		if(newPanel != playsPanel)
			playsPanel.setVisible(false);
		if(newPanel != targetPanel)
			targetPanel.setVisible(false);
		if(newPanel != blankPanel)
			blankPanel.setVisible(false);
		scrollPanel.setViewportView(boardPanel);
	}
}
