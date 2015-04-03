package model;

import java.io.Serializable;

import utils.Utility.*;

public class Weapon implements Serializable{

	private static final long serialVersionUID = -4763458950603820718L;
	WeaponName  type;   // it's name
    boolean ranged;
    boolean active;
    int     length; // on a scale of 0 - 18
    int     speed;
    int     gold;
    ItemWeight weight;

    Weapon(WeaponName n) {
       this.type = n;
       if (type == WeaponName.SHORT_SWORD) {
    	   this.ranged = false;
    	   this.length = 3;
    	   this.speed  = 2;
    	   this.weight = ItemWeight.LIGHT;
    	   this.gold   = 4;
   		}
       else if (type == WeaponName.GREAT_AXE) {
    	   this.ranged = false;
    	   this.length = 5;
    	   this.speed = 4;
    	   this.weight = ItemWeight.HEAVY;
    	   this.gold   = 8;
       }
       else if (type == WeaponName.MACE) {
    	   this.ranged = false;
    	   this.length = 1;
    	   this.speed = 3;
    	   this.weight = ItemWeight.MEDIUM;
    	   this.gold   = 6;
       }
       else if (type == WeaponName.LIGHT_BOW) {
    	   this.ranged = true;
    	   this.length = 14;
    	   this.speed = 1;
    	   this.weight = ItemWeight.LIGHT;
    	   this.gold   = 6;
       }
       else if (type == WeaponName.THRUSTING_SWORD) {
    	   this.ranged = false;
    	   this.length = 4;
    	   this.speed = 2;
    	   this.weight = ItemWeight.LIGHT;
    	   this.gold   = 6;
       }
       else if (type == WeaponName.GREAT_SWORD) {
    	   this.ranged = false;
    	   this.length = 8;
    	   this.speed = 6;
    	   this.weight = ItemWeight.HEAVY;
    	   this.gold   = 10;
       }
       else if (type == WeaponName.FIST) {
    	   this.ranged = false;
    	   this.length = 1;
    	   this.speed = 1;
    	   this.weight = ItemWeight.NEGLIGIBLE;
    	   this.gold   = 0;
       }
       else {
    	   this.ranged = false;
    	   this.length = 1;
    	   this.speed = 7;
    	   this.weight = ItemWeight.NEGLIGIBLE;
    	   this.gold   = 0;
       }
       this.active = true;
    }
    
    public void setRanged(boolean ranged) {
        this.ranged = ranged;
    }
    public boolean isRanged() {
        return ranged;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean isActive() {
        return active;
    }
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
      this.length = length;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public ItemWeight getWeight() {
        return weight;
    }
    public void setWeight(ItemWeight weight) {
        this.weight = weight;
    }
    public WeaponName getType() {
    	return type;
    }
    public String toString(){
    	return this.type.toString();
    }
    public int getGold() {
    	return gold;
    }
}
