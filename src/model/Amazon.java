package model;

// inherits name, startingLocation, startingWeapon
public class Amazon extends Character {
    Amazon() { // might want the name to be sent in
        this.name = "amazon";
        // this.startingLocation = inn; // // not sure how we want to represent this
        this.startingWeapons = new Weapon[2];
        startingWeapons[0]   = new Weapon("short sword");
        startingWeapons[1]   = new Weapon("breatplate");
        startingWeapons[2]   = new Weapon("shield");

    }
}