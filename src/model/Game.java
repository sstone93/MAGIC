package model;

import java.util.Arrays;
import utils.*;
import utils.Utility.ItemWeight;
import utils.Utility.WeaponName;

// include move, alert, rest, search, hide, resetDay, resetWeek, startDay, gameOver
// blocking (including by monsters)
public class Game {
	
    Player[] players = new Player[Config.MAX_CLIENTS] ;
    //Board board = new Board(players);
    int playerCount  = 0;
    int currentDay   = 0;

    // only adds a player if there's enough room for a new one
    // does nothing if the game is already full
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

    // returns true if the player is allowed to move to the clearing
    // retuns false if unable to move, and the player forfeits this phase
    public boolean move(Player player, Clearing newClearing) {
        player.setHidden(false);
        boolean canChange =  (player.getLocation().canChangeClearing(newClearing));
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
            player.removeWeaponsWithLesserWeight(highestWeight);
            player.removeArmourWithLesserWeight(highestWeight);

            player.setLocation(newClearing);

        }
        return canChange;
    }

    public boolean hide(Player player) { // assume it always works
        player.setHidden(true);
        return true;
    }

    // player can can alert or unalert a weapon
    public void alert(Weapon weapon, boolean alert) {
        weapon.setActive(alert);
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
    	return sameClearingPlayers;
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
                    //TODO player does their moves in order
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

    // todo: in next iteration calculate the score properly
    // 1 point per great treasure
    // 1 point for each 10 points of fame, 1 point for each 20 points of notoriety, and 1 point for each 30 gold
    public Player endGame() {
        Player winner = null;
        Player player = null;

        // todo: player has to discard any items an active move chit can't carry
        // todo: treasures

        for (int i = 0; i <= playerCount; i++ ) {
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

    //TODO REMOVE THIS
//    public static void main(String[] args) {
//        Game game = new Game();
//        Character swordsman = new Swordsman();
//        Character elf = new Elf();
//        Player player1 = new Player(swordsman);
//        Player player2 = new Player(elf);
//        game.addPlayer(player1);
//        game.addPlayer(player2);
//        
//        System.out.println("starting game:");
//        game.startDay();
//        System.out.println("current day" + game.currentDay);
//        
//        System.out.println("player order for the day:");
//        System.out.println("player1 : " + player1.order);
//        System.out.println("player2 : " + player2.order);
//        
//        // initial starting weapons/armour
//        for (int i = 0; i < player1.numberOfWeapons; i++) {
//        	System.out.println("player1 weapons:" + player1.weapons[i]);
//        }
//        for (int i = 0; i < player1.numberOfArmour; i++) {
//        	System.out.println("player1 armour:" + player1.armour[i]);
//        }
//        for (int i = 0; i < player2.numberOfWeapons; i++) {
//        	System.out.println("player2 weapons:" + player2.weapons[i]);
//        }
//        for (int i = 0; i < player2.numberOfArmour; i++) {
//        	System.out.println("player2 armour:" + player2.armour[i]);
//        }
//        
//        Weapon weapon1 = new Weapon(WeaponName.GREAT_SWORD);
//        player1.addWeapon(weapon1);
//        System.out.println("added great sword to player1");
//        for (int i = 0; i < player1.numberOfWeapons; i++) {
//        	System.out.println("player1 weapons:" + player1.weapons[i]);
//        }
//        
//        System.out.println("player1 hidden?" + player1.isHidden());
//        game.hide(player1);
//        System.out.println("player1 hidden?" + player1.isHidden());
//        
//        // todo: need to set player location
//        System.out.println("player 1 location: " + player1.getLocation());
//        System.out.println("player 2 location: " + player2.getLocation());
//        
//        
//        
//        
//    }
}
