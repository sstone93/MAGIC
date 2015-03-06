package model;

import utils.Utility.*;

public class Wolf extends Monster {
	/**
	 *
	 */
	private static final long serialVersionUID = -3565210120367313469L;

	public Wolf() {
		weight       = ItemWeight.MEDIUM;
		notorietyPts = 1;
		famePts      = 0;
		armoured     = false;
	}

}
