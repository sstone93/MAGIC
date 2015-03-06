package model;

import utils.Utility.*;

public class HeavyTroll extends Monster {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8706862594792035351L;

	public HeavyTroll() {
        weight       = ItemWeight.HEAVY;
        notorietyPts = 5;
        famePts      = 5;
        armoured     = true;
    }

}
