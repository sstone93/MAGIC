package model;

import utils.Config;
import utils.Utility;
import utils.Utility.ArmorName;
import utils.Utility.WeaponName;

// inherits name, startingLocation, startingWeapon
public class WhiteKnight extends Character {
    WhiteKnight() { // might want the name to be sent in
        this.name             = "whiteknight";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[1];
        this.startingArmour   = new Armour[1];
        startingWeapons[0]    = new Weapon(WeaponName.GREAT_SWORD) ;
        startingArmour[0]     = new Armour(ArmorName.SUIT_OF_ARMOR) ;
    }

}