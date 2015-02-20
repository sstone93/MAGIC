package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class WhiteKnight extends Character {
    WhiteKnight() { // might want the name to be sent in
        this.name             = CharacterName.WHITE_KNIGHT;
        this.startingLocation = TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[1];
        this.startingArmour   = new Armour[1];
        startingWeapons[0]    = new Weapon(WeaponName.GREAT_SWORD) ;
        startingArmour[0]     = new Armour(ArmourName.SUIT_OF_ARMOR) ;
    }
}
