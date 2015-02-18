package model;

import utils.Utility;
import utils.Utility.WeaponName;

// inherits name, startingLocation, startingWeapon
public class Elf extends Character {
    Elf() { // might want the name to be sent in
        this.name = "elf";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons = new Weapon[1];
        startingWeapons[0] = new Weapon(WeaponName.LIGHT_BOW) ;
    }

}