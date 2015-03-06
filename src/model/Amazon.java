package model;

import utils.Utility;
import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Amazon extends Character {

	private static final long serialVersionUID = -7673779318581569510L;

    public static GarrisonName[] possibleStartingLocations = {GarrisonName.INN};
    

	public Amazon() { // might want the name to be sent in
        this.name = CharacterName.AMAZON;
        this.weight = ItemWeight.MEDIUM;
        this.speed = 4;
        this.startingLocation = GarrisonName.INN;
        startingWeapons.add(new Weapon(WeaponName.SHORT_SWORD));
        startingArmour.add(new Armour(ArmourName.BREASTPLATE));
        startingArmour.add(new Armour(ArmourName.SHIELD));
    }
}
