package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Dwarf extends Character {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5092440245558002595L;

	Dwarf() { // might want the name to be sent in
        this.name             = CharacterName.DWARF;
        this.weight           = ItemWeight.HEAVY;
        this.speed            = 3;
        this.startingLocation = GarrisonName.INN;
        this.startingWeapons  = new Weapon[1];
        this.startingArmour   = new Armour[1];
        startingWeapons[0]    = new Weapon(WeaponName.GREAT_AXE) ;
        startingArmour[0]     = new Armour(ArmourName.HELMET) ;
    }

    // starting location can be either inn or guard house
    Dwarf(GarrisonName startingLocation) {
         this.startingLocation = startingLocation;
    }
}
