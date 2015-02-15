package model;

import utils.Utility.TileName;

public class Board {
	
	public Tile[] tiles;
	
	public Board(){
		tiles = new Tile[20];
		setupBoard();
	}
	
	/**
	 * Creates the board based on the images provided by JP
	 */
	private void setupBoard(){
		
		//MANUAL SETUP OF TILES AND CLEARINGS
		
		/*Style is as follows:
		
		- Create a new tile, providing it's name, add it to the array
			-this will automatically cause all clearings on that tile to connect to eachother as they should.
		
		-call the connect method to connect 2 tiles together manually (this is how you de-manualize board building).
		
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
		
		//MANUALLY SET TILE CONNECTIONS (DOES BOTH ENDS)
		//TODO Function to do this with names instead of array positions/
		connect(tiles[0], tiles[1], 1, 2);	//cliff and evil valley
		connect(tiles[0], tiles[2], 2, 3);	//cliff and ledges
		
		connect(tiles[1], tiles[5], 5, 6);	//evil valley and high pass
		connect(tiles[1], tiles[6], 4, 2);	//evil valley and borderlands
		connect(tiles[1], tiles[2], 4, 2);	//evil valley and ledges
		
		connect(tiles[2], tiles[6], 4, 4);	//ledges and borderlands
		connect(tiles[2], tiles[7], 5, 2);	//ledges and oakwoods
		
		connect(tiles[3], tiles[8], 2, 1);	//crag and deep woods
		
		connect(tiles[4], tiles[8], 5, 2);	//dark valley and deep woods
		connect(tiles[4], tiles[9], 1, 1);	//dark valley and curst valley
		
		connect(tiles[5], tiles[10], 3, 5);	//highpass and cavern
		connect(tiles[5], tiles[6], 2, 1);	//highpass and borderlands
		
		connect(tiles[6], tiles[10], 5, 2);	//borderlands and cavern
		connect(tiles[6], tiles[11], 1, 5);	//borderlands and badvalley
		connect(tiles[6], tiles[7], 2, 2);	//borderlands and oakwoods
		
		connect(tiles[7], tiles[11], 5, 1);	//oakvwoods and bad valley
		connect(tiles[7], tiles[12], 5, 5);	//oakwoods and maple woods
		connect(tiles[7], tiles[8], 4, 1);	//oakwoods and deepwoods
		
		//--
		
		//TODO NOT DONE ALL CONNECTIONS YET
		
		
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

}
