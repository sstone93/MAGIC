package model;

import java.io.Serializable;
import utils.Utility.ItemWeight;

public class Monster implements Serializable{

	private static final long serialVersionUID = -582083574086670219L;
	String     name;
	Clearing   location;
	ItemWeight weight;
	int        notoriety;
	int        fame;
	boolean    armoured;
	int        attackLength;
	int        attackSpeed;
	int        health       = 0;
	boolean    dead         = false;
	CombatMoves moves;
    // TODO put weight, in enum
    
	public String getName() {
		return name;
	}
	
    public Clearing getLocation() {
    	return location;
    }
    
    public void setLocation(Clearing location) {
    	this.location = location;
    }
    
    public ItemWeight getWeight() {
    	return weight;
    }
    
    public int getNotoriety() {
    	return notoriety;
    }
    
    public int getFame() {
    	return fame;
    }
    
    public boolean isArmoured() {
    	return armoured;
    }

    public int getAttackLength() {
    	return attackLength;
    }
    
    public int getAttackSpeed() {
    	return attackSpeed;
    }
    
    public int getHealth() {
    	return health;
    }
    
    public void wound() {
    	health++;
    }
    
    public boolean isDead() {
    	return dead;
    }
    
    public void kill() {
    	dead = true;
    }
    
    public CombatMoves getMoves() {
    	return moves;
    }
    
    // TODO randomize monster moves
    public void setMoves() {
    	
    }
}