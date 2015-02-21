package model;

import utils.Config;
import utils.Utility;
import utils.Utility.ItemWeight;

public class Player {
    int victoryPoints = 0;
    int gold          = 10; // can't be negative
    int health        = 0;
    int fatigue       = 0;
    int fame          = 0;
    int notoriety     = 0;
    int finalScore    = 0;
    int order; // in which order does the player play
    int numberOfChits = 0;
    int numberOfWeapons = 0;
    int numberOfArmour  = 0;
    boolean hidden    = false;
    boolean dead      = false;

    Character character;

    Clearing location;
    Armour[] armour;
    Weapon[] weapons;
    Chit[]   chits;
    // Array[]  treasures;
    Clearing[]  secretLocations;

    Player(Character character) {
        this.character = character;
        this.chits     = new Chit[100];
        this.weapons   = new Weapon[Config.WEAPON_AND_ARMOUR_COUNT];
        this.armour    = new Armour[Config.WEAPON_AND_ARMOUR_COUNT];

        for (int i = 0; i < character.startingWeapons.length; i++) {
            weapons[numberOfWeapons] = character.startingWeapons[numberOfWeapons];
            numberOfWeapons++;
        }
        if (character.startingArmour != null) {
            for (int i = 0; i < character.getStartingArmour().length; i++) {
                armour[numberOfArmour] = character.startingArmour[numberOfArmour];
                numberOfArmour++;
            }
        }
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    public void setHealth(int health) {
    	this.health = health;
    }
    
    public int getHealth() {
    	return health;
    }
    
    public int getGold() {
        return gold;
    }
    public void addGold(int gold) {
        this.gold += gold;
    }

    public boolean removeGold(int gold) {
        if (this.gold - gold <= 0) {
            return false;
        }
        this.gold -= gold;
        return true;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hide) {
        hidden = hide;
    }

    public boolean isDead() {
    	return dead;
    }
    
    public void kill() {
    	dead = true;
    }
    
    public int getFatigue() {
        return fatigue;
    }
    public void setFatigue(int fatigue) {
        this.fatigue = fatigue;
    }

    public int getNotoriety() {
        return notoriety;
    }
    public void addNotoriety(int not) {
        this.notoriety += not;
    }

    public void removeNotoriety(int not) {
        this.notoriety -= not;
    }

    public int getFame() {
        return fame;
    }
    public void addFame(int fame) {
        this.fame += fame;
    }

    public void removeFame(int fame) {
        this.fame -= fame;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void addVictoryPoints(int victoryPoints) {
        this.victoryPoints += victoryPoints;
    }

    public Clearing getLocation() {
        return location;
    }

    public void setLocation(Clearing location) {
        this.location = location;
    }

    public Weapon[] getWeapons() {
        return weapons;
    }

    public void addWeapon(Weapon weapon) {
        weapons[numberOfWeapons] = weapon;
        numberOfWeapons++;
    }
    // removes weapons from the array with a higher weight then the one sent in
    // ignores weapons with negligible weight
    public void removeWeaponsWithHigherWeight(ItemWeight weight) {
        for (int i = 0; i < numberOfWeapons; i++) {
            if (weapons[i].getWeight() == ItemWeight.NEGLIGIBLE)
                continue;
            if (weapons[i].getWeight() == weight) {
                continue;
            }

            if (Utility.isWeightHeavier(weapons[i].getWeight(), weight)) {
                // remove weapon in the array
                for (int j = i; j < numberOfWeapons - 1; j++) {
                    weapons[j] = weapons[j+1];
                }
                numberOfWeapons--;
            }
        }
    }

    public void removeWeapon(Weapon weapon) {
        for (int i = 0; i < numberOfWeapons; i++ ) {
            if (weapons[i] == weapon) {
                for (int j = i; j < numberOfWeapons - 1; j++) {
                    weapons[j] = weapons[j+1];
                }
                numberOfWeapons--;
                break;
            }
        }
    }

    // removes armour from the array with a higher weight then the one sent in
    // ignores armour with negligible weight
    public void removeArmourWithHigherWeight(ItemWeight weight) {
        for (int i = 0; i < numberOfArmour; i++) {
            if (armour[i].getWeight() == ItemWeight.NEGLIGIBLE)
                continue;
            if (armour[i].getWeight() == weight) {
                continue;
            }

            if (Utility.isWeightHeavier(armour[i].getWeight(), weight)) {
                // remove armour in the array
                for (int j = i; j < numberOfArmour - 1; j++) {
                    armour[j] = armour[j+1];
                }
                numberOfArmour--;
            }
        }
    }


    public void setFinalScore(int score) {
        finalScore = score;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public Chit[] getChits() {
        return chits;
    }

    public void addChits(Chit chit) {
        this.chits[numberOfChits] = chit;
        numberOfChits++;
    }
}