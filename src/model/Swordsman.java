package model;

import utils.Utility;
import utils.Utility.WeaponName;

// inherits name, startingLocation, startingWeapon
public class Swordsman extends Character {
    Swordsman() { // might want the name to be sent in
        this.name             = "swordsman";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[1];
        startingWeapons[0]    = new Weapon(WeaponName.THRUSTING_SWORD) ;
    }

}