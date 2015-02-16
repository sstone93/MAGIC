package model;

import java.util.Arrays;

public class Clearing {
	
    public String type;
    public String dwelling; 			// not sure what type this should actually be
    public int location;		 		// indicating which clearing on the tile this is CHANGED BACK TO AN INT
    public Clearing[] connections;
    public int nextConnection =0;
    
    Clearing(int location) {
        this.type     = "";
        this.dwelling = "";
        this.location = location;
        connections = new Clearing[4];		//4 is the most connections had by any 
    }
    
    Clearing(String type, String dwelling, int location) {
        this.type     = type;
        this.dwelling = dwelling;
        this.location = location;
        connections = new Clearing[4];		//4 is the most connections had by any 
    }
    
    Clearing(String type, String dwelling, int location, Clearing connectedTo) {
        this.type     = type;
        this.dwelling = dwelling;
        this.location = location;
        connections = new Clearing[4];
        
        this.addConnection(connectedTo);	//link the clearings to eachother
        connectedTo.addConnection(this);
        
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

    public String getDwelling() {
        return dwelling;
    }

    public void setDwelling(String dwelling) {
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

}