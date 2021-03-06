package model;

import java.io.Serializable;
import java.util.ArrayList;

import utils.Utility;
import utils.Utility.Actions;
import utils.Utility.ClearingType;
import utils.Utility.ItemWeight;
import utils.Utility.LargeTreasureName;
import utils.Utility.MonsterName;
import utils.Utility.PhaseType;
import utils.Utility.CharacterName;
import utils.Utility.SmallTreasureName;

public class Player implements Serializable{

	private static final long serialVersionUID = 4084261472014880590L;
	int ID;
    int victoryPoints = 0;
    int gold          = 200; // can't be negative
    int health        = 0;
    int fatigue       = 0;
    int fame          = 0;
    int notoriety     = 0;
    int finalScore    = 0;
    
    boolean hidden    = false;
    boolean dead      = false;
    boolean blocked   = false;
    
    boolean goneInCave = false;
    boolean finishedDaylight = false;
    boolean finishedBasic = false;
    boolean addedSunlight = false;
    boolean usedLantern = false;
    
    public boolean actuallydone = false;

    Character character;
    CombatMoves moves;
    Clearing location;
    Clearing lastMove;
    
    ArrayList<Player> target = new ArrayList<Player>();
    ArrayList<Monster> monsterTarget = new ArrayList<Monster>();
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
    	if (dead) {
    		blocked = true;
    		finishedDaylight = true;
        	finishedBasic = true;
        	addedSunlight = true;
        	goneInCave = true;
        	actuallydone = true;
        	usedLantern = true;
    	} else {
    		
    		blocked = false;
    		finishedDaylight = false;
        	finishedBasic = false;
        	addedSunlight = false;
        	actuallydone = false;
        	usedLantern = false;
 
        	if(location.getType() != ClearingType.CAVE){
        		goneInCave = false;
        	}else{
        		goneInCave = true;
        	}
        	
        	phases = new ArrayList<Phase>();
    		
    		//adds basic phases
        	phases.add(new Phase(PhaseType.BASIC));
        	phases.add(new Phase(PhaseType.BASIC));
        	
        	//determines + adds character special phases
        	if(this.getCharacter().getName() == CharacterName.AMAZON)
        		phases.add(new Phase(PhaseType.SPECIAL, new Actions[] {Actions.MOVE, Actions.PASS}));
            if(this.getCharacter().getName() == CharacterName.BERSERKER)
            	phases.add(new Phase(PhaseType.SPECIAL, new Actions[] {Actions.REST, Actions.PASS}));
            if(this.getCharacter().getName() == CharacterName.ELF)
            	phases.add(new Phase(PhaseType.SPECIAL, new Actions[] {Actions.HIDE, Actions.PASS}));
            if(this.getCharacter().getName() == CharacterName.WHITE_KNIGHT)
            	phases.add(new Phase(PhaseType.SPECIAL, new Actions[] {Actions.REST, Actions.PASS}));
            
            //THE CAPTAIN SPECIAL ABILITY, cant have used it, and be the captain
            if(this.getCharacter() instanceof Captain){
            	((Captain) this.getCharacter()).usedSpecial = false;
            	updateSpecial();
            }
            
        	//determines + adds treasure special phases
        	//cloak of mists = hide
        	if(this.hasTreasure(SmallTreasureName.CLOAK_OF_MIST.toString()))
        		phases.add(new Phase(PhaseType.TREASURE, new Actions[] {Actions.HIDE, Actions.PASS}));
        	//magic spectacles = search
        	if(this.hasTreasure(SmallTreasureName.MAGIC_SPECTACLES.toString()))
        		phases.add(new Phase(PhaseType.TREASURE, new Actions[] {Actions.SEARCH, Actions.PASS}));
        	//regent of jewels = trade
        	if(this.hasTreasure(LargeTreasureName.REGENT_OF_JEWELS.toString()))
        		phases.add(new Phase(PhaseType.TREASURE, new Actions[] {Actions.TRADE, Actions.PASS}));
        	//7=league boots = move
        	if(this.hasTreasure(SmallTreasureName.LEAGUE_BOOTS_7.toString()))
        		phases.add(new Phase(PhaseType.TREASURE, new Actions[] {Actions.MOVE, Actions.PASS}));
        	if(this.hasTreasure(SmallTreasureName.SHIELDED_LANTERN.toString()))
        		updateLantern();
    	}
    }

    public void updateSpecial(){
    	if(!((Captain) this.getCharacter()).usedSpecial && this.location.dwelling != null){
    		phases.add(new Phase(PhaseType.SPECIAL, Actions.values()));
    	} else if (this.location.dwelling == null){
    		for(Phase p : phases){
    			if(p.equals(new Phase(PhaseType.SPECIAL, Actions.values()))){
    				phases.remove(p);
    				break;
    			}
    		}
    	}
    }
    
    public void updateLantern(){
    	if(!usedLantern && this.location.getType() == ClearingType.CAVE){
    		phases.add(new Phase(PhaseType.TREASURE, Actions.values()));
    	} else {
    		for(Phase p : phases){
    			if(p.equals(new Phase(PhaseType.TREASURE, Actions.values()))){
    				phases.remove(p);
    				break;
    			}
    		}
    	}
    }
    
    public boolean hasTreasure(String name){
    	for(Treasure t: treasures){
    		if( t.getName().equalsIgnoreCase(name)){
    			return true;
    		}
    	}
    	return false;
    }
    
    public Weapon getActiveWeapon() {
    	if (weapons != null) {
    		for (int i = 0; i < weapons.size(); i++) {
    			if (this.weapons.get(i).isActive() == true) {
    				return this.weapons.get(i);
    			}
    		}
    	}
    	return new Weapon(Utility.WeaponName.FIST);
    }
    
    public ArrayList<Player> getTarget(){
    	return this.target;
    }
    
    public ArrayList<Monster> getMonsterTarget() {
    	return this.monsterTarget;
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
    	this.target.add(p);
    }
    
    public void removeTarget() {
    	this.target.clear();
    }
    
    public void setMonsterTarget(Monster m) {
    	this.monsterTarget.add(m);
    }

    public void removeMonsterTarget() {
    	this.monsterTarget.clear();
    }
    
    public int getID(){
		return ID;
    }
    
    public void addDiscovery(Object o){
    	if(!discoveries.contains(o))//if they do not already know it
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
    
    public boolean knowsSite(TreasureSite s){
    	boolean state = false;
    	for(Object o : discoveries){
    		if(o instanceof SiteChit){
    			if(((SiteChit) o).equals(s.name)){
    				state = true;
    			}
    		}
    	}
    	return state;
    }
    
    public boolean knowsSound(MapChit c){
    	for(Object o : discoveries){
    		if(o instanceof MapChit && !(o instanceof SiteChit)){
    			if(((MapChit) o).equals(c)){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public boolean knowsWarning(WarningChit c){
    	for(Object o : discoveries){
    		if(o instanceof WarningChit){
    			if(((WarningChit) o).equals(c)){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public boolean knowsSite(SiteChit c){
    	for(Object o : discoveries){
    		if(o instanceof SiteChit){
    			if(((SiteChit) o).equals(c)){
    				return true;
    			}
    		}
    	}
    	return false;
    }

    public void unAlertWeapons(){
    	if (weapons != null) {
    		for (int j = 0; j < weapons.size(); j++ ) {
				if (weapons.get(j) != null) {
					weapons.get(j).setActive(false);
				}
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
    	this.location.removeOccupant(this);
    	dead = true;
    	phases.clear();
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
    public void removeWeapon(Weapon w) {
    	weapons.remove(w);
    }
    public void addArmour(Armour armour) {
    	this.armour.add(armour);
    }
    public void removeArmour(Armour a) {
    	armour.remove(a);
    }
    
    public ArrayList<Weapon> removeWeaponsWithHigherWeight(ItemWeight weight) {
    	ArrayList<Weapon> toDrop = new ArrayList<Weapon>();
    	String s = "";
    	if (weapons != null) {
    		for (int i = 0; i < weapons.size(); i++) {
            	if (Utility.isWeightHeavier(weapons.get(i).getWeight(), weight)) {
            		s+=weapons.get(i).getType();
            		toDrop.add(weapons.get(i));
                	//weapons.remove(i);
            	}
        	}
    		
    		for(Weapon w : toDrop){
        		weapons.remove(w);
    		}
    		
        }
    	System.out.println("Dropping weapons: "+s);
    	return toDrop;
    }

    // removes armour from the array with a higher weight then the one sent in
    // ignores armour with negligible weight
    public ArrayList<Armour> removeArmourWithHigherWeight(ItemWeight weight) {
    	ArrayList<Armour> toDrop = new ArrayList<Armour>();
    	String s = "";
    	if (armour != null) {
    		for (int i = 0; i < armour.size(); i++) {
            	if (Utility.isWeightHeavier(armour.get(i).getWeight(), weight)) {
            		s+=armour.get(i).getType();
            		toDrop.add(armour.get(i));
            	}
        	}
    		
    		for(Armour a : toDrop){
        		armour.remove(a);
    		}
    		
    		
    	}
    	System.out.println("Dropping armour: "+s);
    	return toDrop;
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
		//if the player's turn is done
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
    
    public void removeTreasure(Treasure treasure) {
    	treasures.remove(treasure);
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
	
	public void removeAllWeapons(){
		weapons.clear();
	}
	
	public void removeAllArmour(){
		armour.clear();
	}
	
	public void removeAllTreasures(){
		treasures.clear();
	}

	public void removeAll(){
		removeAllWeapons();
		removeAllArmour();
		removeAllTreasures();
		gold = 0;
	}
	
	public void usePhase(Phase data) {
		for(Phase p : phases){
			
			if(this.getCharacter() instanceof Captain && p.equals(new Phase(PhaseType.SPECIAL, Actions.values()))){
				System.out.println(p+" equals "+data);
				System.out.println("USED CAPTAIN SPECIAL");
				((Captain) this.getCharacter()).usedSpecial = true;
				phases.remove(p);
				break;
			} else if(hasTreasure(SmallTreasureName.SHIELDED_LANTERN.toString()) && p.equals(new Phase(PhaseType.TREASURE, Actions.values()))){
				System.out.println(p+" equals "+data);
				System.out.println("USED SHEILDED LANTERN");
				usedLantern = true;
				phases.remove(p);
				break;
			}else if(p.equals(data)){
				System.out.println(p+" equals "+data);
				phases.remove(p);
				break;
			}else{
				System.out.println(p+" NOT equals "+data);
				//System.out.println(p+" NOT equals "+data);
			}
		}
		
		//DETERMINES IF BASICS ARE DONE
		finishedBasic = true;
		
		for(int i=0; i<phases.size(); i++){
			if(phases.get(i).getType() == PhaseType.BASIC){
				finishedBasic = false;
			}
		}
		
		if(finishedBasic){
			System.out.println(this.character.getName()+ " is done basic");
		}
		
		//if the player's turn is done
		if(phases.size() == 0 && addedSunlight == true){
			finishedDaylight = true;
			System.out.println(this.character.getName()+ " is finished daylight");
		}
		
	}
	
	//returns a list of all the actions associated with the treasures the player has
	public ArrayList<Actions> getTreasureActions(){
		ArrayList<Actions> arr = new ArrayList<Actions>();
		for(int i = 0; i < phases.size(); i++){
			if(phases.get(i).getType() == PhaseType.TREASURE){
				for(Actions a : phases.get(i).getAction()){
					arr.add(a);
				}
			}
		}
		return arr;
	}
	
	//returns the first monster with a matching name in the same clearing.
	public Monster getMonsterInSameClearing(MonsterName name){
		for(Monster m: this.location.monsters){
			if(m.getName() == name){
				return m;
			}
		}
		return null;
	}
}
