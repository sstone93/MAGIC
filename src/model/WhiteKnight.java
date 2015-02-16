package model;

import utils.Config;
import utils.Utility;

// inherits name, startingLocation, startingWeapon
public class WhiteKnight extends Character {
    WhiteKnight() { // might want the name to be sent in
        this.name             = "whiteknight";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[1];
        startingWeapons[0]    = new Weapon("great sword") ;
        startingWeapons[1]    = new Weapon("suit of armour") ;
    }

}