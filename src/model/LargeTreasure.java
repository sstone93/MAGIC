package model;

import utils.Utility;
import utils.Utility.LargeTreasureName;

public class LargeTreasure extends Treasure{
	
	private static final long serialVersionUID = -6793022336572660328L;
	
	public LargeTreasure(LargeTreasureName n){
		super(Utility.randomInRange(40,50), n.toString());
	}

	@Override
	public String toString(){
		return name+": ("+gold+" gold, "+notoriety+" notoriety, "+fame+" fame)";
	}
	
}
