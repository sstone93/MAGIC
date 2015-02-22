package model;

import java.util.Arrays;
import utils.*;
import utils.Utility.Actions;
import utils.Utility.Attacks;
import utils.Utility.Defenses;
import utils.Utility.ItemWeight;
import utils.Utility.Maneuvers;
import utils.Utility.WeaponName;

// include move, alert, rest, search, hide, resetDay, resetWeek, startDay, gameOver
// blocking (including by monsters)
public class Game {

    Player[] players = new Player[Config.MAX_CLIENTS] ;
    Board board ; // initialized in startGame
    int playerCount    = 0;
    int currentDay     = 0;
    boolean hasStarted = false;

    // function that is called to start a game
    public boolean startGame() {
        if (playerCount > 0) {
            board = new Board(players);
            hasStarted = true;
        }
        return hasStarted;
    }

    // only adds a player if there's enough room for a new one
    // does nothing if the game is already full
    // return true: the player has been added
    // return false: the player was unable to be added
    public boolean addPlayer(Player player) {
        boolean canAdd = false;
        if (playerCount < players.length) {
            players[playerCount] = player;
            playerCount++;
            canAdd = true;
        }
        return canAdd;
    }

    public int getPlayerCount() {
        return playerCount;
    }
    // TODO: this is the function for monsters changing clearings
    public boolean move(Monster monster, Clearing newClearing) {
    	boolean canChange =  (monster.getLocation().canChangeClearing(newClearing));
    	block(monster); // see if it can block any players
    	return canChange;
    }

    // returns true if the player is allowed to move to the clearing
    // returns false if unable to move, and the player forfeits this phase
    public boolean move(Player player, Object newClearing) {
        player.setHidden(false);
        boolean canChange =  (player.getLocation().canChangeClearing((Clearing)newClearing));
        // todo: check if player knows secret locations
        Chit[] chits             = player.getChits();
        ItemWeight highestWeight = Utility.ItemWeight.NEGLIGIBLE;

        // find the highest weight of the active move chits of the player
        for (int i = 0; i < chits.length; i++) {
            if (chits[i].isVisible()) {
                if (chits[i].getType() == Utility.Actions.MOVE) {
                    ItemWeight currentWeight = Utility.getItemWeight(chits[i].getName());
                    boolean check = Utility.isWeightHeavier(currentWeight, highestWeight);
                    if (check) {
                        highestWeight = currentWeight;
                    }
                }
            }
        }

        if (canChange) {
        	// discard anything that player can't carry
            player.removeWeaponsWithHigherWeight(highestWeight);
            player.removeArmourWithHigherWeight(highestWeight);

            player.setLocation((Clearing)newClearing);

        }
        return canChange;
    }

    public boolean hide(Player player) { // assume it always works
        player.setHidden(true);
        return true;
    }

    // player can can alert or unalert a weapon
    public void alert(Object weapon, Object alert) {
    	Weapon weapon2 = (Weapon) weapon;
        weapon2.setActive(((Boolean) alert).booleanValue());
    }

    public void rest(Player player) {
        player.setFatigue(0); // I'm assuming it resets the fatigue, which I'm pretty sure is wrong
    }

    // player is the player that's searching for others in the clearing
    // for iteration 1, I'm just going to have them discover other players, maybe treasure
    // returns the player(s) found, otherwise returns an empty array
    public Player[] search(Player player) {
    	Tile tile = player.getLocation().parent;
    	Player[] sameClearingPlayers = new Player[playerCount];
    	int foundPlayers = 0;
    	for (int i = 0; i < playerCount; i++) {
    		// I'm assuming that we won't have players with the same character
    		if (players[i].getCharacter().getName() != player.getCharacter().getName()) {
	    		if (players[i].getLocation().parent == tile ) {
	    			if (players[i].getLocation() == player.getLocation()) {
	    				players[i].setHidden(false);
	    				sameClearingPlayers[foundPlayers] = players[i];
	    				foundPlayers++;
	    			}
	    		}
    		}
    	}
    	Treasure[] clearingTreasures = player.getLocation().getTreasures();
    	for (int i = 0; i < clearingTreasures.length; i++) {
    		if (clearingTreasures[i] != null) {
    			player.addTreasure(clearingTreasures[i]);
    			player.getLocation().removeTreasure(clearingTreasures[i]);
    		}
    	}
    	return sameClearingPlayers;
    }

    // can block other players in the clearing
    public Player[] block(Player player) {
    	Player[] blockedPlayers = new Player[playerCount];
    	
    	Tile tile = player.location.parent;
    	int blocked = 0;
    	// finds the unhidden players in the same clearing as them
    	for (int i = 0; i < playerCount; i++) {
    		if (players[i].getLocation().parent == tile) {
    			if (players[i].getLocation() == player.location) {
    				if (!players[i].isHidden()) {
    					blockedPlayers[blocked] = players[i];
    					blocked++;

    				}
    			}
    		}

    	}
    	return blockedPlayers;

    }
    // blocks all unhidden players in the clearing
    public Player[] block(Monster monster) {
    	Tile tile = monster.location.parent;
    	int blocked = 0;
    	Player[] blockedPlayers = new Player[playerCount];
    	// finds the unhidden players in the same clearing as them
    	for (int i = 0; i < playerCount; i++) {
    		if (players[i].getLocation().parent == tile) {
    			if (players[i].getLocation() == monster.location) {
    				if (!players[i].isHidden()) {
    					players[i].setBlocked(true);
    					blockedPlayers[blocked] = players[i];
    					blocked++;

    				}
    			}
    		}

    	}
    	return blockedPlayers;
    }

    // returns true if day was reset
    // returns false if it's the 28th day
    public boolean resetDay() {
        if (currentDay == 28) {
            return false;
        }
        // unalert all weapons
        for (int i = 0; i < playerCount; i++) {
            Weapon[] weapons = players[i].getWeapons();
            for (int j = 0; j < weapons.length; j++ ) {
            	if (weapons[j] != null) {
            		weapons[j].setActive(false);
            	}
            }
        }
        return true;

        // face up map chits (except lost city and lost castle) are turned face down
    }

    public void resetWeek() {
        resetDay();
        // return monsters to their place
        // reset natives
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

    // TODO: need to figure out how to save the clearing the player wants to move to
    // and the weapons/armour (??) they want to alert/unalert
    public void doTodaysActivities(Player player) {
    	for (int i = 0; i < player.activities.length; i++) {
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
    			int moves = 0;
    			if (player.activities[moves] != null) {
		    		switch((Actions) player.activities[0]) {
		    		
		    		case MOVE: move(player, player.activities[moves + 1]); moves = moves + 2; break;
		    		case HIDE: hide(player); moves++; break;
		    		case ALERT: alert(player.activities[moves + 1], player.activities[moves + 2]); moves = moves + 3; break;
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

	// Combat code.
    //TODO Needs to print out to console, plus all of the other TODO's in the code

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


//    TODO REMOVE THIS
// public static void main(String[] args) {
//     Game game = new Game();
//     Character swordsman = new Swordsman();
//     Character elf = new Elf();
//     Player player1 = new Player(swordsman);
//     Player player2 = new Player(elf);
//     game.addPlayer(player1);
//     game.addPlayer(player2);
//
//     System.out.println("starting game:");
//     game.startGame();
//     game.startDay();
//     System.out.println("current day" + game.currentDay);
//
//     System.out.println("player order for the day:");
//     System.out.println("player1 : " + player1.order);
//     System.out.println("player2 : " + player2.order);
//
//     // initial starting weapons/armour
//     for (int i = 0; i < player1.numberOfWeapons; i++) {
//     	System.out.println("player1 weapons:" + player1.weapons[i]);
//     }
//     for (int i = 0; i < player1.numberOfArmour; i++) {
//     	System.out.println("player1 armour:" + player1.armour[i]);
//     }
//     for (int i = 0; i < player2.numberOfWeapons; i++) {
//     	System.out.println("player2 weapons:" + player2.weapons[i]);
//     }
//     for (int i = 0; i < player2.numberOfArmour; i++) {
//     	System.out.println("player2 armour:" + player2.armour[i]);
//     }
//
//     Weapon weapon1 = new Weapon(WeaponName.GREAT_SWORD);
//     player1.addWeapon(weapon1);
//     System.out.println("added great sword to player1");
//     for (int i = 0; i < player1.numberOfWeapons; i++) {
//     	System.out.println("player1 weapons:" + player1.weapons[i]);
//     }
//
//     System.out.println("player1 hidden?" + player1.isHidden());
//     game.hide(player1);
//     System.out.println("player1 hidden?" + player1.isHidden());
//
//     // todo: need to set player location
//     System.out.println("player 1 location: " + player1.getLocation());
//     System.out.println("player 2 location: " + player2.getLocation());
//
//     System.out.println("checking weapon alert " + player1.weapons[0].isActive());
//     game.alert(player1.weapons[0], false);
//     System.out.println("checking weapon alert " + player1.weapons[0].isActive());
//     game.alert(player1.weapons[0], true);
//     System.out.println("checking weapon alert " + player1.weapons[0].isActive());
//
//     System.out.println(player1.weapons[0].weight);
//     System.out.println(player1.weapons[1].weight);
//
//     System.out.println("setting weapon weight to medium:");
//     player1.weapons[1].setWeight(ItemWeight.MEDIUM);
//     System.out.println(player1.weapons[1].weight);
//     System.out.println("removing weapons that are greater weight then medium (should not change");
//     player1.removeWeaponsWithHigherWeight(ItemWeight.MEDIUM);
//     for (int i = 0; i < player1.numberOfWeapons; i++) {
//       	System.out.println("player1 weapons:" + player1.weapons[i]);
//     }
//     System.out.println("removing weapons that are greater weight then light (should remove 1 weapon)");
//     player1.removeWeaponsWithHigherWeight(ItemWeight.LIGHT);
//     for (int i = 0; i < player1.numberOfWeapons; i++) {
//       	System.out.println("player1 weapons:" + player1.weapons[i]);
//     }
//
//
//     game.resetDay();
//
//     Player winner = game.endGame();
//     System.out.println("winner: " + winner.character.name);
// }
}
