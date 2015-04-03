package model;

import java.io.Serializable;

import utils.Utility;
import utils.Utility.ItemWeight;
import utils.Utility.MonsterName;

public class Monster implements Serializable{

	private static final long serialVersionUID = -582083574086670219L;
	MonsterName name;
	Clearing   location;
    Clearing   startingLocation;
	ItemWeight weight;
	int        notoriety;
	int        fame;
	boolean    armoured;
	int        attackLength;
	int        attackSpeed;
	int        health       = 0;
	boolean    dead         = false;
	boolean    prowling     = false;
	boolean    blocked      = false;
	CombatMoves moves;

	Monster(MonsterName name) {
		this.name = name;
		if (name == MonsterName.GHOST) {
			this.weight       = ItemWeight.MEDIUM;
			this.notoriety    = 2;
			this.fame         = 0;
			this.armoured     = false;
			this.attackLength = 2;
			this.attackSpeed  = 2;
		}
		else if (name == MonsterName.GIANT) {
			this.weight       = ItemWeight.TREMENDOUS;
			this.notoriety    = 8;
			this.fame         = 8;
			this.armoured     = false;
			this.attackLength = 5;
			this.attackSpeed  = 5;
		}
		else if (name == MonsterName.HEAVY_DRAGON) {
			this.weight       = ItemWeight.HEAVY;
			this.notoriety    = 5;
			this.fame         = 5;
			this.armoured     = true;
			this.attackLength = 4;
			this.attackSpeed  = 4;
		}
		else if (name == MonsterName.HEAVY_TROLL) {
			this.weight       = ItemWeight.HEAVY;
			this.notoriety    = 5;
			this.fame         = 5;
			this.armoured     = true;
			this.attackLength = 4;
			this.attackSpeed  = 4;
		}
		else if (name == MonsterName.VIPER) {
			this.weight       = ItemWeight.MEDIUM;
			this.notoriety    = 2;
			this.fame         = 1;
			this.armoured     = true;
			this.attackLength = 4;
			this.attackSpeed  = 4;
		}
		else if (name == MonsterName.WOLF) {
			this.weight       = ItemWeight.MEDIUM;
			this.notoriety    = 1;
			this.fame         = 0;
			this.armoured     = false;
			this.attackLength = 3;
			this.attackSpeed  = 4;
		}
		else if (name == MonsterName.HEAVY_SPIDER) {
			this.weight       = ItemWeight.MEDIUM;
			this.notoriety    = 3;
			this.fame         = 3;
			this.armoured     = false; 
			this.attackLength = 4;
			this.attackSpeed  = 3; 
		}
		else if (name == MonsterName.GIANT_BAT) {
			this.weight       = ItemWeight.HEAVY;
			this.notoriety    = 3;
			this.fame         = 3;
			this.armoured     = false; 
			this.attackLength = 3;
			this.attackSpeed  = 2; 
		}
		else {
			this.weight       = ItemWeight.NEGLIGIBLE;
			this.notoriety    = 0;
			this.fame         = 0;
			this.armoured     = false;
			this.attackLength = 1;
			this.attackSpeed  = 1;
		}
	}
	
	public MonsterName getName() {
		return name;
	}

    public Clearing getLocation() {
    	return location;
    }

    public Clearing getStartingLocation() {
        return location;
    }

    public void setStartingLocation(Clearing location) {
        startingLocation = location;
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
    
    public void resetDead() {
    	dead = false;
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
    
    public boolean isProwling() {
    	return prowling;
    }
    
    public void setProwling(boolean prowl) {
    	prowling = prowl;
    }
    
    public boolean isBlocked() {
    	return blocked;
    }
    
    public void setBlocked(boolean block) {
    	blocked = block;
    }
    
    public void move() {
    	int roll = Utility.roll(this.getLocation().getConnections().size());
    	this.location.removeMonster(this);
    	Clearing newClearing = this.getLocation().getConnections().get(roll - 1).getDestination(location);
    	this.setLocation(newClearing);
    	newClearing.addMonster(this);
    	this.blocked = false;
    } 
}