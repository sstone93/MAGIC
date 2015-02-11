package model;



public class Player {
    int victoryPoints;
    int gold;
    int health;
    int fatigue;
    Character character;
    Armour armour;
    Clearing location;
    Array[] treasures;

    Player(Character character) {
        victoryPoints  = 0;
        gold           = 10;
        health         = 10;
        fatigue        = 0;
        this.character = character;
    }

    public int getGold() {
        return gold;
    }
    public void addGold(int gold) {
        this.gold += gold;
    }

    public void removeGold(int gold) {
        this.gold -= gold;
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
}