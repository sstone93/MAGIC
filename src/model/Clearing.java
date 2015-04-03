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
    private int xOffset;
    private int yOffset;
    
    //used by the view to place items, calculated from tile rotation, offsets, and parent coordinates
    public int x;
    public int y;
    
    TreasureSite site = null;
    TreasurePile pile = null;
    
    ArrayList<Path> connections = new ArrayList<Path>();
    ArrayList<Treasure> treasures = new ArrayList<Treasure>();
    ArrayList<Player> occupants = new ArrayList<Player>();
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    
    Clearing(int location, Tile parent, ClearingType t, int x, int y) {
        this.location = location;
        this.parent   = parent;
        this.type = t;
        this.xOffset = x;
        this.yOffset = y;
        this.calculateXandY();
    }

    public ArrayList<Path> getConnections(){
    	return this.connections;
    }
    public void addOccupant(Player p){
    	//System.out.println("adding player" + p);
    	occupants.add(p);
    }
    
    public void addMonster(Monster m) {
    	monsters.add(m);
    }
    
    public TreasureSite getSite(){
    	return this.site;
    }
    
    public void setSite(TreasureSite s){
    	site = s;
    }
    
    public TreasurePile getPile(){
    	return this.pile;
    }
    
    public void setPile(TreasurePile p) {
    	pile = p;
    }
    
    /**
     * 
     * @param p
     */
    public void removeOccupant(Player p){
    	//System.out.println("removing player" + p);
    	occupants.remove(p);
    }
    
    /**
     * 
     * @param m
     */
    public void removeMonster(Monster m) {
    	monsters.remove(m);
    }
    
    /**
     * Returns all the players currently in the clearing
     * @return List of Players in the clearing
     */
    public ArrayList<Player> getOccupants(){
    	return this.occupants;
    }
    
    public ArrayList<Monster> getMonsters() {
    	return this.monsters;
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
    
    public void removeTreasure(Treasure treasure) {
    	treasures.remove(treasure);
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
    
    private void calculateXandY(){
    	if(parent.getRot() == 0.00){
    		this.x = this.parent.getX() + this.xOffset;
    		this.y = this.parent.getY() + this.yOffset;
    	}
    	else if(parent.getRot() == 180.00){
    		this.x = this.parent.getX() - this.xOffset;
    		this.y = this.parent.getY() - this.yOffset;
    	}
    	else{
    		double hyp = Math.sqrt(Math.pow(this.xOffset, 2) + Math.pow(this.yOffset, 2));
    		double angle = Math.acos(this.xOffset/hyp);
    		
    		//correcting for angles below the x-axis.
    		if(this.yOffset > 0){
    			angle = Math.toRadians(360.00) - angle;
    		}
    		
    		angle += Math.toRadians(360 - this.parent.getRot());//adding the rotation(inverted because image rotation is opposite)
    		System.out.println(this.parent.getName().toString() + this.location + " " + Math.toDegrees(angle));
    		this.x = this.parent.getX() + (int) (Math.cos(angle) * hyp);
    		this.y = this.parent.getY() + (int) (Math.sin(angle) * hyp *-1);//needs to be corrected because above center is treated as negative y.
    	}
    }

}
