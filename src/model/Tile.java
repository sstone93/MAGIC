package model;

import java.io.Serializable;
import utils.Utility.TileName;

/**
 * 
 * @author Nick
 *
 */
public class Tile implements Serializable{
	
	private static final long serialVersionUID = -85070866055297558L;
	private Clearing[] clearings;
	private TileName name;
	private int nextClearing =0;
	private int x;
	private int y;
	private MapChit sound;
	private WarningChit warning;
	
	/**
	 * 
	 * @param name
	 */
	public Tile(TileName name, int x, int y){
		this.name = name;
		this.x = x;
		this.y = y;
		this.clearings = new Clearing[determineClearings(name)];
		setUpTile();			//this will set up the clearings on this tile.
	}
	
	public void setWarningChit(WarningChit s){
		this.warning = s;
	}
	
	public WarningChit getWarningChit(){
		return this.warning;
	}
	
	public void setMapChit(MapChit s){
		this.sound = s;
	}
	
	public MapChit getMapChit(){
		return this.sound;
	}
	
	/**
	 * This function returns how many clearings a tile will have, based on it's name
	 * @param name The name of the tile
	 * @return The number of clearings found on this tile
	 */
	public int determineClearings(TileName name){
		switch(name){
		case AWFULVALLEY:	return 4;
		case BADVALLEY: 	return 4;
		case BORDERLAND: 	return 6;
		case CAVERN:		return 6;
		case CAVES:			return 6;
		case CLIFF: 		return 6;
		case CRAG: 			return 6;
		case CURSTVALLEY: 	return 4;
		case DARKVALLEY: 	return 4;
		case DEEPWOODS: 	return 6;
		case EVILVALLEY:	return 4;
		case HIGHPASS:		return 6;
		case LEDGES:		return 6;
		case LINDENWOODS:	return 3;
		case MAPLEWOODS:	return 3;
		case MOUNTAIN:		return 6;
		case NUTWOODS:		return 3;
		case OAKWOODS:		return 3;
		case PINEWOODS:		return 3;
		case RUINS:			return 6;
		default:			return 0;
		}
	}
	
	/**
	 * This will create and interconnect all clearings on a tile based on it's name
	 */
	public void setUpTile(){
		if(name == TileName.AWFULVALLEY){
			int [ ] num = {1,2,4,5};
			addNewClearings(num);
			connect(1,4);
			connect(2,5);
		} else if (name == TileName.BADVALLEY){
			int [ ] num = {1,2,4,5};
			addNewClearings(num);
			connect(1,4);
			connect(2,5);
		} else if (name == TileName.BORDERLAND){
			int [ ] num = {1,2,3,4,5,6};
			addNewClearings(num);
			connect(1,6);
			connect(6,3);
			connect(3,2);
			connect(6,4);
			connect(4,5);
		} else if (name == TileName.CAVERN){
			int [ ] num = {1,2,3,4,5,6};
			addNewClearings(num);
			connect(1,3);
			connect(2,3);
			connect(6,3);
			connect(6,4);
			connect(5,3);
			connect(5,4);
			connect(1,4);
		} else if (name == TileName.CAVES){
			int [ ] num = {1,2,3,4,5,6};
			addNewClearings(num);
			connect(1,6);
			connect(6,4);
			connect(4,2);
			connect(2,3);
			connect(5,3);
		} else if (name == TileName.CLIFF){
			int [ ] num = {1,2,3,4,5,6};
			addNewClearings(num);
			connect(1,6);
			connect(6,4);
			connect(6,3);
			connect(3,5);
			connect(2,3);
			connect(2,5);
		} else if (name == TileName.CRAG){
			int [ ] num = {1,2,3,4,5,6};
			addNewClearings(num);
			connect(1,4);
			connect(1,6);
			connect(6,4);
			connect(6,3);
			connect(3,5);
			connect(2,3);
			connect(2,5);
		} else if (name == TileName.CURSTVALLEY){
			int [ ] num = {1,2,4,5};
			addNewClearings(num);
			connect(1,4);
			connect(2,5);
		} else if (name == TileName.DARKVALLEY){
			int [ ] num = {1,2,4,5};
			addNewClearings(num);
			connect(1,4);
			connect(2,5);
		} else if (name == TileName.DEEPWOODS){
			int [ ] num = {1,2,3,4,5,6};
			addNewClearings(num);
			connect(1,4);
			connect(1,6);
			connect(4,6);
			connect(4,5);
			connect(6,3);
			connect(2,3);
			connect(3,5);
		} else if (name == TileName.EVILVALLEY){
			int [ ] num = {1,2,4,5};
			addNewClearings(num);
			connect(1,4);
			connect(2,5);
		} else if (name == TileName.HIGHPASS){
			int [ ] num = {1,2,3,4,5,6};
			addNewClearings(num);
			connect(1,5);
			connect(1,4);
			connect(2,4);
			connect(6,3);
		} else if (name == TileName.LEDGES){
			int [ ] num = {1,2,3,4,5,6};
			addNewClearings(num);
			connect(1,4);
			connect(2,5);
			connect(4,6);
			connect(1,6);
			connect(3,6);
			connect(1,3);
		} else if (name == TileName.LINDENWOODS){
			int [ ] num = {2,4,5};
			addNewClearings(num);
			connect(2,4);
		} else if (name == TileName.MAPLEWOODS){
			int [ ] num = {2,4,5};
			addNewClearings(num);
			connect(2,4);
		} else if (name == TileName.MOUNTAIN){
			int [ ] num = {1,2,3,4,5,6};
			addNewClearings(num);
			connect(1,3);
			connect(3,6);
			connect(5,6);
			connect(2,5);
			connect(2,4);
		} else if (name == TileName.NUTWOODS){
			int [ ] num = {2,4,5};
			addNewClearings(num);
			connect(2,4);
		} else if (name == TileName.OAKWOODS){
			int [ ] num = {2,4,5};
			addNewClearings(num);
			connect(2,4);
		} else if (name == TileName.PINEWOODS){
			int [ ] num = {2,4,5};
			addNewClearings(num);
			connect(2,4);
		} else if (name == TileName.RUINS){
			int [ ] num = {1,2,3,4,5,6};
			addNewClearings(num);
			connect(2,1);
			connect(1,4);
			connect(5,1);
			connect(5,3);
			connect(3,6);
			connect(6,4);
		} else {
			System.out.println("ERROR COULD NOT FIND TILENAME");
		}
	}
	
	/**
	 * Connects the two clearings provided
	 * @param p1 Location number of the first clearing
	 * @param p2 Location number of the second clearing
	 */
	public void connect(int p1, int p2){
		getClearing(p1).addConnection(getClearing(p2));
		getClearing(p2).addConnection(getClearing(p1));
	}

	/**
	 * 
	 * @param position
	 */
	public void addNewClearing(int position){
		
		this.clearings[nextClearing] = new Clearing(position, this);
		nextClearing +=1;
	}
	
	/**
	 * 
	 * @param positions
	 */
	public void addNewClearings(int[] positions){
		
		for(int i=0; i<positions.length;i++){
			addNewClearing(positions[i]);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public TileName getName(){
		return this.name;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	/**
	 * 
	 * @return
	 */
	public Clearing[] getClearings(){
		return this.clearings;
	}
	
	/**
	 * Returns the clearing with the specified clearing number
	 * @param clearingNumber the number of the clearing you are looking for
	 * @return the clearing you were looking for (or null if it doesnt exist)
	 */
	public Clearing getClearing(int clearingNumber){
		for(int i=0; i <clearings.length; i++){
			if(clearings[i].getClearingNumber() == clearingNumber){
				return clearings[i];
			}
		}
		return null;
	}
	
	@Override
	public String toString(){
		
		System.out.println(this.name.toString());	//name of tile
		
		System.out.println("	has WarningChit: "+warning);
		
		for(int i=0;i<clearings.length;i++){			//iterates over clearings on tile
			System.out.println("	"+clearings[i]);
		}
		
		//iterate map chits
		
		return "";
	}
}
