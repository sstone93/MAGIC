package model;

import java.io.Serializable;

import utils.Utility.SoundChits;
import utils.Utility.TreasureLocations;

public class TreasureSite extends MapChit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3725905807462747523L;
	private TreasureLocations name;
	private MapChit[] sounds = new MapChit[5];
	private Treasure[] treasures;
	
	public TreasureSite(TreasureLocations name){
		super(SoundChits.FLUTTER_1);
		this.name = name;
		
	}
	
	public void setTreasures(Treasure[] t){
		this.treasures = t;
	}

	public String toString(){
		System.out.println(name.toString()+", Contains: ");
		for(int i=0;i<this.sounds.length;i++){					//prints sounds it contains
			if(sounds[i] != null){
				System.out.println("	"+this.sounds[i]);
			}
		}
		for(int i=0;i<this.treasures.length;i++){					//prints treasures it contains
			if(treasures[i] != null){
				System.out.println("	"+this.treasures[i]);
			}
		}
		return "";
	}
}
