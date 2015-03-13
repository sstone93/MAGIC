package model;

import utils.Utility.*;

public class Viper extends Monster {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8428957616151818588L;

	public Viper() {
        weight       = ItemWeight.MEDIUM;
        notoriety    = 2;
		fame         = 1;
		armoured     = true;
		attackLength = 4;
		attackSpeed  = 4;
    }

}
