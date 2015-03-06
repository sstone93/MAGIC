package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class BlackKnight extends Character {
    /**
	 * 
	 */
	private static final long serialVersionUID = -108574095905190050L;
	
	public static GarrisonName[] possibleStartingLocations = {GarrisonName.INN};

	public BlackKnight() { // might want the name to be sent in
        this.name             = CharacterName.BLACK_KNIGHT;
        this.weight           = ItemWeight.MEDIUM;
        this.speed            = 5;
        this.startingLocation = GarrisonName.INN;
        startingWeapons.add(new Weapon(WeaponName.MACE)) ;
        startingArmour.add(new Armour(ArmourName.SUIT_OF_ARMOR)) ;
        startingArmour.add(new Armour(ArmourName.SHIELD)) ;
    }
}
