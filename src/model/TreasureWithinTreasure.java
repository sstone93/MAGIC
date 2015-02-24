package model;

public class TreasureWithinTreasure extends Treasure{
	
	private TreasureWithinTreasure name;
	private Treasure[] treasures;
	
	public TreasureWithinTreasure(TreasureWithinTreasure n, Treasure[] t){
		super(10);
		this.name = n;
		treasures = t;
	}
	
	public TreasureWithinTreasure getName(){
		return this.name;
	}
	
	public Treasure[] getTreasures(){
		return this.treasures;
	}
	
	@Override
	public String toString(){
		System.out.println(name+", Value:"+gold);
		for(int i=0;i<treasures.length;i++){
			System.out.println("	"+treasures[i]);
		}
		return "";
	}

}
