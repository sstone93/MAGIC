package model;

import utils.Config;
import utils.Utility;

// inherits name, startingLocation, startingWeapon
public class Berserker extends Character {
    Berserker() { // might want the name to be sent in
        this.name             = "berserker";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[0];
        this.startingArmour   = new Armour[0];
        startingWeapons[0]    = new Weapon("great axe") ;
        startingArmour[0]     = new Armour(Utility.ArmourName.HELMET) ;
    }

}