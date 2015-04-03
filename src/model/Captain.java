package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Captain extends Character {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1293193372571286411L;
	
	public static GarrisonName[] possibleStartingLocations = {GarrisonName.INN, GarrisonName.GUARD};
	public boolean usedSpecial = false;
	
	public Captain() { // might want the name to be sent in
        this.name             = CharacterName.CAPTAIN;
        this.weight           = ItemWeight.MEDIUM;
        this.speed            = 4;
        this.startingLocation = GarrisonName.INN;; // just put in the default inn for now
        startingWeapons.add(new Weapon(WeaponName.SHORT_SWORD));
        startingArmour.add(new Armour(ArmourName.HELMET));
        startingArmour.add(new Armour(ArmourName.BREASTPLATE));
        startingArmour.add(new Armour(ArmourName.SHIELD));
    }

    // starting location can be either inn, house, or guard house
    Captain(GarrisonName startingLocation) {
         this.startingLocation = startingLocation;
    }

}
