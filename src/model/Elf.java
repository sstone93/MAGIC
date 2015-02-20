package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Elf extends Character {
    Elf() {
        this.name             = CharacterName.ELF;
        this.weight           = ItemWeight.LIGHT;
        this.speed            = 3;
        this.startingLocation = GarrisonName.INN;
        this.startingWeapons  = new Weapon[1];
        startingWeapons[0]    = new Weapon(WeaponName.LIGHT_BOW) ;
    }

}