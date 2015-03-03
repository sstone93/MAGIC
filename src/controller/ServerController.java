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
//import model.Treasure;
//import model.TreasureWithinTreasure;
import model.WhiteKnight;

import java.util.ArrayList;
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
	ArrayList<Player> players = new ArrayList<Player>();
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

	public Player charToPlayer(CharacterName n){
		for(int j=0; j< playerCount; j++){
			if(players.get(j).getCharacter().getName() == n){
				return players.get(j);
			}
		}
		return null;
	}

	/**
	 * This is the method that handles incomming messages from the networking components
	 * @param ID The ID of the client sending the message
	 * @param message the contents of the message
	 */
	public void handle(int ID, Object message){

		if(message == null){

		}

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

					//turns the recieved charctername into a player
					findPlayer(ID).setTarget(charToPlayer((CharacterName) m.getData().get(0)));
				}else{
					network.send(ID, "NOT ACCEPTING COMBAT TARGETS ATM");
				}
			}
			if( m.getType() == MessageType.COMBAT_MOVES){
				if(state == GameState.CHOOSE_COMBATMOVES){
					recievedCombat += 1;
					findPlayer(ID).setMoves((CombatMoves) m.getData().get(0));
				}else{
					network.send(ID, "NOT ACCEPTING COMBAT MOVES ATM");
				}
			}
			if( m.getType() == MessageType.CHARACTER_SELECT){
				if(state == GameState.CHOOSE_CHARACTER){
					switch((CharacterName) m.getData().get(0)){

					case AMAZON: players.add(new Player(new Amazon(), ID)); break;
					case BERSERKER:players.add(new Player(new Berserker() , ID));
					break;
					case BLACK_KNIGHT:players.add(new Player(new BlackKnight(), ID));
					break;
					case CAPTAIN:players.add(new Player(new Captain(), ID));
					break;
					case DWARF:players.add(new Player(new Dwarf(), ID));
					break;
					case ELF:players.add(new Player(new Elf(), ID));
					break;
					case SWORDSMAN:players.add(new Player(new Swordsman(), ID));
					break;
					case WHITE_KNIGHT:players.add(new Player(new WhiteKnight(), ID));
					break;
					default:;
					break;

					}
					this.addedPlayers += 1;

					System.out.println((CharacterName) m.getData().get(0));
					System.out.println(addedPlayers);

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
			if(players.get(0).getID() == ID){
				return players.get(0);
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
        player.setFatigue(0);
        if (player.getHealth() > 0) {
        	player.setHealth(player.getHealth() - 1);
        }
    }

	/**
	 * Determines which players a certain player is currently able to block
	 * @param player The player who's options are being determined
	 * @return The players that the (param) player is able to block
	 */
    public ArrayList<Player> blockable(Player player) {
    	ArrayList<Player> blockedPlayers = player.getLocation().getOccupants();
    	for (int i = 0; i < blockedPlayers.size(); i++) {
    		if (blockedPlayers.get(i) != null && !blockedPlayers.get(i).isHidden() && blockedPlayers.get(i).getID() != player.getID()) {
    			blockedPlayers.remove(i);
    		}
    	}
    	return blockedPlayers;
    }

    /**
     * Blocks all unhidden players in the same clearing as the monster
     * @param monster the monster blocking people
     */
    public void block(Monster monster) {
    	ArrayList<Player> blockablePlayers = monster.getLocation().getOccupants();
    	for (int i = 0; i < blockablePlayers.size(); i++) {
    		if (!blockablePlayers.get(i).isHidden()) {
    			players.get(i).setBlocked(true);
    		}
    	}
    }

	/**
	 * Unhides all players in the same clearing as player and returns a list of them
	 * @param The player doing the searching
	 * @return A list of all players in the clearing (now unhidden)
	 */
    public ArrayList<Player> search(Player player) {
    	ArrayList<Player> sameClearingPlayers = player.getLocation().getOccupants();
    	for (int i = 0; i < sameClearingPlayers.size(); i++) {
    		// do not unhide yourself
    		if (sameClearingPlayers.get(i) != null && sameClearingPlayers.get(i).getID() != player.getID())
    		    sameClearingPlayers.get(i).setHidden(false);
    	}

    	//TODO NICK: I disabled this for now, going to need search tables anyways
    	
    	/*ArrayList<Treasure> clearingTreasures = player.getLocation().getTreasures();
        for (int i = 0; i < clearingTreasures.length; i++) {
            if (clearingTreasures[i] != null) {
                player.addTreasure(clearingTreasures[i]);
                network.send(player.getID(), "You've found a treasure! + " + clearingTreasures[i].getGold() + "gold");
                player.getLocation().removeTreasure(clearingTreasures[i]);
            }
        }*/
    	
    	return sameClearingPlayers;
    }

    /**
     * Sets a player's weapon to the opposite state of what it is currently
     * @param p The player
     */
    public void alert(Player p) {
       p.getWeapons().get(0).setActive(!p.getWeapons().get(0).isActive());
    }

    /**
     * resets the day and determines if the game is over
     * @return returns true if day was reset, false if it's the 28th day
     */
    public boolean resetDay() {
    	System.out.println("ResetDay start");
        if (currentDay == 28) {
            return false;
        }

        unAlertWeapons();

        //TODO face up map chits (except lost city and lost castle) are turned face down

        // reset their fatigue and order
        for (int i = 0; i < playerCount; i++) {
        	players.get(i).setFatigue(0);
        	players.get(i).setOrder(0);
        }
        System.out.println("ResetDay end");
        return true;
    }

    /**
     * Cycles through players, unalerts their weapons
     */
    public void unAlertWeapons(){
    	for (int i = 0; i < players.size(); i++) {
    		players.get(i).unAlertWeapons();
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

    /**
     * Cycles through players and their moves for the day
     */
    public void doTodaysActivities(Player player) {
    	int moves = 0;
    	while (moves < player.getActivities().size()) {
    		// TODO: for testing
    		System.out.println("moves: " + moves + " activities: " + player.getActivities().size());



    		ArrayList<Object> activities = player.getActivities();

    		if (!player.isBlocked()) {	//assuming the player is not being blocked by another
    			// format: [MOVE, clearing]

    			if (activities.get(moves) != null) {

    				ArrayList<Player> canBlock = blockable(player);					// check if they can block another player

    	    		if (currentDay != 1) {
    		    		for (int j = 0; j < canBlock.size(); j++) {
    		    			if (canBlock.get(j) != null) {
    		    				canBlock.get(j).setBlocked(true);
    		    				System.out.println("blocking player!"); // TODO: for testing
    		    				network.send(canBlock.get(j).getID(), "You've been blocked! :( " );
    		    			}
    		    		}
    	    		}

		    		switch((Actions) activities.get(moves)) {

		    		case MOVE: board.move(player, (Clearing)activities.get(moves+1)); moves = moves + 2; network.broadCast(player.getCharacter().getName() + " is moving!"); break;
		    		case HIDE: hide(player); moves = moves + 2; network.broadCast(player.getCharacter().getName() + " is hiding!"); break;
		    		case ALERT: alert(player); moves = moves + 2; network.broadCast(player.getCharacter().getName() + " is alerting their weapon!"); break;
		    		case REST: rest(player); moves = moves + 2; network.broadCast(player.getCharacter().getName() + " is resting!"); break;
		    		case SEARCH: search(player); moves = moves + 2; network.broadCast(player.getCharacter().getName() + " is searching!"); break;
		    		case TRADE: moves = moves + 2; network.broadCast(player.getCharacter().getName() + " is trading!"); break;
		    		case FOLLOW: moves = moves + 2; network.broadCast(player.getCharacter().getName() + " is following!"); break;
		    		}
    			}
    		}
    		else if (activities.get(moves) == Utility.Actions.HIDE) {
    			player.setBlocked(false);
    			hide(player);
    			moves=moves+2;
    		}
    	}
    }

    /**
     * Ends the game, basicly determines platyers scores and determines the winner
     * @return The winning player
     */
    public void endGame() {

    	System.out.println("End Game Called");
        Player winner = null;
        Player player = null;

        // TODO: treasures
        // TODO: Actually determine score based on individual victory points? or is it different for a day 28 time out?
        for (int i = 0; i < playerCount; i++ ) {
            player = players.get(i);
            if (winner == null)
                winner = players.get(i);

            player.removeArmourWithHigherWeight(player.getCharacter().getWeight());
            player.removeWeaponsWithHigherWeight(player.getCharacter().getWeight());

            int basicScore     = 0;
            int fameScore      = players.get(i).getFame() / 10;
            int notorietyScore = players.get(i).getNotoriety() / 20;
            int goldScore      = players.get(i).getGold() / 30;

            basicScore = fameScore + notorietyScore + goldScore;
            player.setFinalScore(basicScore);

            if (basicScore > winner.getFinalScore()) {
                winner = player;
            }
        }

        network.broadCast(winner.getCharacter().getName()+ " is the winner");
        network.stop();
    }

    /**
     * Waits untill all activities have been submitted (and tells the clients to send them)
     */
    public void collectActivities(){
    	state = GameState.CHOOSE_PLAYS;
    	network.broadCast("SEND MOVES");
    	recievedMoves = 0;
    	while(recievedMoves < playerCount){
    		try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}	//TODO HANDLE PLAYERS DROPPING OUT DURING THIS STEP
    	state = GameState.NULL;
    	System.out.println("FINISH COLLECTING activities");
    }

    public void collectCombat(){
    	state = GameState.CHOOSE_COMBAT;
    	recievedCombat = 0;
    	network.broadCast("SEND COMBAT");
    	while(recievedCombat < playerCount){
    		try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}	//TODO HANDLE PLAYERS DROPPING OUT DURING THIS STEP
    	state = GameState.NULL;
    	System.out.println("FINISH COLLECTING COMBATTARGET");
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
        network.broadCast("This is the start of Day " + currentDay + "!");

        updateClients();
        collectActivities(); //asks player's for their activities and waits until it gets them all

        orderPlayers();	//randomly orders the players

        if (playerCount == 1) {
        	network.broadCast("There is only one player alive");
        	endGame();

        }

        //Does the activities of all players
        int nextMover = 0;
        while (nextMover < playerCount) {
            for (int i = 0; i < playerCount; i++) {
                if (players.get(i).order == nextMover) {
                	doTodaysActivities(players.get(i));
                    nextMover++;
                    break;
                }
            }
        }

        updateClients();
        collectCombat(); //2 players, 1 attacker 1 deffender

        //All players choose attackers
        nextMover = 0;
        while (nextMover < playerCount) {
            for (int i = 0; i < playerCount; i++) {
                if (players.get(i).order == nextMover) {
                	if (players.get(i).getTarget() != null) {
                		encounter(players.get(i), players.get(i).getTarget());	
                	}
                	nextMover++;
                    break;
                }
            }
        }

        //Progresses to the next day or ends the game
//        boolean thing = resetDay();
//        if(thing == false){ //if it is not the 28th day....
        	startDay();
//        } else {
//        	endGame();
//        }
    }

    /**
     * Randomly orders the players //TODO AFTER ARRAYLIST CONVERSION, JUST .SHUFFLE
     */
    public void orderPlayers(){
    	
    	// Silly way to order players from 1 to playerCount+1
        for (int i = 0; i < playerCount; i++) {
        	System.out.println(i);
        	if (players.get(0).isDead()) {
        		for (int j = i; j < playerCount - 1; j++) {
        			players.set(j, players.get(j+1));
        		}
        		playerCount--;
        		i--;
        	}
        	players.get(0).order = Utility.roll(100);
        }

        int[] ordering = new int[playerCount];

        for (int i = 0; i < playerCount; i++) {
            ordering[i] = players.get(0).order;
        }

        Arrays.sort(ordering);

        for (int i = 0; i < playerCount; i++) {
            for (int j = 0; j < playerCount; j++) {
                if (ordering[i] == players.get(j).order) {
                    players.get(j).order = i;
                    network.broadCast(players.get(j).getCharacter().getName() + " is in position # " + (players.get(j).getOrder() + 1));
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

    	if (attacker == defender) {
    		network.send(attacker.getID(), "Stop attacking yourself!");
    		return;
    	}

    	//ask clients to send moves!
    	state = GameState.CHOOSE_COMBATMOVES;
    	recievedCombat = 0;
    	network.send(attacker.getID(), "SEND COMBATMOVES");
    	network.send(defender.getID(), "SEND COMBATMOVES");

    	while(recievedCombat < 2){
    		try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

    	System.out.println("got combat moves");
    	state = GameState.NULL;

    	if (attacker.isDead() == true) {
    		network.send(attacker.getID(), "You are dead.");
    		return;
    	}
    	else if (defender.isDead() == true) {
    		network.send(defender.getID(), "You are dead.");
    		return;
    	}
    	else {
    		doFight(attacker, defender);
    	}
	}

	//TODO Finding active weapon rather than assuming the active weapon is at position 0
	public void doFight(Player attacker, Player defender) {
		if (defender.getWeapons().get(0).getSpeed() < attacker.getWeapons().get(0).getSpeed()) {
			if ((defender.getWeapons().get(0).getSpeed() - attacker.getMoves().attackFatigue) <= (attacker.getCharacter().getSpeed() - defender.getMoves().maneuverFatigue)) {
				hit(attacker, defender);
			}
			else if (attacker.getMoves().getAttack() == Attacks.THRUST && defender.getMoves().getManeuver() == Maneuvers.CHARGE) {
				hit(attacker, defender);
			}
			else if (attacker.getMoves().getAttack() == Attacks.SWING && defender.getMoves().getManeuver() == Maneuvers.DODGE) {
				hit(attacker, defender);
			}
			else if (attacker.getMoves().getAttack() == Attacks.SMASH && defender.getMoves().getManeuver() == Maneuvers.DUCK) {
				hit(attacker, defender);
			}

			if (defender.isDead() == false) {
				if ((attacker.getWeapons().get(0).getSpeed() - defender.getMoves().attackFatigue) <= (defender.getCharacter().getSpeed() - attacker.getMoves().maneuverFatigue)) {
					hit(defender, attacker);
				}
				else if (defender.getMoves().getAttack() == Attacks.THRUST && attacker.getMoves().getManeuver() == Maneuvers.CHARGE) {
					hit(defender, attacker);
				}
				else if (defender.getMoves().getAttack() == Attacks.SWING && attacker.getMoves().getManeuver() == Maneuvers.DODGE) {
					hit(defender, attacker);
				}
				else if (defender.getMoves().getAttack() == Attacks.SMASH && attacker.getMoves().getManeuver() == Maneuvers.DUCK) {
					hit(defender, attacker);
				}
			}
		}
		else if (defender.getWeapons().get(0).getSpeed() > attacker.getWeapons().get(0).getSpeed()) {
			if ((attacker.getWeapons().get(0).getSpeed() - defender.getMoves().attackFatigue) <= (defender.getCharacter().getSpeed() - attacker.getMoves().maneuverFatigue)) {
				hit(defender, attacker);
			}
			else if (defender.getMoves().getAttack() == Attacks.THRUST && attacker.getMoves().getManeuver() == Maneuvers.CHARGE) {
				hit(defender, attacker);
			}
			else if (defender.getMoves().getAttack() == Attacks.SWING && attacker.getMoves().getManeuver() == Maneuvers.DODGE) {
				hit(defender, attacker);
			}
			else if (defender.getMoves().getAttack() == Attacks.SMASH && attacker.getMoves().getManeuver() == Maneuvers.DUCK) {
				hit(defender, attacker);
			}

			if (defender.isDead() == false) {
				if ((defender.getWeapons().get(0).getSpeed() - attacker.getMoves().attackFatigue) <= (attacker.getCharacter().getSpeed() - defender.getMoves().maneuverFatigue)) {
					hit(attacker, defender);
				}
				else if (attacker.getMoves().getAttack() == Attacks.THRUST && defender.getMoves().getManeuver() == Maneuvers.CHARGE) {
					hit(attacker, defender);
				}
				else if (attacker.getMoves().getAttack() == Attacks.SWING && defender.getMoves().getManeuver() == Maneuvers.DODGE) {
					hit(attacker, defender);
				}
				else if (attacker.getMoves().getAttack() == Attacks.SMASH && defender.getMoves().getManeuver() == Maneuvers.DUCK) {
					hit(attacker, defender);
				}
			}
		}

		else {
			if (defender.getWeapons().get(0).getLength() < attacker.getWeapons().get(0).getLength()) {
				if ((defender.getWeapons().get(0).getSpeed() - attacker.getMoves().attackFatigue) <= (attacker.getCharacter().getSpeed() - defender.getMoves().maneuverFatigue)) {
					hit(attacker, defender);
				}
				else if (attacker.getMoves().getAttack() == Attacks.THRUST && defender.getMoves().getManeuver() == Maneuvers.CHARGE) {
					hit(attacker, defender);
				}
				else if (attacker.getMoves().getAttack() == Attacks.SWING && defender.getMoves().getManeuver() == Maneuvers.DODGE) {
					hit(attacker, defender);
				}
				else if (attacker.getMoves().getAttack() == Attacks.SMASH && defender.getMoves().getManeuver() == Maneuvers.DUCK) {
					hit(attacker, defender);
				}

				if (defender.isDead() == false) {
					if ((attacker.getWeapons().get(0).getSpeed() - defender.getMoves().attackFatigue) <= (defender.getCharacter().getSpeed() - attacker.getMoves().maneuverFatigue)) {
						hit(defender, attacker);
					}
					else if (defender.getMoves().getAttack() == Attacks.THRUST && attacker.getMoves().getManeuver() == Maneuvers.CHARGE) {
						hit(defender, attacker);
					}
					else if (defender.getMoves().getAttack() == Attacks.SWING && attacker.getMoves().getManeuver() == Maneuvers.DODGE) {
						hit(defender, attacker);
					}
					else if (defender.getMoves().getAttack() == Attacks.SMASH && attacker.getMoves().getManeuver() == Maneuvers.DUCK) {
						hit(defender, attacker);
					}
				}
			}
			else if (defender.getWeapons().get(0).getLength() > attacker.getWeapons().get(0).getLength()) {
				if ((attacker.getWeapons().get(0).getSpeed() - defender.getMoves().attackFatigue) <= (defender.getCharacter().getSpeed() - attacker.getMoves().maneuverFatigue)) {
					hit(defender, attacker);
				}
				else if (defender.getMoves().getAttack() == Attacks.THRUST && attacker.getMoves().getManeuver() == Maneuvers.CHARGE) {
					hit(defender, attacker);
				}
				else if (defender.getMoves().getAttack() == Attacks.SWING && attacker.getMoves().getManeuver() == Maneuvers.DODGE) {
					hit(defender, attacker);
				}
				else if (defender.getMoves().getAttack() == Attacks.SMASH && attacker.getMoves().getManeuver() == Maneuvers.DUCK) {
					hit(defender, attacker);
				}

				if (defender.isDead() == false) {
					if ((defender.getWeapons().get(0).getSpeed() - attacker.getMoves().attackFatigue) <= (attacker.getCharacter().getSpeed() - defender.getMoves().maneuverFatigue)) {
						hit(attacker, defender);
					}
					else if (attacker.getMoves().getAttack() == Attacks.THRUST && defender.getMoves().getManeuver() == Maneuvers.CHARGE) {
						hit(attacker, defender);
					}
					else if (attacker.getMoves().getAttack() == Attacks.SWING && defender.getMoves().getManeuver() == Maneuvers.DODGE) {
						hit(attacker, defender);
					}
					else if (attacker.getMoves().getAttack() == Attacks.SMASH && defender.getMoves().getManeuver() == Maneuvers.DUCK) {
						hit(attacker, defender);
					}
				}
			}
			else {
				if ((defender.getWeapons().get(0).getSpeed() - attacker.getMoves().attackFatigue) <= (attacker.getCharacter().getSpeed() - defender.getMoves().maneuverFatigue)) {
					hit(attacker, defender);
				}
				else if (attacker.getMoves().getAttack() == Attacks.THRUST && defender.getMoves().getManeuver() == Maneuvers.CHARGE) {
					hit(attacker, defender);
				}
				else if (attacker.getMoves().getAttack() == Attacks.SWING && defender.getMoves().getManeuver() == Maneuvers.DODGE) {
					hit(attacker, defender);
				}
				else if (attacker.getMoves().getAttack() == Attacks.SMASH && defender.getMoves().getManeuver() == Maneuvers.DUCK) {
					hit(attacker, defender);
				}

				if (defender.isDead() == false) {
					if ((attacker.getWeapons().get(0).getSpeed() - defender.getMoves().attackFatigue) <= (defender.getCharacter().getSpeed() - attacker.getMoves().maneuverFatigue)) {
						hit(defender, attacker);
					}
					else if (defender.getMoves().getAttack() == Attacks.THRUST && attacker.getMoves().getManeuver() == Maneuvers.CHARGE) {
						hit(defender, attacker);
					}
					else if (defender.getMoves().getAttack() == Attacks.SWING && attacker.getMoves().getManeuver() == Maneuvers.DODGE) {
						hit(defender, attacker);
					}
					else if (defender.getMoves().getAttack() == Attacks.SMASH && attacker.getMoves().getManeuver() == Maneuvers.DUCK) {
						hit(defender, attacker);
					}
				}
			}
		}
	}

	public void hit(Player attacker, Player defender) {
		ItemWeight level = attacker.getWeapons().get(0).getWeight();

		network.broadCast(attacker.getCharacter().getName() + " has hit " + defender.getCharacter().getName());

		if (attacker.getMoves().getAttackFatigue() == 1) {
			switch(level){
            	case NEGLIGIBLE: level = ItemWeight.LIGHT; break;
            	case LIGHT: level = ItemWeight.MEDIUM;break;
            	case MEDIUM: level = ItemWeight.HEAVY;break;
            	case HEAVY: level = ItemWeight.TREMENDOUS;break;
            	default: break;
			}
		}
		else if (attacker.getMoves().getAttackFatigue() == 2) {
			switch(level){
        		case NEGLIGIBLE: level = ItemWeight.MEDIUM;break;
        		case LIGHT: level = ItemWeight.HEAVY;break;
        		case MEDIUM: level = ItemWeight.TREMENDOUS;break;
        		case HEAVY: level = ItemWeight.TREMENDOUS;break;
        		default: break;
			}
		}
		//TODO armor destruction
		if (attacker.getMoves().getAttack() == Attacks.THRUST && defender.getMoves().getDefense() == Defenses.AHEAD) {
			switch(level){
        		case NEGLIGIBLE: level = ItemWeight.NEGLIGIBLE;break;
        		case LIGHT: level = ItemWeight.NEGLIGIBLE;break;
        		case MEDIUM: level = ItemWeight.LIGHT;break;
        		case HEAVY: level = ItemWeight.MEDIUM;break;
        		case TREMENDOUS: level = ItemWeight.HEAVY;break;
        		default: break;
			}
		}
		else if (attacker.getMoves().getAttack() == Attacks.SWING && defender.getMoves().getDefense() == Defenses.SIDE) {
			switch(level){
    			case NEGLIGIBLE: level = ItemWeight.NEGLIGIBLE;break;
    			case LIGHT: level = ItemWeight.NEGLIGIBLE;break;
    			case MEDIUM: level = ItemWeight.LIGHT;break;
    			case HEAVY: level = ItemWeight.MEDIUM;break;
    			case TREMENDOUS: level = ItemWeight.HEAVY;break;
    			default: break;
			}
		}
		else if (attacker.getMoves().getAttack() == Attacks.SMASH && defender.getMoves().getDefense() == Defenses.ABOVE) {
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
			deadPlayer(attacker, defender);
		}
		else if (level == ItemWeight.HEAVY) {
			deadPlayer(attacker, defender);
		}
		else if (level == defender.getCharacter().getWeight()) {
			deadPlayer(attacker, defender);
		}
		else if (level == ItemWeight.MEDIUM && defender.getCharacter().getWeight() == ItemWeight.LIGHT) {
			deadPlayer(attacker, defender);
		}
		else if (level == ItemWeight.MEDIUM && defender.getCharacter().getWeight() == ItemWeight.HEAVY) {
			defender.setHealth(defender.getHealth() + 1);
			network.broadCast(defender.getCharacter().getName() + " has been wounded!");
			if (defender.getHealth() == 3) {
				deadPlayer(attacker, defender);
			}
		}
		else if (level == ItemWeight.LIGHT && defender.getCharacter().getWeight() == ItemWeight.HEAVY) {
			defender.setHealth(defender.getHealth() + 1);
			network.broadCast(defender.getCharacter().getName() + " has been wounded!");
			if (defender.getHealth() == 3) {
				deadPlayer(attacker, defender);
			}
		}
		else if (level == ItemWeight.LIGHT && defender.getCharacter().getWeight() == ItemWeight.MEDIUM) {
			defender.setHealth(defender.getHealth() + 1);
			network.broadCast(defender.getCharacter().getName() + " has been wounded!");
			if (defender.getHealth() == 2) {
				deadPlayer(attacker, defender);
			}
		}
	}
	public void deadPlayer(Player attacker, Player defender) {
		attacker.addFame(10); // Arbitrary value
		attacker.addGold(defender.getGold());
		defender.removeGold(defender.getGold());
		attacker.addNotoriety(defender.getNotoriety());
		defender.removeNotoriety(defender.getNotoriety());
		defender.kill();
		network.send(defender.getID(), "You are dead.");
		network.broadCast(defender.getCharacter().getName() + " has been killed!");
	}

	public void distributeCharacters(){

		//sends each player object to the right client
		for(int i=0;i<playerCount;i++){
			Player t = players.get(i).clone();
			network.send(players.get(i).getID(), t);
			players.remove(i);
			players.add(t);
		}

	}

	/**
	 * calld via a "START GAME" message in order to setup the board and start the game.
	 */
	public void startGame(){

		//ask clients to send moves!
		state = GameState.CHOOSE_CHARACTER;
    	this.addedPlayers = 0;
    	network.broadCast("CHARACTER SELECT");
    	System.out.println("start selection loop");

    	while(this.addedPlayers < Config.MAX_CLIENTS){
    		try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

    	//cant get past here until the list of players is done
    	System.out.println("end selection loop");
    	state = GameState.NULL;

		//instanciate the model
		this.board = new Board(players);
		System.out.println("Server Models Created.");

		network.broadCast(board);  				//sends the board to all clients
		// System.out.println(board);
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
