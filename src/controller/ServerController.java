package controller;

import networking.NetworkServer;
import model.Board;
import model.Player;
import model.ServerModel;
import model.Swordsman;
import model.Tile;

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
    int playerCount    = 0;
    int currentDay     = 0;
    boolean hasStarted = false;
	
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
	}
	
	public boolean hide(Player player) { // assume it always works
        player.setHidden(true);
        return true;
    }
	
	public void rest(Player player) {
        player.setFatigue(0); // I'm assuming it resets the fatigue, which I'm pretty sure is wrong
    }
	
	/**
	 * Determines which players a certain player is currently able to block
	 * @param player The player who's options are being determined
	 * @return The players that the (param) player is able to block
	 */
    public Player[] block(Player player) {
    	Player[] blockedPlayers = player.getLocation().getOccupants();
    	// finds the unhidden players in the same clearing as them
    	for (int i = 0; i < blockedPlayers.length; i++) {
    		if (blockedPlayers[i].isHidden()) {
    			blockedPlayers[i] = null;
    		}
    	}
    	return blockedPlayers;
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
    
    // returns true if day was reset
    // returns false if it's the 28th day
    public boolean resetDay() {

        if (currentDay == 28) {
            return false;
        }
        
        unAlertWeapons();		//moved this functionality to the player class, where it belongs
        						//TODO face up map chits (except lost city and lost castle) are turned face down
        return true;
    }
    
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
    		
    		// check if they can block another player
    		Player[] players = block(player);
    		
    		for (int j = 0; j < players.length; j++) {
    			if (players[j] != null) {
    				// TODO: the player has the option to block them. Should I just do that automatically for now??
    				// TODO: otherwise, that'll be networking
    				// TODO: send message to players that have been blocked
    			}
    		}
    		
    		if (!player.isBlocked()) {
    			// format: [MOVE, clearing]
    			// format: [ALERT, weapon, trueOrFalse]
    			
    			Object[] activities = player.getActivities();
    			int moves = 0;
    			
    			if (activities[moves] != null) {
		    		switch((Actions) activities[0]) {
		    		
		    		case MOVE: board.move(player, activities[moves + 1]); moves = moves + 2; break;
		    		case HIDE: hide(player); moves++; break;
		    		case ALERT: alert(activities[moves + 1], activities[moves + 2]); moves = moves + 3; break;
		    		case REST: rest(player); moves++; break;
		    		case SEARCH: search(player); moves++; break;
		    		case TRADE: moves++; break;
		    		case FOLLOW: moves++; break;
		    		}
    			}
    		}
    	}
    }

    // todo: in next iteration calculate the score properly
    // 1 point per great treasure
    // 1 point for each 10 points of fame, 1 point for each 20 points of notoriety, and 1 point for each 30 gold
    public Player endGame() {
        Player winner = null;
        Player player = null;

        // TODO: player has to discard any items an active move chit can't carry
        // TODO: treasures

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

    
    public void startDay() {
        currentDay++;

        //TODO choose moves

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
        // Do moves in order
        int nextMover = 0;
        while (nextMover < playerCount) {
            for (int i = 0; i < playerCount; i++) {
                if (players[i].order == nextMover) {
                    // players do their moves
                	doTodaysActivities(players[i]);
                    nextMover++;
                    break;
                }
            }
        }

        // Choose attacks
        nextMover = 0;
        while (nextMover < playerCount) {
            for (int i = 0; i < playerCount; i++) {
                if (players[i].order == nextMover) {
                    // TODO choose attackers and save somewhere
                    nextMover++;
                    break;
                }
            }
        }

        // TODO combat loop

        resetDay(); // not sure if we want to change resetDay so it will call startDay again if all is well
    }
    
    //TODO Weapons active, networking
    public void Encounter(Player attacker, Player defender) {
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
            	case NEGLIGIBLE: level = ItemWeight.LIGHT;
            	case LIGHT: level = ItemWeight.MEDIUM;
            	case MEDIUM: level = ItemWeight.HEAVY;
            	case HEAVY: level = ItemWeight.TREMENDOUS;
            	default: level = level;
			}
		}
		else if (attackerMoves.getAttackFatigue() == 2) {
			switch(level){
        		case NEGLIGIBLE: level = ItemWeight.MEDIUM;
        		case LIGHT: level = ItemWeight.HEAVY;
        		case MEDIUM: level = ItemWeight.TREMENDOUS;
        		case HEAVY: level = ItemWeight.TREMENDOUS;
        		default: level = level;
			}
		}
		//TODO armor destruction
		if (attackerMoves.getAttack() == Attacks.THRUST && defenderMoves.getDefense() == Defenses.AHEAD) {
			switch(level){
        		case NEGLIGIBLE: level = ItemWeight.NEGLIGIBLE;
        		case LIGHT: level = ItemWeight.NEGLIGIBLE;
        		case MEDIUM: level = ItemWeight.LIGHT;
        		case HEAVY: level = ItemWeight.MEDIUM;
        		case TREMENDOUS: level = ItemWeight.HEAVY;
        		default: level = level;
			}
		}
		else if (attackerMoves.getAttack() == Attacks.SWING && defenderMoves.getDefense() == Defenses.SIDE) {
			switch(level){
    			case NEGLIGIBLE: level = ItemWeight.NEGLIGIBLE;
    			case LIGHT: level = ItemWeight.NEGLIGIBLE;
    			case MEDIUM: level = ItemWeight.LIGHT;
    			case HEAVY: level = ItemWeight.MEDIUM;
    			case TREMENDOUS: level = ItemWeight.HEAVY;
    			default: level = level;
			}
		}
		else if (attackerMoves.getAttack() == Attacks.SMASH && defenderMoves.getDefense() == Defenses.ABOVE) {
			switch(level){
    			case NEGLIGIBLE: level = ItemWeight.NEGLIGIBLE;
    			case LIGHT: level = ItemWeight.NEGLIGIBLE;
    			case MEDIUM: level = ItemWeight.LIGHT;
    			case HEAVY: level = ItemWeight.MEDIUM;
    			case TREMENDOUS: level = ItemWeight.HEAVY;
    			default: level = level;
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
	 * (If actually needed, this will actually start up the client, bring it to life.
	 */
	public void run(){
		//start the network up
		//populate the model as needed
	}
	
	/**
	 * calld via a "START GAME" message in order to setup the board and start the game.
	 */
	public void startGame(){
		
		//run selectCharacters() function so that players can choose their characters
		
		//create array of players, feed it to the board constructor
		int[] IDs = network.getIDs();
		
		for(int i=0; i<Config.MAX_CLIENTS; i++){
			players[i] = new Player(new Swordsman(), IDs[i]);
		}
		
		//instanciate the model
		this.board = new Board(players);
		System.out.println("Server Models Created.");
	
		hasStarted = true;
		
	}
	
	/**
	 * Running this method will trigger the process of creating a MagicRealm server.
	 * @param args Command line arguments, likely to remain unused
	 */
	public static void main(String args[]){
		ServerController control = new ServerController();		//instanciate the controller
		control.run();											//start the controller
	}	

}
