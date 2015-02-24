package model;

public class LargeTreasure extends Treasure{
	
	private LargeTreasure name;
	
	public LargeTreasure(LargeTreasure n){
		super(40);
		this.name = n;
	}
	
	public LargeTreasure getName(){
		return this.name;
	}

	@Override
	public String toString(){
		System.out.println(name+", Value:"+gold);
		return "";
	}
	
}
