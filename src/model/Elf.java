package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Elf extends Character {
    Elf() { // might want the name to be sent in
        this.name = CharacterName.ELF;
        this.startingLocation = TileName.CURSTVALLEY;
        this.startingWeapons = new Weapon[1];
        startingWeapons[0] = new Weapon(WeaponName.LIGHT_BOW) ;
    }

}