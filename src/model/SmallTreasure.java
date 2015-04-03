package model;

import java.util.ArrayList;

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

	@Override
	public ArrayList<Weapon> getWeapons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Treasure> getTreasures() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Armour> getArmour() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
