package model;

import utils.Config;
import utils.Utility;
import utils.Utility.ArmorName;
import utils.Utility.WeaponName;

// inherits name, startingLocation, startingWeapon
public class Berserker extends Character {
    Berserker() { // might want the name to be sent in
        this.name             = "berserker";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[1];
        this.startingArmour   = new Armour[1];
        startingWeapons[0]    = new Weapon(WeaponName.GREAT_AXE) ;
        startingArmour[0]     = new Armour(ArmorName.HELMET) ;
    }

}