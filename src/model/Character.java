package model;

import utils.Utility.TileName;

public class Character {
    String name;
    TileName startingLocation;
    Weapon[] startingWeapons;
    Armour[] startingArmour;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartingLocation(TileName location) {
        startingLocation = location;
    }

    public TileName getStartingLocation() {
        return startingLocation;
    }

    public Weapon[] getStartingWeapons() {
        return startingWeapons;
    }
    
    public Armour[] getStartingArmour() {
    	return startingArmour;
    }
}