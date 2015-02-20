package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Berserker extends Character {
    Berserker() { // might want the name to be sent in
        this.name             = CharacterName.BERSERKER;
        this.startingLocation = TileName.CURSTVALLEY;
        this.weight = ItemWeight.HEAVY;
        this.speed = 6;
        this.startingWeapons  = new Weapon[1];
        this.startingArmour   = new Armour[1];
        startingWeapons[0]    = new Weapon(WeaponName.GREAT_AXE) ;
        startingArmour[0]     = new Armour(ArmourName.HELMET) ;
    }
}
