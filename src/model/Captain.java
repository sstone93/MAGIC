package model;

// inherits name, startingLocation, startingWeapon
public class Captain extends Character {
    Captain() { // might want the name to be sent in
        this.name = "captain";
        // this.startingLocation = inn; // // not sure how we want to represent this
        this.startingWeapons = new Weapon[2];
        startingWeapons[0]   = new Weapon("helmet");
        startingWeapons[1]   = new Weapon("breatplate");
        startingWeapons[2]   = new Weapon("shield");

    }

    // starting location can be either inn, house, or guard house
    Captain(Clearing startingLocation) {
        // this.startingLocation = startingLocation
    }

}