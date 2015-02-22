package model;

import java.io.Serializable;
import java.util.Arrays;

import utils.Config;

public class Clearing implements Serializable{
	
	private static final long serialVersionUID = -688806816964894689L;
	public String type;
    public Garrison dwelling = null; 			// not sure what type this should actually be
    public int location;		 		// indicating which clearing on the tile this is CHANGED BACK TO AN INT
    public Clearing[] connections;
    public int nextConnection =0;
    public Tile parent;
    public Player[] occupants = new Player[Config.MAX_CLIENTS];
    public int nextOccupant = 0;
    
    Clearing(int location, Tile parent) {
        this.type     = "";
        this.location = location;
        this.parent = parent;
        connections = new Clearing[4];		//4 is the most connections had by any 
    }

    /**
     * Returns all the players currently in the clearing
     * @return List of Players in the clearing
     */
    public Player[] getOccupants(){
    	return occupants;
    }
    
    /**
     * Move the player p into this clearing
     * @param Player moving into the clearing
     */
    public void moveIn(Player p){
    	p.getLocation().moveOut(p);
    	p.setLocation(this);
    	this.occupants[nextOccupant] = p;
    	this.nextOccupant += 1;	//TODO THIS IS A PROBLEM, TOO MANY MOVES IN AND OUT DESTROY THE ARRAY, NEED AN ARAY LIST!!!!!!!!!!11
    }
    
    /**
     * Moves player p out of the cleaing
     * @param p Player to be moved out
     */
    public void moveOut(Player p){
    	for(int i=0; i< occupants.length;i++){
    		if(occupants[i] == p){
    			occupants[i] = null;
    			nextOccupant = i; //TODO THIS IS A PROBLEM. REALLY WANT AN ARRAY LIST
    		}
    	}
    }
    
    /**
     * This method adds a new connection to this clearing
     * @param toAdd The clearing being added to this one as a connection
     */
    public void addConnection(Clearing toAdd){
    	connections[nextConnection] = toAdd;
    	nextConnection += 1;
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
        // if they're connected
        if (Arrays.asList(connections).contains(clearing)) {
            return true;
        } else {
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
    	for(int i=0;i<occupants.length;i++){
			if(connections[i] != null){
				System.out.println("		-contains Player "+occupants[i].getID());
			}
		}
		for(int i=0;i<connections.length;i++){
			if(connections[i] != null){
				System.out.println("		-connected to "+connections[i].parent.getName()+" clearing #"+connections[i].location);
			}
		}
		return "";
	}

}