package model;

import utils.Utility.*;

public class Wolf extends Monster {
	/**
	 *
	 */
	private static final long serialVersionUID = -3565210120367313469L;

	public Wolf() {
		name         = MonsterName.WOLF;
		weight       = ItemWeight.MEDIUM;
		notoriety    = 1;
		fame         = 0;
		armoured     = false;
		attackLength = 3;
		attackSpeed  = 4;
	}

}
