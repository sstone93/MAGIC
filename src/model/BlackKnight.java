package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class BlackKnight extends Character {
    /**
	 * 
	 */
	private static final long serialVersionUID = -108574095905190050L;

	public BlackKnight() { // might want the name to be sent in
        this.name             = CharacterName.BLACK_KNIGHT;
        this.weight           = ItemWeight.MEDIUM;
        this.speed            = 5;
        this.startingLocation = GarrisonName.INN;
        this.startingWeapons  = new Weapon[1];
        this.startingArmour   = new Armour[2];
        startingWeapons[0]    = new Weapon(WeaponName.MACE) ;
        startingArmour[0]     = new Armour(ArmourName.SUIT_OF_ARMOR) ;
        startingArmour[1]     = new Armour(ArmourName.SHIELD) ;
    }
}
