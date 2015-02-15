package model;

import utils.Config;


// include move, alert, rest, search, hide, resetDay, resetWeek, startDay, gameOver
// blocking (including by monsters)
public class Game {

    Player[] players = new Player[Config.MAX_CLIENTS] ;
    int playerCount  = 0;
    int currentDay   = 1;

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
    public boolean move(Player player, Clearing newClearing) {
        player.setHidden(false);
        boolean canChange =  (player.getLocation().canChangeClearing(newClearing));
        if (canChange)
            player.setLocation(newClearing);
        return canChange;
    }

    public boolean hide(Player player) {
        // todo: check if they can hide
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

    public void resetDay() {
        if (currentDay == 28) {
            endGame();
            return;
        }
        // unalert all weapons
        for (int i = 0; i <= playerCount; i++) {
            Weapon[] weapons = players[i].getWeapons();
            for (int j = 0; j < weapons.length; j++ ) {
                weapons[j].setActive(false);
            }
        }

        // face up map chits (except lost city and lost castle) are turned face down
    }

    public void resetWeek() {
        resetDay();
        // return monsters to their place
        // reset natives
    }

    public void startDay() {
        currentDay++;

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
}