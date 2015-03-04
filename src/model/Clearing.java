package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Clearing implements Serializable{
	
	private static final long serialVersionUID = -688806816964894689L;
	public String type;
    public Garrison dwelling = null; 			// not sure what type this should actually be
    public int location;		 		// indicating which clearing on the tile this is CHANGED BACK TO AN INT
    public Tile parent;
    ArrayList<Clearing> connections = new ArrayList<Clearing>();
    ArrayList<Treasure> treasures = new ArrayList<Treasure>();
    ArrayList<Player> occupants = new ArrayList<Player>();
    
    Clearing(int location, Tile parent) {
        this.type     = "";
        this.location = location;
        this.parent   = parent;
    }

    public ArrayList<Clearing> getConnections(){
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
    public void addConnection(Clearing toAdd){
    	this.connections.add(toAdd);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public boolean canChangeClearing(Clearing clearing) {
    	if(connections.contains(clearing)){
    		return true;
    	}else{
    		return false;
    	}
    }

    public void addTreasures(ArrayList<Treasure> treasure) {
        this.treasures.addAll(treasure);
    }

    public ArrayList<Treasure> getTreasures() {
        return treasures;
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
				System.out.println("		-connected to "+connections.get(i).parent.getName()+" clearing #"+connections.get(i).location);
			}
		}
		return "";
	}

}
