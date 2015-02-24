package model;

import utils.Utility.ArmourName;
import utils.Utility.TreasureWithinTreasureName;
import utils.Utility.WeaponName;

public class TreasureWithinTreasure extends Treasure{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8782838058247096888L;
	private TreasureWithinTreasureName name;
	private Treasure[] treasures;
	private Weapon[] weapon;
	private Armour[] armour;
	//private Horse[] horse;
	//private Spell[] spells;
	
	public TreasureWithinTreasure(TreasureWithinTreasureName n, Treasure[] t){
		super(10);
		this.name = n;
		treasures = t;
		weapon = new Weapon[1];
		armour = new Armour[3];
		setUpTreasure();
	}
	
	public void setUpTreasure(){
		switch(name){
		case CHEST:
			this.gold = 50;
			break;
		case CRYPT_OF_THE_KNIGHT:
			this.armour[0] = new Armour(ArmourName.TREMENDOUS_ARMOR);
			this.weapon[0] = new Weapon(WeaponName.BANE_SWORD);
			break;
		case ENCHANTED_MEADOW:
			this.weapon[0] = new Weapon(WeaponName.TRUESTEEL_SWORD);
			break;
		case MOULDY_SKELETON:
			this.armour[0] = new Armour(ArmourName.GOLD_HELMET);
			this.armour[1] = new Armour(ArmourName.SILVER_BREASTPLATE);
			this.armour[2] = new Armour(ArmourName.JADE_SHIELD);
			break;
		case REMAINS_OF_THIEF:
			this.weapon[0] = new Weapon(WeaponName.LIVING_SWORD);
			this.gold = 20;
			break;
		case TOADSTOOL_CIRCLE:
			this.weapon[0] = new Weapon(WeaponName.DEVIL_SWORD);
			break;
		default:
			break;
	}
	}
	
	public TreasureWithinTreasureName getName(){
		return this.name;
	}
	
	public Treasure[] getTreasures(){
		return this.treasures;
	}
	
	@Override
	public String toString(){
		System.out.println(name.toString()+", Value:"+gold+", Contains: ");
		for(int i=0;i<treasures.length;i++){					//prints treasures it contains
			System.out.println("	"+treasures[i]);
		}
		for(int i=0;i<armour.length;i++){					//prints weapons it contains
			if(armour[i] != null){
				System.out.println("	"+armour[i]);
			}
		}
		for(int i=0;i<weapon.length;i++){					//prints armour it contains
			if(weapon[i] != null){
				System.out.println("	"+weapon[i]);
			}
		}
		
		return "";
	}

}
