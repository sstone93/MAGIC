package model;

import utils.Utility.*;

public class Ghost extends Monster {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2169292630927906598L;
	
	public Ghost() {
		name         = "Ghost";
		weight       = ItemWeight.MEDIUM;
		notoriety    = 2;
		fame         = 0;
		armoured     = false;
		attackLength = 2;
		attackSpeed  = 2;
	}

}
