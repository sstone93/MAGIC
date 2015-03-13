package model;

import utils.Utility.*;

public class Giant extends Monster {


    /**
	 * 
	 */
	private static final long serialVersionUID = 3266689556786887165L;

	public Giant() {
        weight       = ItemWeight.TREMENDOUS;
        notoriety    = 8;
		fame         = 8;
		armoured     = false;
		attackLength = 5;
		attackSpeed  = 5;
    }

}