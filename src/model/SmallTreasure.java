package model;

import utils.Utility.SmallTreasureName;

public class SmallTreasure extends Treasure{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -723576808162554552L;
	private SmallTreasureName name;
	
	public SmallTreasure(SmallTreasureName n){
		super(10);
		this.name = n;
	}
	
	public SmallTreasureName getName(){
		return this.name;
	}

	@Override
	public String toString(){
		return name.toString()+", Value:"+gold;
	}
	
}
