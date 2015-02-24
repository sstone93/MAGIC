package model;

public class SmallTreasure extends Treasure{
	
	private SmallTreasure name;
	
	public SmallTreasure(SmallTreasure n){
		super(10);
		this.name = n;
	}
	
	public SmallTreasure getName(){
		return this.name;
	}

	@Override
	public String toString(){
		System.out.println(name+", Value:"+gold);
		return "";
	}
	
}
