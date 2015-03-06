package model;

import utils.Utility.*;

public class Giant extends Monster {


    /**
	 * 
	 */
	private static final long serialVersionUID = 3266689556786887165L;

	public Giant() {
        weight       = ItemWeight.TREMENDOUS;
        notorietyPts = 8;
        famePts      = 8;
        armoured     = false;
    }

}
