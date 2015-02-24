package controller;

import networking.Message;
import networking.NetworkServer;
import model.Amazon;
import model.Berserker;
import model.BlackKnight;
import model.Board;
import model.Captain;
import model.Clearing;
import model.CombatMoves;
import model.Dwarf;
import model.Elf;
import model.Monster;
import model.Player;
import model.Swordsman;
import model.Treasure;
import model.WhiteKnight;

import java.util.Arrays;

import utils.Config;
import utils.Utility;
import utils.Utility.*;

/**
 * Main controller class for the server
 * @author Nick
 */
public class ServerController extends Handler{

	public Board board;		//THIS IS THE MODEL
	public NetworkServer network;
	Player[] players = new Player[Config.MAX_CLIENTS] ;
	int addedPlayers = 0;
    int playerCount    = Config.MAX_CLIENTS;
    int currentDay     = 0;
    int recievedCombat = 0;
	GameState state = GameState.NULL;
	int recievedMoves = 0;

	/**
	 * Constructor for a ServerController
	 */
	public ServerController(){

		//instanciate the network, gives the network object a reference to this controller
		this.network = new NetworkServer(this);
		System.out.println("Network Server Created.");

		//waits until player objects are done being made
		System.out.println("Now waiting for clients to connect.");
	}

	/**
	 * This is the method that handles incomming messages from the networking components
	 * @param ID The ID of the client sending the message
	 * @param message the contents of the message
	 */
	public void handle(int ID, Object message){
		if(message instanceof String){
			String text = ((String) message );
			if(text.equalsIgnoreCase("START GAME")){
				System.out.println("Server Controller told to START THE GAME.");
				startGame();
			}
		}
		if(message instanceof Message){
			Message m = (Message) message;
			if( m.getType() == MessageType.ACTIVITIES){
				if(state == GameState.CHOOSE_PLAYS){
					recievedMoves += 1;
					findPlayer(ID).setActivities(m.getData());
				}else{
					network.send(ID, "NOT ACCEPTING ACTIVITIES ATM");
				}
			}
			if( m.getType() == MessageType.COMBAT_TARGET){
				if(state == GameState.CHOOSE_COMBAT){
					recievedCombat += 1;
					findPlayer(ID).setTarget((Player) m.getData()[0]);
				}else{
					network.send(ID, "NOT ACCEPTING COMBAT TARGETS ATM");
				}
			}
			if( m.getType() == MessageType.COMBAT_MOVES){
				if(state == GameState.CHOOSE_COMBATMOVES){
					recievedCombat += 1;
					findPlayer(ID).setMoves((CombatMoves) m.getData()[0]);
				}else{
					network.send(ID, "NOT ACCEPTING COMBAT MOVES ATM");
				}
			}
			if( m.getType() == MessageType.CHARACTER_SELECT){
				if(state == GameState.CHOOSE_CHARACTER){
					switch((CharacterName) m.getData()[0]){
					
					case AMAZON: players[addedPlayers] = new Player(new Amazon(), ID); break;
					case BERSERKER:players[addedPlayers] = new Player(new Berserker() , ID);
					break;
					case BLACK_KNIGHT:players[addedPlayers] = new Player(new BlackKnight(), ID);
					break;
					case CAPTAIN:players[addedPlayers] = new Player(new Captain(), ID);
					break;
					case DWARF:players[addedPlayers] = new Player(new Dwarf(), ID);
					break;
					case ELF:players[addedPlayers] = new Player(new Elf(), ID);
					break;
					case SWORDSMAN:players[addedPlayers] = new Player(new Swordsman(), ID);
					break;
					case WHITE_KNIGHT:players[addedPlayers] = new Player(new WhiteKnight(), ID);
					break;
					default:;
					break;
					
					}
					addedPlayers += 1;
				}else{
					network.send(ID, "NOT ACCEPTING CHARACTER SELECT ATM");
				}
			}
		}
	}

	/**
	 * Turns a player ID into a player
	 * @param ID of the player you are looking for
	 * @return the player you are looknig for
	 */
	public Player findPlayer(int ID){
		for(int i=0;i<playerCount;i++){
			if(players[i].getID() == ID){
				return players[i];
			}
		}
		return null;
	}

	/**
	 * Sets a player's state as hidden
	 * @param player being hidden
	 */
	public void hide(Player player) { // assume it always works
        player.setHidden(true);
    }

	/**
	 * Causes a player to rest
	 * @param player who is resting
	 */
	public void rest(Player player) {
		//TODO IS THIS EVEN WHAT REST DOES?
        player.setFatigue(0); // I'm assuming it resets the fatigue, which I'm pretty sure is wrong
    }

	/**
	 * Determines which players a certain player is currently able to block
	 * @param player The player who's options are being determined
	 * @return The players that the (param) player is able to block
	 */
    public Player[] blockable(Player player) {
    	Player[] blockedPlayers = player.getLocation().getOccupants();
    	for (int i = 0; i < blockedPlayers.length; i++) {
    		if (blockedPlayers[i].isHidden()) {
    			blockedPlayers[i] = null;
    		}
    	}
    	return blockedPlayers;
    }

    /**
     * Blocks all unhidden players in the same clearing as the monster
     * @param monster the monster blocking people
     */
    public void block(Monster monster) {
    	Player[] blockablePlayers = monster.getLocation().getOccupants();
    	for (int i = 0; i < blockablePlayers.length; i++) {
    		if (!blockablePlayers[i].isHidden()) {
    			players[i].setBlocked(true);
    		}
    	}
    }

	/**
	 * Unhides all players in the same clearing as player and returns a list of them
	 * @param The player doing the searching
	 * @return A list of all players in the clearing (now unhidden)
	 */
    public Player[] search(Player player) {
    	Player[] sameClearingPlayers = player.getLocation().getOccupants();
    	for (int i = 0; i < sameClearingPlayers.length; i++) {
    		sameClearingPlayers[i].setHidden(false);
    	}

        Treasure[] clearingTreasures = player.getLocation().getTreasures();
        for (int i = 0; i < clearingTreasures.length; i++) {
            if (clearingTreasures[i] != null) {
                player.addTreasure(clearingTreasures[i]);
                // TODO: let player know they found treasures
                player.getLocation().removeTreasure(clearingTreasures[i]);
            }
        }
    	return sameClearingPlayers;
    }

    /**
     * Sets a player's weapon to a specific state s
     * @param p The player
     * @param pos The position of the weapon in the weapon array
     * @param state the new state of the weapon
     */
    public void alert(Player p, int pos, Boolean state) {
       p.getWeapons()[pos].setActive(state);
    }

    /**
     * resets the day and determines if the game is over
     * @return returns true if day was reset, false if it's the 28th day
     */
    public boolean resetDay() {
        if (currentDay == 28) {
            return false;
        }

        unAlertWeapons();		//moved this functionality to the player class, where it belongs
        						//TODO face up map chits (except lost city and lost castle) are turned face down
        
        for (int i = 0; i < playerCount; i++) {
        	players[i].setFatigue(0);
        }

        return true;
    }

    /**
     * Cycles through players, unalterts their weapons
     */
    public void unAlertWeapons(){
    	for (int i = 0; i < players.length; i++) {
    		players[i].unAlertWeapons();
    	}
    }

    /**
     * Resets the week
     */
    public void resetWeek() {
        resetDay();
        //TODO return monsters to their place
        //TODO reset natives
    }

 // TODO: need to figure out how to save the clearing the player wants to move to
    // and the weapons/armour (??) they want to alert/unalert
    public void doTodaysActivities(Player player) {
    	for (int i = 0; i < player.getActivities().length; i++) {
    		Player[] canBlock = blockable(player);					// check if they can block another player
    		if (currentDay != 1) {
	    		for (int j = 0; j < canBlock.length; j++) {
	    			if (canBlock[j] != null) {
	    				canBlock[j].setBlocked(true);
	    				// TODO: send message to players that have been blocked
	    			}
	    		}
    		}

    		Object[] activities = player.getActivities();
    		int moves = 0;
    		if (!player.isBlocked()) {				//assuming the player is not being blocked by another
    			// format: [MOVE, clearing]
    			// format: [ALERT, weaponposition, trueOrFalse]

    			if (activities[moves] != null) {
		    		switch((Actions) activities[0]) {

		    		case MOVE: board.move(player, (Clearing)activities[moves + 1]); moves = moves + 2; break;
		    		case HIDE: hide(player); moves++; break;
		    		case ALERT: alert(player, (int)activities[moves + 1], (boolean)activities[moves + 2]); moves = moves + 3; break;
		    		case REST: rest(player); moves++; break;
		    		case SEARCH: search(player); moves++; break;
		    		case TRADE: moves++; break;
		    		case FOLLOW: moves++; break;
		    		}
    			}
    		}
    		else if (activities[0] == Utility.Actions.HIDE) {
    			player.setBlocked(false);
    			hide(player);
    			moves++;
    		}
    	}
    }

    /**
     * Ends the game, basicly determines platyers scores and determines the winner
     * @return The winning player
     */
    public Player endGame() {

        Player winner = null;
        Player player = null;

        // TODO: player has to discard any items an active move chit can't carry
        // TODO: treasures
        // TODO: Actually determine score based on individual victory points? or is it different for a day 28 time out?
        for (int i = 0; i < playerCount; i++ ) {
            player = players[i];
            if (winner == null)
                winner = players[i];

            int basicScore     = 0;
            int fameScore      = players[i].getFame() / 10;
            int notorietyScore = players[i].getNotoriety() / 20;
            int goldScore      = players[i].getGold() / 30;

            basicScore = fameScore + notorietyScore + goldScore;
            player.setFinalScore(basicScore);

            if (basicScore > winner.getFinalScore()) {
                winner = player;
            }
        }

        return winner;
    }

    /**
     * Waits untill all activities have been submitted (and tells the clients to send them)
     */
    public void collectActivities(){
    	state = GameState.CHOOSE_PLAYS;
    	network.broadCast("SEND MOVES");
    	recievedMoves = 0;
    	while(recievedMoves < playerCount){}	//TODO HANDLE PLAYERS DROPPING OUT DURING THIS STEP
    	state = GameState.NULL;
    }

    public void collectCombat(){
    	state = GameState.CHOOSE_COMBAT;
    	recievedCombat = 0;
    	network.broadCast("SEND COMBAT");
    	while(recievedCombat < playerCount){}	//TODO HANDLE PLAYERS DROPPING OUT DURING THIS STEP
    	state = GameState.NULL;
    }

    public void updateClients(){
    	network.broadCast(board);
    	distributeCharacters();
    }

    /**
     * Starts a new day
     */
    public void startDay() {

        currentDay++;
        updateClients();
        collectActivities(); //asks player's for their activities and waits until it gets them all

        orderPlayers();	//randomly orders the players

        //Does the activities of all players
        int nextMover = 0;
        while (nextMover < playerCount) {
            for (int i = 0; i < playerCount; i++) {
                if (players[i].order == nextMover) {
                	doTodaysActivities(players[i]);
                    nextMover++;
                    break;
                }
            }
        }

        updateClients();
        collectCombat(); //2 players, 1 attacker 1 deffender

        //TODO MICHAEL RESOLVE COMBAT
        //All players choose attackers
        nextMover = 0;
        while (nextMover < playerCount) {
            for (int i = 0; i < playerCount; i++) {
                if (players[i].order == nextMover) {
                    // TODO choose attackers and save somewhere
                	encounter(players[i], players[i].getTarget());
                	//TODO MICHAEL DO COMBAT HERE

                    nextMover++;
                    break;
                }
            }
        }

        // TODO combat loop

        //Porgresses to the next day or ends the game
        if(!resetDay()){ //if it is not the 28th day....
        	startDay();
        } else {
        	endGame();
        }
    }

    /**
     * Randomly orders the players //TODO AFTER ARRAYLIST CONVERSION, JUST .SHUFFLE
     */
    public void orderPlayers(){
    	// Silly way to order players from 1 to playerCount+1
        for (int i = 0; i < playerCount; i++) {
        	System.out.println(i);
            players[i].order = Utility.roll(100);
        }

        int[] ordering = new int[playerCount];

        for (int i = 0; i < playerCount; i++) {
            ordering[i] = players[i].order;
        }

        Arrays.sort(ordering);

        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < playerCount; j++) {
                if (ordering[i] == players[j].order) {
                    players[j].order = i;
                    break;
                }
            }
        }
    }

    /**
     *
     * @param attacker
     * @param defender
     */
    public void encounter(Player attacker, Player defender) {
    	//TODO Weapons active, networking
		// Check if weapon is active
		/*if (player.weapons[0].isActive() == false) {
			player.weapons[0].setActive(true);
			return;
		}*/

    	//ask clients to send moves!
    	state = GameState.CHOOSE_COMBATMOVES;
    	recievedCombat = 0;
    	network.send(attacker.getID(), "SEND COMBATMOVES");
    	network.send(defender.getID(), "SEND COMBATMOVES");
    	while(recievedCombat < 2){}
    	state = GameState.NULL;

		doFight(attacker, defender);
	}

	//TODO Finding active weapon rather than assuming the active weapon is at position 0
	public void doFight(Player attacker, Player defender) {
		if (attackerMoves.getTarget().getWeapons()[0].getSpeed() < defenderMoves.getTarget().getWeapons()[0].getSpeed()) {
			if ((attackerMoves.getTarget().getWeapons()[0].getSpeed() - attackerMoves.attackFatigue) <= (defenderMoves.getTarget().getCharacter().getSpeed() - defenderMoves.maneuverFatigue)) {
				hit(attackerMoves, defenderMoves);
			}
			else if (attackerMoves.getAttack() == Attacks.THRUST && defenderMoves.getManeuver() == Maneuvers.CHARGE) {
				hit(attackerMoves, defenderMoves);
			}
			else if (attackerMoves.getAttack() == Attacks.SWING && defenderMoves.getManeuver() == Maneuvers.DODGE) {
				hit(attackerMoves, defenderMoves);
			}
			else if (attackerMoves.getAttack() == Attacks.SMASH && defenderMoves.getManeuver() == Maneuvers.DUCK) {
				hit(attackerMoves, defenderMoves);
			}

			if (attackerMoves.getTarget().isDead() == false) {
				if ((defenderMoves.getTarget().getWeapons()[0].getSpeed() - defenderMoves.attackFatigue) <= (attackerMoves.getTarget().getCharacter().getSpeed() - attackerMoves.maneuverFatigue)) {
					hit(defenderMoves, attackerMoves);
				}
				else if (defenderMoves.getAttack() == Attacks.THRUST && attackerMoves.getManeuver() == Maneuvers.CHARGE) {
					hit(defenderMoves, attackerMoves);
				}
				else if (defenderMoves.getAttack() == Attacks.SWING && attackerMoves.getManeuver() == Maneuvers.DODGE) {
					hit(defenderMoves, attackerMoves);
				}
				else if (defenderMoves.getAttack() == Attacks.SMASH && attackerMoves.getManeuver() == Maneuvers.DUCK) {
					hit(defenderMoves, attackerMoves);
				}
			}
		}
		else if (attackerMoves.getTarget().getWeapons()[0].getSpeed() > defenderMoves.getTarget().getWeapons()[0].getSpeed()) {
			if ((defenderMoves.getTarget().getWeapons()[0].getSpeed() - defenderMoves.attackFatigue) <= (attackerMoves.getTarget().getCharacter().getSpeed() - attackerMoves.maneuverFatigue)) {
				hit(defenderMoves, attackerMoves);
			}
			else if (defenderMoves.getAttack() == Attacks.THRUST && attackerMoves.getManeuver() == Maneuvers.CHARGE) {
				hit(defenderMoves, attackerMoves);
			}
			else if (defenderMoves.getAttack() == Attacks.SWING && attackerMoves.getManeuver() == Maneuvers.DODGE) {
				hit(defenderMoves, attackerMoves);
			}
			else if (defenderMoves.getAttack() == Attacks.SMASH && attackerMoves.getManeuver() == Maneuvers.DUCK) {
				hit(defenderMoves, attackerMoves);
			}

			if (attackerMoves.getTarget().isDead() == false) {
				if ((attackerMoves.getTarget().getWeapons()[0].getSpeed() - attackerMoves.attackFatigue) <= (defenderMoves.getTarget().getCharacter().getSpeed() - defenderMoves.maneuverFatigue)) {
					hit(attackerMoves, defenderMoves);
				}
				else if (attackerMoves.getAttack() == Attacks.THRUST && defenderMoves.getManeuver() == Maneuvers.CHARGE) {
					hit(attackerMoves, defenderMoves);
				}
				else if (attackerMoves.getAttack() == Attacks.SWING && defenderMoves.getManeuver() == Maneuvers.DODGE) {
					hit(attackerMoves, defenderMoves);
				}
				else if (attackerMoves.getAttack() == Attacks.SMASH && defenderMoves.getManeuver() == Maneuvers.DUCK) {
					hit(attackerMoves, defenderMoves);
				}
			}
		}

		else {
			if (attackerMoves.getTarget().getWeapons()[0].getLength() < defenderMoves.getTarget().getWeapons()[0].getLength()) {
				if ((attackerMoves.getTarget().getWeapons()[0].getSpeed() - attackerMoves.attackFatigue) <= (defenderMoves.getTarget().getCharacter().getSpeed() - defenderMoves.maneuverFatigue)) {
					hit(attackerMoves, defenderMoves);
				}
				else if (attackerMoves.getAttack() == Attacks.THRUST && defenderMoves.getManeuver() == Maneuvers.CHARGE) {
					hit(attackerMoves, defenderMoves);
				}
				else if (attackerMoves.getAttack() == Attacks.SWING && defenderMoves.getManeuver() == Maneuvers.DODGE) {
					hit(attackerMoves, defenderMoves);
				}
				else if (attackerMoves.getAttack() == Attacks.SMASH && defenderMoves.getManeuver() == Maneuvers.DUCK) {
					hit(attackerMoves, defenderMoves);
				}

				if (attackerMoves.getTarget().isDead() == false) {
					if ((defenderMoves.getTarget().getWeapons()[0].getSpeed() - defenderMoves.attackFatigue) <= (attackerMoves.getTarget().getCharacter().getSpeed() - attackerMoves.maneuverFatigue)) {
						hit(defenderMoves, attackerMoves);
					}
					else if (defenderMoves.getAttack() == Attacks.THRUST && attackerMoves.getManeuver() == Maneuvers.CHARGE) {
						hit(defenderMoves, attackerMoves);
					}
					else if (defenderMoves.getAttack() == Attacks.SWING && attackerMoves.getManeuver() == Maneuvers.DODGE) {
						hit(defenderMoves, attackerMoves);
					}
					else if (defenderMoves.getAttack() == Attacks.SMASH && attackerMoves.getManeuver() == Maneuvers.DUCK) {
						hit(defenderMoves, attackerMoves);
					}
				}
			}
			else if (attackerMoves.getTarget().getWeapons()[0].getLength() > defenderMoves.getTarget().getWeapons()[0].getLength()) {
				if ((defenderMoves.getTarget().getWeapons()[0].getSpeed() - defenderMoves.attackFatigue) <= (attackerMoves.getTarget().getCharacter().getSpeed() - attackerMoves.maneuverFatigue)) {
					hit(defenderMoves, attackerMoves);
				}
				else if (defenderMoves.getAttack() == Attacks.THRUST && attackerMoves.getManeuver() == Maneuvers.CHARGE) {
					hit(defenderMoves, attackerMoves);
				}
				else if (defenderMoves.getAttack() == Attacks.SWING && attackerMoves.getManeuver() == Maneuvers.DODGE) {
					hit(defenderMoves, attackerMoves);
				}
				else if (defenderMoves.getAttack() == Attacks.SMASH && attackerMoves.getManeuver() == Maneuvers.DUCK) {
					hit(defenderMoves, attackerMoves);
				}

				if (attackerMoves.getTarget().isDead() == false) {
					if ((attackerMoves.getTarget().getWeapons()[0].getSpeed() - attackerMoves.attackFatigue) <= (defenderMoves.getTarget().getCharacter().getSpeed() - defenderMoves.maneuverFatigue)) {
						hit(attackerMoves, defenderMoves);
					}
					else if (attackerMoves.getAttack() == Attacks.THRUST && defenderMoves.getManeuver() == Maneuvers.CHARGE) {
						hit(attackerMoves, defenderMoves);
					}
					else if (attackerMoves.getAttack() == Attacks.SWING && defenderMoves.getManeuver() == Maneuvers.DODGE) {
						hit(attackerMoves, defenderMoves);
					}
					else if (attackerMoves.getAttack() == Attacks.SMASH && defenderMoves.getManeuver() == Maneuvers.DUCK) {
						hit(attackerMoves, defenderMoves);
					}
				}
			}
			else {
				if ((attackerMoves.getTarget().getWeapons()[0].getSpeed() - attackerMoves.attackFatigue) <= (defenderMoves.getTarget().getCharacter().getSpeed() - defenderMoves.maneuverFatigue)) {
					hit(attackerMoves, defenderMoves);
				}
				else if (attackerMoves.getAttack() == Attacks.THRUST && defenderMoves.getManeuver() == Maneuvers.CHARGE) {
					hit(attackerMoves, defenderMoves);
				}
				else if (attackerMoves.getAttack() == Attacks.SWING && defenderMoves.getManeuver() == Maneuvers.DODGE) {
					hit(attackerMoves, defenderMoves);
				}
				else if (attackerMoves.getAttack() == Attacks.SMASH && defenderMoves.getManeuver() == Maneuvers.DUCK) {
					hit(attackerMoves, defenderMoves);
				}

				if (attackerMoves.getTarget().isDead() == false) {
					if ((defenderMoves.getTarget().getWeapons()[0].getSpeed() - defenderMoves.attackFatigue) <= (attackerMoves.getTarget().getCharacter().getSpeed() - attackerMoves.maneuverFatigue)) {
						hit(defenderMoves, attackerMoves);
					}
					else if (defenderMoves.getAttack() == Attacks.THRUST && attackerMoves.getManeuver() == Maneuvers.CHARGE) {
						hit(defenderMoves, attackerMoves);
					}
					else if (defenderMoves.getAttack() == Attacks.SWING && attackerMoves.getManeuver() == Maneuvers.DODGE) {
						hit(defenderMoves, attackerMoves);
					}
					else if (defenderMoves.getAttack() == Attacks.SMASH && attackerMoves.getManeuver() == Maneuvers.DUCK) {
						hit(defenderMoves, attackerMoves);
					}
				}
			}
		}
	}

	public void hit(CombatMoves attackerMoves, CombatMoves defenderMoves) {
		ItemWeight level = attackerMoves.getTarget().getWeapons()[0].getWeight();

		//TODO Print out "hit" to console

		if (attackerMoves.getAttackFatigue() == 1) {
			switch(level){
            	case NEGLIGIBLE: level = ItemWeight.LIGHT; break;
            	case LIGHT: level = ItemWeight.MEDIUM;break;
            	case MEDIUM: level = ItemWeight.HEAVY;break;
            	case HEAVY: level = ItemWeight.TREMENDOUS;break;
            	default: break;
			}
		}
		else if (attackerMoves.getAttackFatigue() == 2) {
			switch(level){
        		case NEGLIGIBLE: level = ItemWeight.MEDIUM;break;
        		case LIGHT: level = ItemWeight.HEAVY;break;
        		case MEDIUM: level = ItemWeight.TREMENDOUS;break;
        		case HEAVY: level = ItemWeight.TREMENDOUS;break;
        		default: break;
			}
		}
		//TODO armor destruction
		if (attackerMoves.getAttack() == Attacks.THRUST && defenderMoves.getDefense() == Defenses.AHEAD) {
			switch(level){
        		case NEGLIGIBLE: level = ItemWeight.NEGLIGIBLE;break;
        		case LIGHT: level = ItemWeight.NEGLIGIBLE;break;
        		case MEDIUM: level = ItemWeight.LIGHT;break;
        		case HEAVY: level = ItemWeight.MEDIUM;break;
        		case TREMENDOUS: level = ItemWeight.HEAVY;break;
        		default: break;
			}
		}
		else if (attackerMoves.getAttack() == Attacks.SWING && defenderMoves.getDefense() == Defenses.SIDE) {
			switch(level){
    			case NEGLIGIBLE: level = ItemWeight.NEGLIGIBLE;break;
    			case LIGHT: level = ItemWeight.NEGLIGIBLE;break;
    			case MEDIUM: level = ItemWeight.LIGHT;break;
    			case HEAVY: level = ItemWeight.MEDIUM;break;
    			case TREMENDOUS: level = ItemWeight.HEAVY;break;
    			default: break;
			}
		}
		else if (attackerMoves.getAttack() == Attacks.SMASH && defenderMoves.getDefense() == Defenses.ABOVE) {
			switch(level){
    			case NEGLIGIBLE: level = ItemWeight.NEGLIGIBLE;break;
    			case LIGHT: level = ItemWeight.NEGLIGIBLE;break;
    			case MEDIUM: level = ItemWeight.LIGHT;break;
    			case HEAVY: level = ItemWeight.MEDIUM;break;
    			case TREMENDOUS: level = ItemWeight.HEAVY;break;
    			default: break;
			}
		}

		if (level == ItemWeight.TREMENDOUS) {
			deadPlayer(attackerMoves, defenderMoves);
		}
		else if (level == ItemWeight.HEAVY) {
			deadPlayer(attackerMoves, defenderMoves);
		}
		else if (level == attackerMoves.getTarget().getCharacter().getWeight()) {
			deadPlayer(attackerMoves, defenderMoves);
		}
		else if (level == ItemWeight.MEDIUM && attackerMoves.getTarget().getCharacter().getWeight() == ItemWeight.LIGHT) {
			deadPlayer(attackerMoves, defenderMoves);
		}
		//TODO Change player health on client and server side
		else if (level == ItemWeight.MEDIUM && attackerMoves.getTarget().getCharacter().getWeight() == ItemWeight.HEAVY) {
			attackerMoves.getTarget().setHealth(attackerMoves.getTarget().getHealth() + 1);
			if (attackerMoves.getTarget().getHealth() == 3) {
				deadPlayer(attackerMoves, defenderMoves);
			}
		}
		else if (level == ItemWeight.LIGHT && attackerMoves.getTarget().getCharacter().getWeight() == ItemWeight.HEAVY) {
			attackerMoves.getTarget().setHealth(attackerMoves.getTarget().getHealth() + 1);
			if (attackerMoves.getTarget().getHealth() == 3) {
				deadPlayer(attackerMoves, defenderMoves);
			}
		}
		else if (level == ItemWeight.LIGHT && attackerMoves.getTarget().getCharacter().getWeight() == ItemWeight.MEDIUM) {
			attackerMoves.getTarget().setHealth(attackerMoves.getTarget().getHealth() + 1);
			if (attackerMoves.getTarget().getHealth() == 2) {
				deadPlayer(attackerMoves, defenderMoves);
			}
		}
	}
	//TODO Change values on client and server side
	public void deadPlayer(CombatMoves attackerMoves, CombatMoves defenderMoves) {
		defenderMoves.getTarget().addFame(10); // Arbitrary value
		defenderMoves.getTarget().addGold(attackerMoves.getTarget().getGold());
		attackerMoves.getTarget().removeGold(attackerMoves.getTarget().getGold());
		defenderMoves.getTarget().addNotoriety(attackerMoves.getTarget().getNotoriety());
		attackerMoves.getTarget().removeNotoriety(attackerMoves.getTarget().getNotoriety());
		attackerMoves.getTarget().kill();
		//TODO Print death message to console
	}

	public void distributeCharacters(){

		//sends each player object to the right client
		for(int i=0;i<playerCount;i++){
			network.send(players[i].getID(), players[i]);
		}

	}

	/**
	 * calld via a "START GAME" message in order to setup the board and start the game.
	 */
	public void startGame(){
		
		//ask clients to send moves!
		state = GameState.CHOOSE_CHARACTER;
    	addedPlayers = 0;
    	network.broadCast("CHARACTER SELECT");
    	while(addedPlayers < Config.MAX_CLIENTS){}		//cant get past here until the list of players is done
    	state = GameState.NULL;

		//instanciate the model
		this.board = new Board(players);
		System.out.println("Server Models Created.");

		network.broadCast(board);  				//sends the board to all clients
		distributeCharacters();					//broadcast each player to the proper client
		startDay();								//starts the game!

	}

	/**
	 * Running this method will trigger the process of creating a MagicRealm server.
	 * @param args Command line arguments, likely to remain unused
	 */
	@SuppressWarnings("unused")
	public static void main(String args[]){
		ServerController control = new ServerController();		//instanciate the controller
	}

}
