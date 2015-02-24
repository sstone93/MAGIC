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
	
	public TreasureSite(TreasureLocations name, Treasure[] t){
		super(SoundChits.FLUTTER_1);
		this.name = name;
		this.treasures = t;
		
	}
	
	public void setTreasures(Treasure[] t){
		this.treasures = t;
	}

	public String toString(){
		String t = name.toString()+":\n+ Contains Sounds: ";
		for(int i=0;i<this.sounds.length;i++){					//prints sounds it contains
			if(sounds[i] != null){
				t+=sounds[i]+", ";
			}
		}
		//System.out.println(name.toString()+", Contains Sounds: "+t);
		t += "\n + Contains Treasures: ";
		for(int i=0;i<this.treasures.length;i++){					//prints treasures it contains
			if(treasures[i] != null){
				t+=treasures[i]+", ";
			}
		}
		//System.out.println(name.toString()+", Contains Treasures: "+t);
		return t;
	}
}
