package model;

import utils.Utility.ArmourName;
import utils.Utility.ItemWeight;

public class Armour {
    ItemWeight  weight;
    String      health;
    ArmourName  type;
    boolean     damaged;
    boolean     active;

    Armour(ArmourName type) {
    	this.type = type;
    }

    public ItemWeight getWeight() {
        return weight;
    }

    public void setWeight(ItemWeight weight) {
        this.weight = weight;
    }

    public String getHealth() {
        return health;
    }
    public void setHealth(String health) {
        this.health = health;
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
