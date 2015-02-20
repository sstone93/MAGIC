package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Amazon extends Character {
    Amazon() { // might want the name to be sent in
        this.name = CharacterName.AMAZON;
        this.startingLocation = TileName.CURSTVALLEY; 
        this.startingWeapons = new Weapon[1];
        this.startingArmour  = new Armour[2];
        startingWeapons[0]   = new Weapon(WeaponName.SHORT_SWORD);
        startingArmour[0]    = new Armour(ArmourName.BREASTPLATE);
        startingArmour[1]    = new Armour(ArmourName.SHIELD);
    }
}
