package model;

import utils.Utility.SmallTreasureName;

public class SmallTreasure extends Treasure{
	
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
		System.out.println(name.toString()+", Value:"+gold);
		return "";
	}
	
}
