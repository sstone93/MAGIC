package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Berserker extends Character {

	private static final long serialVersionUID = -5104001601596506012L;
	
	public static GarrisonName[] possibleStartingLocations = {GarrisonName.INN};

	public Berserker() { // might want the name to be sent in
        this.name             = CharacterName.BERSERKER;
        this.weight           = ItemWeight.HEAVY;
        this.speed            = 6;
        this.startingLocation = GarrisonName.INN;
        startingWeapons.add(new Weapon(WeaponName.GREAT_AXE)) ;
        startingArmour.add(new Armour(ArmourName.HELMET)) ;
    }
}
