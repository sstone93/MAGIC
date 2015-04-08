package model;

import java.io.Serializable;

import utils.Utility.ArmourName;
import utils.Utility.ItemWeight;

public class Armour implements Serializable {

	private static final long serialVersionUID = 5965156814348780830L;
	ItemWeight  weight;
    ArmourName  type;
    boolean     damaged;
    boolean     active;
    int         gold;

    Armour(ArmourName type) {
    	this.type = type;
    	this.active = true;
    	this.damaged = false;
    	if (type == ArmourName.BREASTPLATE) {
    		this.weight = ItemWeight.MEDIUM;
    		this.gold   = 9;
    	}
    	else if (type == ArmourName.HELMET) {
    		this.weight = ItemWeight.LIGHT;
    		this.gold = 30;
    	}
    	else if (type == ArmourName.SHIELD) {
    		this.weight = ItemWeight.LIGHT;
    		this.gold   = 7;
    	}
    	else if (type == ArmourName.SUIT_OF_ARMOR) {
    		this.weight = ItemWeight.HEAVY;
    		this.gold   = 17;
    	}
    	else if (type == ArmourName.GOLD_HELMET){
    		this.weight = ItemWeight.HEAVY;
    		this.gold   = 30;
    	}
    	else if (type == ArmourName.JADE_SHIELD){
    		this.weight = ItemWeight.HEAVY;
    		this.gold   = 20;
    	}
    	else if (type == ArmourName.SILVER_BREASTPLATE){
    		this.weight = ItemWeight.HEAVY;
    		this.gold   = 25;
    	}
    	else if (type == ArmourName.TREMENDOUS_ARMOR){
    		this.weight = ItemWeight.TREMENDOUS;
    		this.gold   = 25;
    	}
    	else {
    		this.weight = ItemWeight.NEGLIGIBLE;
    		this.gold   = 0;
    	}
    }

    public ItemWeight getWeight() {
        return weight;
    }

    public void setWeight(ItemWeight weight) {
        this.weight = weight;
    }

    public ArmourName getType() {
        return type;
    }
    public void setType(ArmourName type) {
        this.type = type;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public void setDamaged(boolean damage) {
        damaged = damage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public String toString(){
    	return this.type.toString();
    }
    
    public int getGold() {
    	return gold;
    }
}
