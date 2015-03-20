package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import utils.Utility.ClearingType;
import utils.Utility.GarrisonName;
import utils.Utility.LargeTreasureName;
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
	
	public Board(ArrayList<Player> players, ArrayList<Object> manualInputs){
		
		//TODO USE MANUALINPUTS TO SET THE SOUND AND WARNING CHITS
		//TODO ASDASDASDA
		
		setupBoard();		//creates all of the tiles and clearings. establishes all of the connections
		
		//placeWarningChits();//TODO GIVE IT MANUALY SELECTED WARNING CHITS
		
		initializeMonsters();
		instanciateGarrisons();	//instanciates the garrisons
		instanciateTreasures();
		
		//TODO FIX THESE MANUALLY?
		//setUpMapChits();	//places the warning chits, places the treasures, places the map chits, places the garrisons
		
		placeGarrisons();	//reveal all of the VALLEY map chits (after character selection stuff done)
		placePlayers(players);	//places the players based on their starting location
		
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
		tiles.add(new Tile(TileName.CLIFF, 400, 82));
		tiles.add(new Tile(TileName.EVILVALLEY, 253, 167));
		tiles.add(new Tile(TileName.LEDGES, 400, 252));
		tiles.add(new Tile(TileName.CRAG, 547, 337));
		tiles.add(new Tile(TileName.DARKVALLEY, 694, 422));
		tiles.add(new Tile(TileName.HIGHPASS, 106, 252));
		tiles.add(new Tile(TileName.BORDERLAND, 253, 337));
		tiles.add(new Tile(TileName.OAKWOODS, 400, 422));
		tiles.add(new Tile(TileName.DEEPWOODS, 547, 507));
		tiles.add(new Tile(TileName.CURSTVALLEY, 694, 592));
		tiles.add(new Tile(TileName.CAVERN, 106, 422));
		tiles.add(new Tile(TileName.BADVALLEY, 253, 507));
		tiles.add(new Tile(TileName.MAPLEWOODS, 400, 592));
		tiles.add(new Tile(TileName.NUTWOODS, 547, 677));
		tiles.add(new Tile(TileName.MOUNTAIN, 106, 592));
		tiles.add(new Tile(TileName.CAVES, 253, 677));
		tiles.add(new Tile(TileName.RUINS, 400, 762));
		tiles.add(new Tile(TileName.AWFULVALLEY, 547, 847));
		tiles.add(new Tile(TileName.PINEWOODS, 106, 762));
		tiles.add(new Tile(TileName.LINDENWOODS, 400, 932));

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
		
		//8 orange site chits
		//10 red sound chits
		ArrayList<MapChit> mc = new ArrayList<MapChit>();
		for (SoundChits n : SoundChits.values()) {
			  mc.add(new MapChit(n));
		}

		mc.add(new TreasureSite(TreasureLocations.ALTAR, createTreasureArray(0,4)));
		mc.add(new TreasureSite(TreasureLocations.CAIRNS, createTreasureArray(6,1)));
		mc.add(new TreasureSite(TreasureLocations.HOARD, createTreasureArray(4,5)));
		mc.add(new TreasureSite(TreasureLocations.LAIR, createTreasureArray(4,3)));
		mc.add(new TreasureSite(TreasureLocations.POOL, createTreasureArray(6,3)));
		mc.add(new TreasureSite(TreasureLocations.SHRINE, createTreasureArray(2,2)));
		mc.add(new TreasureSite(TreasureLocations.STATUE, createTreasureArray(2,1)));
		mc.add(new TreasureSite(TreasureLocations.VAULT, createTreasureArray(0,5)));

		//dwellings (company, woodfolk, patrol, lancers, bashkars)
		//all 5 take 2 small treasures

		//visitors
		//scholar = 3 small

		//garrisons (chapel, house, inn, guard(house)
		//all 4 take 2 small treasures each
		garrisons.get(0).setTreasures(createTreasureArray(2,0));
		garrisons.get(1).setTreasures(createTreasureArray(2,0));
		garrisons.get(2).setTreasures(createTreasureArray(2,0));
		garrisons.get(3).setTreasures(createTreasureArray(2,0));

		//mix the sound and treasure site chits
		Collections.shuffle(mc);

		//5 given to the lost city
		ArrayList<MapChit> c1 = new ArrayList<MapChit>();
		mc.get(0); mc.get(1); mc.get(2); mc.get(3); mc.get(4);
		mc.remove(0);mc.remove(0);mc.remove(0);mc.remove(0);mc.remove(0);
		LostCity lostcity = new LostCity(c1);
		//5 given to the lost castle
		ArrayList<MapChit> c2 = new ArrayList<MapChit>();
		mc.get(0); mc.get(1); mc.get(2); mc.get(3); mc.get(4);
		mc.remove(0);mc.remove(0);mc.remove(0);mc.remove(0);mc.remove(0);
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
		placeChits(caves, 5,6,10,15,16);

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
		placeChits(mountains, 0,2,3,9,14);
	}
	
	
	// adds monsters to the board
	public void initializeMonsters() {
		for (int i = 0; i < 5; i++) {
			monsters.add(new Monster(MonsterName.HEAVY_DRAGON));
			monsters.add(new Monster(MonsterName.VIPER));
			monsters.add(new Monster(MonsterName.WOLF));
		}
		for (int i = 0; i < 2; i++) {
			monsters.add(new Monster(MonsterName.HEAVY_TROLL));
			monsters.add(new Monster(MonsterName.GIANT));
			monsters.add(new Monster(MonsterName.GHOST));
		}
		monsters.add(new Monster(MonsterName.HEAVY_TROLL));

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

	public void placeChits(ArrayList<Object> a,int p1,int p2,int p3, int p4, int p5){

		int[] temps = {p1, p2, p3, p4, p5};
		for(int i=0; i<5; i++){
			if(a.get(0) instanceof LostPlace){
				tiles.get(temps[i]).setLostPlace((LostPlace) a.get(0));
			}else{
				tiles.get(temps[i]).setMapChit((MapChit)a.get(0));
			}
			a.remove(0);
		}
	}
	
	private Clearing matchClearing(Clearing c){
		for(int i=0; i< tiles.size();i++){
			for(int j=0; j< tiles.get(i).getClearings().size();j++){
				if(tiles.get(i).getClearings().get(j).equals(c)){
					return tiles.get(i).getClearings().get(j);
				}
			}
		}
		return null;
	}

	public void placePlayers(ArrayList<Player> p){

		//cycle through lsit of characters
		//set their current location as the INN
		// note: you can grab the players characters startingLocation by doing player.getCharacter().getStartingLocation();
		// which right now is a garrison name
		//update the INN with all the new occupants

		for(int i=0; i < p.size(); i++){

			GarrisonName startLoc = p.get(i).getCharacter().getStartingLocation();
			//System.out.println(startLoc);

			for(int j=0; j< garrisons.size(); j++){
				if(garrisons.get(j).getName() == startLoc){
					p.get(i).setLocation(garrisons.get(j).getLocation());
					garrisons.get(j).getLocation().addOccupant(p.get(i));
					//TODO ADDING TREASURES TO PLAYERS OFF THE START
					p.get(i).addTreasure(new SmallTreasure(SmallTreasureName.LEAGUE_BOOTS_7));
					p.get(i).addTreasure(new SmallTreasure(SmallTreasureName.CLOAK_OF_MIST));
				}
			}
		}
	}

	 // TODO: this is the function for monsters changing clearings
    //public boolean move(Monster monster, Clearing newClearing) {
    	//boolean canChange =  (monster.getLocation().canChangeClearing(newClearing));
    	//block(monster); // see if it can block any players
    	//return canChange;
   // }

    //private boolean weightRestrictions(){
    	 //  ItemWeight highestWeight = Utility.ItemWeight.NEGLIGIBLE;

        // find the highest weight of the active move chits of the player
//        for (int i = 0; i < chits.length; i++) {
//            if (chits[i].isVisible()) {
//                if (chits[i].getType() == Utility.Actions.MOVE) {
//                    ItemWeight currentWeight = Utility.getItemWeight(chits[i].getName());
//                    boolean check = Utility.isWeightHeavier(currentWeight, highestWeight);
//                    if (check) {
//                        highestWeight = currentWeight;
//                    }
//                }
//            }
//        }

        // remove items that have a higher weight than the characters weight
      //  ItemWeight currentWeight = player.getCharacter().getWeight();
       // boolean check = Utility.isWeightHeavier(currentWeight, highestWeight);
	  //  if (check) {
	    //	highestWeight = currentWeight;
	  //  }

    	// discard anything that player can't carry
        //player.removeWeaponsWithHigherWeight(highestWeight);
        //player.removeArmourWithHigherWeight(highestWeight);

    //}

    private boolean canUsePath(Player player, Path route){
    	//TODO ANYTHING SPECIAL
    	switch(route.type){
		case HIDDEN_PATH:
			if(!player.knowsPath(route)){
				return true;
			}
			return false;
		case OPEN_ROAD:
			return true;
		case SECRET_PASSAGEWAY:
			if(!player.knowsPath(route)){
				return true;
			}
			return false;
		case TUNNEL:
			return true;
		default:
			return false;
    	}
    }

    //TODO IS IT A DIFFERENT INSTANCEOF CLEARING THAN THE BOARD HAS?
    public void move(Player player, Clearing newClearing){
    	player.getLocation().removeOccupant(player);
		player.setLocation(newClearing);
		newClearing.addOccupant(player);
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
    	//5. Special Move Abilities !!! <Captain, Dwarf>

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
