package model;

import java.io.Serializable;
import java.util.ArrayList;

import utils.Utility.ClearingType;
import utils.Utility.PathType;
import utils.Utility.TileName;
import utils.Utility.TileType;

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
	private double rot;
	private ArrayList<MapChit> sound = new ArrayList<MapChit>();
	private WarningChit warning;
	private TileType type;
	
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
	public Tile(TileName name, int x, int y, double r){
		this.name = name;
		this.x = x;
		this.y = y;
		this.rot = r;
		setUpTile();			//this will set up the clearings on this tile.
	}
	
	public TileType getType(){
		return this.type;
	}
	
	public void setWarningChit(WarningChit s){
		this.warning = s;
		s.setTile(this);
	}
	
	public WarningChit getWarningChit(){
		return this.warning;
	}
	
	public ArrayList<MapChit> getMapChit(){
		return this.sound;
	}
	
	public void addMapChit(MapChit m){
		sound.add(m);
		m.setTile(this);
	}
	
	/**
	 * This will create and interconnect all clearings on a tile based on it's name
	 */
	public void setUpTile(){
		if(name == TileName.AWFULVALLEY){
			int [ ] num = {1,2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {-46,-49,23,-3};
			int [] y = {-27,24,15,-54};
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
			this.type = TileType.VALLEY;
		} else if (name == TileName.BADVALLEY){
			int [ ] num = {1,2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {47,-46,-28,45};//change this
			int [] y = {26,26,-44,-27};//change this
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
			this.type = TileType.VALLEY;
		} else if (name == TileName.BORDERLAND){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE};
			int [] x = {-31,56,28,-31,-23,-11};//change this
			int [] y = {-57,-5,-50,57,21,-13};//change this
			addNewClearings(num, t, x, y);
			connect(1,6,PathType.OPEN_ROAD);
			connect(6,3,PathType.OPEN_ROAD);
			connect(3,2,PathType.OPEN_ROAD);
			connect(6,4,PathType.TUNNEL);
			connect(4,5,PathType.SECRET_PASSAGEWAY);
			this.type = TileType.CAVES;
		} else if (name == TileName.CAVERN){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE};
			int [] x = {48,0,-2,2,-45,18};//change this
			int [] y = {-29,-57,-15,57,-26,22};//change this
			addNewClearings(num, t, x, y);
			connect(1,3,PathType.TUNNEL);
			connect(2,3,PathType.TUNNEL);
			connect(6,3,PathType.TUNNEL);
			connect(6,4,PathType.TUNNEL);
			connect(5,3,PathType.SECRET_PASSAGEWAY);
			connect(5,4,PathType.TUNNEL);
			connect(1,4,PathType.SECRET_PASSAGEWAY);
			this.type = TileType.CAVES;
		} else if (name == TileName.CAVES){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE,ClearingType.CAVE};
			int [] x = {46,-46,-12,12,-46,27};//change this
			int [] y = {26,22,1,52,-24,-53};//change this
			addNewClearings(num, t, x, y);
			connect(1,6,PathType.TUNNEL);
			connect(6,4,PathType.TUNNEL);
			connect(4,2,PathType.TUNNEL);
			connect(2,3,PathType.SECRET_PASSAGEWAY);
			connect(5,3,PathType.TUNNEL);
			this.type = TileType.CAVES;
		} else if (name == TileName.CLIFF){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.MOUNTAIN,ClearingType.WOODS,ClearingType.WOODS,ClearingType.MOUNTAIN,ClearingType.WOODS,ClearingType.MOUNTAIN};
			int [] x = {-50,-48,0,46,46,0};//change this
			int [] y = {-26,28,1,-27,27,-55};//change this
			addNewClearings(num, t, x, y);
			connect(1,6,PathType.OPEN_ROAD);
			connect(6,4,PathType.OPEN_ROAD);
			connect(6,3,PathType.SECRET_PASSAGEWAY);
			connect(3,5,PathType.OPEN_ROAD);
			connect(2,3,PathType.OPEN_ROAD);
			connect(2,5,PathType.HIDDEN_PATH);
			this.type = TileType.MOUNTAINS;
		} else if (name == TileName.CRAG){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.MOUNTAIN,ClearingType.MOUNTAIN,ClearingType.MOUNTAIN,ClearingType.MOUNTAIN,ClearingType.MOUNTAIN,ClearingType.MOUNTAIN};
			int [] x = {-8,0,-31,27,29,-35};
			int [] y = {59,-54,-17,30,-17,26};
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(1,6,PathType.SECRET_PASSAGEWAY);
			connect(6,4,PathType.OPEN_ROAD);
			connect(6,3,PathType.OPEN_ROAD);
			connect(3,5,PathType.OPEN_ROAD);
			connect(2,3,PathType.HIDDEN_PATH);
			connect(2,5,PathType.OPEN_ROAD);
			this.type = TileType.MOUNTAINS;
		} else if (name == TileName.CURSTVALLEY){
			int [ ] num = {1,2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,-47,0,42};
			int [] y = {-52,-26,31,-26};
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
			this.type = TileType.VALLEY;
		} else if (name == TileName.DARKVALLEY){
			int [ ] num = {1,2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {47,0,-25,44};//change this
			int [] y = {-27,-54,13,25};//change this
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
			this.type = TileType.VALLEY;
		} else if (name == TileName.DEEPWOODS){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {-33,53,20,-65,-41,-4};
			int [] y = {-45,0,50,0,40,5};
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.HIDDEN_PATH);
			connect(1,6,PathType.OPEN_ROAD);
			connect(4,6,PathType.OPEN_ROAD);
			connect(4,5,PathType.OPEN_ROAD);
			connect(6,3,PathType.HIDDEN_PATH);
			connect(2,3,PathType.OPEN_ROAD);
			connect(3,5,PathType.OPEN_ROAD);
			this.type = TileType.MOUNTAINS;
		} else if (name == TileName.EVILVALLEY){
			int [ ] num = {1,2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {-47,-47,26,45};//change this
			int [] y = {25,-27,-45,25};//change this
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
			this.type = TileType.VALLEY;
		} else if (name == TileName.HIGHPASS){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.MOUNTAIN,ClearingType.MOUNTAIN,ClearingType.CAVE,ClearingType.MOUNTAIN,ClearingType.MOUNTAIN,ClearingType.CAVE};
			int [] x = {35,-2,41,-13,1,-46};//change this
			int [] y = {15,-53,-26,5,52,-27};//change this
			addNewClearings(num, t, x, y);
			connect(1,5,PathType.OPEN_ROAD);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,4,PathType.OPEN_ROAD);
			connect(6,3,PathType.TUNNEL);
			this.type = TileType.CAVES;
		} else if (name == TileName.LEDGES){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.MOUNTAIN,ClearingType.WOODS,ClearingType.WOODS,ClearingType.MOUNTAIN,ClearingType.WOODS};
			int [] x = {7,45,52,0,-37,-29};//change this
			int [] y = {36,-25,22,-4,-38,54};//change this
			addNewClearings(num, t, x, y);
			connect(1,4,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
			connect(4,6,PathType.HIDDEN_PATH);
			connect(1,6,PathType.OPEN_ROAD);
			connect(3,6,PathType.OPEN_ROAD);
			connect(1,3,PathType.HIDDEN_PATH);
			this.type = TileType.MOUNTAINS;
		} else if (name == TileName.LINDENWOODS){
			int [ ] num = {2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {0,-47,19};//change this
			int [] y = {22,-26,-37};//change this
			addNewClearings(num, t, x, y);
			connect(2,4,PathType.OPEN_ROAD);
			this.type = TileType.WOODS;
		} else if (name == TileName.MAPLEWOODS){
			int [ ] num = {2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {26,48,-44};//change this
			int [] y = {-42,23,0};//change this
			addNewClearings(num, t, x, y);
			connect(2,4,PathType.OPEN_ROAD);
			this.type = TileType.WOODS;
		} else if (name == TileName.MOUNTAIN){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.MOUNTAIN,ClearingType.WOODS,ClearingType.MOUNTAIN,ClearingType.WOODS,ClearingType.MOUNTAIN,ClearingType.MOUNTAIN};
			int [] x = {-31,-52,-31,0,50,39};//change this
			int [] y = {-15,29,27,-59,27,-23};//change this
			addNewClearings(num, t, x, y);
			connect(1,3,PathType.OPEN_ROAD);
			connect(3,6,PathType.HIDDEN_PATH);
			connect(5,6,PathType.OPEN_ROAD);
			connect(2,5,PathType.OPEN_ROAD);
			connect(2,4,PathType.OPEN_ROAD);
			this.type = TileType.MOUNTAINS;
		} else if (name == TileName.NUTWOODS){
			int [ ] num = {2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {-25,43,0};
			int [] y = {-44,-33,26};
			addNewClearings(num, t, x, y);
			connect(2,4,PathType.OPEN_ROAD);
			this.type = TileType.WOODS;
		} else if (name == TileName.OAKWOODS){
			int [ ] num = {2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {44,-46,-24};//change this
			int [] y = {-4,24,-41};//change this
			addNewClearings(num, t, x, y);
			connect(2,4,PathType.OPEN_ROAD);
			this.type = TileType.WOODS;
		} else if (name == TileName.PINEWOODS){
			int [ ] num = {2,4,5};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS};
			int [] x = {-52,-5,46};
			int [] y = {-8,-54,0};
			addNewClearings(num, t, x, y);
			connect(2,4,PathType.OPEN_ROAD);
			this.type = TileType.WOODS;
		} else if (name == TileName.RUINS){
			int [ ] num = {1,2,3,4,5,6};
			ClearingType [ ] t = {ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.WOODS,ClearingType.CAVE};
			int [] x = {-36,30,50,-5,-39,33};//change this
			int [] y = {-28,-51,27,19,43,-13};//change this
			addNewClearings(num, t, x, y);
			connect(2,1,PathType.OPEN_ROAD);
			connect(1,4,PathType.OPEN_ROAD);
			connect(5,1,PathType.HIDDEN_PATH);
			connect(5,3,PathType.OPEN_ROAD);
			connect(3,6,PathType.OPEN_ROAD);
			connect(6,4,PathType.OPEN_ROAD);
			this.type = TileType.CAVES;
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
		for(int i=0;i<clearings.size();i++){			//iterates over clearings on tile
			System.out.println("	"+clearings.get(i));
		}
		return "";
	}

	public double getRot() {
		return rot;
	}
}
