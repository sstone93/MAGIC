package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Dwarf extends Character {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5092440245558002595L;

	public Dwarf() { // might want the name to be sent in
        this.name             = CharacterName.DWARF;
        this.weight           = ItemWeight.HEAVY;
        this.speed            = 3;
        this.startingLocation = GarrisonName.INN;
        startingWeapons.add(new Weapon(WeaponName.GREAT_AXE)) ;
        startingArmour.add(new Armour(ArmourName.HELMET)) ;
    }

    // starting location can be either inn or guard house
    Dwarf(GarrisonName startingLocation) {
         this.startingLocation = startingLocation;
    }
}
