package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import utils.ImageUtils;
import utils.Utility;
import utils.Utility.CharacterName;
import utils.Utility.GarrisonName;
import utils.Utility.MonsterName;
import utils.Utility.TileName;
import controller.ClientController;
import model.Board;
import model.Clearing;
import model.MapChit;
import model.Monster;
import model.Player;
import model.SiteChit;
import model.Tile;
import model.WarningChit;

@SuppressWarnings("serial")
public class BoardPanel extends JLayeredPane {

	private ClientController control;
	private HashMap<Tile, JPanel> tileHoverOvers = new HashMap<Tile, JPanel>();
	private HashMap<Clearing, JPanel> clearingHoverOvers = new HashMap<Clearing, JPanel>();
	
	//These are stored separately because they will only be created once.
	private JLabel [] tileLbls;
	private JLabel [] garrisonLbls;

	//labels for chits and character images
	private ArrayList<JLabel> labels;
	
	private boolean boardMade;
	
	public BoardPanel(ClientController control) {
		this.control = control;
		this.boardMade = false;
		this.labels = new ArrayList<JLabel>();
		setPreferredSize(new Dimension(1100, 1318));
	}
	
	private void makeBoard(){
		Board b = control.model.getBoard();
		if (b != null) {
			tileLbls = new JLabel[b.tiles.size()];
			garrisonLbls = new JLabel[b.garrisons.size()];
			
			BufferedImage pic;
			for (int i = 0; i < b.tiles.size(); i++){
				try {
					TileName name = b.tiles.get(i).getName();
					pic = ImageIO.read(this.getClass().getResource(Utility.getTileImage(name)));
					pic = ImageUtils.tilt(pic, Math.toRadians(b.tiles.get(i).getRot()));
					tileLbls[i] = new JLabel(new ImageIcon(pic));
					tileLbls[i].setBounds(b.tiles.get(i).getX() - 100, b.tiles.get(i).getY() - 86, 200, 173);
					add(tileLbls[i]);
				}catch (IOException e){
					
				}
			}
			
			for (int i = 0; i < b.garrisons.size(); i++){
				try {
					GarrisonName name = b.garrisons.get(i).getName();
					pic = ImageIO.read(this.getClass().getResource(Utility.getGarrisonImage(name)));
					garrisonLbls[i] = new JLabel(new ImageIcon(pic));
					garrisonLbls[i].setBounds(b.garrisons.get(i).getLocation().x - 25, b.garrisons.get(i).getLocation().y - 21, 50, 43);
					add(garrisonLbls[i], new Integer(5), 0);
				} catch (IOException e){
					
				}
			}
		}
		
		boardMade = true;
	}
	
	@SuppressWarnings("rawtypes")
	public void update(){
		Board b = control.model.getBoard();
		if (b != null) {
			
			//ENSURE TH BOARD IS ONLY DRAWN ONCE
			if (!this.boardMade){
				makeBoard();
			}
			
			Iterator it = tileHoverOvers.entrySet().iterator();
			
			while (it.hasNext()) {
				JPanel panel = (JPanel)((HashMap.Entry)it.next()).getValue();
				if(panel != null){
					remove(panel);
				}
			}

			tileHoverOvers.clear();
			
			it = clearingHoverOvers.entrySet().iterator();
			
			while (it.hasNext()) {
				JPanel panel = (JPanel)((HashMap.Entry)it.next()).getValue();
				if(panel != null){
					remove(panel);
				}
			}

			clearingHoverOvers.clear();
			
			for(int i = 0; i < labels.size(); i++){
				remove(labels.get(i));
			}
			
			labels.clear();
			
			BufferedImage pic;
			
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
							JLabel l = new JLabel(new ImageIcon(pic));
							l.setBounds(clearings.get(j).x - 25, clearings.get(j).y - 25, 50, 50);
							add(l, new Integer(5), 0);
								
							if (!clearingHoverOvers.containsKey(clearings.get(j))){
								JPanel newPane = new JPanel();
								newPane.setBounds(clearings.get(j).x, clearings.get(j).y, 200, 300);
								newPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
								newPane.setBorder(new LineBorder(Color.GRAY));
								newPane.setVisible(false);
								
								JLabel lbl = new JLabel(clearings.get(j).parent.getName() + " " + clearings.get(j).location, 
										SwingConstants.CENTER);
								lbl.setPreferredSize(new Dimension(200, 20));
								lbl.setAlignmentX(CENTER_ALIGNMENT);
								lbl.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
								newPane.add(lbl);
								
								add(newPane, new Integer(10), 0);
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
					
						//4.go through the monsters in the clearing, monster k
						ArrayList<Monster> monsters = clearings.get(j).getMonsters();
						for(int k = 0; k < monsters.size(); k++){
							MonsterName monster = monsters.get(k).getName();
							pic = ImageIO.read(this.getClass().getResource(Utility.getMonsterImage(monster)));
							JLabel l = new JLabel(new ImageIcon(pic));
							l.setBounds(clearings.get(j).x - 25, clearings.get(j).y - 25, 50, 50);
							add(l, new Integer(5), 0);
								
							if (!clearingHoverOvers.containsKey(clearings.get(j))){
								JPanel newPane = new JPanel();
								newPane.setBounds(clearings.get(j).x, clearings.get(j).y, 200, 300);
								newPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
								newPane.setBorder(new LineBorder(Color.GRAY));
								newPane.setVisible(false);
								
								JLabel lbl = new JLabel(clearings.get(j).parent.getName() + " " + clearings.get(j).location, 
										SwingConstants.CENTER);
								lbl.setPreferredSize(new Dimension(200, 20));
								lbl.setAlignmentX(CENTER_ALIGNMENT);
								lbl.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
								newPane.add(lbl);
								
								add(newPane, new Integer(10), 0);
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
							if(mapChits.get(j) instanceof SiteChit){
								String lblString;
								if(control.model.getPlayer().knowsSite((SiteChit)mapChits.get(j))){
									pic = ImageIO.read(this.getClass().getResource(Utility.getSiteImage(((SiteChit) mapChits.get(j)).getLocation())));
									lblString = ((SiteChit)mapChits.get(j)).getLocation().toString();
								}
								else {
									continue;
									//pic = ImageIO.read(this.getClass().getResource("/images/facedownsound.jpg"));
									//lblString = "Sound Chit";
								}
								
								JLabel l = new JLabel(new ImageIcon(pic));
								l.setBounds(b.tiles.get(i).getX() - 25, b.tiles.get(i).getY() - 25, 50, 50);
								add(l, new Integer(5), 0);
									
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
									
									add(newPane, new Integer(10), 0);
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
							else{
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
								add(l, new Integer(5), 0);
									
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
									
									add(newPane, new Integer(10), 0);
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
							add(l, new Integer(5), 0);
								
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
								
								add(newPane, new Integer(10), 0);
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
						newPane.setBounds(clearing.x, clearing.y, 200, 300);
						newPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
						newPane.setBorder(new LineBorder(Color.GRAY));
						newPane.setVisible(false);
						
						JLabel lbl = new JLabel(clearing.parent.getName() + " " + clearing.location, SwingConstants.CENTER);
						lbl.setPreferredSize(new Dimension(200, 20));
						lbl.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
						lbl.setAlignmentX(CENTER_ALIGNMENT);
						newPane.add(lbl);
						
						add(newPane, new Integer(10), 0);
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
	}
}
