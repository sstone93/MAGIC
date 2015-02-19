package model;

import utils.Utility;
import utils.Utility.TileName;

// inherits name, startingLocation, startingWeapon
public class Captain extends Character {
    Captain() { // might want the name to be sent in
        this.name = "captain";
        this.startingLocation = Utility.TileName.CURSTVALLEY; // just put in the default inn for now
        this.startingWeapons  = new Weapon[0];
        this.startingArmour   = new Armour[2];
        startingWeapons[0]    = new Weapon("short sword");
        startingArmour[0]     = new Armour(Utility.ArmourName.HELMET);
        startingArmour[1]     = new Armour(Utility.ArmourName.BREASTPLATE);
        startingArmour[2]     = new Armour(Utility.ArmourName.SHIELD);

    }

    // starting location can be either inn, house, or guard house
    Captain(TileName startingLocation) {
         this.startingLocation = startingLocation;
    }

}