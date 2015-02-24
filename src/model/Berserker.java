package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Berserker extends Character {

	private static final long serialVersionUID = -5104001601596506012L;

	public Berserker() { // might want the name to be sent in
        this.name             = CharacterName.BERSERKER;
        this.weight           = ItemWeight.HEAVY;
        this.speed            = 6;
        this.startingLocation = GarrisonName.INN;
        this.startingWeapons  = new Weapon[1];
        this.startingArmour   = new Armour[1];
        startingWeapons[0]    = new Weapon(WeaponName.GREAT_AXE) ;
        startingArmour[0]     = new Armour(ArmourName.HELMET) ;
    }
}
