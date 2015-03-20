package model;

import java.io.Serializable;

import utils.Utility;

public class Treasure implements Serializable{

	private static final long serialVersionUID = -2440018576886400929L;
    int     gold;
    int		notoriety;
    int		fame;

    Treasure(int g) {
        this.gold = g;//does this so that large treasures can have diff. values
        this.notoriety = Utility.randomInRange(1,20);
        this.fame = Utility.randomInRange(-5,30);
    }

    public int getGold() {
        return gold;
    }
    
    public int getFame() {
        return fame;
    }
    
    public int getNotoriety() {
        return notoriety;
    }

}