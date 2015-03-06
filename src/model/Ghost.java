package model;

import utils.Utility.*;

public class Ghost extends Monster {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2169292630927906598L;
	
	public Ghost() {
		weight       = ItemWeight.MEDIUM;
		notorietyPts = 2;
		famePts      = 0;
		armoured     = false;
	}

}
