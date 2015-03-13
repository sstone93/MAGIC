package model;

import utils.Utility.*;

public class HeavyDragon extends Monster {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9074002135440697188L;

	public HeavyDragon() {
        weight       = ItemWeight.HEAVY;
        notoriety    = 5;
		fame         = 5;
		armoured     = true;
		attackLength = 4;
		attackSpeed  = 4;
    }

}
