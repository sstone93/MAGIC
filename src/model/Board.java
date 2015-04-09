package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import utils.Utility.ClearingType;
import utils.Utility.GarrisonName;
import utils.Utility.LargeTreasureName;
import utils.Utility.LostName;
import utils.Utility.MonsterName;
import utils.Utility.PathType;
import utils.Utility.SmallTreasureName;
import utils.Utility.SoundChits;
import utils.Utility.TileName;
import utils.Utility.TileType;
import utils.Utility.TreasureLocations;
import utils.Utility.TreasureWithinTreasureName;
import utils.Utility.WarningChits;

/**
 *
 * @author Nick
 *
 */
public class Board implements Serializable{

	private static final long serialVersionUID = -4906643706682852990L;
	public ArrayList<Tile> tiles = new ArrayList<Tile>();
	public ArrayList<Garrison> garrisons = new ArrayList<Garrison>();
	public ArrayList<SmallTreasure> small = new ArrayList<SmallTreasure>();
	public ArrayList<Treasure> large = new ArrayList<Treasure>();
	public ArrayList<TreasureWithinTreasure> twit = new ArrayList<TreasureWithinTreasure>();
	public ArrayList<Monster> monsters = new ArrayList<Monster>();

	public Board(ArrayList<Player> players){
		setupBoard();			//creates all of the tiles and clearings. establishes all of the connections
		placeWarningChits();	//creates and places all the warning chits
		initializeMonsters();	
		instanciateGarrisons();	//instanciates the garrisons and populates them with weapons and armour
		instanciateTreasures();	//instanciates all treasures
		
		setUpMapChits();	//places the treasures, places the map chits, places the garrisons
		placeGarrisons();	//reveal all of the VALLEY map chits (after character selection stuff done)
		placePlayers(players);	//places the players based on their starting location
		
	}
	
	public Board(ArrayList<Player> players, Scanner inputScanner){

		setupBoard();		//creates all of the tiles and clearings. establishes all of the connections
		instanciateGarrisons();	//instanciates the garrisons
		instanciateTreasures();
		
		String[] lostCastle= inputScanner.nextLine().split(" ");
		String[] lostCity= inputScanner.nextLine().split(" ");
		
		for(int i=0; i<20; i++){
			String temp = inputScanner.nextLine();
			String[] inputLine = temp.split(" ");
			//System.out.println(temp);
			for(TileName name : TileName.values()){
				if(inputLine[0].toString().equals(name.toString())){
					Tile workingTile = tiles.get(convertTileName(name));
					
					for(int j=1; j< inputLine.length; j++){
						
						//identify what it is, add it (WARNING, SITE, SOUND)
						identifyAndPlaceMapChit(inputLine[j], name, workingTile);
						
						//is it lost castle or lost city?
						for(LostName l : LostName.values()){
							if(inputLine[j].equals(l.toString())){
								
								if(l == LostName.LOST_CASTLE){
									for(int k=1; k<6; k++){
										identifyAndPlaceMapChit(lostCastle[k], name, workingTile);
									}
								}
								if(l == LostName.LOST_CITY){
									for(int k=1; k<6; k++){
										identifyAndPlaceMapChit(lostCity[k], name, workingTile);
									}
								}

							}
						}
						
					}
					
				}
			}
			
		}
		
		initializeMonsters();
		
		//garrisons (chapel, house, inn, guard(house)
		//all 4 take 2 small treasures each
		garrisons.get(0).addTreasures(createTreasureArray(2,0));
		garrisons.get(1).addTreasures(createTreasureArray(2,0));
		garrisons.get(2).addTreasures(createTreasureArray(2,0));
		garrisons.get(3).addTreasures(createTreasureArray(2,0));
		
		placeGarrisons();	//reveal all of the VALLEY map chits (after character selection stuff done)
		placePlayers(players);	//places the players based on their starting location
		
	}

	public void identifyAndPlaceMapChit(String inputLine, TileName name, Tile workingTile){
		//is it a warning chit?
		for(WarningChits w : WarningChits.values()){
			if(inputLine.equals(w.toString())){
				workingTile.setWarningChit(new WarningChit(w));
			}
		}
		
		//is it a sound chit?
		for(SoundChits s : SoundChits.values()){
			if(inputLine.equals(s.toString())){
				workingTile.addMapChit(new MapChit(s));
			}
		}
		
		//is it a site chit?
		//is it a sound chit?
		for(TreasureLocations t : TreasureLocations.values()){
			if(inputLine.equals(t.toString())){
				switch (t){
				case ALTAR:
					placeMapChit(new SiteChit(TreasureLocations.ALTAR, 1), convertTileName(name));
					break;
				case CAIRNS:
					placeMapChit(new SiteChit(TreasureLocations.CAIRNS, 5), convertTileName(name));
					break;
				case HOARD:
					placeMapChit(new SiteChit(TreasureLocations.HOARD, 6), convertTileName(name));
					break;
				case LAIR:
					placeMapChit(new SiteChit(TreasureLocations.LAIR, 3), convertTileName(name));
					break;
				case POOL:
					placeMapChit(new SiteChit(TreasureLocations.POOL, 6), convertTileName(name));
					break;
				case SHRINE:
					placeMapChit(new SiteChit(TreasureLocations.SHRINE, 4), convertTileName(name));
					break;
				case STATUE:
					placeMapChit(new SiteChit(TreasureLocations.STATUE, 2), convertTileName(name));
					break;
				case VAULT:
					placeMapChit(new SiteChit(TreasureLocations.VAULT, 3), convertTileName(name));
					break;
				default:
					break;
				
				}

			}
		}
	}
	

	public int convertTileName(TileName n){
		for(int i=0; i<20; i++){
			if(tiles.get(i).getName() == n){
				return i;
			}
		}
		return -1;
	}

	/**
	 * Creates the board based on the images provided by JP
	 */
	private void setupBoard(){
		//CREATE ALL THE TILES (THIS IS SETUP INTERNAL CLEARINGS AUTOMATICALLY)
		tiles.add(new Tile(TileName.CLIFF, 550, 232, 300.00));
		tiles.add(new Tile(TileName.EVILVALLEY, 403, 317, 120.00));
		tiles.add(new Tile(TileName.LEDGES, 550, 402, 240.00));
		tiles.add(new Tile(TileName.CRAG, 697, 487, 180.00));
		tiles.add(new Tile(TileName.DARKVALLEY, 844, 572, 120.00));
		tiles.add(new Tile(TileName.HIGHPASS, 256, 402, 120.00));
		tiles.add(new Tile(TileName.BORDERLAND, 403, 487, 300.00));
		tiles.add(new Tile(TileName.OAKWOODS, 550, 572, 240.00));
		tiles.add(new Tile(TileName.DEEPWOODS, 697, 657, 0.00));
		tiles.add(new Tile(TileName.CURSTVALLEY, 844, 742, 0.00));
		tiles.add(new Tile(TileName.CAVERN, 256, 572, 60.00));
		tiles.add(new Tile(TileName.BADVALLEY, 403, 657, 300.00));
		tiles.add(new Tile(TileName.MAPLEWOODS, 550, 742, 120.00));
		tiles.add(new Tile(TileName.NUTWOODS, 697, 827, 180.00));
		tiles.add(new Tile(TileName.MOUNTAIN, 256, 742, 300.00));
		tiles.add(new Tile(TileName.CAVES, 403, 827, 120.00));
		tiles.add(new Tile(TileName.RUINS, 550, 912, 120.00));
		tiles.add(new Tile(TileName.AWFULVALLEY, 697, 997, 0.00));
		tiles.add(new Tile(TileName.PINEWOODS, 256, 912, 0.00));
		tiles.add(new Tile(TileName.LINDENWOODS, 550, 1082, 60.00));

		//MANUALLY SET TILE CONNECTIONS (DOES BOTH ENDS IN 1 CONNECT)
		connect(tiles.get(0), tiles.get(1), 1, 2);		//cliff and evil valley
		connect(tiles.get(0), tiles.get(2), 2, 3);		//cliff and ledges
		connect(tiles.get(1), tiles.get(5), 5, 6);		//evil valley and high pass
		connect(tiles.get(1), tiles.get(6), 4, 2);		//evil valley and borderlands
		connect(tiles.get(1), tiles.get(2), 4, 2);		//evil valley and ledges
		connect(tiles.get(2), tiles.get(6), 4, 4);		//ledges and borderlands
		connect(tiles.get(2), tiles.get(7), 5, 2);		//ledges and oakwoods
		connect(tiles.get(3), tiles.get(8), 2, 1);		//crag and deep woods
		connect(tiles.get(4), tiles.get(8), 5, 2);		//dark valley and deep woods
		connect(tiles.get(4), tiles.get(9), 1, 1);		//dark valley and curst valley
		connect(tiles.get(5), tiles.get(10), 3, 5);		//highpass and cavern
		connect(tiles.get(5), tiles.get(6), 2, 1);		//highpass and borderlands
		connect(tiles.get(6), tiles.get(10), 5, 2);		//borderlands and cavern
		connect(tiles.get(6), tiles.get(11), 1, 5);		//borderlands and badvalley
		connect(tiles.get(6), tiles.get(7), 2, 2);		//borderlands and oakwoods
		connect(tiles.get(7), tiles.get(11), 5, 1);		//oakvwoods and bad valley
		connect(tiles.get(7), tiles.get(12), 5, 5);		//oakwoods and maple woods
		connect(tiles.get(7), tiles.get(8), 4, 1);		//oakwoods and deepwoods
		connect(tiles.get(8), tiles.get(12), 5, 5);		//deepwoods and maple woods
		connect(tiles.get(8), tiles.get(9), 2, 2);		//deepwoods and curst valley
		connect(tiles.get(9), tiles.get(13), 4, 5);		//curst valley and nut woods
		connect(tiles.get(10), tiles.get(11), 1, 4);	//cavern and bad valley
		connect(tiles.get(11), tiles.get(14), 4, 5);	//bad valley and mountain
		connect(tiles.get(11), tiles.get(15), 2, 2);	//bad valley and caves
		connect(tiles.get(12), tiles.get(15), 4, 5);	//maple woods and caves
		connect(tiles.get(12), tiles.get(16), 2, 5);	//maple woods and ruins
		connect(tiles.get(12), tiles.get(13), 2, 5);	//maple woods and nut woods
		connect(tiles.get(13), tiles.get(16), 4, 1);	//nut woods and ruins
		connect(tiles.get(13), tiles.get(17), 2, 5);	//nut woods and awful valley
		connect(tiles.get(14), tiles.get(18), 2, 4);	//mountain and pine woods
		connect(tiles.get(15), tiles.get(18), 1, 5);	//caves and pine woods
		connect(tiles.get(16), tiles.get(19), 2, 4);	//ruins and linden woods
		connect(tiles.get(16), tiles.get(17), 2, 1);	//ruins and awful valley
		connect(tiles.get(17), tiles.get(19), 2, 5);	//awful valley and linden woods
	}

	/**
	 * This functions connects 2 tiles together via the supplied clearing numbers
	 * @param t1 the first tile being connected
	 * @param t2 the second tile being connected
	 * @param c1 the clearing number of the clearing being connected on t1
	 * @param c2 the clearing number of the clearing being connected on t2
	 */
	public void connect(Tile t1, Tile t2, int c1, int c2){
		Path temp = new Path(t1.getClearing(c1), t2.getClearing(c2), PathType.OPEN_ROAD);//All tiles connect with open roads
		t1.getClearing(c1).addConnection(temp);
		t2.getClearing(c2).addConnection(temp);
	}

	/**
	 * Instanciates and sets up the garrisons, DOES NOT PLACE THEM
	 */
	public void instanciateGarrisons(){
		this.garrisons.add(new Garrison(GarrisonName.CHAPEL));
		this.garrisons.add(new Garrison(GarrisonName.GUARD));
		this.garrisons.add(new Garrison(GarrisonName.HOUSE));
		this.garrisons.add(new Garrison(GarrisonName.INN));
	}

	public void placeGarrisons(){
		for(int i=0; i< tiles.size(); i++){
			if(tiles.get(i).getType() == TileType.VALLEY){
				if(tiles.get(i).getWarningChit().getName() == WarningChits.STINK){
					this.garrisons.get(3).setLocation(tiles.get(i).getClearing(5));	//places the inn//setting the location, also places th edewlling on the tile
				}
				if(tiles.get(i).getWarningChit().getName() == WarningChits.BONES){
					//places two ghosts 
					ArrayList<Monster> ghosts = getMonsters(MonsterName.GHOST);
					ghosts.get(0).setLocation(tiles.get(i).getClearing(5));
					ghosts.get(1).setLocation(tiles.get(i).getClearing(5));
					
					ghosts.get(0).setStartingLocation(tiles.get(i).getClearing(5));
					ghosts.get(1).setStartingLocation(tiles.get(i).getClearing(5));
					
					// add ghosts to the tile
					tiles.get(i).getClearing(5).addMonster(ghosts.get(0));
					tiles.get(i).getClearing(5).addMonster(ghosts.get(1));
					
				}
				if(tiles.get(i).getWarningChit().getName() == WarningChits.DANK){
					this.garrisons.get(0).setLocation(tiles.get(i).getClearing(5)); //places the chapel
				}
				if(tiles.get(i).getWarningChit().getName() == WarningChits.SMOKE){
					this.garrisons.get(2).setLocation(tiles.get(i).getClearing(5)); // places the house
				}
				if(tiles.get(i).getWarningChit().getName() == WarningChits.RUINS){
					this.garrisons.get(1).setLocation(tiles.get(i).getClearing(5)); //places the guardhouse
				}	
			}
		}
	}

	public void instanciateTreasures(){
		//instanciate the small treasures
		for (SmallTreasureName n : SmallTreasureName.values()) {
			small.add(new SmallTreasure(n));
		}

		//instanciate the large treasures
		for (LargeTreasureName n : LargeTreasureName.values()) {
			large.add(new LargeTreasure(n));
		}

		//1. choose 5 random LARGE treasures and put them "in" the twts'
		//instanciate the treasures within treasures
		//chest = 2 large, thief = 1, toad = 1, crypt = 1
		twit.add(new TreasureWithinTreasure(TreasureWithinTreasureName.CHEST, createTreasureArray(0,2)));
		twit.add(new TreasureWithinTreasure(TreasureWithinTreasureName.REMAINS_OF_THIEF, createTreasureArray(0,1)));
		twit.add(new TreasureWithinTreasure(TreasureWithinTreasureName.TOADSTOOL_CIRCLE, createTreasureArray(0,1)));
		twit.add(new TreasureWithinTreasure(TreasureWithinTreasureName.CRYPT_OF_THE_KNIGHT, createTreasureArray(0,1)));
		twit.add(new TreasureWithinTreasure(TreasureWithinTreasureName.ENCHANTED_MEADOW, createTreasureArray(0,0)));
		twit.add(new TreasureWithinTreasure(TreasureWithinTreasureName.MOULDY_SKELETON, createTreasureArray(0,0)));

		Collections.shuffle(small);
		Collections.shuffle(large);
		Collections.shuffle(twit);
		
		while(!twit.isEmpty()){
			large.add(twit.get(0));
			twit.remove(0);
		}
	}
	
	public void createAndPlaceWarningChits(TileType type){
		ArrayList<WarningChit> temp = new ArrayList<WarningChit>();
		for(WarningChits n : WarningChits.values()){
			temp.add(new WarningChit(n));
		}
		Collections.shuffle(temp);
		for(int j=0; j<5; j++){
			for(int i=0; i< tiles.size(); i++){
				if(tiles.get(i).getType() == type && tiles.get(i).getWarningChit() == null){
					tiles.get(i).setWarningChit(temp.get(j));
					System.out.println("placed "+temp.get(j)+" on "+tiles.get(i).getName());
					break;
				}
			}
		}
	}

	private ArrayList<Treasure> createTreasureArray(int small, int large){
		ArrayList<Treasure> t = new ArrayList<Treasure>();

		for(int i=0; i<small;i++){
			t.add(this.small.get(0));
			this.small.remove(0);
		}
		for(int i=0; i<large;i++){
			t.add(this.large.get(0));
			this.large.remove(0);
		}

		return t;
	}

	private void placeWarningChits(){
		//20 yellow warning chits, divide into 4 groups of 5 by letter
		//Assigned to the 20 tiles (1 per tile)
		//5 OF THESE (VALLEY) BECOME DEWLLINGS + GHOST
		createAndPlaceWarningChits(TileType.CAVES);	//sets up Caves
		createAndPlaceWarningChits(TileType.MOUNTAINS);	//sets up Mountains
		createAndPlaceWarningChits(TileType.WOODS);	//sets up Woods
		createAndPlaceWarningChits(TileType.VALLEY);//sets up valleys
	}
	
	
	public void setUpMapChits(){
		
		ArrayList<MapChit> mc = new ArrayList<MapChit>();
	
		//8 orange site chits
		mc.add(new SiteChit(TreasureLocations.ALTAR, 1));
		mc.add(new SiteChit(TreasureLocations.CAIRNS, 5));
		mc.add(new SiteChit(TreasureLocations.HOARD, 6));
		mc.add(new SiteChit(TreasureLocations.LAIR, 3));
		mc.add(new SiteChit(TreasureLocations.POOL, 6));
		mc.add(new SiteChit(TreasureLocations.SHRINE, 4));
		mc.add(new SiteChit(TreasureLocations.STATUE, 2));
		mc.add(new SiteChit(TreasureLocations.VAULT, 3));
		
		//10 red sound chits
		for (SoundChits n : SoundChits.values()) {
			  mc.add(new MapChit(n));
		}

		//mix the sound and treasure site chits
		Collections.shuffle(mc);

		//garrisons (chapel, house, inn, guard(house)
		//all 4 take 2 small treasures each
		garrisons.get(0).addTreasures(createTreasureArray(2,0));
		garrisons.get(1).addTreasures(createTreasureArray(2,0));
		garrisons.get(2).addTreasures(createTreasureArray(2,0));
		garrisons.get(3).addTreasures(createTreasureArray(2,0));
		
		//5 given to the lost city
		ArrayList<MapChit> c1 = new ArrayList<MapChit>();
		for(int i =0; i<5;i++){
			c1.add(mc.get(0));mc.remove(0);
		}
		LostCity lostcity = new LostCity(c1);
		//5 given to the lost castle
		ArrayList<MapChit> c2 = new ArrayList<MapChit>();
		for(int i =0; i<5;i++){
			c2.add(mc.get(0));mc.remove(0);
		}
		LostCastle lostcastle = new LostCastle(c2);

		//8 left, 2 groups of 4
		//add lost city to 1 of the groups of 4 (now 5)
		ArrayList<Object> caves = new ArrayList<Object>();
		caves.add(mc.get(0));mc.remove(0);
		caves.add(mc.get(0));mc.remove(0);
		caves.add(mc.get(0));mc.remove(0);
		caves.add(mc.get(0));mc.remove(0);
		caves.add(lostcity);
		Collections.shuffle(caves);
		//1 placed in each caves tile

		//puts the chits into the caves tiles
		placeChits(caves, getAllTiles(TileType.CAVES));

		//add lost castle to the other group
		ArrayList<Object> mountains = new ArrayList<Object>();
		mountains.add(mc.get(0));mc.remove(0);
		mountains.add(mc.get(0));mc.remove(0);
		mountains.add(mc.get(0));mc.remove(0);
		mountains.add(mc.get(0));mc.remove(0);
		mountains.add(lostcastle);
		Collections.shuffle(mountains);
		//put one into each mountain tile

		//puts the chits into the caves tiles
		placeChits(mountains, getAllTiles(TileType.MOUNTAINS));
	}
	
	public int[] getAllTiles(TileType t){
		int[] indices= new int[5];
		int next =0;
		
		for(int i=0; i<tiles.size();i++){
			if(tiles.get(i).getType() == t){
				indices[next] = i;
				next++;
			}
		}
		
		return indices;
		
	}
	
	// adds monsters to the board
	public void initializeMonsters() {
		for (int i = 0; i < 5; i++) {
			monsters.add(new Monster(MonsterName.HEAVY_DRAGON));
			monsters.add(new Monster(MonsterName.VIPER));
			monsters.add(new Monster(MonsterName.WOLF));
			monsters.add(new Monster(MonsterName.HEAVY_SPIDER));
		}
		for (int i = 0; i < 2; i++) {
			monsters.add(new Monster(MonsterName.HEAVY_TROLL));
			monsters.add(new Monster(MonsterName.GIANT_BAT));
			monsters.add(new Monster(MonsterName.GIANT));
			monsters.add(new Monster(MonsterName.GHOST));
		}
		monsters.add(new Monster(MonsterName.HEAVY_TROLL));
		monsters.add(new Monster(MonsterName.GIANT_BAT));

	}
	
	public ArrayList<Monster> getMonsters(MonsterName name) {
		ArrayList<Monster> specificMonsters = new ArrayList<Monster>();
		for (int i = 0; i < monsters.size(); i++) {
			if (monsters.get(i).getName() == name) {
				specificMonsters.add(monsters.get(i));
			}
		}
		return specificMonsters;
	}
	
	public ArrayList<Monster> getProwlingMonsters() {
		ArrayList<Monster> prowlingMonsters = new ArrayList<Monster>();
		for (int i = 0; i < monsters.size(); i++) {
			if (monsters.get(i).isProwling()) {
				prowlingMonsters.add(monsters.get(i));
			}
		}
		return prowlingMonsters;
	}
	
	
	// assumes that the monster to be placed is already prowling
	// places the monster in the clearing specified
	public void placeMonstersAtStartingLocation(MonsterName name, Clearing clearing) {
		System.out.println("placing monsters: " + name + " in clearing " + clearing );
		ArrayList<Monster> monsters = getMonsters(name);
		int numberProwling = 0; // # of monsters on the board
		
		if (monsters.size() == 0) {
			return;
		}
		for (int i = 0; i < monsters.size(); i++) {
			if (monsters.get(i).startingLocation != null) {
				numberProwling++;
			}
		}
		
		int howManyCanProwl = 0;
		if (name == MonsterName.HEAVY_DRAGON || name == MonsterName.VIPER || name == MonsterName.HEAVY_SPIDER) {
			howManyCanProwl = 5; 
		}
		else if (name == MonsterName.WOLF) {
			howManyCanProwl = 6;
		}
		else if (name == MonsterName.HEAVY_TROLL || name == MonsterName.GIANT_BAT) {
			howManyCanProwl = 3;
		} 
		else if (name == MonsterName.GIANT) {
			howManyCanProwl = 2; 
		}
		
		// add one prowling monster to the board
		if (numberProwling < howManyCanProwl) {
			for (int i = 0; i < monsters.size(); i++) {
				if (monsters.get(i).getStartingLocation() == null) {
					System.out.println("placing monster!!");
					monsters.get(i).setStartingLocation(clearing);
					clearing.addMonster(monsters.get(i));
					break;
				} 
			}
		}
	}
		
	public void placeChits(ArrayList<Object> a, int[] temps){
		for(int i=0; i<temps.length; i++){
			if(a.get(0) instanceof LostPlace){
				for(MapChit m : ((LostPlace) a.get(0)).getChits()){
					placeMapChit(m, temps[i]);
				}
			}else{//sounds
				placeMapChit((MapChit)a.get(0), temps[i]);
			}
			a.remove(0);
		}
		
	}
	
	private void placeMapChit(MapChit m, int i){
		if(m instanceof SiteChit){
			tiles.get(i).addMapChit(m);
			switch(((SiteChit)m).getLocation()){
			
			case ALTAR:
				tiles.get(i).getClearing(((SiteChit)m).getNumber()).setSite(new TreasureSite(TreasureLocations.ALTAR, createTreasureArray(0,4)));
				break;
			case CAIRNS:
				tiles.get(i).getClearing(((SiteChit)m).getNumber()).setSite(new TreasureSite(TreasureLocations.CAIRNS, createTreasureArray(6,1)));
				break;
			case HOARD:
				tiles.get(i).getClearing(((SiteChit)m).getNumber()).setSite(new TreasureSite(TreasureLocations.HOARD, createTreasureArray(4,5)));
				break;
			case LAIR:
				tiles.get(i).getClearing(((SiteChit)m).getNumber()).setSite(new TreasureSite(TreasureLocations.LAIR, createTreasureArray(4,3)));
				break;
			case POOL:
				tiles.get(i).getClearing(((SiteChit)m).getNumber()).setSite(new TreasureSite(TreasureLocations.POOL, createTreasureArray(6,3)));
				break;
			case SHRINE:
				tiles.get(i).getClearing(((SiteChit)m).getNumber()).setSite(new TreasureSite(TreasureLocations.SHRINE, createTreasureArray(2,2)));
				break;
			case STATUE:
				tiles.get(i).getClearing(((SiteChit)m).getNumber()).setSite(new TreasureSite(TreasureLocations.STATUE, createTreasureArray(2,1)));
				break;
			case VAULT:
				tiles.get(i).getClearing(((SiteChit)m).getNumber()).setSite(new TreasureSite(TreasureLocations.VAULT, createTreasureArray(0,5)));
				break;
			default:
				break;
			}
		}else{//sounds
			tiles.get(i).addMapChit(m);
		}
	}

	public void placePlayers(ArrayList<Player> p){
		for(int i=0; i < p.size(); i++){

			GarrisonName startLoc = p.get(i).getCharacter().getStartingLocation();
			
			for(int t : getAllTiles(TileType.VALLEY)){
				p.get(i).addDiscovery(tiles.get(t).getWarningChit());
			}
			
			for(int j=0; j< garrisons.size(); j++){
				if(garrisons.get(j).getName() == startLoc){
					p.get(i).setLocation(garrisons.get(j).getLocation());
					garrisons.get(j).getLocation().addOccupant(p.get(i));
				}
			}
		}
	}

    public boolean canUsePath(Player player, Path route){
    	switch(route.type){
		case HIDDEN_PATH:
			if(player.knowsPath(route)){
				return true;
			}
			return false;
		case OPEN_ROAD:
			return true;
		case SECRET_PASSAGEWAY:
			if(player.knowsPath(route)){
				return true;
			}
			return false;
		case TUNNEL:
			return true;
		default:
			return false;
    	}
    }

    public void move(Player player, Clearing newClearing){
    	player.getLocation().removeOccupant(player);
		player.setLocation(newClearing);
		newClearing.addOccupant(player);
		if(player.getCharacter() instanceof Captain){
			player.updateSpecial();
		}
		if(player.hasTreasure(SmallTreasureName.SHIELDED_LANTERN.toString())){
			player.updateLantern();
		}
    }

    /**
     * Moves the player to the new clearing if possible, if not, they forfeit the phase
     * @param player being moved
     * @param newClearing clearing being moved to
     * @return boolean based on if the action was valid or not
     */
    public boolean move(Player player, Phase p) {

    	//Convert newClearing into the actual clearing
    	String[] temp = ((String) p.getExtraInfo()).split(" ");
    	Clearing newClearing =  tiles.get(convertTileName(TileName.valueOf(temp[0]))).getClearing(Integer.parseInt(temp[1]));

    	boolean moving = false;

    	//Steps to Move
    	//1. Is your clearing connected to the destination clearing?
    	//2. Are you able to use the path connecting the two clearings?
    	//3. What kind of clearing are you moving to?
    	//		-To climb a mountain, need 2 consecutive moves ON THE SAME TURN
    	//		-Cannot enter a cave on a turn where sunlight phase was used?
    	//4. Weight Restrictions

    	//1. Checks for clearings being connected.
    	Path route = (player.getLocation().routeTo(newClearing));
    	System.out.println(route);
    	if (route!=null) {
    		//2. Handles special conditions based on path type
    		if (canUsePath(player, route)){
    			//3. ClearingType Restrictions
    			//handles moving to a mountain
    			if(newClearing.getType() == ClearingType.MOUNTAIN){
    				if(player.getLastMove() != null && player.getLastMove().equals(newClearing)){
    					move(player, newClearing);
    					System.out.println(player.getCharacter().getName()+" SUCCEEDED move to "+newClearing.parent.getName().toString()+" "+newClearing.location);
    					moving = true;
    				}else{
    					//sets up the player to move to the mountain next move
    					player.setLastMove(newClearing);
    					System.out.println(player.getCharacter().getName()+" (Failed) BEGAN MOVE TO "+newClearing.parent.getName().toString()+" "+newClearing.location);
    				}
    			// handles moving to a cave
    			}else if(newClearing.getType() == ClearingType.CAVE){
    				move(player, newClearing);
    				player.setGoneInCave(true);
    				System.out.println(player.getCharacter().getName()+" SUCCEEDED move to "+newClearing.parent.getName().toString()+" "+newClearing.location);
    				moving = true;
    			//handles moving to woods
    			}else{
    				move(player, newClearing);
    				System.out.println(player.getCharacter().getName()+" SUCCEEDED move to "+newClearing.parent.getName().toString()+" "+newClearing.location);
    				moving = true;
    			}
    		}else{	//THIS MEANS YOU FAILED TO MOVE CLEARINGS DUE TO A LACK OF PATH BETWEEN YOUR LOCATION AND THE DESTINATION
    			System.out.println(player.getCharacter().getName()+" failed to move to "+newClearing.parent.getName().toString()+" "+newClearing.location+" (no path)");
    		}
    	}
		return moving;
    }

    	@Override
	public String toString(){
		for(int i=0;i<tiles.size();i++){
			System.out.println(tiles.get(i));
		}
		return "";
	}

}
