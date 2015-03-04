package model;

import java.io.Serializable;
import java.util.ArrayList;
import utils.Utility;
import utils.Utility.ItemWeight;

public class Player implements Serializable{

	private static final long serialVersionUID = 4084261472014880590L;
	int ID;
    int victoryPoints = 0;
    int gold          = 10; // can't be negative
    int health        = 0;
    int fatigue       = 0;
    int fame          = 0;
    int notoriety     = 0;
    int finalScore    = 0;
    public int order; // in which order does the player play
    boolean hidden    = false;
    boolean dead      = false;
    boolean blocked   = false;

    Character character;
    CombatMoves moves;
    Player target;
    Clearing location;
    ArrayList<Armour> armour = new ArrayList<Armour>();
    ArrayList<Weapon> weapons = new ArrayList<Weapon>();
    ArrayList<Object> activities = new ArrayList<Object>();
    ArrayList<Treasure>  treasures = new ArrayList<Treasure>();

    /**
     * Used to correct object transmission
     */
    public Player clone (){
    	Player p = new Player(this.getCharacter(), this.getID());
    	p.victoryPoints = this.victoryPoints;
    	p.gold = this.gold;
        p.health = this.health;
        p.fatigue = this.fatigue;
        p.fame  = this.fame;
        p.notoriety = this.notoriety;
        p.finalScore    = this.finalScore;
        p.order = this.order; // in which order does the player play
        p.hidden = this.hidden;
        p.dead = this.dead;
        p.blocked = this.blocked;
        p.character = this.character;
        p.moves = this.moves;
        p.target = this.target;
        p.location = this.location;
        p.location.occupants = this.location.occupants;
        p.armour = this.armour;
        p.weapons = this.weapons;
        p.activities = this.activities; // the players moves for the day
        p.treasures = this.treasures;
        
    	return p;
    }
    
    public Player(Character character, int ID) {
        this.character = character;
        this.ID = ID;
        this.armour = character.startingArmour;
        this.weapons = character.startingWeapons;
    }

    public Player getTarget(){
    	return this.target;
    }

    public CombatMoves getMoves(){
    	return this.moves;
    }
    
    public void setMoves(CombatMoves m){
    	this.moves = m;
    }
    
    public void setTarget(Player p){
    	this.target = p;
    }
    
    public int getID(){
		return ID;
    }
    
    // replaces all the previous activities
    public void addActivities(ArrayList<Object> newActivities) {
    	activities = null;
    	activities = newActivities;
    }

    public void unAlertWeapons(){
		for (int j = 0; j < weapons.size(); j++ ) {
			if (weapons.get(j) != null) {
				weapons.get(j).setActive(false);
			}
		}
    }

    public ArrayList<Object> getActivities() {
    	return activities;
    }

    public void setActivities(ArrayList<Object> o){
    	activities = o;
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

    public void setBlocked(boolean block) {
        blocked = block;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public int getGold() {
        return gold;
    }
    public void addGold(int gold) {
        this.gold += gold;
    }

    public boolean removeGold(int gold) {
        if (this.gold - gold <= 0) {
        	gold = 0;
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

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }
    
    public void removeWeaponsWithHigherWeight(ItemWeight weight) {
        for (int i = 0; i < weapons.size(); i++) {
            if (weapons.get(i).getWeight() == ItemWeight.NEGLIGIBLE)
                continue;
            if (weapons.get(i).getWeight() == weight) {
                continue;
            }
            if (Utility.isWeightHeavier(weapons.get(i).getWeight(), weight)) {
                weapons.remove(i);
            }
        }
    }

    // removes armour from the array with a higher weight then the one sent in
    // ignores armour with negligible weight
    public void removeArmourWithHigherWeight(ItemWeight weight) {
        for (int i = 0; i < armour.size(); i++) {
            if (armour.get(i).getWeight() == ItemWeight.NEGLIGIBLE)
                continue;
            if (armour.get(i).getWeight() == weight) {
                continue;
            }

            if (Utility.isWeightHeavier(armour.get(i).getWeight(), weight)) {
            	armour.remove(i);
            }
        }
    }


    public void setFinalScore(int score) {
        finalScore = score;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void addTreasure(Treasure treasure) {
        this.treasures.add(treasure);
    }

    public ArrayList<Treasure> getTreasures(){
    	return treasures;
    }

    public ArrayList<Armour> getArmour() {
    	return armour;
    }
}
