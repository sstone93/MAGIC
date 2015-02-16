package model;

import utils.Utility;

// inherits name, startingLocation, startingWeapon
public class Swordsman extends Character {
    Swordsman() { // might want the name to be sent in
        this.name             = "swordsman";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[0];
        startingWeapons[0]    = new Weapon("thrusting sword") ;
    }

}