package model;

import utils.Utility.LargeTreasureName;

public class LargeTreasure extends Treasure{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6793022336572660328L;
	private LargeTreasureName name;
	
	public LargeTreasure(LargeTreasureName n){
		super(40);
		this.name = n;
	}
	
	public LargeTreasureName getName(){
		return this.name;
	}

	@Override
	public String toString(){
		System.out.println(name.toString()+", Value:"+gold);
		return "";
	}
	
}
