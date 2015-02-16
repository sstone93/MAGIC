package model;

import utils.Utility;

// inherits name, startingLocation, startingWeapon
public class Amazon extends Character {
    Amazon() { // might want the name to be sent in
        this.name = "amazon";
        this.startingLocation = Utility.TileName.CURSTVALLEY; 
        this.startingWeapons = new Weapon[0];
        this.startingArmour  = new Armour[1];
        startingWeapons[0]   = new Weapon("short sword");
        startingArmour[0]    = new Armour("breastplate");
        startingArmour[1]    = new Armour("shield");

    }
}