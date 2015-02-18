package model;

import utils.Utility;
import utils.Utility.ArmorName;
import utils.Utility.WeaponName;

// inherits name, startingLocation, startingWeapon
public class Amazon extends Character {
    Amazon() { // might want the name to be sent in
        this.name = "amazon";
        this.startingLocation = Utility.TileName.CURSTVALLEY; 
        this.startingWeapons = new Weapon[1];
        this.startingArmour  = new Armour[2];
        startingWeapons[0]   = new Weapon(WeaponName.SHORT_SWORD);
        startingArmour[0]    = new Armour(ArmorName.BREASTPLATE);
        startingArmour[1]    = new Armour(ArmorName.SHIELD);

    }
}