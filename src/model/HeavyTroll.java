package model;

import utils.Utility.*;

public class HeavyTroll extends Monster {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8706862594792035351L;

	public HeavyTroll() {
        weight       = ItemWeight.HEAVY;
        notoriety    = 5;
		fame         = 5;
		armoured     = true;
		attackLength = 4;
		attackSpeed  = 4;
    }

}
