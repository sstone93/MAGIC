package model;

import utils.Utility;

// inherits name, startingLocation, startingWeapon
public class Elf extends Character {
    Elf() { // might want the name to be sent in
        this.name = "elf";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons = new Weapon[0];
        startingWeapons[0] = new Weapon("light bow") ;
    }

}