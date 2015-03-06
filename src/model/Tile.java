package model;

import java.io.Serializable;
import java.util.ArrayList;

import utils.Utility.ClearingType;
import utils.Utility.PathType;
import utils.Utility.TileName;

/**
 * 
 * @author Nick
 *
 */
public class Tile implements Serializable{
	
	private static final long serialVersionUID = -85070866055297558L;
	private ArrayList<Clearing> clearings = new ArrayList<Clearing>();
	private TileName name;
	private int x;
	private int y;
	private MapChit sound;
	private WarningChit warning;
	private LostPlace lost;
	
	public boolean equals(Tile t){
		if(t.getName() == this.name){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param name
	 */
	public Tile(TileName name, int x, int y){
		this.name = name;
		this.x = x;
		this.y = y;
		setUpTile();			//this will set up the clearings on this tile.
	}
	
	public void setLostPlace(LostPlace l){
		this.lost = l;
	}
	
	public LostPlace getLostPlace(){
		return this.lost;
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
	 * This will create and interconnect all clearings on a tile based on it's name
	 */
	public void setUpTile(){
		if(name == TileName.AWFULVALLEY){
			int [ ] num = {1,2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
		} else if (name == TileName.BADVALLEY){
			int [ ] num = {1,2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
		} else if (name == TileName.BORDERLAND){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,6,PathType.OPEN_ROAD);
			connect(6,3,PathType.OPEN_ROAD);
			connect(3,2,PathType.OPEN_ROAD);
			connect(6,4,PathType.TUNNEL);
			connect(4,5,PathType.SECRET_PASSAGEWAY);
		} else if (name == TileName.CAVERN){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,3,PathType.TUNNEL);
			connect(2,3,PathType.TUNNEL);
			connect(6,3,PathType.TUNNEL);
			connect(6,4,PathType.TUNNEL);
			connect(5,3,PathType.SECRET_PASSAGEWAY);
			connect(5,4,PathType.TUNNEL);
			connect(1,4,PathType.SECRET_PASSAGEWAY);
		} else if (name == TileName.CAVES){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,6,PathType.TUNNEL);
			connect(6,4,PathType.TUNNEL);
			connect(4,2,PathType.TUNNEL);
			connect(2,3,PathType.SECRET_PASSAGEWAY);
			connect(5,3,PathType.TUNNEL);
		} else if (name == TileName.CLIFF){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.MOUNTAIN,ClearingType.WOODS,ClearingType.WOODS,ClearingType.MOUNTAIN,ClearingType.WOODS,ClearingType.MOUNTAIN};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,6,PathType.OPEN_ROAD);
			connect(6,4,PathType.OPEN_ROAD);
			connect(6,3,PathType.SECRET_PASSAGEWAY);
			connect(3,5,PathType.OPEN_ROAD);
			connect(2,3,PathType.OPEN_ROAD);
			connect(2,5,PathType.HIDDEN_PATH);
		} else if (name == TileName.CRAG){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.MOUNTAIN,ClearingType.MOUNTAIN,ClearingType.MOUNTAIN,ClearingType.MOUNTAIN,ClearingType.MOUNTAIN,ClearingType.MOUNTAIN};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(1,6,PathType.SECRET_PASSAGEWAY);
			connect(6,4,PathType.OPEN_ROAD);
			connect(6,3,PathType.OPEN_ROAD);
			connect(3,5,PathType.OPEN_ROAD);
			connect(2,3,PathType.HIDDEN_PATH);
			connect(2,5,PathType.OPEN_ROAD);
		} else if (name == TileName.CURSTVALLEY){
			int [ ] num = {1,2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
		} else if (name == TileName.DARKVALLEY){
			int [ ] num = {1,2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
		} else if (name == TileName.DEEPWOODS){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.HIDDEN_PATH);
			connect(1,6,PathType.OPEN_ROAD);
			connect(4,6,PathType.OPEN_ROAD);
			connect(4,5,PathType.OPEN_ROAD);
			connect(6,3,PathType.HIDDEN_PATH);
			connect(2,3,PathType.OPEN_ROAD);
			connect(3,5,PathType.OPEN_ROAD);
		} else if (name == TileName.EVILVALLEY){
			int [ ] num = {1,2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
		} else if (name == TileName.HIGHPASS){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.MOUNTAIN,ClearingType.MOUNTAIN,ClearingType.CAVE,ClearingType.MOUNTAIN,ClearingType.MOUNTAIN,ClearingType.CAVE};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,5,PathType.OPEN_ROAD);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,4,PathType.OPEN_ROAD);
			connect(6,3,PathType.TUNNEL);
		} else if (name == TileName.LEDGES){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.MOUNTAIN,ClearingType.WOODS,ClearingType.WOODS,ClearingType.MOUNTAIN,ClearingType.WOODS};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
			connect(4,6,PathType.HIDDEN_PATH);
			connect(1,6,PathType.OPEN_ROAD);
			connect(3,6,PathType.OPEN_ROAD);
			connect(1,3,PathType.HIDDEN_PATH);
		} else if (name == TileName.LINDENWOODS){
			int [ ] num = {2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(2,4,PathType.OPEN_ROAD);
		} else if (name == TileName.MAPLEWOODS){
			int [ ] num = {2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(2,4,PathType.OPEN_ROAD);
		} else if (name == TileName.MOUNTAIN){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.MOUNTAIN,ClearingType.WOODS,ClearingType.MOUNTAIN,ClearingType.WOODS,ClearingType.MOUNTAIN,ClearingType.MOUNTAIN};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(1,3,PathType.OPEN_ROAD);
			connect(3,6,PathType.HIDDEN_PATH);
			connect(5,6,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
			connect(2,4,PathType.OPEN_ROAD);
		} else if (name == TileName.NUTWOODS){
			int [ ] num = {2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(2,4,PathType.OPEN_ROAD);
		} else if (name == TileName.OAKWOODS){
			int [ ] num = {2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(2,4,PathType.OPEN_ROAD);
		} else if (name == TileName.PINEWOODS){
			int [ ] num = {2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(2,4,PathType.OPEN_ROAD);
		} else if (name == TileName.RUINS){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.CAVE};
			int [] x = {0,0,0,0,0,0};
			int [] y = {0,0,0,0,0,0};
			addNewClearings(num, t, x, y);
			connect(2,1,PathType.OPEN_ROAD);
			connect(1,4,PathType.OPEN_ROAD);
			connect(5,1,PathType.HIDDEN_PATH);
			connect(5,3,PathType.OPEN_ROAD);
			connect(3,6,PathType.OPEN_ROAD);
			connect(6,4,PathType.OPEN_ROAD);
		} else {
			System.out.println("ERROR COULD NOT FIND TILENAME");
		}
	}
	
	/**
	 * Connects the two clearings provided
	 * @param p1 Location number of the first clearing
	 * @param p2 Location number of the second clearing
	 */
	public void connect(int p1, int p2, PathType t){
		Path temp = new Path(getClearing(p1), getClearing(p2), t);
		getClearing(p1).addConnection(temp);
		getClearing(p2).addConnection(temp);
	}

	/**
	 * 
	 * @param position
	 */
	public void addNewClearing(int position, ClearingType t, int x, int y){
		this.clearings.add(new Clearing(position, this, t, x, y));
	}
	
	/**
	 * 
	 * @param positions
	 */
	public void addNewClearings(int[] positions, ClearingType[] types, int[] x, int[] y){
		
		for(int i=0; i<positions.length;i++){
			addNewClearing(positions[i], types[i], x[i], y[i]);
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
	public ArrayList<Clearing> getClearings(){
		return this.clearings;
	}
	
	/**
	 * Returns the clearing with the specified clearing number
	 * @param clearingNumber the number of the clearing you are looking for
	 * @return the clearing you were looking for (or null if it doesnt exist)
	 */
	public Clearing getClearing(int clearingNumber){
		for(int i=0; i <clearings.size(); i++){
			if(clearings.get(i).getClearingNumber() == clearingNumber){
				return clearings.get(i);
			}
		}
		return null;
	}
	
	@Override
	public String toString(){
		
		System.out.println(this.name.toString());	//name of tile
		if(warning != null){
			System.out.println("	has WarningChit: " + warning.toString());
		}
		if(sound != null){
			System.out.println("	has MapChit: " + sound.toString());
		}
		if(lost != null){
			System.out.println("	has LostPlace: " + lost.toString());	
		}
		for(int i=0;i<clearings.size();i++){			//iterates over clearings on tile
			System.out.println("	"+clearings.get(i));
		}
		return "";
	}
}
