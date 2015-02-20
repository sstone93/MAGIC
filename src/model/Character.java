package model;

import utils.Utility.*;

public class Character {
	CharacterName name;
    ItemWeight    weight;
    int           speed;
    GarrisonName  startingLocation;
    Weapon[]      startingWeapons;
    Armour[]      startingArmour;

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