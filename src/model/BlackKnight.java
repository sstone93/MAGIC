package model;

import utils.Utility;

// inherits name, startingLocation, startingWeapon
public class BlackKnight extends Character {
    BlackKnight() { // might want the name to be sent in
        this.name             = "blackknight";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[2];
        startingWeapons[0]    = new Weapon("mace") ;
        startingWeapons[1]    = new Weapon("suit of armour") ;
        startingWeapons[2]    = new Weapon("shield") ;
    }

}