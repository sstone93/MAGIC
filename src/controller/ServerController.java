package controller;

import networking.Message;
import networking.NetworkServer;
import model.Amazon;
import model.Armour;
import model.Berserker;
import model.BlackKnight;
import model.Board;
import model.Captain;
import model.Clearing;
import model.CombatMoves;
import model.Dwarf;
import model.Elf;
import model.MapChit;
import model.Monster;
import model.Path;
import model.Phase;
import model.Player;
import model.Swordsman;
import model.Tile;
import model.Treasure;
import model.TreasurePile;
import model.TreasureSite;
import model.WarningChit;
import model.Weapon;
import model.WhiteKnight;

import java.util.ArrayList;

import utils.Config;
import utils.Utility;
import utils.Utility.MonsterName;
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
	int finishedPlayers = 0;
	public int nextRoll = 0;
	private int monsterRoll;

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

	public int roll(int max){
		if(Config.CHEAT_MODE && this.nextRoll != 0){
			return this.nextRoll;
		}else{
			return Utility.roll(max);
		}
	}

	/**
	 * This is the method that handles incoming messages from the networking components
	 * @param ID The ID of the client sending the message
	 * @param message the contents of the message
	 */
	@SuppressWarnings("unchecked")
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

					//MAIN HANDLER FOR INDIVIDUAL PHASE SUBMISSION.

		    		//- determine if they move on to
		    		//- when a character is done all actions, set their state to finished, and check to see if all players are done and if you need to move on.

					Player p = findPlayer(ID);

					//1. is it valid? (blocked + move in their queue)
					if(p.isBlocked() == false){
						//do it

						//if it is a move, then don't reset last move
						if(((Phase) m.getData().get(0)).getAction() == Actions.MOVE){
							handleAction(p,(Phase)m.getData().get(0));
						}else{
							//if the action was not a move, reset player's last move
							p.setLastMove(null);
							handleAction(p,(Phase)m.getData().get(0));
						}

						//subtract from queue
						p.usePhase((Phase) m.getData().get(0));

						//tell client it worked
						network.send(ID, "ACTION SUCCEEDED");

						//check to see if basics are over, if so add sunlight
						p.checkAndAddSunlight();
						
						
						
						//broadcast new board and player states
						updateClients();	

						//see if that was their last action
						if(p.getDaylight()){
							finishedPlayers += 1;
							network.send(ID, "NO PHASES LEFT");
						}
						

					} else {
						if (((Phase) m.getData().get(0)).getAction() == Actions.HIDE) {
							handleAction(p, (Phase)m.getData().get(0));
						}
						//return error to client
						else {
							network.send(ID, "ACTION FAILED, YOU ARE BLOCKED");
						}
					}

				}else{
					network.send(ID, "NOT ACCEPTING ACTIVITIES ATM");
				}
			}
			if( m.getType() == MessageType.COMBAT_TARGET){
				if(state == GameState.CHOOSE_COMBATTARGET){
					recievedCombat += 1;
					//TODO: the data will now contain 2 arrayLists, one for the player targets(<CharacterName>)
					//and one for the monster targets(<MonsterName>)
					//I have temporarily modified this to just take the first player, 
					//but it will need to be changed so that the player has all of their targets.
					//NOTE: right now it breaks if you try to fight no one(index out of bounds because it tries to go straight to 0
					//even though size is 0, this will be fixed when we loop through)
					System.out.println("This is the target's name!");
					System.out.println(charToPlayer(((ArrayList<CharacterName>) m.getData().get(0)).get(0)));
					Player temp = charToPlayer(((ArrayList<CharacterName>) m.getData().get(0)).get(0));
					System.out.println(temp.getCharacter().getName());
					
					//this is how you would get the first monster in the arraylist.
					//Monster mon = findPlayer(ID).getMonsterInSameClearing(((ArrayList<MonsterName>) m.getData().get(1)).get(0)));
					
					//turns the received character name into a player
					findPlayer(ID).setTarget(charToPlayer(((ArrayList<CharacterName>) m.getData().get(0)).get(0)));
				}else{
					network.send(ID, "NOT ACCEPTING COMBAT TARGETS ATM");
				}
			}
			if( m.getType() == MessageType.BLOCK){
				Player blockTarget = (Player) m.getData().get(0);
				block(blockTarget);
				block(findPlayer(ID));
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
					Player temp;
					switch((CharacterName) m.getData().get(0)){
					case AMAZON: temp = new Player(new Amazon(), ID);
					break;
					case BERSERKER: temp = new Player(new Berserker() , ID);
					break;
					case BLACK_KNIGHT: temp = new Player(new BlackKnight(), ID);
					break;
					case CAPTAIN: temp = new Player(new Captain(), ID);
					break;
					case DWARF: temp = new Player(new Dwarf(), ID);
					break;
					case ELF: temp = new Player(new Elf(), ID);
					break;
					case SWORDSMAN: temp = new Player(new Swordsman(), ID);
					break;
					case WHITE_KNIGHT: temp = new Player(new WhiteKnight(), ID);
					break;
					default:
						 temp = null;
					break;
					}

					this.addedPlayers += 1;
					temp.getCharacter().setStartingLocation((GarrisonName) m.getData().get(1)); //sets the starting location
					players.add(temp);
					System.out.println("Added ");
					//System.out.println((CharacterName) m.getData().get(0));
					//System.out.println(addedPlayers);

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
	public void hide(Player player) {

		int roll;
		boolean oneDie = Utility.checkRollOneDie(player, "hide");
		if (oneDie) {
			network.send(player.getID(), "Congrats, you had the shoes of stealth which allows you to roll only 1 die!");
			roll = rollForTables(player, 1);
		}
		else {
			roll = rollForTables(player, 2);
		}

		if (roll != 6) {
			player.setHidden(true);
			player.setBlocked(false);
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
    			block(players.get(i));
    		}
    	}
    }

	/**
	 * Unhides all players in the same clearing as player and returns a list of them
	 * @param The player doing the searching
	 * @return A list of all players in the clearing (now unhidden)
	 */
    public void search(Player player, SearchTables table) {

		if (table == Utility.SearchTables.LOCATE) {
			boolean reduceDie = Utility.checkRollOneDie(player, "locate");

			int roll;
			if (reduceDie) {
				network.send(player.getID(), "You have something which allows you to only roll one die! Congrats");
				roll = rollForTables(player, 1);
			}
			else {
				roll = rollForTables(player, 2);
			}

			locate(player, roll);
		}
		else if (table == Utility.SearchTables.LOOT) {
			boolean reduceDie = Utility.checkRollOneDie(player, "loot");

			int roll;
			if (reduceDie) {
				network.send(player.getID(), "You have something, which allows you to only roll one die! Congrats");
				roll = rollForTables(player, 1);
			}
			else {
				roll = rollForTables(player, 2);
			}

			//if you know about the site and are stading on the clearing containing it.
			TreasureSite site = player.getLocation().getSite();

			if(site != null && player.knowsSite(site)){

				ArrayList<Treasure> treasures = site.getTreasures();

				if (roll <= treasures.size()) {
					player.addTreasure(treasures.get(roll-1));
					network.send(player.getID(), "you've found "+treasures.get(roll-1).getName()+"!!");
					site.takeTreasure(treasures.get(roll-1));
				}
				else  {
					network.send(player.getID(), "You didn't find any treasures this time");
				}
			}
			
			TreasurePile pile = player.getLocation().getPile();
			
			if(pile != null){
				ArrayList<Treasure> treasures = pile.getTreasures();
				ArrayList<Weapon> weapons = pile.getWeapons();
				ArrayList<Armour> armour = pile.getArmour();
				
				if (roll <= treasures.size()) {
					player.addTreasure(treasures.get(roll-1));
					network.send(player.getID(), "you've found "+treasures.get(roll-1).getName()+"!!");
					pile.takeTreasure(treasures.get(roll-1));
				}
				else  {
					network.send(player.getID(), "You didn't find any treasures this time");
				}
				
				if (roll <= weapons.size()) {
					player.addWeapon(weapons.get(roll-1));
					network.send(player.getID(), "you've found "+weapons.get(roll-1).getType()+"!!");
					pile.takeWeapon(weapons.get(roll-1));
				}
				else  {
					network.send(player.getID(), "You didn't find any weapons this time");
				}
				
				if (roll <= armour.size()) {
					player.addArmour(armour.get(roll-1));
					network.send(player.getID(), "you've found "+armour.get(roll-1).getType()+"!!");
					pile.takeArmour(armour.get(roll-1));
				}
				else  {
					network.send(player.getID(), "You didn't find any armour this time");
				}
			}
		}
    }

    // rolls dice for the player
    // returns the lowest of the two rolls if the player has to roll 2 die
    // otherwise returns the first roll done
    public int rollForTables(Player player, int numberOfDie) {
    	int roll = roll(6);
    	if (numberOfDie == 2) {
	    	int roll2 = roll(6);
	    	network.broadCast(player.getCharacter().getName() + " rolled " + roll + " and " + roll2 );
	    	roll = Math.max(roll, roll2);
    	}
		network.broadCast(player.getCharacter().getName() + " is using " + roll );

		return roll;
    }

    public void locate(Player player, int roll) {
    	if (roll == 1) {
    		ArrayList<Path> connections = player.getLocation().getConnections() ;
    		boolean foundHidden = false;
    		for (int i = 0; i < connections.size(); i++) {
    			if (connections.get(i).getType() == Utility.PathType.SECRET_PASSAGEWAY) {
    				player.addDiscovery(connections.get(i));
    				network.send(player.getID(), "You've discovered secret passageways!");
    				foundHidden = true;
    			}
    		}
    		if (!foundHidden) {
    			network.send(player.getID(), "No secret passageways to discover!");
    		}
    	}
    	else if (roll == 2 || roll == 3) {
    		// note: I'm taking out clues from the 2 roll
    		ArrayList<Path> connections = player.getLocation().getConnections() ;
    		boolean foundHidden = false;
    		for (int i = 0; i < connections.size(); i++) {
    			if (connections.get(i).getType() == Utility.PathType.HIDDEN_PATH) {
    				player.addDiscovery(connections.get(i));
    				network.send(player.getID(), "You've discovered hidden paths!");
    				foundHidden = true;
    			}
    		}
    		if (!foundHidden) {
    			network.send(player.getID(), "No hidden paths to discover!");
    		}
    	}
    	else if (roll == 4) { // discover chits
    		// TODO: discover every site chit in the clearing you are searching

    		//    		You secretly look at the map chits in the tile you are searching.
    		//    		You discover every Site chit in the clearing you are searching and cross it off your Discoveries list.
    		//    		Henceforward, you can LOOT this Site chit whenever you are in its clearing.

    		ArrayList<MapChit> mapChit = player.getLocation().parent.getMapChit();

    		if (mapChit.isEmpty() != true) {
    			for(MapChit m : mapChit){
    				player.addDiscovery(m);
    			}
    			network.send(player.getID(), "You've discovered " + mapChit + "!");
    			discoverMonstersWithSiteChits(player);
    		} else {
    			network.send(player.getID(), "No map chits to discover!");
    		}
    	}
    	else if (roll == 5 || roll == 6) {
    		// do nothing
    		network.send(player.getID(), "You've discovered nothing") ;
    	}

    }

    private void block(Player p){
    	p.setBlocked(true);
    	finishedPlayers +=1;
    }

    // TODO: add warning chit to players discoveries?
    // TODO: make sure that map chit isn't null
    private void discoverMonstersWithSiteChits(Player player) {
    	ArrayList<MapChit> mapC  = player.getLocation().parent.getMapChit();
    	WarningChit  warningChit = player.getLocation().parent.getWarningChit();
		WarningChits warningName = warningChit.getName();
		TileType     type        = player.getLocation().parent.getType();
		MonsterName  monsterName = null;
		
		System.out.println("warning chit name: " + warningName);
		System.out.println("tile type: " + type);
		
		if (warningChit.hasSummoned() == false) {
			if (type == TileType.WOODS) {
    			if (monsterRoll == 2) {
					if (warningName == WarningChits.DANK) {
						monsterName = MonsterName.VIPER;
					}
    			}
    			else if (monsterRoll == 3) {
					if (warningName == WarningChits.RUINS) {
						monsterName = MonsterName.WOLF;
					}
    			}
    		}
			else if (type == TileType.MOUNTAINS) {
    			if (monsterRoll == 1) {
    				if (warningName == WarningChits.SMOKE) {
    					monsterName = MonsterName.HEAVY_DRAGON;
    				}
    			}
    			else if (monsterRoll == 4) {
    				if (warningName == WarningChits.BONES || warningName == WarningChits.STINK   ) {
    					monsterName = MonsterName.GIANT;
    				}
    			}
    			else if (monsterRoll == 5) {
    				if (warningName == WarningChits.STINK || warningName == WarningChits.DANK) {
    					monsterName = MonsterName.HEAVY_SPIDER;
    				}
    			}
    			else if (monsterRoll == 6) {
    				if (warningName == WarningChits.BONES || warningName == WarningChits.RUINS ) {
    					monsterName = MonsterName.GIANT_BAT;
    				}
    			}
			}
			else if (type == TileType.CAVES) {
				if (monsterRoll == 1) {
					if (warningName == WarningChits.SMOKE) {
    					monsterName = MonsterName.HEAVY_DRAGON;
    				}
				}
				else if (monsterRoll == 2) {
					if (warningName == WarningChits.DANK) {
    					monsterName = MonsterName.VIPER;
    				}
				}
				else if (monsterRoll == 3) {
					if (warningName == WarningChits.RUINS ) {
    					monsterName = MonsterName.HEAVY_TROLL;
    				}
				}
				else if (monsterRoll == 4) {
					if (warningName == WarningChits.BONES || warningName == WarningChits.STINK   ) {
    					monsterName = MonsterName.HEAVY_TROLL;
    				}
				}
			}
		}
		if (monsterName != null) {
			placeMonsterAndBlock(player, monsterName);
			warningChit.setSummoned(true);
		}
		if (monsterName == null) { // this means that warning chit didn't place anything
	    	for(MapChit mapChit : mapC) {
	    		if (mapChit == null) {
	    			continue;
	    		}
	    		if (mapChit.hasSummoned() == true) {
	    			continue;
	    		}
	    		System.out.println("!!! map chit name: " + mapChit.getName());
	    		monsterName          = null;
	    		SoundChits soundName = mapChit.getName();
	
	
	    		if (type == TileType.MOUNTAINS) {
	    			if (monsterRoll == 1) {
	    				if (soundName == SoundChits.SLITHER_3 || soundName == SoundChits.SLITHER_6) {
	    					monsterName = MonsterName.HEAVY_DRAGON;
	    				}
	    				else if (soundName == SoundChits.FLUTTER_1 || soundName == SoundChits.FLUTTER_2) {
	    					monsterName = MonsterName.HEAVY_DRAGON;
	    				}
	    			}
	    			else if (monsterRoll == 2) {
	    				if (soundName == SoundChits.SLITHER_3 || soundName == SoundChits.SLITHER_6) {
	    					monsterName = MonsterName.VIPER;
	    				}
	    			}
	    			else if (monsterRoll == 4) {
	    				if (soundName == SoundChits.ROAR_4 || soundName == SoundChits.ROAR_6) {
	    					monsterName = MonsterName.HEAVY_TROLL;
	    				}
	    			}
	    			else if (monsterRoll == 5) {
	    				if (soundName == SoundChits.PATTER_2 || soundName == SoundChits.PATTER_5) {
	    					monsterName = MonsterName.HEAVY_SPIDER;
	    				}
	    			}
	    			else if (monsterRoll == 6) {
	    				if (soundName == SoundChits.HOWL_4 || soundName == SoundChits.HOWL_5) {
	    					monsterName = MonsterName.GIANT_BAT;
	    				}
	    				else if (soundName == SoundChits.FLUTTER_1 || soundName == SoundChits.FLUTTER_2) {
	    					monsterName = MonsterName.GIANT_BAT;
	    				}
	    			}
	    		}
	    		else if (type == TileType.CAVES) {
	    			if (monsterRoll == 1) {
	    				if (soundName == SoundChits.SLITHER_3 || soundName == SoundChits.SLITHER_6) {
	    					monsterName = MonsterName.HEAVY_DRAGON;
	    				}
	    				else if (soundName == SoundChits.ROAR_4 || soundName == SoundChits.ROAR_6) {
	    					monsterName = MonsterName.HEAVY_DRAGON;
	    				}
	    				else if (soundName == SoundChits.FLUTTER_1 || soundName == SoundChits.FLUTTER_2) {
	    					monsterName = MonsterName.HEAVY_DRAGON;
	    				}
	    			}
	    			else if (monsterRoll == 2) {
	    				if (soundName == SoundChits.SLITHER_3 || soundName == SoundChits.SLITHER_6) {
	    					monsterName = MonsterName.VIPER;
	    				}
	    			}
	    			else if (monsterRoll == 3) {
	    				if (soundName == SoundChits.HOWL_4 || soundName == SoundChits.HOWL_5 ) {
	    					monsterName = MonsterName.HEAVY_TROLL; // replaced goblins with trolls
	    				}
	    				else if (soundName == SoundChits.PATTER_2 || soundName == SoundChits.PATTER_5 ) {
	    					monsterName = MonsterName.HEAVY_TROLL; // replaced goblins with trolls
	    				}
	    			}
	    			else if (monsterRoll == 4) {
	    				if (soundName == SoundChits.ROAR_4 || soundName == SoundChits.ROAR_6) {
	    					monsterName = MonsterName.HEAVY_TROLL;
	    				}
	    			}
	    			else if (monsterRoll == 6) {
	    				if (soundName == SoundChits.FLUTTER_1 || soundName == SoundChits.FLUTTER_2) {
	    					monsterName = MonsterName.GIANT_BAT;
	    				}
	    			}
	    		}
	    		if (monsterName != null) {
	    			mapChit.setSummoned(true);
	    			placeMonsterAndBlock(player, monsterName);
	    		}
	    	}
		}
    }
    
    /**
     * places new monster in clearing based on @monsterName
     * monster blocks @player in clearing if they are unhidden 
     **/
    private void placeMonsterAndBlock(Player player, MonsterName monsterName) {
		ArrayList<Monster> monstersInClearing = player.getLocation().getMonsters();
		if (monsterName != null) {
			board.placeMonstersAtStartingLocation(monsterName, player.getLocation());
			network.broadCast("player has summoned " + monsterName + "!"  );
		}

		if (!player.isBlocked() && !player.isHidden()) {
			for (int i = 0; i < monstersInClearing.size(); i++) {
				if (!monstersInClearing.get(i).isBlocked()) {
					monstersInClearing.get(i).setBlocked(true);
					block(player);
					network.send(player.getID(), "You've been blocked by " + monstersInClearing.get(i).getName());
					break;
				}
			}
		}
    }

    private void printBoard() {
    	ArrayList<Tile> tiles = board.tiles;
    	for (int i = 0; i < tiles.size(); i++) {
    		Tile tile = tiles.get(i);
    		System.out.println("tile and type : " + tile.getName() + ", " + tile.getType() + " with warning chit: " + tile.getWarningChit() + " map chit: " + tile.getMapChit());
    	}
    }

    /**
     * Sets a player's weapon to the opposite state of what it is currently
     * @param p The player
     */
    public void alert(Player p) {
    	if (p.getActiveWeapon().getType() != Utility.WeaponName.FIST) {
    		p.getActiveWeapon().setActive(!p.getActiveWeapon().isActive());
    	}
    	else {
    		p.getWeapons().get(0).setActive(true);
    	}
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

        // reset their fatigue
        for (int i = 0; i < playerCount; i++) {
        	players.get(i).setFatigue(0);
        	players.get(i).setGoneInCave(false);
        	players.get(i).setBlocked(false);
        }

        // reset map chits and warning chits so they can summon monsters
        for (int i = 0; i < board.tiles.size(); i++) {
        	board.tiles.get(i).getWarningChit().setSummoned(false);
        	for(MapChit m : board.tiles.get(i).getMapChit()){
        		if (m != null) {
            		m.setSummoned(false);
            	}
        	}
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

        for (int i = 0; i < board.monsters.size(); i++) {
        	Monster currentMonster = board.monsters.get(i);
        	currentMonster.setLocation(currentMonster.getStartingLocation());
        	currentMonster.resetDead();
        	currentMonster.setProwling(false);
        	currentMonster.setBlocked(false);
        }
    }

    public void handleAction(Player p, Phase a){
    	switch(a.getAction()) {
    	case MOVE:
    		boolean move = board.move(p, a);
    		network.broadCast(p.getCharacter().getName() + " is moving? : " + move);
    		break;
    	case HIDE:
    		hide(p);
    		break;
    	case ALERT:
    		alert(p);
    		network.broadCast(p.getCharacter().getName() + " is alerting their weapon!");
    		break;
    	case REST:
    		rest(p);
    		network.broadCast(p.getCharacter().getName() + " is resting!");
    		break;
    	case SEARCH:
    		search(p, (SearchTables) a.getExtraInfo());
    		break;
    	case TRADE:
    		//TODO MAKE TRADING A THING
    		network.broadCast(p.getCharacter().getName() + " is trading!");
    		break;
    	case PASS:
    		network.broadCast(p.getCharacter().getName() + " is PASSING!");
    		break;
    	default:
    		break;
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
            int fameScore      = players.get(i).getFame();
            int notorietyScore = players.get(i).getNotoriety();
            int goldScore      = players.get(i).getGold();
            
            if (players.get(i).getTreasures() != null) {
            	for (int j = 0; j < players.get(i).getTreasures().size(); j++) {
            		if (players.get(i).getTreasures().get(j) != null) {
            			notorietyScore += players.get(i).getTreasures().get(j).getNotoriety();
            			fameScore += players.get(i).getTreasures().get(j).getFame();
            			goldScore += players.get(i).getTreasures().get(j).getGold();
            		}
            	}
            }

            fameScore = fameScore / 10;
            notorietyScore = notorietyScore / 20;
            goldScore = goldScore / 30;

            basicScore = fameScore + notorietyScore + goldScore;
            player.setFinalScore(basicScore);

            network.send(player.getID(), player.getFinalScore() + " is your score!");

            if (basicScore > winner.getFinalScore()) {
                winner = player;
            }
        }

        network.broadCast(winner.getCharacter().getName()+ " is the winner");
        network.stop();
    }

    private void setSunlightTrue(){
    	for(int i=0; i<playerCount;i++){
    		if(players.get(i).getCharacter().getName() != CharacterName.DWARF){
    			players.get(i).setGoneInCave(true);
    		}
    	}
    }

    public void calculatePhases(){
    	for(int i=0;i<playerCount;i++){
			players.get(i).calculatePhases();
		}
    }


    /**
     * Waits until all activities have been submitted (and tells the clients to send them)
     */
    public void startActivitiesHandler(){

    	setSunlightTrue();		//sets the sunlight for all players back to true (minus the dwarf)

    	calculatePhases();			//sets up all the players
    	distributeCharacters();		//tell the client's their options

    	//1. Send each player their notice for 2 basic moves
    	network.broadCast("SEND MOVES");

    	//2. START HANDLER FOR MOVE SUBMISSIONS
    	state = GameState.CHOOSE_PLAYS;
    	finishedPlayers = 0;

    	while(finishedPlayers < playerCount){
    		try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
    	}	//TODO HANDLE PLAYERS DROPPING OUT DURING THIS STEP

    	//5. Finish phase collection and move on
    	state = GameState.NULL;
    	System.out.println("ALL PLAYERS FINISHED DAYLIGHT PHASE.");
    	/*try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}*/
    }

    // gets the player that just finished
    /*private void getFinishedPlayer() {
    	for (int i = 0; i < players.size(); i++) {
    		// check if they're done
    	}
    }*/

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

    // sets specific monsters (based on the monster roll) to prowling
    public void rollForMonsters() {

    	// set all the monsters to dormant
    	for (int i = 0; i < board.monsters.size(); i++) {
    		board.monsters.get(i).setProwling(false);
    	}

    	int roll = roll(6);
    	network.broadCast("Monster roll: " + roll);
    	monsterRoll = roll;

    	setProwlingMonsters(MonsterName.GHOST);
    	if (roll == 1) {
    		network.broadCast("Ghosts and Heavy Dragons are on the prowl!");
    		setProwlingMonsters(MonsterName.HEAVY_DRAGON);
    	} else if (roll == 2) {
    		network.broadCast("Ghosts and Vipers are on the prowl!");
    		setProwlingMonsters(MonsterName.VIPER);
    	} else if (roll == 3) {
    		network.broadCast("Ghosts and Wolves are on the prowl!");
    		setProwlingMonsters(MonsterName.WOLF);
    	} else if (roll == 4) {
    		network.broadCast("Ghosts, Heavy Trolls, and Giants are on the prowl!");
    		setProwlingMonsters(MonsterName.HEAVY_TROLL);
    		setProwlingMonsters(MonsterName.GIANT);
    	} else if (roll == 5) {
    		network.broadCast("Ghosts and Heavy Spiders are on the prowl!");
    		setProwlingMonsters(MonsterName.HEAVY_SPIDER);
    	} else if (roll == 6) {
    		network.broadCast("Ghosts and Giant Bats are on the prowl!");
    		setProwlingMonsters(MonsterName.GIANT_BAT);
    	}
    }

    private void setProwlingMonsters(MonsterName name) {
    	ArrayList<Monster> prowlingMonsters = new ArrayList<Monster>();
    	prowlingMonsters = board.getMonsters(name);

		for (int i = 0; i < prowlingMonsters.size(); i++) {
			prowlingMonsters.get(i).setProwling(true);
		}
    }

    public void allowMonstersToProwl(){
    	network.broadCast("MONSTERS PROWLING STARTS NOW");
    	// checks to see who can prowl
    	ArrayList<Monster> prowlingMonsters = board.getProwlingMonsters();
    	for (int i = 0; i < prowlingMonsters.size(); i++) {
    		if (prowlingMonsters.get(i).getStartingLocation() != null) { // if they're on the board

    			Monster monster = null;
    			monster = prowlingMonsters.get(i);

    			if (monster.isProwling()) {
    				System.out.println("MONSTER IS MOVING");
    				// then they can prowl
    				monster.move();
    			}
    			if (!monster.isBlocked()) { // they can block others if they're not already blocked
					System.out.println("MONSTER BLOCKING");
					monster.block();
				}
    		}
    	}
    }

    /*
     * players turn over the tiles in their clearing, and monsters are appropriately summoned
     * the monsters that are in the same tile as the player are "summoned" to their clearing and block the player (if they're unhidden)
     *
     */
    private void summonMonstersToTile() {
    	for (int i = 0; i < players.size(); i ++) {
	    	// add warning chit to discoveries
	   		players.get(i).addDiscovery(players.get(i).getLocation().parent.getWarningChit());

	    	ArrayList<Clearing> clearings = players.get(i).getLocation().parent.getClearings();

	    	for (int j = 0; j < clearings.size(); j++) {
	    		if (!clearings.get(j).equals(players.get(i).getLocation())) {
	    			ArrayList<Monster> monsters = clearings.get(j).getMonsters();
	    			for (int k = 0; k < monsters.size(); k++) {
	    				if (monsters.get(k).isBlocked() == false) {
	    					clearings.get(j).removeMonster(monsters.get(k));
	    					monsters.get(k).setBlocked(false);
	    					players.get(i).getLocation().addMonster(monsters.get(k));
	    					monsters.get(k).block();
	    				}
	    			}
	    		}
	    	}
    	}
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
        printBoard();
        updateClients();

        rollForMonsters();

        startActivitiesHandler();
        allowMonstersToProwl();
        summonMonstersToTile();

        collectCombat(); //2 players, 1 attacker 1 defender

        //All players choose attackers
        for (int i = 0; i < players.size(); i++) {
        	if (players.get(i).getTarget() != null) {
        		for (int j = 0; j < players.get(i).getTarget().size(); j++) {
        			encounter(players.get(i), players.get(i).getTarget().get(j));
        		}
        		System.out.println("Finished encounter");
        	}
        	players.get(i).removeTarget();
        }

        //Progresses to the next day or ends the game
        boolean thing = resetDay();
        if(thing == true){ //if it is not the 28th day....
        	int alivePlayers = 0;
        	for (int i = 0; i < players.size(); i++) {
        		if (players.get(i).isDead() == false) {
        			alivePlayers++;
        		}
        	}
        	if (alivePlayers > 1) {
        		startDay();
        	}
        	else {
        		endGame();
        	}
        } else {
        	endGame();
        }
    }

    public void encounter(Player player, Monster monster) {

    	int unhurtRounds = 0;

    	while (unhurtRounds < 2) {

	    	if (player.isDead() == true) {
	    		network.send(player.getID(), "You are dead");
	    		return;
	    	}
	    	else if (monster.isDead() == true) {
	    		network.send(player.getID(), "That monster is dead");
	    		return;
	    	}

	    	int playerHurt = player.getHealth();
	    	int monsterHurt = monster.getHealth();

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
	    		player.setFatigue(player.getFatigue() + player.getMoves().getAttackFatigue() + player.getMoves().getManeuverFatigue());
	    		if (player.getFatigue() >= 10) {
	    			player.getActiveWeapon().setActive(!player.getActiveWeapon().isActive());
	    			network.send(player.getID(), "You are overfatigued!");
	    		}
        		network.send(player.getID(), player);
	    		doFight(player, monster);
	    	}

	    	if (playerHurt == player.getHealth() && monsterHurt == monster.getHealth()) {
        		unhurtRounds++;
        	}
        	else {
        		unhurtRounds = 0;
        	}
    	}
    }

    /**
     *
     * @param attacker
     * @param defender
     */
    public void encounter(Player attacker, Player defender) {
    	if (attacker == defender) {
    		network.send(attacker.getID(), "Stop attacking yourself!");
    		return;
    	}
    	else if (attacker.getID() == defender.getID()) {
    		network.send(attacker.getID(), "Stop attacking yourself!");
    		return;
    	}

    	int unhurtRounds = 0;

    	while (unhurtRounds < 2) {
    		if (attacker.isDead() == true) {
        		network.send(attacker.getID(), "You are dead.");
        		return;
        	}
        	else if (defender.isDead() == true) {
        		network.send(defender.getID(), "You are dead.");
        		return;
        	}

    		int attackerHurt = attacker.getHealth();
    		int defenderHurt = defender.getHealth();

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
        		attacker.setFatigue(attacker.getFatigue() + attacker.getMoves().getAttackFatigue() + attacker.getMoves().getManeuverFatigue());
        		defender.setFatigue(defender.getFatigue() + defender.getMoves().getAttackFatigue() + defender.getMoves().getManeuverFatigue());
        		if (attacker.getFatigue() >= 10) {
	    			attacker.getActiveWeapon().setActive(!attacker.getActiveWeapon().isActive());
	    			network.send(attacker.getID(), "You are overfatigued!");
	    		}
        		if (defender.getFatigue() >= 10) {
	    			defender.getActiveWeapon().setActive(!defender.getActiveWeapon().isActive());
	    			network.send(defender.getID(), "You are overfatigued!");
	    		}
        		network.send(attacker.getID(), attacker);
        		network.send(defender.getID(), defender);
        		doFight(attacker, defender);
        	}

        	if (attackerHurt == attacker.getHealth() && defenderHurt == defender.getHealth()) {
        		unhurtRounds++;
        	}
        	else {
        		unhurtRounds = 0;
        	}
    	}
	}

    public void doFight(Player player, Monster monster) {
    	if (player.getMoves().getManeuver() == Maneuvers.RUN && player.getFatigue() <= 10) {
			network.broadCast(player.getCharacter().getName() + " has run away!");
			player.setFatigue(player.getFatigue() + 2);
			if (player.getFatigue() >= 10) {
    			player.getActiveWeapon().setActive(!player.getActiveWeapon().isActive());
    			network.send(player.getID(), "You are overfatigued!");
    		}
			network.send(player.getID(), player);
			return;
		}

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
		if (attacker.getMoves().getManeuver() == Maneuvers.RUN && attacker.getFatigue() <= 10) {
			network.broadCast(attacker.getCharacter().getName() + " has run away!");
			attacker.setFatigue(attacker.getFatigue() + 2);
			if (attacker.getFatigue() >= 10) {
    			attacker.getActiveWeapon().setActive(!attacker.getActiveWeapon().isActive());
    			network.send(attacker.getID(), "You are overfatigued!");
    		}
			network.send(attacker.getID(), attacker);
			return;
		}
		else if (defender.getMoves().getManeuver() == Maneuvers.RUN && defender.getFatigue() <= 10) {
			network.broadCast(defender.getCharacter().getName() + " has run away!");
			defender.setFatigue(defender.getFatigue() + 2);
			if (defender.getFatigue() >= 10) {
    			defender.getActiveWeapon().setActive(!defender.getActiveWeapon().isActive());
    			network.send(defender.getID(), "You are overfatigued!");
    		}
			network.send(defender.getID(), defender);
			return;
		}

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
		ItemWeight level;

		if (player.getActiveWeapon().isRanged() == true) {

			int roll;
			boolean oneDie = Utility.checkRollOneDie(player, "missile");
			if (oneDie) {
				network.send(player.getID(), "Congrats, you are an elf so you only roll one die!");
				roll = rollForTables(player, 1);
			}
			else {
				roll = rollForTables(player, 2);
			}

			if(player.getCharacter().getName() == CharacterName.AMAZON ||
					player.getCharacter().getName() == CharacterName.BLACK_KNIGHT ||
							player.getCharacter().getName() == CharacterName.CAPTAIN){
				if(roll != 1){
					network.send(player.getID(), "Congrats, the 'AIM' special subtracted one from your roll!");
					roll --;	//SUBTACT 1 FOR THE "AIM" ABILITY
				}
			}

			if (roll == 1) {

				level = ItemWeight.HEAVY;
			}
			else if (roll == 2) {
				level = ItemWeight.MEDIUM;
			}
			else {
				level = ItemWeight.LIGHT;
			}
		}
		else {
			level = player.getActiveWeapon().getWeight();

			if (player.hasTreasure(SmallTreasureName.GLOVES_OF_STRENGTH.toString())) {
				level = ItemWeight.TREMENDOUS;
			}
			else if (player.hasTreasure(SmallTreasureName.POWER_GAUNTLETS.toString()) && (level == ItemWeight.MEDIUM || level == ItemWeight.LIGHT || level == ItemWeight.NEGLIGIBLE)){
				level = ItemWeight.HEAVY;
			}
			else if (player.hasTreasure(SmallTreasureName.HANDY_GLOVES.toString()) && (level == ItemWeight.LIGHT || level == ItemWeight.NEGLIGIBLE)) {
				level = ItemWeight.MEDIUM;
			}
			else if (player.hasTreasure(SmallTreasureName.DEFT_GLOVES.toString()) && (level == ItemWeight.NEGLIGIBLE)) {
				level = ItemWeight.LIGHT;
			}
		}

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
			if (player.getCharacter().getName() == CharacterName.BERSERKER) {
				player.setHealth(player.getHealth() + 1);
				network.broadCast(player.getCharacter().getName() + " has been wounded!");
				if (player.getHealth() == 4) {
					deadPlayer(player, monster);
				}
			}
			else {
				deadPlayer(player, monster);
			}
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

		ItemWeight level;

		if (attacker.getActiveWeapon().isRanged() == true) {

			int roll;

			boolean oneDie = Utility.checkRollOneDie(attacker, "missile");

			if (oneDie) {
				network.send(attacker.getID(), "Congrats, something means you only roll one die!");
				roll = rollForTables(attacker, 1);
			}
			else {
				roll = rollForTables(attacker, 2);
			}

			if(attacker.getCharacter().getName() == CharacterName.AMAZON ||
					attacker.getCharacter().getName() == CharacterName.BLACK_KNIGHT ||
					attacker.getCharacter().getName() == CharacterName.CAPTAIN){
				if(roll != 1){
					network.send(attacker.getID(), "Congrats, the 'AIM' special subtracted one from your roll!");
					roll --;	//SUBTACT 1 FOR THE "AIM" ABILITY
				}
			}

			if (roll == 1) {
				level = ItemWeight.HEAVY;
			}
			else if (roll == 2) {
				level = ItemWeight.MEDIUM;
			}
			else {
				level = ItemWeight.LIGHT;
			}
		}
		else {
			level = attacker.getActiveWeapon().getWeight();

			if (attacker.hasTreasure(SmallTreasureName.GLOVES_OF_STRENGTH.toString())) {
				level = ItemWeight.TREMENDOUS;
			}
			else if (attacker.hasTreasure(SmallTreasureName.POWER_GAUNTLETS.toString()) && (level == ItemWeight.MEDIUM || level == ItemWeight.LIGHT || level == ItemWeight.NEGLIGIBLE)){
				level = ItemWeight.HEAVY;
			}
			else if (attacker.hasTreasure(SmallTreasureName.HANDY_GLOVES.toString()) && (level == ItemWeight.LIGHT || level == ItemWeight.NEGLIGIBLE)) {
				level = ItemWeight.MEDIUM;
			}
			else if (attacker.hasTreasure(SmallTreasureName.DEFT_GLOVES.toString()) && (level == ItemWeight.NEGLIGIBLE)) {
				level = ItemWeight.LIGHT;
			}
		}

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
			if (defender.getCharacter().getName() == CharacterName.BERSERKER) {
				defender.setHealth(defender.getHealth() + 1);
				network.broadCast(defender.getCharacter().getName() + " has been wounded!");
				if (defender.getHealth() == 4) {
					deadPlayer(attacker, defender);
				}
			}
			else {
				deadPlayer(attacker, defender);
			}
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
		TreasurePile pile = new TreasurePile(player.getTreasures(), player.getArmour(), player.getWeapons());
		player.getLocation().setPile(pile);
		player.removeAll();
		player.kill();
		network.send(player.getID(), "You are dead.");
		network.broadCast(player.getCharacter().getName() + "has been killed!");
	}

	public void deadMonster(Player player, Monster monster) {
		player.addFame(monster.getFame());
		player.addNotoriety(monster.getNotoriety());
		monster.kill();
		network.broadCast(monster.getName() + "has been killed!");
	}

	public void deadPlayer(Player attacker, Player defender) {
		TreasurePile pile = new TreasurePile(defender.getTreasures(), defender.getArmour(), defender.getWeapons());
		defender.getLocation().setPile(pile);
		defender.removeAll();
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
