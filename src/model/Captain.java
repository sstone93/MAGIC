package model;

import utils.Utility;
import utils.Utility.TileName;

// inherits name, startingLocation, startingWeapon
public class Captain extends Character {
    Captain() { // might want the name to be sent in
        this.name = "captain";
        this.startingLocation = Utility.TileName.CURSTVALLEY; // just put in the default inn for now
        this.startingWeapons = new Weapon[2];
        startingWeapons[0]   = new Weapon("helmet");
        startingWeapons[1]   = new Weapon("breatplate");
        startingWeapons[2]   = new Weapon("shield");

    }

    // starting location can be either inn, house, or guard house
    Captain(TileName startingLocation) {
         this.startingLocation = startingLocation;
    }

}