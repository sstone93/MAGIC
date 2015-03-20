package model;

import utils.Utility;
import utils.Utility.SmallTreasureName;

public class SmallTreasure extends Treasure{
	
	private static final long serialVersionUID = -723576808162554552L;
	
	public SmallTreasure(SmallTreasureName n){
		super(Utility.randomInRange(10,50), n.toString());
	}

	@Override
	public String toString(){
		return name+": ("+gold+" gold, "+notoriety+" notoriety, "+fame+" fame)";
	}
	
}
