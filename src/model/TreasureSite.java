package model;

import utils.Utility.TreasureLocations;

public class TreasureSite {
	
	private TreasureLocations name;
	private MapChit[] sounds = new MapChit[5];
	
	public TreasureSite(){
		
	}

	public String toString(){
		System.out.println(name.toString()+", Contains: ");
		for(int i=0;i<this.sounds.length;i++){					//prints treasures it contains
			System.out.println("	"+this.sounds[i]);
		}
		return "";
	}
}
