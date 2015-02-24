package model;

import java.io.Serializable;

import utils.Utility.SoundChits;

public class MapChit implements Serializable{

	private static final long serialVersionUID = 8505767754211085702L;
	private SoundChits name;
	
	public MapChit(SoundChits name){
		this.name = name;
	}
	
	public SoundChits getName(){
		return this.name;
	}
	
	public String toString(){
		System.out.println("sound: "+name.toString());
		return "";
	}

}
