package model;

import java.io.Serializable;

import utils.Utility.*;

public class Character implements Serializable{

	private static final long serialVersionUID = -766962108034752244L;
	CharacterName name;
    ItemWeight    weight;
    int           speed;
    GarrisonName  startingLocation;
    Weapon[]      startingWeapons;
    Armour[]      startingArmour;
    
    // player can can alert or unalert a weapon
    public void alert(Weapon weapon, Object alert) {
    	Weapon weapon2 = (Weapon) weapon;
        weapon2.setActive(((Boolean) alert).booleanValue());
    }
    
    public CharacterName getName() {
        return name;
    }

    public void setName(CharacterName name) {
        this.name = name;
    }

    public void setStartingLocation(GarrisonName name) {
        startingLocation = name;
    }

    public GarrisonName getStartingLocation() {
        return startingLocation;
    }

    public ItemWeight getWeight() {
    	return weight;
    }

    public int getSpeed() {
    	return speed;
    }

    public Weapon[] getStartingWeapons() {
        return startingWeapons;
    }

    public Armour[] getStartingArmour() {
    	return startingArmour;
    }
}