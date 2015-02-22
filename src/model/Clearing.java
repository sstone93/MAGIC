package model;

import java.util.Arrays;

public class Clearing {

    public String type;
    public Garrison dwelling = null; 			// not sure what type this should actually be
    public int location;		 		// indicating which clearing on the tile this is CHANGED BACK TO AN INT
    public Clearing[] connections;
    public int nextConnection =0;
    public Tile parent;
    public Treasure[] treasures;


    Clearing(int location, Tile parent) {
        this.type     = "";
        this.location = location;
        this.parent   = parent;
        connections   = new Clearing[4];		//4 is the most connections had by any
        treasures     = new Treasure[10]; // arbitrary number
        // TODO: how should the treasures be added to the tile?
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
		for(int i=0;i<connections.length;i++){
			if(connections[i] != null){
				System.out.println("		-connected to "+connections[i].parent.getName()+" clearing #"+connections[i].location);
			}
		}
		return "";
	}

}