package model;

import utils.Config;


// include move, alert, rest, search, hide, resetDay, resetWeek, startDay, gameOver
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
        
        // roll for which character goes first
        // set their orders 

    }

    public void endGame() {
        // player has to discard any items an active move chit can't carry

        //great treasure score: number of treasures owned - great treasures needed for his victory requirements

        // fame score:recorded fame + fame of belongings - fame needed for victory requirements (do not count the fame reward of belongings)

        // notoriety score: recorded notoriety + notoreity value of belongings - notoriety needed for VR
        // (belonging with negative notoriety values subtract from score)

        // gold score: recorded gold - gold for VR

        // basic score: divides each category by its factor to convert to victory points (round all fractions down)

        // bonus score: multiplies basic score in each category by the number of victory points he assigned to that category

        // total score: basic + bonus
    }


}