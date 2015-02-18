package model;

import utils.Utility;
import utils.Utility.ArmorName;
import utils.Utility.TileName;
import utils.Utility.WeaponName;

// inherits name, startingLocation, startingWeapon
public class Captain extends Character {
    Captain() { // might want the name to be sent in
        this.name = "captain";
        this.startingLocation = Utility.TileName.CURSTVALLEY; // just put in the default inn for now
        this.startingWeapons  = new Weapon[1];
        this.startingArmour   = new Armour[3];
        startingWeapons[0]    = new Weapon(WeaponName.SHORT_SWORD);
        startingArmour[0]     = new Armour(ArmorName.HELMET);
        startingArmour[1]     = new Armour(ArmorName.BREASTPLATE);
        startingArmour[2]     = new Armour(ArmorName.SHIELD);

    }

    // starting location can be either inn, house, or guard house
    Captain(TileName startingLocation) {
         this.startingLocation = startingLocation;
    }

}