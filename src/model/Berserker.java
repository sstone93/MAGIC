package model;

import utils.Config;
import utils.Utility;

// inherits name, startingLocation, startingWeapon
public class Berserker extends Character {
    Berserker() { // might want the name to be sent in
        this.name             = "berserker";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[1];
        startingWeapons[0]    = new Weapon("great axe") ;
        startingWeapons[1]    = new Weapon("helmet") ;
    }

}