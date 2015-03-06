package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Swordsman extends Character {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8947910234723882366L;
	
	public static GarrisonName[] possibleStartingLocations = {GarrisonName.INN};

	public Swordsman() { // might want the name to be sent in
        this.name             = CharacterName.SWORDSMAN;
        this.weight = ItemWeight.LIGHT;
        this.speed = 4;
        this.startingLocation = GarrisonName.INN;
        startingWeapons.add(new Weapon(WeaponName.THRUSTING_SWORD)) ;
    }
}