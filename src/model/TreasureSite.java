package model;

import java.io.Serializable;
import java.util.ArrayList;
import utils.Utility.TreasureLocations;

public class TreasureSite implements Serializable{
	
	private static final long serialVersionUID = -3725905807462747523L;
	public TreasureLocations name;
	private ArrayList<MapChit> sounds = new ArrayList<MapChit>();
	private ArrayList<Treasure> treasures = new ArrayList<Treasure>();
	
	public TreasureSite(TreasureLocations name){
		this.name = name;
	}
	
	public TreasureSite(TreasureLocations name, ArrayList<Treasure> t){
		this.name = name;
		this.treasures = t;
	}
	
	public void setTreasures(ArrayList<Treasure> t){
		this.treasures = t;
	}
	
	public ArrayList<Treasure> getTreasures(){
		return treasures;
	}
	
	public void takeTreasure(Treasure t){
		this.treasures.remove(t);
	}

	public String toString(){
		System.out.println("		"+name.toString()+":");
		String t = "";
		for(int i=0;i<this.sounds.size();i++){					//prints sounds it contains
			if(sounds.get(i) != null){
				t+=sounds.get(i)+", ";
			}
		}
		System.out.println("			Contains Sounds: "+t);
		t = "";
		for(int i=0;i<this.treasures.size();i++){					//prints treasures it contains
			if(treasures.get(i) != null){
				t+=treasures.get(i)+", ";
			}
		}
		System.out.println("			Contains Treasures: "+t);
		return t;
	}
}
