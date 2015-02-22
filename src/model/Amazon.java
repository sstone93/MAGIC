package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Amazon extends Character {

	private static final long serialVersionUID = -7673779318581569510L;

	Amazon() { // might want the name to be sent in
        this.name = CharacterName.AMAZON;
        this.weight = ItemWeight.MEDIUM;
        this.speed = 4;
        this.startingLocation = GarrisonName.INN;
        this.startingWeapons = new Weapon[1];
        this.startingArmour  = new Armour[2];
        startingWeapons[0]   = new Weapon(WeaponName.SHORT_SWORD);
        startingArmour[0]    = new Armour(ArmourName.BREASTPLATE);
        startingArmour[1]    = new Armour(ArmourName.SHIELD);
    }
}
