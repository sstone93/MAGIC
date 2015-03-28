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
import utils.Utility.*;
import controller.ClientController;
import model.Board;
import model.Clearing;
import model.MapChit;
import model.Monster;
import model.Player;
import model.Tile;
import model.WarningChit;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
	//private JScrollPane upgradedText;
	private JTextArea textDisplay;
	private JPanel	blankPanel;

	private HashMap<Tile, JPanel> tileHoverOvers = new HashMap<Tile, JPanel>();
	private HashMap<Clearing, JPanel> clearingHoverOvers = new HashMap<Clearing, JPanel>();
	private JLabel [] tileLbls;
	private JLabel [] garrisonLbls;
	private JLabel [] charLbls;
	private ArrayList<JLabel> labels;
	
	private CharacterInfoPanel characterInfoPanel;
	private ActivitiesPanel playsPanel;
	private CombatPanel combatPanel;
	private CharacterSelectPanel characterSelectPanel;
	private TargetPanel targetPanel;
	private CharacterDetailsPanel characterDetailsPanel;

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
		//upgradedText =  new JScrollPane (textDisplay, 
				  // JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		textDisplay.setBounds(0, 500, 750, 192);
		textDisplay.setLineWrap(true);
		//contentPane.add(upgradedText);
		contentPane.add(textDisplay);
		textDisplay.setEditable(false);
		//upgradedText.setVisible(true);
		
		//adds the scroll bars
		scrollPanel = new JScrollPane();
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setBounds(0, 0, 819, 500);
		contentPane.add(scrollPanel);
		
		//adds the board panel
		boardPanel = new JLayeredPane();
		boardPanel.setPreferredSize(new Dimension(1100, 1318));
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
		
		//creates the character details panel
		characterDetailsPanel = new CharacterDetailsPanel(control);
		
		//creates the character select panel
		characterSelectPanel = new CharacterSelectPanel(control);
		contentPane.add(characterSelectPanel);
		
		//creates a new combatpanel and adds it
		combatPanel = new CombatPanel(control);
		contentPane.add(combatPanel);
		
		//creates a new targetpanel, exact same functionality as before, but in a seperate class to reduce clutter
		targetPanel = new TargetPanel(control);
		contentPane.add(targetPanel);
	}
	
	private void makeBoard(){
		Board b = control.model.getBoard();
		if (b != null) {
			tileLbls = new JLabel[b.tiles.size()];
			garrisonLbls = new JLabel[b.garrisons.size()];
			charLbls = new JLabel[control.model.getNumPlayers()];
			labels = new ArrayList<JLabel>();
			
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
					garrisonLbls[i].setBounds(b.garrisons.get(i).getLocation().parent.getX() + b.garrisons.get(i).getLocation().xOffset - 25,
							b.garrisons.get(i).getLocation().parent.getY() + b.garrisons.get(i).getLocation().yOffset - 21, 50, 43);
					boardPanel.add(garrisonLbls[i], new Integer(5), 0);
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
			
			Iterator it = tileHoverOvers.entrySet().iterator();

			//TODO Concurrent thread access, is the View thread and the Client control thread both calling update at the same time? possible.
			//TODO CONTROL THIS!!!!
			
			while (it.hasNext()) {
				JPanel panel = (JPanel)((HashMap.Entry)it.next()).getValue();
				if(panel != null){
					boardPanel.remove(panel);
				}
			}

			tileHoverOvers.clear();
			
			it = clearingHoverOvers.entrySet().iterator();

			//TODO Concurrent thread access, is the View thread and the Client control thread both calling update at the same time? possible.
			//TODO CONTROL THIS!!!!
			
			while (it.hasNext()) {
				JPanel panel = (JPanel)((HashMap.Entry)it.next()).getValue();
				if(panel != null){
					boardPanel.remove(panel);
				}
			}

			clearingHoverOvers.clear();
			
			for(int i = 0; i < charLbls.length; i++){
				if(charLbls[i] != null){
					boardPanel.remove(charLbls[i]);
				}
			}
			
			charLbls = new JLabel[control.model.getNumPlayers()];
			
			for(int i = 0; i < labels.size(); i++){
				boardPanel.remove(labels.get(i));
			}
			
			labels.clear();
			
			BufferedImage pic;
			
			int chars = 0;
			//1. Iterates through every single tile in the board (always 20 TBH), (tile i)
			for (int i = 0; i < b.tiles.size(); i++){
				try {
					ArrayList<Clearing> clearings = b.tiles.get(i).getClearings();
					//2. Gets all clearings on a given tile, then cycles through all of them, (clearing j)
					for(int j = 0; j < clearings.size(); j++){
						ArrayList<Player> occupants = clearings.get(j).getOccupants();
						//3. Gets all Occupants of a clearing, and cycles through them. (occupant k)
						for(int k = 0; k < occupants.size(); k++){
							CharacterName character = occupants.get(k).getCharacter().getName();
							pic = ImageIO.read(this.getClass().getResource(Utility.getCharacterImage(character)));
							charLbls[chars] = new JLabel(new ImageIcon(pic));
							charLbls[chars].setBounds(b.tiles.get(i).getX() + clearings.get(j).xOffset - 25,
									b.tiles.get(i).getY() + clearings.get(j).yOffset - 25, 50, 50);
							boardPanel.add(charLbls[chars], new Integer(5), 0);
								
							if (!clearingHoverOvers.containsKey(clearings.get(j))){
								JPanel newPane = new JPanel();
								newPane.setBounds(b.tiles.get(i).getX() + clearings.get(j).xOffset,
										b.tiles.get(i).getY() + clearings.get(j).yOffset, 200, 300);
								newPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
								newPane.setBorder(new LineBorder(Color.GRAY));
								newPane.setVisible(false);
								
								JLabel lbl = new JLabel(clearings.get(j).parent.getName() + " " + clearings.get(j).location, 
										SwingConstants.CENTER);
								lbl.setPreferredSize(new Dimension(200, 20));
								lbl.setAlignmentX(CENTER_ALIGNMENT);
								lbl.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
								newPane.add(lbl);
								
								boardPanel.add(newPane, new Integer(10), 0);
								clearingHoverOvers.put(clearings.get(j), newPane);
							}
							
							JPanel panel = new JPanel();
							panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
							panel.setPreferredSize(new Dimension(90, 75));
							
							JLabel img = new JLabel(new ImageIcon(pic));
							img.setSize(50, 50);
							panel.add(img);
								
							JLabel lbl = new JLabel(character.toString(), SwingConstants.CENTER);
							lbl.setPreferredSize(new Dimension(90, 15));
							lbl.setAlignmentY(CENTER_ALIGNMENT);
							panel.add(lbl);
							
							clearingHoverOvers.get(clearings.get(j)).add(panel);
							
							final int index = j;
							
							charLbls[chars].addMouseListener(new MouseAdapter() {
								@Override
								public void mouseEntered(MouseEvent e) {
									clearingHoverOvers.get(clearings.get(index)).setVisible(true);
								}
							});
							charLbls[chars].addMouseListener(new MouseAdapter() {
								@Override
								public void mouseExited(MouseEvent e) {
									clearingHoverOvers.get(clearings.get(index)).setVisible(false);
								}
							});
							chars++;
						}
					
						//4.go through the monsters in the clearing, monster k
						ArrayList<Monster> monsters = clearings.get(j).getMonsters();
						for(int k = 0; k < monsters.size(); k++){
							MonsterName monster = monsters.get(k).getName();
							pic = ImageIO.read(this.getClass().getResource(Utility.getMonsterImage(monster)));
							JLabel l = new JLabel(new ImageIcon(pic));
							l.setBounds(b.tiles.get(i).getX() + clearings.get(j).xOffset - 25,
									b.tiles.get(i).getY() + clearings.get(j).yOffset - 25, 50, 50);
							boardPanel.add(l, new Integer(5), 0);
								
							if (!clearingHoverOvers.containsKey(clearings.get(j))){
								JPanel newPane = new JPanel();
								newPane.setBounds(b.tiles.get(i).getX() + clearings.get(j).xOffset,
										b.tiles.get(i).getY() + clearings.get(j).yOffset, 200, 300);
								newPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
								newPane.setBorder(new LineBorder(Color.GRAY));
								newPane.setVisible(false);
								
								JLabel lbl = new JLabel(clearings.get(j).parent.getName() + " " + clearings.get(j).location, 
										SwingConstants.CENTER);
								lbl.setPreferredSize(new Dimension(200, 20));
								lbl.setAlignmentX(CENTER_ALIGNMENT);
								lbl.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
								newPane.add(lbl);
								
								boardPanel.add(newPane, new Integer(10), 0);
								clearingHoverOvers.put(clearings.get(j), newPane);
							}
							
							JPanel panel = new JPanel();
							panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
							panel.setPreferredSize(new Dimension(90, 75));
							
							JLabel img = new JLabel(new ImageIcon(pic));
							img.setSize(50, 50);
							panel.add(img);
							
							JLabel lbl = new JLabel(monster.toString(), SwingConstants.CENTER);
							lbl.setPreferredSize(new Dimension(90, 15));
							lbl.setAlignmentY(CENTER_ALIGNMENT);
							panel.add(lbl);
							
							clearingHoverOvers.get(clearings.get(j)).add(panel);
							
							final int index = j;
							
							l.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseEntered(MouseEvent e) {
									clearingHoverOvers.get(clearings.get(index)).setVisible(true);
								}
							});
							l.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseExited(MouseEvent e) {
									clearingHoverOvers.get(clearings.get(index)).setVisible(false);
								}
							});
							
							labels.add(l);
						}
					}
					if(control.model.getPlayer() != null){
						ArrayList<MapChit> mapChits = b.tiles.get(i).getMapChit();
						for(int j = 0; j < mapChits.size(); j++){
							String lblString;
							if(control.model.getPlayer().knowsSound(mapChits.get(j))){
								pic = ImageIO.read(this.getClass().getResource(Utility.getSoundImage(mapChits.get(j).getName())));
								lblString = mapChits.get(j).getName().toString();
							}
							else {
								pic = ImageIO.read(this.getClass().getResource("/images/facedownsound.jpg"));
								lblString = "Sound Chit";
							}
							
							JLabel l = new JLabel(new ImageIcon(pic));
							l.setBounds(b.tiles.get(i).getX() - 25, b.tiles.get(i).getY() - 25, 50, 50);
							boardPanel.add(l, new Integer(5), 0);
								
							if (!tileHoverOvers.containsKey(b.tiles.get(i))){
								JPanel newPane = new JPanel();
								newPane.setBounds(b.tiles.get(i).getX(), b.tiles.get(i).getY(), 200, 300);
								newPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
								newPane.setBorder(new LineBorder(Color.GRAY));
								newPane.setVisible(false);
								
								JLabel lbl = new JLabel(b.tiles.get(i).getName().toString(), SwingConstants.CENTER);
								lbl.setPreferredSize(new Dimension(200, 20));
								lbl.setAlignmentX(CENTER_ALIGNMENT);
								lbl.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
								newPane.add(lbl);
								
								boardPanel.add(newPane, new Integer(10), 0);
								tileHoverOvers.put(b.tiles.get(i), newPane);
							}
							
							JPanel panel = new JPanel();
							panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
							panel.setPreferredSize(new Dimension(90, 75));
							
							JLabel img = new JLabel(new ImageIcon(pic));
							img.setSize(50, 50);
							panel.add(img);
							
							JLabel lbl = new JLabel(lblString, SwingConstants.CENTER);
							lbl.setPreferredSize(new Dimension(90, 15));
							lbl.setAlignmentY(CENTER_ALIGNMENT);
							panel.add(lbl);
							
							tileHoverOvers.get(b.tiles.get(i)).add(panel);
							
							final int index = i;
							
							l.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseEntered(MouseEvent e) {
									tileHoverOvers.get(b.tiles.get(index)).setVisible(true);
								}
							});
							l.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseExited(MouseEvent e) {
									tileHoverOvers.get(b.tiles.get(index)).setVisible(false);
								}
							});
							
							labels.add(l);
						}
						WarningChit warningChit = b.tiles.get(i).getWarningChit();
						if(warningChit != null){
							String lblString;
							if(control.model.getPlayer().knowsWarning(warningChit)){
								pic = ImageIO.read(this.getClass().getResource(Utility.getWarningImage(warningChit.getName(), b.tiles.get(i).getType())));
								lblString = warningChit.getName().toString();
							}
							else {
								pic = ImageIO.read(this.getClass().getResource("/images/facedownwarning.jpg"));
								lblString = "Warning Chit";
							}
							
							JLabel l = new JLabel(new ImageIcon(pic));
							l.setBounds(b.tiles.get(i).getX() - 25, b.tiles.get(i).getY() - 25, 50, 50);
							boardPanel.add(l, new Integer(5), 0);
								
							if (!tileHoverOvers.containsKey(b.tiles.get(i))){
								JPanel newPane = new JPanel();
								newPane.setBounds(b.tiles.get(i).getX(), b.tiles.get(i).getY(), 200, 300);
								newPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
								newPane.setBorder(new LineBorder(Color.GRAY));
								newPane.setVisible(false);
								
								JLabel lbl = new JLabel(b.tiles.get(i).getName().toString(), SwingConstants.CENTER);
								lbl.setPreferredSize(new Dimension(200, 20));
								lbl.setAlignmentX(CENTER_ALIGNMENT);
								lbl.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
								newPane.add(lbl);
								
								boardPanel.add(newPane, new Integer(10), 0);
								tileHoverOvers.put(b.tiles.get(i), newPane);
							}
							
							JPanel panel = new JPanel();
							panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
							panel.setPreferredSize(new Dimension(90, 75));
							
							JLabel img = new JLabel(new ImageIcon(pic));
							img.setSize(50, 50);
							panel.add(img);
							
							JLabel lbl = new JLabel(lblString, SwingConstants.CENTER);
							lbl.setPreferredSize(new Dimension(90, 15));
							lbl.setAlignmentY(CENTER_ALIGNMENT);
							panel.add(lbl);
							
							tileHoverOvers.get(b.tiles.get(i)).add(panel);
							
							final int index = i;
							
							l.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseEntered(MouseEvent e) {
									tileHoverOvers.get(b.tiles.get(index)).setVisible(true);
								}
							});
							l.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseExited(MouseEvent e) {
									tileHoverOvers.get(b.tiles.get(index)).setVisible(false);
								}
							});
							
							labels.add(l);
						}
					}
				} catch (IOException e){
					
				}
			}
			for (int i = 0; i < b.garrisons.size(); i++){
				try {
					GarrisonName name = b.garrisons.get(i).getName();
					pic = ImageIO.read(this.getClass().getResource(Utility.getGarrisonImage(name)));
					final Clearing clearing = b.garrisons.get(i).getLocation();
					
					if (!clearingHoverOvers.containsKey(clearing)){
						JPanel newPane = new JPanel();
						newPane.setBounds(clearing.parent.getX() + clearing.xOffset, clearing.parent.getY() + clearing.yOffset, 200, 300);
						newPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
						newPane.setBorder(new LineBorder(Color.GRAY));
						newPane.setVisible(false);
						
						JLabel lbl = new JLabel(clearing.parent.getName() + " " + clearing.location, SwingConstants.CENTER);
						lbl.setPreferredSize(new Dimension(200, 20));
						lbl.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
						lbl.setAlignmentX(CENTER_ALIGNMENT);
						newPane.add(lbl);
						
						boardPanel.add(newPane, new Integer(10), 0);
						clearingHoverOvers.put(clearing, newPane);
					}
					
					JPanel panel = new JPanel();
					panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
					panel.setPreferredSize(new Dimension(90, 75));
					
					JLabel img = new JLabel(new ImageIcon(pic));
					img.setSize(50, 50);
					panel.add(img);
					
					JLabel lbl = new JLabel(name.toString(), SwingConstants.CENTER);
					lbl.setPreferredSize(new Dimension(90, 15));
					lbl.setAlignmentY(CENTER_ALIGNMENT);
					panel.add(lbl);
					
					clearingHoverOvers.get(clearing).add(panel);
					
					MouseListener[] listeners = garrisonLbls[i].getMouseListeners();
					
					//must remove old listeners to prevent trying to show hover overs that no longer exist.
					for(int j = 0; j < listeners.length; j ++){
						garrisonLbls[i].removeMouseListener(listeners[j]);
					}
					
					garrisonLbls[i].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							clearingHoverOvers.get(clearing).setVisible(true);
						}
					});
					garrisonLbls[i].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseExited(MouseEvent e) {
							clearingHoverOvers.get(clearing).setVisible(false);
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
			scrollPanel.getVerticalScrollBar().setValue(p.getLocation().yOffset + 
					p.getLocation().parent.getY() - 250);
			scrollPanel.getHorizontalScrollBar().setValue(p.getLocation().xOffset + 
					p.getLocation().parent.getX() - 400);
			
		}
		updateNonBoardGUI(p);
	}
	
	public void updateNonBoardGUI(Player p){

		//UPDATES THE TEXTBOX
		textDisplay.setText(control.model.getMessages());

		//UPDATES THE INPUT PANEL BASED ON IT'S TYPE
		switch(control.state){
		case CHOOSE_CHARACTER:
			characterSelectPanel.update();
			makePanelVisible(characterSelectPanel);
			scrollPanel.setViewportView(characterDetailsPanel);//this must be called AFTER makePanelVisible
			break;
		case CHOOSE_PLAYS:
			playsPanel.update();
			makePanelVisible(playsPanel);
			break;
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
		if(newPanel != characterSelectPanel)
			characterSelectPanel.setVisible(false);
		scrollPanel.setViewportView(boardPanel);
	}
	
	public CharacterDetailsPanel getCharacterDetailsPanel() {
		return characterDetailsPanel;
	}
}
