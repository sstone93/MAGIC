package model;

import java.lang.reflect.Array;

public class Player {
    int victoryPoints = 0;
    int gold          = 10; // can't be negative
    int health        = 10;
    int fatigue       = 0;
    int fame          = 0;
    int notoriety     = 0;
    boolean hidden    = false;
    Character character;

    Clearing location;
    Armour[] armour;
    Weapon[] weapons;
    Array[]  treasures;

    Player(Character character) {
        this.character = character;
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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hide) {
        hidden = hide;
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
    	// add it to the array of weapons
    }
}