package model;

import java.io.Serializable;

import utils.Utility.SoundChits;
import utils.Utility.TreasureLocations;

public class SiteChit extends MapChit implements Serializable{
	
	private TreasureLocations location;
	private int number;
	private static final long serialVersionUID = 6490710500332686353L;

	public SiteChit(TreasureLocations t, int n){
		super(SoundChits.TREASURE_SITE);
		this.location = t;
		this.number = n;
	}
	
	public TreasureLocations getLocation(){
		return this.location;
	}
	
	public int getNumber(){
		return this.number;
	}
	
	public boolean equals(TreasureLocations n) {
    	if(n == location){
    		return true;
    	}else{
    		return false;
    	}
    }
}
