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
import utils.Utility.SoundChits;
import utils.Utility.TileName;
import utils.Utility.TileType;
import utils.Utility.TreasureLocations;
import utils.Utility.WarningChits;
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
	private HashMap<TileName, JPanel> tileHoverOvers;
	private HashMap<TileName, JPanel> tilePopUps;
	private HashMap<Clearing, JPanel> clearingHoverOvers;
	
	private HashMap<TileName, ImageIcon> tilePics;
	private HashMap<GarrisonName, ImageIcon> garrisonPics;
	private HashMap<CharacterName, ImageIcon> characterPics;
	private HashMap<MonsterName, ImageIcon> monsterPics;
	private HashMap<SoundChits, ImageIcon> soundPics;
	private HashMap<TreasureLocations, ImageIcon> treasurePics;
	private HashMap<WarningChits, ImageIcon> warningCPics;
	private HashMap<WarningChits, ImageIcon> warningMPics;
	private HashMap<WarningChits, ImageIcon> warningVPics;
	private HashMap<WarningChits, ImageIcon> warningWPics;
	private ImageIcon faceDownWarning;
	private ImageIcon faceDownMap;
	
	
	//These are stored separately because they will only be created once.
	private JLabel [] tileLbls;
	private JLabel [] garrisonLbls;

	//labels for character and monster images
	private ArrayList<JLabel> labels;
	private ArrayList<JLabel> treasureLbls;
	private ArrayList<JLabel> soundLbls;
	private ArrayList<JLabel> warningLbls;
	
	private boolean boardMade;
	
	public BoardPanel(ClientController control) {
		this.control = control;
		this.boardMade = false;
		this.labels = new ArrayList<JLabel>();
		this.treasureLbls = new ArrayList<JLabel>();
		this.soundLbls = new ArrayList<JLabel>();
		this.warningLbls = new ArrayList<JLabel>();
		this.tileHoverOvers = new HashMap<TileName, JPanel>();
		this.tilePopUps = new HashMap<TileName, JPanel>();
		this.clearingHoverOvers = new HashMap<Clearing, JPanel>();
		setPreferredSize(new Dimension(1100, 1318));
	}
	
	//This function sets it up so that all of the board elements that don't move.
	//Tiles, garrisons, warning, sound, and map chits.
	private void makeBoard(){
		loadImages();//All of the images will now be loaded, this is only called once.
		
		Board b = control.model.getBoard();
		tileLbls = new JLabel[b.tiles.size()];
		garrisonLbls = new JLabel[b.garrisons.size()];

		for (int i = 0; i < b.tiles.size(); i++){
			tileLbls[i] = new JLabel(tilePics.get(b.tiles.get(i).getName()));
			tileLbls[i].setBounds(b.tiles.get(i).getX() - 100, b.tiles.get(i).getY() - 86, 200, 173);
			add(tileLbls[i]);
			
			JPanel ho = new JPanel();
			ho.setBounds(b.tiles.get(i).getX() + 55, b.tiles.get(i).getY() - 233 , 200, 210);
			ho.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			ho.setBorder(new LineBorder(Color.GRAY));
			ho.setVisible(false);
			
			JLabel tlbl = new JLabel(b.tiles.get(i).getName().toString(), SwingConstants.CENTER);
			tlbl.setPreferredSize(new Dimension(200, 20));
			tlbl.setAlignmentX(CENTER_ALIGNMENT);
			tlbl.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
			ho.add(tlbl);
			
			JLabel timg = new JLabel(tilePics.get(b.tiles.get(i).getName()));
			timg.setPreferredSize(new Dimension(200, 173));
			ho.add(timg);
			
			add(ho, new Integer(10), 0);
			tilePopUps.put(b.tiles.get(i).getName(), ho);
			
			final int index = i;
			
			tileLbls[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					tilePopUps.get(b.tiles.get(index).getName()).setVisible(true);
				}
			});
			
			tileLbls[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent e) {
					tilePopUps.get(b.tiles.get(index).getName()).setVisible(false);
				}
			});
			
			ArrayList<MapChit> mapChits = b.tiles.get(i).getMapChit();
			for(int j = 0; j < mapChits.size(); j++){
					
				JLabel l = new JLabel(faceDownMap);
				l.setBounds(b.tiles.get(i).getX() - 25, b.tiles.get(i).getY() - 25, 50, 50);
				add(l, new Integer(5), 0);
					
				if (!tileHoverOvers.containsKey(b.tiles.get(i).getName())){
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
					tileHoverOvers.put(b.tiles.get(i).getName(), newPane);
				}
				
				JPanel panel = new JPanel();
				panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				panel.setPreferredSize(new Dimension(90, 75));
				
				JLabel img = new JLabel(faceDownMap);
				img.setSize(50, 50);
				panel.add(img);
				
				JLabel lbl = new JLabel("Map Chit", SwingConstants.CENTER);
				lbl.setPreferredSize(new Dimension(90, 15));
				lbl.setAlignmentY(CENTER_ALIGNMENT);
				panel.add(lbl);
				
				tileHoverOvers.get(b.tiles.get(i).getName()).add(panel);
			
				//Makes center tile hoverover appear
				l.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						tileHoverOvers.get(b.tiles.get(index).getName()).setVisible(true);
					}
				});
				//makes center tile hoverover disappear
				l.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseExited(MouseEvent e) {
						tileHoverOvers.get(b.tiles.get(index).getName()).setVisible(false);
					}
				});
				
				if((mapChits.get(j) instanceof SiteChit)){
					treasureLbls.add(l);
				}
				else {
					soundLbls.add(l);
				}
			}
			
			WarningChit warningChit = b.tiles.get(i).getWarningChit();
			if(warningChit != null){
				JLabel l = new JLabel(faceDownWarning);
				l.setBounds(b.tiles.get(i).getX() - 25, b.tiles.get(i).getY() - 25, 50, 50);
				add(l, new Integer(5), 0);
					
				if (!tileHoverOvers.containsKey(b.tiles.get(i).getName())){
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
					tileHoverOvers.put(b.tiles.get(i).getName(), newPane);
				}
				
				JPanel panel = new JPanel();
				panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				panel.setPreferredSize(new Dimension(90, 75));
				
				JLabel img = new JLabel(faceDownWarning);
				img.setSize(50, 50);
				panel.add(img);
				
				JLabel lbl = new JLabel("Warning Chit", SwingConstants.CENTER);
				lbl.setPreferredSize(new Dimension(90, 15));
				lbl.setAlignmentY(CENTER_ALIGNMENT);
				panel.add(lbl);
				
				tileHoverOvers.get(b.tiles.get(i).getName()).add(panel);
				
				l.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						tileHoverOvers.get(b.tiles.get(index).getName()).setVisible(true);
					}
				});
				l.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseExited(MouseEvent e) {
						tileHoverOvers.get(b.tiles.get(index).getName()).setVisible(false);
					}
				});
				
				warningLbls.add(l);
			}
		}
		
		for (int i = 0; i < b.garrisons.size(); i++){
			GarrisonName name = b.garrisons.get(i).getName();
			garrisonLbls[i] = new JLabel(garrisonPics.get(name));
			garrisonLbls[i].setBounds(b.garrisons.get(i).getLocation().x - 25, b.garrisons.get(i).getLocation().y - 21, 50, 43);
			add(garrisonLbls[i], new Integer(5), 0);
			
		}
		
		boardMade = true;
	}
	
	@SuppressWarnings("rawtypes")
	public void update(){
		System.out.println("In boardPanel update");
		//1. GETS THE BOARD FROM THE MODEL
		Board b = control.model.getBoard();
		if (b != null) {
			int wCount = 0;
			int tCount = 0;
			int sCount = 0;
			int tileItems = 0;
			
			//ENSURES THE BOARD IS ONLY DRAWN ONCE
			if (!this.boardMade){
				makeBoard();
			}
			
			Iterator it = clearingHoverOvers.entrySet().iterator();
			
			while (it.hasNext()) {
				JPanel panel = (JPanel)((HashMap.Entry)it.next()).getValue();
				if(panel != null){
					remove(panel);
				}
			}

			clearingHoverOvers.clear();
			
			///REMOVES ALL LABELS ALREADY DRAWN (CHARACTERS and monsters)
			for(int i = 0; i < labels.size(); i++){
				remove(labels.get(i));
			}
			
			labels.clear();
			
			//1. Iterates through every single tile in the board (always 20 TBH), (tile i)
			for (int i = 0; i < b.tiles.size(); i++){
				tileItems = 1;
				ArrayList<Clearing> clearings = b.tiles.get(i).getClearings();
				//2. Gets all clearings on a given tile, then cycles through all of them, (clearing j)
				for(int j = 0; j < clearings.size(); j++){
					ArrayList<Player> occupants = clearings.get(j).getOccupants();
					//3. Gets all Occupants of a clearing, and cycles through them. (occupant k)
					for(int k = 0; k < occupants.size(); k++){
						
						//adds the character label
						CharacterName character = occupants.get(k).getCharacter().getName();
						JLabel l = new JLabel(characterPics.get(character));
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
						
						JLabel img = new JLabel(characterPics.get(character));
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
						JLabel l = new JLabel(monsterPics.get(monster));
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
						
						JLabel img = new JLabel(monsterPics.get(monster));
						img.setSize(50, 50);
						panel.add(img);
						
						JLabel lbl = new JLabel(monster.toString(), SwingConstants.CENTER);
						lbl.setPreferredSize(new Dimension(90, 15));
						lbl.setAlignmentY(CENTER_ALIGNMENT);
						panel.add(lbl);
						
						clearingHoverOvers.get(clearings.get(j)).add(panel);
						
						final int index = j;
						
						//Make clearing hover over appear
						l.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseEntered(MouseEvent e) {
								clearingHoverOvers.get(clearings.get(index)).setVisible(true);
							}
						});
						//Make clearing hover over DISAPPEAR
						l.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseExited(MouseEvent e) {
								clearingHoverOvers.get(clearings.get(index)).setVisible(false);
							}
						});
						
						labels.add(l);//adds the label to the labels array
					}
				}
				//iterating over tiles now, tile i.
				if(control.model.getPlayer() != null){
					ImageIcon icon;
					String lblString;
					ArrayList<MapChit> mapChits = b.tiles.get(i).getMapChit();
					for(int j = 0; j < mapChits.size(); j++){
						if((mapChits.get(j)) instanceof SiteChit){
							if(treasureLbls.get(tCount).getIcon() == faceDownMap
									&& control.model.getPlayer().knowsSite((SiteChit)mapChits.get(j))){
								icon = treasurePics.get(((SiteChit)mapChits.get(j)).getLocation());
								lblString = ((SiteChit)mapChits.get(j)).getLocation().toString();
								treasureLbls.get(tCount).setIcon(icon);
								((JLabel)((JPanel)tileHoverOvers.get(b.tiles.get(i).getName()).getComponent(tileItems))
								.getComponent(0)).setIcon(icon);
								((JLabel)((JPanel)tileHoverOvers.get(b.tiles.get(i).getName()).getComponent(tileItems))
										.getComponent(1)).setText(lblString);
							}
							tCount++;
						}
						else{
							if(soundLbls.get(sCount).getIcon() == faceDownMap
									&& control.model.getPlayer().knowsSound(mapChits.get(j))){
								icon = soundPics.get(mapChits.get(j).getName());
								lblString = mapChits.get(j).getName().toString();
								soundLbls.get(sCount).setIcon(icon);
								((JLabel)((JPanel)tileHoverOvers.get(b.tiles.get(i).getName()).getComponent(tileItems))
								.getComponent(0)).setIcon(icon);
								((JLabel)((JPanel)tileHoverOvers.get(b.tiles.get(i).getName()).getComponent(tileItems))
										.getComponent(1)).setText(lblString);
							}
							sCount++;
						}
						tileItems++;
					}
					
					WarningChit warningChit = b.tiles.get(i).getWarningChit();
					
					if(warningLbls.get(wCount).getIcon() == faceDownWarning
							&& control.model.getPlayer().knowsWarning(warningChit)){
						switch(warningChit.getTile().getType()){
						case CAVES:
							icon = warningCPics.get(warningChit.getName());
							break;
						case MOUNTAINS:
							icon = warningMPics.get(warningChit.getName());
							break;
						case VALLEY:
							icon = warningVPics.get(warningChit.getName());
							break;
						case WOODS:
							icon = warningWPics.get(warningChit.getName());
							break;
						default:
							icon = null;
							break;
						}
						lblString = warningChit.getName().toString();
						warningLbls.get(wCount).setIcon(icon);
						((JLabel)((JPanel)tileHoverOvers.get(b.tiles.get(i).getName()).getComponent(tileItems))
						.getComponent(0)).setIcon(icon);
						((JLabel)((JPanel)tileHoverOvers.get(b.tiles.get(i).getName()).getComponent(tileItems))
								.getComponent(1)).setText(lblString);
					}
					wCount++;
					tileItems++;
				}
			}
			for (int i = 0; i < b.garrisons.size(); i++){
				GarrisonName name = b.garrisons.get(i).getName();
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
				
				JLabel img = new JLabel(garrisonPics.get(name));
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
			}	
		}	
	}
	
	private void loadImages(){
		BufferedImage pic;
		String ps;
		
		characterPics = new HashMap<CharacterName, ImageIcon>();
		for(CharacterName c: CharacterName.values()){
			ps = Utility.getCharacterImage(c);
			if(ps != null){
				try{
					pic = ImageIO.read(this.getClass().getResource(ps));
					characterPics.put(c, new ImageIcon(pic));
				}
				catch(IOException e){
					System.out.println("Cant find image for " + c.toString());
				}
			}
		}
		
		garrisonPics = new HashMap<GarrisonName, ImageIcon>();	
		for(GarrisonName g: GarrisonName.values()){
			ps = Utility.getGarrisonImage(g);
			if(ps != null){
				try{
					pic = ImageIO.read(this.getClass().getResource(ps));
					garrisonPics.put(g, new ImageIcon(pic));
				}
				catch(IOException e){
					System.out.println("Cant find image for " + g.toString());
				}
			}
		}
		
		monsterPics = new HashMap<MonsterName, ImageIcon>();
		for(MonsterName m: MonsterName.values()){
			ps = Utility.getMonsterImage(m);
			if(ps != null){
				try{
					pic = ImageIO.read(this.getClass().getResource(ps));
					monsterPics.put(m, new ImageIcon(pic));
				}
				catch(IOException e){
					System.out.println("Cant find image for " + m.toString());
				}
			}
		}
		
		soundPics = new HashMap<SoundChits, ImageIcon>();
		for(SoundChits s: SoundChits.values()){
			ps = Utility.getSoundImage(s);
			if(ps != null){
				try{
					pic = ImageIO.read(this.getClass().getResource(ps));
					soundPics.put(s, new ImageIcon(pic));
				}
				catch(IOException e){
					System.out.println("Cant find image for " + s.toString());
				}
			}
		}
		
		tilePics = new HashMap<TileName, ImageIcon>();
		for (Tile t: control.model.getBoard().tiles){
			ps = Utility.getTileImage(t.getName());
			if(ps != null){
				try {
					pic = ImageIO.read(this.getClass().getResource(ps));
					pic = ImageUtils.tilt(pic, Math.toRadians(t.getRot()));
					tilePics.put(t.getName(), new ImageIcon(pic));
				}catch (IOException e){
					System.out.println("Can't find image for " + t);
				}
			}
		}
		
		treasurePics = new HashMap<TreasureLocations, ImageIcon>();
		for(TreasureLocations t: TreasureLocations.values()){
			ps = Utility.getSiteImage(t);
			if(ps != null){
				try{
					pic = ImageIO.read(this.getClass().getResource(ps));
					treasurePics.put(t, new ImageIcon(pic));
				}
				catch(IOException e){
					System.out.println("Cant find image for " + t.toString());
				}
			}
		}
		
		warningCPics = new HashMap<WarningChits, ImageIcon>();
		for(WarningChits c: WarningChits.values()){
			ps = Utility.getWarningImage(c, TileType.CAVES);
			if(ps != null){
				try{
					pic = ImageIO.read(this.getClass().getResource(ps));
					warningCPics.put(c, new ImageIcon(pic));
				}
				catch(IOException e){
					System.out.println("Cant find image for " + c.toString());
				}
			}
		}
		
		warningMPics = new HashMap<WarningChits, ImageIcon>();
		for(WarningChits c: WarningChits.values()){
			ps = Utility.getWarningImage(c, TileType.MOUNTAINS);
			if(ps != null){
				try{
					pic = ImageIO.read(this.getClass().getResource(ps));
					warningMPics.put(c, new ImageIcon(pic));
				}
				catch(IOException e){
					System.out.println("Cant find image for " + c.toString());
				}
			}
		}
		
		warningVPics = new HashMap<WarningChits, ImageIcon>();
		for(WarningChits c: WarningChits.values()){
			ps = Utility.getWarningImage(c, TileType.VALLEY);
			if(ps != null){
				try{
					pic = ImageIO.read(this.getClass().getResource(ps));
					warningVPics.put(c, new ImageIcon(pic));
				}
				catch(IOException e){
					System.out.println("Cant find image for " + c.toString());
				}
			}
		}
		
		warningWPics = new HashMap<WarningChits, ImageIcon>();
		for(WarningChits c: WarningChits.values()){
			ps = Utility.getWarningImage(c, TileType.WOODS);
			if(ps != null){
				try{
					pic = ImageIO.read(this.getClass().getResource(ps));
					warningWPics.put(c, new ImageIcon(pic));
				}
				catch(IOException e){
					System.out.println("Cant find image for " + c.toString());
				}
			}
		}
		
		try{
			faceDownMap = new ImageIcon(ImageIO.read(this.getClass().getResource("/images/facedownsound.jpg")));
		}
		catch(IOException e){
			System.out.println("Can't find image for face down map chit");
				
		}
		
		try{
			faceDownWarning = new ImageIcon(ImageIO.read(this.getClass().getResource("/images/facedownwarning.jpg")));
		}
		catch(IOException e){
			System.out.println("Can't find image for face down warning chit");
				
		}
	}
}
