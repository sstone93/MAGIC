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

    Armour(ArmourName type) {
    	this.type = type;
    	this.active = true;
    	this.damaged = false;
    	if (type == ArmourName.BREASTPLATE) {
    		this.weight = ItemWeight.MEDIUM;
    	}
    	else if (type == ArmourName.HELMET) {
    		this.weight = ItemWeight.LIGHT;
    	}
    	else if (type == ArmourName.SHIELD) {
    		this.weight = ItemWeight.LIGHT;
    	}
    	else if (type == ArmourName.SUIT_OF_ARMOR) {
    		this.weight = ItemWeight.HEAVY;
    	}
    	else {
    		this.weight = ItemWeight.NEGLIGIBLE;
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
}
