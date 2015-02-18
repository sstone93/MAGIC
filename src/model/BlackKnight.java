package model;

import utils.Utility;
import utils.Utility.ArmorName;
import utils.Utility.WeaponName;

// inherits name, startingLocation, startingWeapon
public class BlackKnight extends Character {
    BlackKnight() { // might want the name to be sent in
        this.name             = "blackknight";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[1];
        this.startingArmour   = new Armour[2];
        startingWeapons[0]    = new Weapon(WeaponName.MACE) ;
        startingArmour[0]     = new Armour(ArmorName.SUIT_OF_ARMOR) ;
        startingArmour[1]     = new Armour(ArmorName.SHIELD) ;
    }

}