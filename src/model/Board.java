package model;

import utils.Utility.TileName;

/**
 * 
 * @author Nick
 *
 */
public class Board {
	
	public Tile[] tiles;
	
	/**
	 * 
	 */
	public Board(){
		tiles = new Tile[20];
		setupBoard();
		System.out.println("Built Board with no Crashes");
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
		tiles[0] = new Tile(TileName.CLIFF);
		tiles[1] = new Tile(TileName.EVILVALLEY);
		tiles[2] = new Tile(TileName.LEDGES);
		tiles[3] = new Tile(TileName.CRAG);
		tiles[4] = new Tile(TileName.DARKVALLEY);
		tiles[5] = new Tile(TileName.HIGHPASS);
		tiles[6] = new Tile(TileName.BORDERLAND);
		tiles[7] = new Tile(TileName.OAKWOODS);
		tiles[8] = new Tile(TileName.DEEPWOODS);
		tiles[9] = new Tile(TileName.CURSTVALLEY);
		tiles[10] = new Tile(TileName.CAVERN);
		tiles[11] = new Tile(TileName.BADVALLEY);
		tiles[12] = new Tile(TileName.MAPLEWOODS);
		tiles[13] = new Tile(TileName.NUTWOODS);
		tiles[14] = new Tile(TileName.MOUNTAIN);
		tiles[15] = new Tile(TileName.CAVES);
		tiles[16] = new Tile(TileName.RUINS);
		tiles[17] = new Tile(TileName.AWFULVALLEY);
		tiles[18] = new Tile(TileName.PINEWOODS);
		tiles[19] = new Tile(TileName.LINDENWOODS);
		
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
	
	//TODO REMOVE THIS
	//TODO WRITE PRINT METHODS FOR CLEARING, TILE AND BOARD (WITH NICE INDENTS)
	//TODO ADD A TYPE PARAMETER TO A CONNECTION (FOR SECRET PASSAGEGES, MAKE A CONNECTION OBJECT MAYBE??)
	public static void main(String[] args) {
		Board test = new Board();
		System.out.println(test);
	}
	
}
