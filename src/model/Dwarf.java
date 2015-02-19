package model;

import utils.Utility;
import utils.Utility.TileName;

// inherits name, startingLocation, startingWeapon
public class Dwarf extends Character {
    Dwarf() { // might want the name to be sent in
        this.name             = "dwarf";
        this.startingLocation = Utility.TileName.CURSTVALLEY;
        this.startingWeapons  = new Weapon[0];
        this.startingArmour   = new Armour[0];
        startingWeapons[0]    = new Weapon("great axe") ;
        startingArmour[0]     = new Armour(Utility.ArmourName.HELMET) ;
    }

    // starting location can be either inn or guard house
    Dwarf(TileName startingLocation) {
         this.startingLocation = startingLocation;
    }

}