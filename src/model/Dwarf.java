package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Dwarf extends Character {
    Dwarf() { // might want the name to be sent in
        this.name             = CharacterName.DWARF;
        this.startingLocation = TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[1];
        this.startingArmour   = new Armour[1];
        startingWeapons[0]    = new Weapon(WeaponName.GREAT_AXE) ;
        startingArmour[0]     = new Armour(ArmourName.HELMET) ;
    }

    // starting location can be either inn or guard house
    Dwarf(TileName startingLocation) {
         this.startingLocation = startingLocation;
    }
}
