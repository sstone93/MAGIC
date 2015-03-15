package controller;

import networking.Message;
import networking.NetworkServer;
import model.Amazon;
import model.Berserker;
import model.BlackKnight;
import model.Board;
import model.Captain;
import model.CombatMoves;
import model.Dwarf;
import model.Elf;
import model.Monster;
import model.Path;
import model.Player;
import model.SmallTreasure;
import model.Swordsman;
import model.Treasure;
import model.WhiteKnight;

import java.util.ArrayList;
import java.util.Collections;

import utils.Config;
import utils.Utility;
import utils.Utility.*;
import view.ServerView;

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
	 * This is the method that handles incoming messages from the networking components
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
					System.out.println(ID);



					findPlayer(ID).setActivities(m.getData());
				}else{
					network.send(ID, "NOT ACCEPTING ACTIVITIES ATM");
				}
			}
			if( m.getType() == MessageType.COMBAT_TARGET){
				if(state == GameState.CHOOSE_COMBATTARGET){
					recievedCombat += 1;
					System.out.println("This is the target's name!");
					System.out.println(charToPlayer((CharacterName) m.getData().get(0)));
					Player temp = charToPlayer((CharacterName) m.getData().get(0));
					System.out.println(temp.getCharacter().getName());
					//turns the received character name into a player
					findPlayer(ID).setTarget(charToPlayer((CharacterName) m.getData().get(0)));
				}else{
					network.send(ID, "NOT ACCEPTING COMBAT TARGETS ATM");
				}
			}
			if( m.getType() == MessageType.COMBAT_MOVES){
				if(state == GameState.CHOOSE_COMBATMOVES){
					recievedCombat += 1;
					findPlayer(ID).setMoves((CombatMoves) m.getData().get(0));
					//TODO remove console printout
					System.out.println(findPlayer(ID).getCharacter().getName());
					System.out.println(findPlayer(ID).getMoves().getAttack());
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
	 * @return the player you are looking for
	 */
	public Player findPlayer(int ID){
		for(int i=0;i<playerCount;i++){
			if(players.get(i).getID() == ID){
				return players.get(i);
			}
		}
		System.out.println("failed to find player");
		return null;
	}

	/**
	 * Sets a player's state as hidden
	 * @param player being hidden
	 */
	public void hide(Player player) { // assume it always works

		int roll;
		boolean oneDie = checkRollOneDie(player, "hide");
		if (oneDie) {
			network.send(player.getID(), "Congrats, you had the shoes of stealth which allows you to roll only 1 die!");
			roll = rollForTables(player, 1);
		}
		else {
			roll = rollForTables(player, 2);
		}

		if (roll != 6) {
			player.setHidden(true);
			network.broadCast( player.getCharacter().getName() + " is hidden!");
		}
		else {
			network.broadCast("Hide had no effect on " + player.getCharacter().getName());
		}
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
    public void search(Player player, SearchTables table) {
    	// TODO: check if they have a treasure that decreases the amount of die needed


		// todo: check the deft gloves and

		if (table == Utility.SearchTables.LOCATE) {
			int roll = rollForTables(player, 2);
			locate(player, roll);
		}
		else if (table == Utility.SearchTables.LOOT) {


			// TODO: should they be rolling the equivalent of 2 dice?
			// TODO: check if they can actually loot (ie. check for if they've discovered treasure sites)?

			boolean reduceDie = checkRollOneDie(player, "loot");

			int roll;
			if (reduceDie) {
				network.send(player.getID(), "You have the deft gloves, which allows you to only roll one die! Congrats");
				roll = rollForTables(player, 1);
			}
			else {
				roll = rollForTables(player, 2);
			}

			ArrayList<Treasure> treasures = player.getLocation().getTreasures();
			if (roll <= treasures.size()) {
				player.addTreasure(treasures.get(roll));
				network.send(player.getID(), "you've found " + treasures.get(roll).getType() + " !!");
				player.getLocation().removeTreasure(treasures.get(roll));
			}
			else  {
				network.send(player.getID(), "You didn't find any treasures this time");
			}
		}

    }

    // rolls dice for the player
    // returns the lowest of the two rolls if the player has to roll 2 die
    // otherwise returns the first role done
    public int rollForTables(Player player, int numberOfDie) {
    	int roll = Utility.roll(6);
    	if (numberOfDie == 2) {
	    	int roll2 = Utility.roll(6);
	    	network.broadCast(player.getCharacter().getName() + " rolled " + roll + " and " + roll2 );
	    	roll = Math.min(roll, roll2);
    	}
		network.broadCast(player.getCharacter().getName() + " is using " + roll );

		return roll;
    }

    // checks to see if the player has a treasure that allows them to roll only one die on the table
    // returns true if they can
    // returns false if they don't have a roll reducing treasure
    public boolean checkRollOneDie(Player player, String table) {
    	boolean onlyOne = false;

    	ArrayList<Treasure> treasures = player.getTreasures();

    	for(Treasure t: treasures) {
			SmallTreasure temp = (SmallTreasure) t;
			if ( temp.getName() == SmallTreasureName.SHOES_OF_STEALTH && table == "hide")
				onlyOne = true;
			else if (temp.getName() == SmallTreasureName.DEFT_GLOVES && table == "loot")
				onlyOne = true;
    	}

    	return onlyOne;
    }


    public void locate(Player player, int roll) {
    	if (roll == 1) {
    		network.send(player.getID(), "You have a choice to make my friend");
    		// TODO: They can choose to discover passageways or chits
    	}
    	else if (roll == 2 || roll == 3) {
    		// note: I'm taking out clues from the 2 roll
    		boolean hidden = false;
    		ArrayList<Path> connections = player.getLocation().getConnections() ;
    		for (int i = 0; i < connections.size(); i++) {
    			if (connections.get(i).getType() == Utility.PathType.HIDDEN_PATH) {
    				player.addDiscovery(connections.get(i));
    				network.send(player.getID(), "You've discovered hidden passageways!");
    				hidden = true;
    			}
    		}

    		if (!hidden) {
    			network.send(player.getID(), "There was no hidden passageways to discover in this clearing!");
    		}
    	}
    	else if (roll == 4) { // discover chits
    		// TODO: discover every site chit in the clearing you are searching
    		network.send(player.getID(), "You've discovered chits!");
    	}
    	else if (roll == 5 || roll == 6) {
    		// do nothing
    		network.send(player.getID(), "You've discovered nothing") ;
    	}

    }

    /**
     * Sets a player's weapon to the opposite state of what it is currently
     * @param p The player
     */
    public void alert(Player p) {
    	p.getActiveWeapon().setActive(!p.getActiveWeapon().isActive());
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

        for (int i = 0; i < addedPlayers; i++) {
        	players.get(i).setGoneInCave(false);
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

    	System.out.println("Start "+player.getCharacter().getName()+" activities: " + player.getActivities().size());

    	while (moves < player.getActivities().size()) {

    		// For testing
    		System.out.println("	moves: " + moves + " activities: " + player.getActivities().size());

    		ArrayList<Object> activities = player.getActivities();

    		if (!player.isBlocked()) {	//assuming the player is not being blocked by another
    			// format: [MOVE, clearing]

    			if (activities.get(moves) != null) {

    				/*ArrayList<Player> canBlock = blockable(player);					// check if they can block another player

    	    		if (currentDay != 1) {
    		    		for (int j = 0; j < canBlock.size(); j++) {
    		    			if (canBlock.get(j) != null) {
    		    				canBlock.get(j).setBlocked(true);
    		    				System.out.println("blocking player!"); // For testing
    		    				network.send(canBlock.get(j).getID(), "You've been blocked! :( " );
    		    			}
    		    		}
    	    		}*/

		    		switch((Actions) activities.get(moves)) {

		    		case MOVE:
		    			boolean move = board.move(moves, player, (String) activities.get(moves+1)); moves = moves + 2;
		    			network.broadCast(player.getCharacter().getName() + " is moving? : " + move);
		    			break;
		    		case HIDE: hide(player); moves = moves + 2; break;
		    		case ALERT: alert(player); moves = moves + 2; network.broadCast(player.getCharacter().getName() + " is alerting their weapon!"); break;
		    		case REST: rest(player); moves = moves + 2; network.broadCast(player.getCharacter().getName() + " is resting!"); break;
		    		case SEARCH:
		    			search(player, (SearchTables) activities.get(moves+1));
		    			moves = moves + 2;
		    			break;
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
    		else {
    			moves=moves+2;
    		}
    	}
    }

    /**
     * Ends the game, basically determines players scores and determines the winner
     * @return The winning player
     */
    public void endGame() {

    	System.out.println("End Game Called");
        Player winner = null;
        Player player = null;

        // TODO: treasures
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
     * Waits until all activities have been submitted (and tells the clients to send them)
     */
    public void collectActivities(){
    	state = GameState.CHOOSE_PLAYS;
    	network.broadCast("SEND MOVES");
    	recievedMoves = 0;
    	while(recievedMoves < playerCount){
    		try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
    	}	//TODO HANDLE PLAYERS DROPPING OUT DURING THIS STEP
    	state = GameState.NULL;
    	System.out.println("FINISH COLLECTING activities");
    }

    public void collectCombat(){
    	state = GameState.CHOOSE_COMBATTARGET;
    	recievedCombat = 0;
    	network.broadCast("SEND COMBAT");
    	while(recievedCombat < playerCount){
    		try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
    	}	//TODO HANDLE PLAYERS DROPPING OUT DURING THIS STEP
    	state = GameState.NULL;
    	System.out.println("FINISH COLLECTING COMBATTARGET");
    }

    /**
     * Sends up to date board to clients, along with their proper character
     */
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
                }
            }
        }

        updateClients();
        collectCombat(); //2 players, 1 attacker 1 defender

        //All players choose attackers
        nextMover = 0;
        while (nextMover < playerCount) {
            for (int i = 0; i < playerCount; i++) {
                if (players.get(i).order == nextMover) {
                	if (players.get(i).getTarget() != null) {
                		encounter(players.get(i), players.get(i).getTarget());
                		//System.out.println("")
                	}
                	nextMover++;
                }
            }
        }

        //Progresses to the next day or ends the game
        boolean thing = resetDay();
        if(thing == true){ //if it is not the 28th day....
        	startDay();
        } else {
        	endGame();
        }
    }

    /**
     * Randomly orders the players
     */
    public void orderPlayers(){
    	Collections.shuffle(players);
    	for (int j = 0; j < playerCount; j++) {
    		players.get(j).order = j;
    		network.broadCast(players.get(j).getCharacter().getName() + " is in position # " + (players.get(j).getOrder() + 1));
    	}
    }

    public void encounter(Player player, Monster monster) {
    	if (player.isDead() == true) {
    		network.send(player.getID(), "You are dead");
    		return;
    	}
    	else if (monster.isDead() == true) {
    		network.send(player.getID(), "That monster is dead");
    		return;
    	}

    	state = GameState.CHOOSE_COMBATMOVES;
    	recievedCombat = 0;
    	network.send(player.getID(), "SEND COMBATMOVES");

    	while(recievedCombat < 1){
    		try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
    	}

    	monster.setMoves();

    	System.out.println("got combat moves");
    	state = GameState.NULL;

    	if (player.getMoves() == null) {
    		network.send(player.getID(), "It messed up...");
    		return;
    	}
    	else if (monster.getMoves() == null) {
    		network.send(player.getID(), "It messed up...");
    		return;
    	}
    	else {
    		doFight(player, monster);
    	}
    }

    /**
     *
     * @param attacker
     * @param defender
     */
    public void encounter(Player attacker, Player defender) {
    	//TODO Weapons active
		// Check if weapon is active
		/*if (player.weapons[0].isActive() == false) {
			player.weapons[0].setActive(true);
			return;
		}*/

    	if (attacker == defender) {
    		network.send(attacker.getID(), "Stop attacking yourself!");
    		return;
    	}
    	else if (attacker.getID() == defender.getID()) {
    		network.send(attacker.getID(), "Stop attacking yourself!");
    		return;
    	}

    	if (attacker.isDead() == true) {
    		network.send(attacker.getID(), "You are dead.");
    		return;
    	}
    	else if (defender.isDead() == true) {
    		network.send(defender.getID(), "You are dead.");
    		return;
    	}

    	//System.out.println(attacker.getTarget().getCharacter().getName());
    	//System.out.println(defender.getTarget().getCharacter().getName());

    	//ask clients to send moves!
    	state = GameState.CHOOSE_COMBATMOVES;
    	recievedCombat = 0;
    	network.send(attacker.getID(), "SEND COMBATMOVES");
    	network.send(defender.getID(), "SEND COMBATMOVES");

    	while(recievedCombat < 2){
    		try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
    	}

    	System.out.println("got combat moves");
    	state = GameState.NULL;


    	if (attacker.getMoves() == null) {
    		network.send(attacker.getID(), "It messed up...");
    		return;
    	}
    	else if (defender.getMoves() == null) {
    		network.send(defender.getID(), "It messed up...");
    		return;
    	}
    	else {
    		doFight(attacker, defender);
    	}
	}

    public void doFight(Player player, Monster monster) {
    	if (player.getActiveWeapon().getSpeed() < monster.getAttackSpeed()) {
    		checkHit(monster, player);
    	}
    	else if (player.getActiveWeapon().getSpeed() > monster.getAttackSpeed()) {
    		checkHit(player, monster);
    	}

    	else {
    		if (player.getActiveWeapon().getLength() < monster.getAttackLength()) {
    			checkHit(monster, player);
    		}
    		else if (player.getActiveWeapon().getLength() > monster.getAttackLength()) {
    			checkHit(player, monster);
    		}
    		else {
    			checkHit(monster, player);
    		}
    	}
    }

	public void doFight(Player attacker, Player defender) {
		if (defender.getActiveWeapon().getSpeed() < attacker.getActiveWeapon().getSpeed()) {
			checkHit(attacker, defender);
		}
		else if (defender.getActiveWeapon().getSpeed() > attacker.getActiveWeapon().getSpeed()) {
			checkHit(defender, attacker);
		}

		else {
			if (defender.getActiveWeapon().getLength() < attacker.getActiveWeapon().getLength()) {
				checkHit(attacker, defender);
			}
			else if (defender.getActiveWeapon().getLength() > attacker.getActiveWeapon().getLength()) {
				checkHit(defender, attacker);
			}
			else {
				checkHit(attacker, defender);
			}
		}
	}

	public void checkHit(Player player, Monster monster) {
		if (player.isDead() == false && monster.isDead() == false) {
			if ((monster.getAttackSpeed() - player.getMoves().getAttackFatigue()) <= (player.getCharacter().getSpeed() - monster.getMoves().getManeuverFatigue())) {
				hit(player, monster);
			}
			else if (player.getMoves().getAttack() == Attacks.THRUST && monster.getMoves().getManeuver() == Maneuvers.CHARGE) {
				hit(player, monster);
			}
			else if (player.getMoves().getAttack() == Attacks.SWING && monster.getMoves().getManeuver() == Maneuvers.DODGE) {
				hit(player, monster);
			}
			else if (player.getMoves().getAttack() == Attacks.SMASH && monster.getMoves().getManeuver() == Maneuvers.DUCK) {
				hit(player, monster);
			}
		}
		if (player.isDead() == false && monster.isDead() == false) {
			if ((player.getCharacter().getSpeed() - monster.getMoves().getAttackFatigue()) <= (monster.getAttackSpeed() - player.getMoves().getManeuverFatigue())) {
				hit(monster, player);
			}
			else if (monster.getMoves().getAttack() == Attacks.THRUST && player.getMoves().getManeuver() == Maneuvers.CHARGE) {
				hit(monster, player);
			}
			else if (monster.getMoves().getAttack() == Attacks.SWING && player.getMoves().getManeuver() == Maneuvers.DODGE) {
				hit(monster, player);
			}
			else if (monster.getMoves().getAttack() == Attacks.SMASH && player.getMoves().getManeuver() == Maneuvers.DUCK) {
				hit(monster, player);
			}
		}
	}

	public void checkHit(Monster monster, Player player) {
		if (player.isDead() == false && monster.isDead() == false) {
			if ((player.getCharacter().getSpeed() - monster.getMoves().getAttackFatigue()) <= (monster.getAttackSpeed() - player.getMoves().getManeuverFatigue())) {
				hit(monster, player);
			}
			else if (monster.getMoves().getAttack() == Attacks.THRUST && player.getMoves().getManeuver() == Maneuvers.CHARGE) {
				hit(monster, player);
			}
			else if (monster.getMoves().getAttack() == Attacks.SWING && player.getMoves().getManeuver() == Maneuvers.DODGE) {
				hit(monster, player);
			}
			else if (monster.getMoves().getAttack() == Attacks.SMASH && player.getMoves().getManeuver() == Maneuvers.DUCK) {
				hit(monster, player);
			}
		}
		if (player.isDead() == false && monster.isDead() == false) {
			if ((monster.getAttackSpeed() - player.getMoves().getAttackFatigue()) <= (player.getCharacter().getSpeed() - monster.getMoves().getManeuverFatigue())) {
				hit(player, monster);
			}
			else if (player.getMoves().getAttack() == Attacks.THRUST && monster.getMoves().getManeuver() == Maneuvers.CHARGE) {
				hit(player, monster);
			}
			else if (player.getMoves().getAttack() == Attacks.SWING && monster.getMoves().getManeuver() == Maneuvers.DODGE) {
				hit(player, monster);
			}
			else if (player.getMoves().getAttack() == Attacks.SMASH && monster.getMoves().getManeuver() == Maneuvers.DUCK) {
				hit(player, monster);
			}
		}
	}

	public void checkHit(Player attacker, Player defender) {
		if (attacker.isDead() == false && defender.isDead() == false) {
	    	if ((defender.getActiveWeapon().getSpeed() - attacker.getMoves().getAttackFatigue()) <= (attacker.getCharacter().getSpeed() - defender.getMoves().getManeuverFatigue())) {
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
		if (defender.isDead() == false && attacker.isDead() == false) {
			if ((attacker.getActiveWeapon().getSpeed() - defender.getMoves().getAttackFatigue()) <= (defender.getCharacter().getSpeed() - attacker.getMoves().getManeuverFatigue())) {
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

	public void hit(Player player, Monster monster) {
		ItemWeight level = player.getActiveWeapon().getWeight();

		network.broadCast(player.getCharacter().getName() + " has hit " + monster.getName());

		if (player.getMoves().getAttackFatigue() == 1) {
			switch(level){
            	case NEGLIGIBLE: level = ItemWeight.LIGHT; break;
            	case LIGHT: level = ItemWeight.MEDIUM;break;
            	case MEDIUM: level = ItemWeight.HEAVY;break;
            	case HEAVY: level = ItemWeight.TREMENDOUS;break;
            	default: break;
			}
		}
		else if (player.getMoves().getAttackFatigue() == 2) {
			switch(level){
        		case NEGLIGIBLE: level = ItemWeight.MEDIUM;break;
        		case LIGHT: level = ItemWeight.HEAVY;break;
        		case MEDIUM: level = ItemWeight.TREMENDOUS;break;
        		case HEAVY: level = ItemWeight.TREMENDOUS;break;
        		default: break;
			}
		}
		//TODO armor destruction
		if (player.getMoves().getAttack() == Attacks.THRUST && monster.getMoves().getDefense() == Defenses.AHEAD) {
			switch(level){
        		case NEGLIGIBLE: level = ItemWeight.NEGLIGIBLE;break;
        		case LIGHT: level = ItemWeight.NEGLIGIBLE;break;
        		case MEDIUM: level = ItemWeight.LIGHT;break;
        		case HEAVY: level = ItemWeight.MEDIUM;break;
        		case TREMENDOUS: level = ItemWeight.HEAVY;break;
        		default: break;
			}
		}
		else if (player.getMoves().getAttack() == Attacks.SWING && monster.getMoves().getDefense() == Defenses.SIDE) {
			switch(level){
    			case NEGLIGIBLE: level = ItemWeight.NEGLIGIBLE;break;
    			case LIGHT: level = ItemWeight.NEGLIGIBLE;break;
    			case MEDIUM: level = ItemWeight.LIGHT;break;
    			case HEAVY: level = ItemWeight.MEDIUM;break;
    			case TREMENDOUS: level = ItemWeight.HEAVY;break;
    			default: break;
			}
		}
		else if (player.getMoves().getAttack() == Attacks.SMASH && monster.getMoves().getDefense() == Defenses.ABOVE) {
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
			deadMonster(player, monster);
		}
		else if (level == monster.getWeight()) {
			deadMonster(player, monster);
		}
		else if (level == ItemWeight.HEAVY && monster.getWeight() == ItemWeight.LIGHT) {
			deadMonster(player, monster);
		}
		else if (level == ItemWeight.HEAVY && monster.getWeight() == ItemWeight.MEDIUM) {
			deadMonster(player, monster);
		}
		else if (level == ItemWeight.HEAVY && monster.getWeight() == ItemWeight.TREMENDOUS) {
			monster.wound();
			network.broadCast(monster.getName() + "has been wounded!");
			if (monster.getHealth() == 4) {
				deadMonster(player, monster);
			}
		}
		else if (level == ItemWeight.MEDIUM && monster.getWeight() == ItemWeight.LIGHT) {
			deadMonster(player, monster);
		}
		else if (level == ItemWeight.MEDIUM && monster.getWeight() == ItemWeight.HEAVY) {
			monster.wound();
			network.broadCast(monster.getName() + "has been wounded!");
			if (monster.getHealth() == 3) {
				deadMonster(player, monster);
			}
		}
		else if (level == ItemWeight.MEDIUM && monster.getWeight() == ItemWeight.TREMENDOUS) {
			monster.wound();
			network.broadCast(monster.getName() + "has been wounded!");
			if (monster.getHealth() == 4) {
				deadMonster(player, monster);
			}
		}
		else if (level == ItemWeight.LIGHT && monster.getWeight() == ItemWeight.TREMENDOUS) {
			monster.wound();
			network.broadCast(monster.getName() + "has been wounded!");
			if (monster.getHealth() == 4) {
				deadMonster(player, monster);
			}
		}
		else if (level == ItemWeight.LIGHT && monster.getWeight() == ItemWeight.HEAVY) {
			monster.wound();
			network.broadCast(monster.getName() + "has been wounded!");
			if (monster.getHealth() == 3) {
				deadMonster(player, monster);
			}
		}
		else if (level == ItemWeight.LIGHT && monster.getWeight() == ItemWeight.MEDIUM) {
			monster.wound();
			network.broadCast(monster.getName() + "has been wounded!");
			if (monster.getHealth() == 2) {
				deadMonster(player, monster);
			}
		}
	}

	public void hit(Monster monster, Player player) {
		ItemWeight level = monster.getWeight();

		network.broadCast(monster.getName() + " has hit " + player.getCharacter().getName());

		if (monster.getMoves().getAttackFatigue() == 1) {
			switch(level){
            	case NEGLIGIBLE: level = ItemWeight.LIGHT; break;
            	case LIGHT: level = ItemWeight.MEDIUM;break;
            	case MEDIUM: level = ItemWeight.HEAVY;break;
            	case HEAVY: level = ItemWeight.TREMENDOUS;break;
            	default: break;
			}
		}
		else if (monster.getMoves().getAttackFatigue() == 2) {
			switch(level){
        		case NEGLIGIBLE: level = ItemWeight.MEDIUM;break;
        		case LIGHT: level = ItemWeight.HEAVY;break;
        		case MEDIUM: level = ItemWeight.TREMENDOUS;break;
        		case HEAVY: level = ItemWeight.TREMENDOUS;break;
        		default: break;
			}
		}
		//TODO armor destruction
		if (monster.getMoves().getAttack() == Attacks.THRUST && player.getMoves().getDefense() == Defenses.AHEAD) {
			switch(level){
        		case NEGLIGIBLE: level = ItemWeight.NEGLIGIBLE;break;
        		case LIGHT: level = ItemWeight.NEGLIGIBLE;break;
        		case MEDIUM: level = ItemWeight.LIGHT;break;
        		case HEAVY: level = ItemWeight.MEDIUM;break;
        		case TREMENDOUS: level = ItemWeight.HEAVY;break;
        		default: break;
			}
		}
		else if (monster.getMoves().getAttack() == Attacks.SWING && player.getMoves().getDefense() == Defenses.SIDE) {
			switch(level){
    			case NEGLIGIBLE: level = ItemWeight.NEGLIGIBLE;break;
    			case LIGHT: level = ItemWeight.NEGLIGIBLE;break;
    			case MEDIUM: level = ItemWeight.LIGHT;break;
    			case HEAVY: level = ItemWeight.MEDIUM;break;
    			case TREMENDOUS: level = ItemWeight.HEAVY;break;
    			default: break;
			}
		}
		else if (monster.getMoves().getAttack() == Attacks.SMASH && player.getMoves().getDefense() == Defenses.ABOVE) {
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
			deadPlayer(player, monster);
		}
		else if (level == ItemWeight.HEAVY) {
			deadPlayer(player, monster);
		}
		else if (level == player.getCharacter().getWeight()) {
			deadPlayer(player, monster);
		}
		else if (level == ItemWeight.MEDIUM && player.getCharacter().getWeight() == ItemWeight.LIGHT) {
			deadPlayer(player, monster);
		}
		else if (level == ItemWeight.MEDIUM && player.getCharacter().getWeight() == ItemWeight.HEAVY) {
			player.setHealth(player.getHealth() + 1);
			network.broadCast(player.getCharacter().getName() + " has been wounded!");
			if (player.getHealth() == 3) {
				deadPlayer(player, monster);
			}
		}
		else if (level == ItemWeight.LIGHT && player.getCharacter().getWeight() == ItemWeight.HEAVY) {
			player.setHealth(player.getHealth() + 1);
			network.broadCast(player.getCharacter().getName() + " has been wounded!");
			if (player.getHealth() == 3) {
				deadPlayer(player, monster);
			}
		}
		else if (level == ItemWeight.LIGHT && player.getCharacter().getWeight() == ItemWeight.MEDIUM) {
			player.setHealth(player.getHealth() + 1);
			network.broadCast(player.getCharacter().getName() + " has been wounded!");
			if (player.getHealth() == 2) {
				deadPlayer(player, monster);
			}
		}
	}

	public void hit(Player attacker, Player defender) {
		ItemWeight level = attacker.getActiveWeapon().getWeight();

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

	public void deadPlayer(Player player, Monster monster) {
		//TODO pile
		player.kill();
		network.send(player.getID(), "You are dead.");
		network.broadCast(player.getCharacter().getName() + "has been killed!");
	}

	public void deadMonster(Player player, Monster monster) {
		//TODO pile
		player.addFame(monster.getFame());
		player.addNotoriety(monster.getNotoriety());
		monster.kill();
		network.broadCast(monster.getName() + "has been killed!");
	}

	public void deadPlayer(Player attacker, Player defender) {
		//TODO pile
		attacker.addFame(10); // Arbitrary value
		attacker.addGold(defender.getGold());
		defender.removeGold(defender.getGold());
		attacker.addNotoriety(defender.getNotoriety());
		defender.removeNotoriety(defender.getNotoriety());
		defender.kill();
		network.send(defender.getID(), "You are dead.");
		network.broadCast(defender.getCharacter().getName() + " has been killed!");
	}

	/**
	 * Sends each client their corresponding player object
	 */
	public void distributeCharacters(){
		for(int i=0;i<playerCount;i++){
			network.send(players.get(i).getID(), players.get(i));
		}
	}

	/**
	 * called via a "START GAME" message in order to setup the board and start the game.
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
				// Auto-generated catch block
				e.printStackTrace();
			}
    	}

    	//can't get past here until the list of players is done
    	System.out.println("end selection loop");
    	state = GameState.NULL;

		//instanciate the model
		this.board = new Board(players);
		System.out.println("Server Models Created.");

		//starts the game (first thing called distributes characters and board
		startDay();								//starts the game!

	}

	/**
	 * Running this method will trigger the process of creating a MagicRealm server.
	 * @param args Command line arguments, likely to remain unused
	 */
	public static void main(String args[]){
		ServerController control = new ServerController();		//instanciate the controller
		ServerView view = new ServerView(control);
		view.setVisible(true);
	}

}
