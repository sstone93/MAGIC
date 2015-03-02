package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Elf extends Character {

	private static final long serialVersionUID = -2632362486714584059L;

	public Elf() {
        this.name             = CharacterName.ELF;
        this.weight           = ItemWeight.LIGHT;
        this.speed            = 3;
        this.startingLocation = GarrisonName.INN;
        startingWeapons.add(new Weapon(WeaponName.LIGHT_BOW)) ;
    }

}