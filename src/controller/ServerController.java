package controller;

import networking.Message;
import networking.NetworkServer;
import model.Board;
import model.Clearing;
import model.CombatMoves;
import model.Monster;
import model.Player;
import model.Swordsman;
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
    int playerCount    = Config.MAX_CLIENTS;
    int currentDay     = 0;
    int recievedCombat = 0;
	boolean acceptingCombat = false;
	int recievedMoves = 0;
	boolean acceptingMoves = false;
	
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
				if(acceptingMoves){
					recievedMoves += 1;
					findPlayer(ID).setActivities(m.getData());
				}else{
					network.send(ID, "NOT ACCEPTING MOVES ATM");
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
    		for (int j = 0; j < canBlock.length; j++) {
    			if (canBlock[j] != null) {
    				// TODO: the player has the option to block them. Should I just do that automatically for now??
    				// TODO: otherwise, that'll be networking
    				// TODO: send message to players that have been blocked
    			}
    		}
    		
    		if (!player.isBlocked()) {				//assuming the player is not being blocked by another
    			// format: [MOVE, clearing]
    			// format: [ALERT, weaponposition, trueOrFalse]
    			
    			Object[] activities = player.getActivities();
    			int moves = 0;
    			
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
    	acceptingMoves = true;
    	network.broadCast("SEND MOVES");
    	recievedMoves = 0;
    	while(recievedMoves < playerCount){}	//TODO HANDLE PLAYERS DROPPING OUT DURING THIS STEP
    	acceptingMoves = false;
    }
    
    public void collectCombat(){
    	acceptingCombat = true;
    	recievedCombat = 0;
    	network.broadCast("SEND COMBAT");
    	while(recievedCombat < playerCount){}	//TODO HANDLE PLAYERS DROPPING OUT DURING THIS STEP
    	acceptingCombat = false;
    }
    
    /**
     * Starts a new day
     */
    public void startDay() {
        currentDay++;
        
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

        collectCombat(); //2 players, 1 attacker 1 deffender
        
        //TODO MICHAEL RESOLVE COMBAT
        //All players choose attackers
        nextMover = 0;
        while (nextMover < playerCount) {
            for (int i = 0; i < playerCount; i++) {
                if (players[i].order == nextMover) {
                    // TODO choose attackers and save somewhere
                	//
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
    public void Encounter(Player attacker, Player defender) {
    	//TODO Weapons active, networking
		// Check if weapon is active
		/*if (player.weapons[0].isActive() == false) {
			player.weapons[0].setActive(true);
			return;
		}*/

    	//TODO getMoves should query the clients, which will then choose their moves and send them back to the server. I just left it as is for now
		CombatMoves attackerMoves = getMoves(attacker);
		CombatMoves defenderMoves = getMoves(defender);

		doFight(attackerMoves, defenderMoves);
	}

    // Will be run on the client side
    public CombatMoves getMoves(Player player) {
    	// Player chooses target, attack, maneuver, defense, and fatigue levels for each
    	boolean properFatigue = false;
    	CombatMoves moves = null;
    	Player target = null;
		Attacks attack = null;
		int attackFatigue = 0;
		Maneuvers maneuver = null;
		int maneuverFatigue = 0;
		Defenses defense = null;

    	while (properFatigue == false) {
    		//TODO Will change based on how the panels are implemented, needs to grab input from dropdown boxes for all
    		// Each value will be filled depending on what the user chooses from the panel
    		moves.target = target;
    		moves.attack = attack;
    		moves.attackFatigue = attackFatigue;
    		moves.maneuver = maneuver;
    		moves.maneuverFatigue = maneuverFatigue;
    		moves.defense = defense;

    		int totalFatigue = (moves.getAttackFatigue() + moves.getManeuverFatigue());
    		if (totalFatigue <= 2 && player.getFatigue() <= 8) { // Choosing 10 as the arbitrary value of total fatigue
    			properFatigue = true;
    			player.setFatigue(player.getFatigue() + totalFatigue);
    		}
    		//TODO Print "improper amounts of fatigue" to console or some other error message
    	}
    	return moves; //TODO send this to the server
    }

	//TODO Finding active weapon rather than assuming the active weapon is at position 0
	public void doFight(CombatMoves attackerMoves, CombatMoves defenderMoves) {
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
	
	/**
	 * calld via a "START GAME" message in order to setup the board and start the game.
	 */
	public void startGame(){
		
		//selectCharacters();	//broadcast to clients to submit character choices and wait until all conflicts are resolved.
		
		//create array of players, feed it to the board constructor
		int[] IDs = network.getIDs();
		
		for(int i=0; i<Config.MAX_CLIENTS; i++){
			players[i] = new Player(new Swordsman(), IDs[i]);
		}
		
		//instanciate the model
		this.board = new Board(players);
		System.out.println("Server Models Created.");
		
		network.broadCast(board);  				//sends the board to all clients
		
		//starts the game!
		startDay();
		
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
