package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Captain extends Character {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1293193372571286411L;

	Captain() { // might want the name to be sent in
        this.name             = CharacterName.CAPTAIN;
        this.weight           = ItemWeight.MEDIUM;
        this.speed            = 4;
        this.startingLocation = GarrisonName.INN;; // just put in the default inn for now
        this.startingWeapons  = new Weapon[1];
        this.startingArmour   = new Armour[3];
        startingWeapons[0]    = new Weapon(WeaponName.SHORT_SWORD);
        startingArmour[0]     = new Armour(ArmourName.HELMET);
        startingArmour[1]     = new Armour(ArmourName.BREASTPLATE);
        startingArmour[2]     = new Armour(ArmourName.SHIELD);
    }

    // starting location can be either inn, house, or guard house
    Captain(GarrisonName startingLocation) {
         this.startingLocation = startingLocation;
    }

}
