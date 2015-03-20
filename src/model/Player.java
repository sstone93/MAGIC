package model;

import java.io.Serializable;
import java.util.ArrayList;

import utils.Utility;
import utils.Utility.ClearingType;
import utils.Utility.ItemWeight;
import utils.Utility.PhaseType;
import utils.Utility.CharacterName;

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
    
    boolean goneInCave = false;
    boolean finishedDaylight = false;
    boolean finishedBasic = false;
    boolean addedSunlight = false;

    Character character;
    CombatMoves moves;
    Player target;
    Clearing location;
    Clearing lastMove;
    
    ArrayList<Armour> armour = new ArrayList<Armour>();
    ArrayList<Weapon> weapons = new ArrayList<Weapon>();
    ArrayList<Treasure>  treasures = new ArrayList<Treasure>();
    ArrayList<Object> discoveries = new ArrayList<Object>();
    ArrayList<Phase> phases = new ArrayList<Phase>();
    
    public Player(Character character, int ID) {
        this.character = character;
        this.ID = ID;
        this.armour = character.startingArmour;
        this.weapons = character.startingWeapons;
    }

    public void calculatePhases(){
    	
    	//resets finisheddaylight and finished basic
    	finishedDaylight = false;
    	finishedBasic = false;
    	addedSunlight = false;
    	goneInCave = false;
    	
    	//check if they are starting the day in a cave
    	if(location.getType() == ClearingType.CAVE){
    		goneInCave = true;
    	}
    	
    	//adds basic phases
    	phases.add(new Phase(PhaseType.BASIC));
    	phases.add(new Phase(PhaseType.BASIC));
    	
    	//determines + adds character special phases
    	//TODO CALCULATE SPECIAL PHASES
    	
    	//determines + adds treasure special phases
    	//TODO CALCULATE TREASURE PHASES
    	
    }
    
    public Weapon getActiveWeapon() {
    	for (int i = 0; i < weapons.size(); i++) {
    		if (this.weapons.get(i).isActive() == true) {
    			return this.weapons.get(i);
    		}
    	}
    	return new Weapon(Utility.WeaponName.FIST);
    }
    
    public Player getTarget(){
    	return this.target;
    }
    
    public void setDaylight(Boolean s){
    	this.finishedDaylight = s;
    }
    
    public Boolean getDaylight(){
    	return this.finishedDaylight;
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
    
    public void addDiscovery(Object o){
    	this.discoveries.add(o);
    }
    
    public ArrayList<Object> getDiscoveries(){
    	return this.discoveries;
    }
    
    public boolean knowsPath(Path r){
    	if(this.discoveries.contains(r)){
    		return true;
    	}else{
    		return false;
    	}
    }

    public void unAlertWeapons(){
		for (int j = 0; j < weapons.size(); j++ ) {
			if (weapons.get(j) != null) {
				weapons.get(j).setActive(false);
			}
		}
    }

    public ArrayList<Phase> getPhases() {
    	return phases;
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

    public void checkAndAddSunlight(){
    	if(this.getFinishedBasic() && this.addedSunlight == false){
    		
    		this.addedSunlight = true;
    		System.out.println(this.character.getName()+ " tried to add sunlight");
    		
    		//checks to see if you have gone in a cave today
    		if(goneInCave == false && this.character.getName() != CharacterName.DWARF) {
    			phases.add(new Phase(PhaseType.SUNLIGHT));
            	phases.add(new Phase(PhaseType.SUNLIGHT));
    		}
    	}
		//if the player's turn is done	//TODO A SECOND CHECK, CAN REMOVE FIRST CHECK IN USE PHASE IF WE WNAT
		if(phases.size() == 0 && addedSunlight == true){
			finishedDaylight = true;
			System.out.println(this.character.getName()+ " is finished daylight");
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
    
    public boolean getFinishedBasic() {
    	return finishedBasic;
    }
    
    public void setFinishedBasic(boolean b) {
    	finishedBasic = b;
    }
    
    public boolean hasGoneInCave() {
    	return goneInCave;
    }
    
    public void setGoneInCave(boolean cave) {
    	goneInCave = cave;
    }

	public void setLastMove(Clearing newClearing) {
		lastMove = newClearing;
	}
	
	public Clearing getLastMove(){
		return lastMove;
	}

	public void usePhase(Phase data) {
		Phase t = new Phase(data.getType());
		//TODO
		//THIS ASSUMES THAT PHASE EQUALITY MEANS JUST THE TYPES MATCH (which i think i did)
		
		//TODO THIS IS A TESTING MEASURE, REMOVE ME PLEASE, SHOULD BE A SPECIFIC PHASE, NOT PHASE AT POSITION 0
		phases.remove(0);
		
		//DETERMINES IF BASICS ARE DONE
		finishedBasic = true;
		
		for(int i=0; i<phases.size(); i++){
			if(phases.get(i).getType() == PhaseType.BASIC){
				finishedBasic = false;
				System.out.println(this.character.getName()+ " is done basic");
			}
		}
		
		//if the player's turn is done
		if(phases.size() == 0 && addedSunlight == true){
			finishedDaylight = true;
			System.out.println(this.character.getName()+ " is finished daylight");
		}
		
	}
}
