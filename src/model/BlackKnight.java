package model;

import utils.Utility;

// inherits name, startingLocation, startingWeapon
public class BlackKnight extends Character {
    BlackKnight() { // might want the name to be sent in
        this.name             = "blackknight";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[0];
        this.startingArmour   = new Armour[1];
        startingWeapons[0]    = new Weapon("mace") ;
        startingArmour[0]     = new Armour(Utility.ArmourName.SUIT_OF_ARMOR) ;
        startingArmour[1]     = new Armour(Utility.ArmourName.SHIELD) ;
    }

}