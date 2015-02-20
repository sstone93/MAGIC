package model;

import utils.Utility.*;

public class Character {
	CharacterName name;
    TileName startingLocation;
    ItemWeight weight;
    int speed;
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