package model;

import java.io.Serializable;
import java.util.ArrayList;

import utils.Utility.ClearingType;

public class Clearing implements Serializable{
	
	private static final long serialVersionUID = -688806816964894689L;
    public Garrison dwelling = null; 			// not sure what type this should actually be
    public int location;		 		// indicating which clearing on the tile this is CHANGED BACK TO AN INT
    public Tile parent;
    ClearingType type;
    ArrayList<Path> connections = new ArrayList<Path>();
    ArrayList<Treasure> treasures = new ArrayList<Treasure>();
    ArrayList<Player> occupants = new ArrayList<Player>();
    
    Clearing(int location, Tile parent, ClearingType t) {
        this.location = location;
        this.parent   = parent;
    }

    public ArrayList<Path> getConnections(){
    	return this.connections;
    }
    public void addOccupant(Player p){
    	System.out.println("adding player" + p);
    	occupants.add(p);
    }
    
    /**
     * 
     * @param p
     */
    public void removeOccupant(Player p){
    	System.out.println("removing player" + p);
    	occupants.remove(p);
    }
    
    /**
     * Returns all the players currently in the clearing
     * @return List of Players in the clearing
     */
    public ArrayList<Player> getOccupants(){
    	return this.occupants;
    }
    
    /**
     * This method adds a new connection to this clearing
     * @param toAdd The clearing being added to this one as a connection
     */
    public void addConnection(Path toAdd){
    	this.connections.add(toAdd);
    }

    public ClearingType getType() {
        return type;
    }

    public void setType(ClearingType type) {
        this.type = type;
    }

    public Garrison getDwelling() {
        return dwelling;
    }

    public void setDwelling(Garrison dwelling) {
        this.dwelling = dwelling;
    }

    public int getClearingNumber(){
    	return this.location;
    }
    
    public Path routeTo(Clearing clearing){
    	for(int i=0; i< connections.size(); i++){
    		if(connections.get(i).getDestination(this).equals(clearing)){
    			return connections.get(i);
    		}
    	}
    	return null;
    }

    public void addTreasures(ArrayList<Treasure> treasure) {
        this.treasures.addAll(treasure);
    }

    public ArrayList<Treasure> getTreasures() {
        return treasures;
    }

    public boolean equals(Clearing c) {
    	if(c.parent.equals(this.parent) && c.location == this.location){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    @Override
	public String toString(){
    	System.out.println("	Clearing #"+location);
    	if(this.dwelling != null){
    		System.out.println("		-has garrison: "+dwelling.getName());
    		System.out.println("		"+dwelling);
    	}
    	for(int i=0;i<occupants.size();i++){
			if(occupants.get(i) != null){
				System.out.println("		-contains Player "+occupants.get(i).getID());
			}
		}
		for(int i=0;i<connections.size();i++){
			if(connections.get(i) != null){
				System.out.println("		-Path: "+connections.get(i));
			}
		}
		return "";
	}

}
