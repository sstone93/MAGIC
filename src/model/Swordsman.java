package model;

import utils.Config;

// inherits name, startingLocation, startingWeapon
public class Swordsman extends Character {
    Swordsman() { // might want the name to be sent in 
        this.name = "swordsman";
        // this.startingLocation = inn; // // not sure how we want to represent this
        this.startingWeapons = new Weapon[0]; 
        startingWeapons[0] = new Weapon("thrusting sword") ;
    }
    
}