package model;

import java.io.Serializable;

import utils.Utility.SoundChits;

public class MapChit implements Serializable{

	private static final long serialVersionUID = 8505767754211085702L;
	private SoundChits name;
	private boolean    summoned = false;
	
	public MapChit(SoundChits name){
		this.name = name;
	}
	
	public SoundChits getName(){
		return this.name;
	}
	
	public String toString(){
		return name.toString();
	}
	
	public boolean hasSummoned() {
		return summoned;
	}
	
	public void setSummoned(boolean s) {
		summoned = s;
	}
	

}
