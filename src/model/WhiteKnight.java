package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class WhiteKnight extends Character {
    /**
	 * 
	 */
	private static final long serialVersionUID = -222522844248917902L;
	
	public static GarrisonName[] possibleStartingLocations = {GarrisonName.INN, GarrisonName.CHAPEL};

	public WhiteKnight() { // might want the name to be sent in
        this.name             = CharacterName.WHITE_KNIGHT;
        this.weight           = ItemWeight.HEAVY;
        this.speed            = 5;
        this.startingLocation = GarrisonName.INN;
        startingWeapons.add(new Weapon(WeaponName.GREAT_SWORD)) ;
        startingArmour.add(new Armour(ArmourName.SUIT_OF_ARMOR)) ;
    }
}
