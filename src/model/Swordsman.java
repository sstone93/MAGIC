package model;

import utils.Utility.*;

// inherits name, startingLocation, startingWeapon
public class Swordsman extends Character {
    Swordsman() { // might want the name to be sent in
        this.name             = CharacterName.SWORDSMAN;
        this.startingLocation = TileName.CURSTVALLEY;
        this.weight = ItemWeight.LIGHT;
        this.speed = 4;
        this.startingWeapons  = new Weapon[1];
        startingWeapons[0]    = new Weapon(WeaponName.THRUSTING_SWORD) ;
    }
}