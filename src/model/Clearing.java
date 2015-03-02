package model;

import java.io.Serializable;
import java.util.Arrays;
import utils.Config;

public class Clearing implements Serializable{
	
	private static final long serialVersionUID = -688806816964894689L;
	public String type;
    public Garrison dwelling = null; 			// not sure what type this should actually be
    public int location;		 		// indicating which clearing on the tile this is CHANGED BACK TO AN INT
    public int numberOfTreasures = 0;
    public Clearing[] connections;
    public int nextConnection =0;
    public Tile parent;
    public Treasure[] treasures;
    public Player[] occupants;
    public int nextOccupant = 0;

    Clearing(int location, Tile parent) {
        this.type     = "";
        this.location = location;
        this.parent   = parent;
        connections   = new Clearing[4];		//4 is the most connections had by any
        treasures     = new Treasure[10]; // arbitrary number
        this.occupants = new Player[Config.MAX_CLIENTS];
       // occupants = 
        // TODO: how should the treasures be added to the tile?
    }

    public void addOccupant(Player p){
    	System.out.println(this);
    	System.out.println("adding player" + p);
    	System.out.println(nextOccupant);
    	occupants[nextOccupant] = p;
    	nextOccupant += 1;
    	System.out.println(this.occupants);
    }
    
    //This will be removed once we change to ArrayLists, I just needed to test the GUI.
    public void removeOccupant(Player p){
    	System.out.println(this);
    	System.out.println("removing player" + p);
    	int count = 0;
    	while (count < nextOccupant){
    		if(occupants[count] == p){
    			System.out.println("found player to remove");
    			if(count != nextOccupant - 1){
	    			int countDown = nextOccupant - 2;
	    			while (countDown >= count){
	    				occupants[countDown] = occupants[countDown + 1];
	    				countDown--;
	    			}
    			}
    			occupants[nextOccupant - 1] = null;
    			nextOccupant--;
    		}
    		count++;
    	}
    }
    
    /**
     * Returns all the players currently in the clearing
     * @return List of Players in the clearing
     */
    public Player[] getOccupants(){
    	return this.occupants;
    }
    
    /**
     * Move the player p into this clearing
     * @param Player moving into the clearing
     */
    public void moveIn(Player p){
    	p.getLocation().moveOut(p);
    	p.setLocation(this);
//    	this.addOccupant(p);
//    	this.nextOccupant += 1;	//TODO THIS IS A PROBLEM, TOO MANY MOVES IN AND OUT DESTROY THE ARRAY, NEED AN ARAY LIST!!!!!!!!!!11
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
    	
        for (int i = 0; i < 4; i++) {
        	if (connections[i] != null) {
	        	if (clearing.getClearingNumber() == connections[i].getClearingNumber() ) 
	        		if (clearing.parent.getName() == connections[i].parent.getName())
	        			return true;
        	}
        } 
        return false;
    }

    public void addTreasures(Treasure[] treasure) {
        treasures = treasure;
        numberOfTreasures = treasure.length;
    }

    // removes treasure from the clearing
    // used when a player finds the treasure
    public void removeTreasure(Treasure treasure) {
        for (int j = 0; j < numberOfTreasures - 1; j++) {
            treasures[j] = treasures[j+1];
        }
        numberOfTreasures--;
    }

    public Treasure[] getTreasures() {
        return treasures;
    }

    public int getNumberOfTreasures() {
        return numberOfTreasures;
    }


    @Override
	public String toString(){
    	System.out.println("	Clearing #"+location);
    	if(this.dwelling != null){
    		System.out.println("		-has garrison: "+dwelling.getName());
    		System.out.println("		"+dwelling);
    	}
    	for(int i=0;i<occupants.length;i++){
			if(occupants[i] != null){
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
