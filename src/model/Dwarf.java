package model;

import utils.Utility;
import utils.Utility.ArmorName;
import utils.Utility.TileName;
import utils.Utility.WeaponName;

// inherits name, startingLocation, startingWeapon
public class Dwarf extends Character {
    Dwarf() { // might want the name to be sent in
        this.name             = "dwarf";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[1];
        this.startingArmour   = new Armour[1];
        startingWeapons[0]    = new Weapon(WeaponName.GREAT_AXE) ;
        startingArmour[0]     = new Armour(ArmorName.HELMET) ;
    }

    // starting location can be either inn or guard house
    Dwarf(TileName startingLocation) {
         this.startingLocation = startingLocation;
    }

}