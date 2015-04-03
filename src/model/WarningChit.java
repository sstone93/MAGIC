package model;

import java.io.Serializable;

import utils.Utility.WarningChits;

public class WarningChit implements Serializable{

	private WarningChits name;
	private Tile tile;
	private static final long serialVersionUID = -5168229883049997240L;
	private boolean summoned = false;
	
	public WarningChit(WarningChits n){
		this.name = n;
	}
	
	public WarningChits getName(){
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
	
	public Tile getTile(){
		return tile;
	}
	
	public void setTile(Tile t) {
		tile = t;
	}
	
	public boolean equals(WarningChit other) {
		return (this.name == other.name) && (this.tile.equals(other.tile));
	}
}
