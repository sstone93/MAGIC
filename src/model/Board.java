package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import utils.Utility;
import utils.Utility.GarrisonName;
import utils.Utility.ItemWeight;
import utils.Utility.LargeTreasureName;
import utils.Utility.SmallTreasureName;
import utils.Utility.SoundChits;
import utils.Utility.TileName;
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
	public Tile[] tiles;
	public Garrison[] garrisons;
	//public Treasure
	/**
	 * 
	 */
	public Board(Player[] players){
		tiles = new Tile[20];
		setupBoard();
		setupGarrisons();
		placeGarrisons();
		placePlayers(players);
		setUpMapChits();
		treasureSetup();
		//System.out.println("Built Board with no Crashes");
		//instanciateTreasures();
	}
	
	/**
	 * Creates the board based on the images provided by JP
	 */
	private void setupBoard(){
		/*Style is as follows:
		
		- Create a new tile, providing it's name, add it to the array
			-this will automatically cause all clearings on that tile to connect to eachother as they should.
		-call the connect method to connect 2 tiles together manually (this is how you de-manualize board building)
		*/
		
		//CREATE ALL THE TILES (THIS IS SETUP INTERNAL CLEARINGS AUTOMATICALLY)
		tiles[0] = new Tile(TileName.CLIFF, 400, 82);
		tiles[1] = new Tile(TileName.EVILVALLEY, 253, 167);
		tiles[2] = new Tile(TileName.LEDGES, 400, 252);
		tiles[3] = new Tile(TileName.CRAG, 547, 337);
		tiles[4] = new Tile(TileName.DARKVALLEY, 694, 422);
		tiles[5] = new Tile(TileName.HIGHPASS, 106, 252);
		tiles[6] = new Tile(TileName.BORDERLAND, 253, 337);
		tiles[7] = new Tile(TileName.OAKWOODS, 400, 422);
		tiles[8] = new Tile(TileName.DEEPWOODS, 547, 507);
		tiles[9] = new Tile(TileName.CURSTVALLEY, 694, 592);
		tiles[10] = new Tile(TileName.CAVERN, 106, 422);
		tiles[11] = new Tile(TileName.BADVALLEY, 253, 507);
		tiles[12] = new Tile(TileName.MAPLEWOODS, 400, 592);
		tiles[13] = new Tile(TileName.NUTWOODS, 547, 677);
		tiles[14] = new Tile(TileName.MOUNTAIN, 106, 592);
		tiles[15] = new Tile(TileName.CAVES, 253, 677);
		tiles[16] = new Tile(TileName.RUINS, 400, 762);
		tiles[17] = new Tile(TileName.AWFULVALLEY, 547, 847);
		tiles[18] = new Tile(TileName.PINEWOODS, 106, 762);
		tiles[19] = new Tile(TileName.LINDENWOODS, 400, 932);
		
		//MANUALLY SET TILE CONNECTIONS (DOES BOTH ENDS IN 1 CONNECT)
		connect(tiles[0], tiles[1], 1, 2);		//cliff and evil valley
		connect(tiles[0], tiles[2], 2, 3);		//cliff and ledges		
		connect(tiles[1], tiles[5], 5, 6);		//evil valley and high pass
		connect(tiles[1], tiles[6], 4, 2);		//evil valley and borderlands
		connect(tiles[1], tiles[2], 4, 2);		//evil valley and ledges		
		connect(tiles[2], tiles[6], 4, 4);		//ledges and borderlands
		connect(tiles[2], tiles[7], 5, 2);		//ledges and oakwoods		
		connect(tiles[3], tiles[8], 2, 1);		//crag and deep woods		
		connect(tiles[4], tiles[8], 5, 2);		//dark valley and deep woods
		connect(tiles[4], tiles[9], 1, 1);		//dark valley and curst valley		
		connect(tiles[5], tiles[10], 3, 5);		//highpass and cavern
		connect(tiles[5], tiles[6], 2, 1);		//highpass and borderlands		
		connect(tiles[6], tiles[10], 5, 2);		//borderlands and cavern
		connect(tiles[6], tiles[11], 1, 5);		//borderlands and badvalley
		connect(tiles[6], tiles[7], 2, 2);		//borderlands and oakwoods		
		connect(tiles[7], tiles[11], 5, 1);		//oakvwoods and bad valley
		connect(tiles[7], tiles[12], 5, 5);		//oakwoods and maple woods
		connect(tiles[7], tiles[8], 4, 1);		//oakwoods and deepwoods
		connect(tiles[8], tiles[12], 5, 5);		//deepwoods and maple woods
		connect(tiles[8], tiles[9], 2, 2);		//deepwoods and curst valley		
		connect(tiles[9], tiles[13], 4, 5);		//curst valley and nut woods		
		connect(tiles[10], tiles[11], 1, 4);	//cavern and bad valley		
		connect(tiles[11], tiles[14], 4, 5);	//bad valley and mountain
		connect(tiles[11], tiles[15], 2, 2);	//bad valley and caves		
		connect(tiles[12], tiles[15], 4, 5);	//maple woods and caves
		connect(tiles[12], tiles[16], 2, 5);	//maple woods and ruins
		connect(tiles[12], tiles[13], 2, 5);	//maple woods and nut woods		
		connect(tiles[13], tiles[16], 4, 1);	//nut woods and ruins
		connect(tiles[13], tiles[17], 2, 5);	//nut woods and awful valley
		connect(tiles[14], tiles[18], 2, 4);	//mountain and pine woods		
		connect(tiles[15], tiles[18], 1, 5);	//caves and pine woods
		connect(tiles[16], tiles[19], 2, 4);	//ruins and linden woods
		connect(tiles[16], tiles[17], 2, 1);	//ruins and awful valley
		connect(tiles[17], tiles[19], 2, 5);	//awful valley and linden woods	
	}
	
	/**
	 * This functions connects 2 tiles together via the supplied clearing numbers
	 * @param t1 the first tile being connected
	 * @param t2 the second tile being connected
	 * @param c1 the clearing number of the clearing being connected on t1
	 * @param c2 the clearing number of the clearing being connected on t2
	 */
	public void connect(Tile t1, Tile t2, int c1, int c2){
		t1.getClearing(c1).addConnection(t2.getClearing(c2));
		t2.getClearing(c2).addConnection(t1.getClearing(c1));
	}
	
	public void setupGarrisons(){
		this.garrisons = new Garrison[4];
		this.garrisons[0] = new Garrison(GarrisonName.CHAPEL);
		this.garrisons[1] = new Garrison(GarrisonName.GUARD);
		this.garrisons[2] = new Garrison(GarrisonName.HOUSE);
		this.garrisons[3] = new Garrison(GarrisonName.INN);
	}
	
	public void placeGarrisons(){
		this.garrisons[0].setLocation(tiles[17].getClearing(5));
		this.garrisons[1].setLocation(tiles[4].getClearing(5));
		this.garrisons[2].setLocation(tiles[9].getClearing(5));
		this.garrisons[3].setLocation(tiles[11].getClearing(5));
	}
	
	public void treasureSetup(){
		
		ArrayList<SmallTreasure> small = new ArrayList<SmallTreasure>();
		ArrayList<LargeTreasure> large = new ArrayList<LargeTreasure>();
		ArrayList<TreasureWithinTreasure> twit = new ArrayList<TreasureWithinTreasure>();
		
		//instanciate the small treasures
		for (SmallTreasureName n : SmallTreasureName.values()) {
			  small.add(new SmallTreasure(n));
		}
		
		//instanciate the large treasures
		for (LargeTreasureName n : LargeTreasureName.values()) {
			  large.add(new LargeTreasure(n));
		}
		
		//instanciate the treasures within treasures
		for (TreasureWithinTreasureName n : TreasureWithinTreasureName.values()) {
			  twit.add(new TreasureWithinTreasure(n));
		}
		
		Collections.shuffle(small);
		Collections.shuffle(large);
		Collections.shuffle(twit);
		
		//setup treasure within treasures
		//1. choose 5 random LARGE treasures and put them "in" the twts'
		//chest = 2 large, thief = 1, toad = 1, crypt = 1
		
		
		
		//2. now we merge tne twts and large treasures together
		//hoard = 5 large, 4 small (below large treasures)
		
		//lair = 3 large, 4 small (below large treasures)
		//altar = 4 large
		//shrine = 2 large, 2 small (below large treasures)
		//pool = 3 large, 6 small (below large treasures)
		//vault = 5 large
		//cairns = 1 large, 6 small (below large treasures)
		//statue = 1 large, 2 small (below large treasures)
		
		//dwellings (company, woodfolk, patrol, lancers, bashkars)
		//all 5 take 2 small treasures
		
		//visitors
		//scholar = 3 small
		
		//garrisons (chapel, house, inn, guard(house)
		//all 4 take 2 small treasures each
		
		
		//thats all large treasures 24/24, and 
		
	}
	
	public void createAndPlaceWarningChits(int p1, int p2, int p3, int p4, int p5){
		ArrayList<WarningChit> temp = new ArrayList<WarningChit>();
		for(WarningChits n : WarningChits.values()){
			temp.add(new WarningChit(n));
		}
		Collections.shuffle(temp);
		
		int[] temps = {p1, p2, p3, p4, p5};
		
		for(int i=0; i< temps.length; i++){
			tiles[temps[i]].setWarningChit(temp.get(i));
		}
		
	}
	
	public void setUpMapChits(){
		//20 yellow warning chits, divide into 4 groups of 5 by letter
		//Assigned to the 20 tiles (1 per tile)
		//5 OF THESE (VALLEY) BECOME DEWLLINGS + GHOST
		createAndPlaceWarningChits(5,6,10,15,16);	//sets up Caves
		createAndPlaceWarningChits(0,2,3,9,14);	//sets up Mountains
		createAndPlaceWarningChits(7,12,13,18,19);	//sets up Woods
		
		//8 orange site chits
		//10 red sound chits
		ArrayList<MapChit> mc = new ArrayList<MapChit>();
		for (SoundChits n : SoundChits.values()) {
			  mc.add(new MapChit(n));
		}
		for (TreasureLocations n : TreasureLocations.values()) {
			  mc.add(new TreasureSite(n));
		}
		
		Collections.shuffle(mc);
		
		//mix the sound and treasure site chits
		//5 given to the lost city
		//5 given to the lost castle
		
		//8 left, 2 groups of 4
		
		//add lost city to 1 of the groups of 4 (now 5)
		//1 placed in each caves tile
		
		//add lost castle to the other group
		//put one into each mountain tile
		
		//reveal all of the VALLEY map chits (after character selection stuff done)
		//this is done already!
	}
	
	public void placePlayers(Player[] p){
		
		//cycle through lsit of characters
		//set their current location as the INN
		// note: you can grab the players characters startingLocation by doing player.getCharacter().getStartingLocation();
		// which right now is a garrison name
		//update the INN with all the new occupants
		
		for(int i=0; i < p.length; i++){
			p[i].setLocation(tiles[11].getClearing(5));
			tiles[11].getClearing(5).addOccupant(p[i]);
		}
		
		
		
		
	}
	
	 // TODO: this is the function for monsters changing clearings
    public boolean move(Monster monster, Clearing newClearing) {
    	boolean canChange =  (monster.getLocation().canChangeClearing(newClearing));
    	//block(monster); // see if it can block any players
    	return canChange;
    }
    
    /**
     * Moves the player to the new clearing if possible, if not, they forfeit the phase
     * @param player being moved
     * @param newClearing clearing being moved to
     * @return boolean based on if the action was valid or not
     */
    public boolean move(Player player, Clearing newClearing) {
    	
        player.setHidden(false);
        boolean canChange =  (player.getLocation().canChangeClearing((Clearing)newClearing));
        
        //TODO check if player knows secret locations
        Chit[] chits             = player.getChits();
        ItemWeight highestWeight = Utility.ItemWeight.NEGLIGIBLE;

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
        ItemWeight currentWeight = player.getCharacter().getWeight();
        boolean check = Utility.isWeightHeavier(currentWeight, highestWeight);
	    if (check) {
	    	highestWeight = currentWeight;
	    }

        if (canChange) {
        	// discard anything that player can't carry
            player.removeWeaponsWithHigherWeight(highestWeight);
            player.removeArmourWithHigherWeight(highestWeight);

            player.moveTo(newClearing);

        }
        return canChange;
    }
	
	@Override
	public String toString(){
		for(int i=0;i<tiles.length;i++){
			System.out.println(tiles[i]);
		}
		return "";
	}
	
}
