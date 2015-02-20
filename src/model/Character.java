package model;

import utils.Utility.*;

public class Character {
	CharacterName name;
    TileName startingLocation;
    Weapon[] startingWeapons;
    Armour[] startingArmour;

    public CharacterName getName() {
        return name;
    }

    public void setName(CharacterName name) {
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