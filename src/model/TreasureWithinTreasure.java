package model;

import java.util.ArrayList;
import utils.Utility.ArmourName;
import utils.Utility.TreasureWithinTreasureName;
import utils.Utility.WeaponName;

public class TreasureWithinTreasure extends Treasure{
	
	private static final long serialVersionUID = -8782838058247096888L;
	private TreasureWithinTreasureName name;
	private ArrayList<Treasure> treasures = new ArrayList<Treasure>();
	private ArrayList<Weapon> weapon = new ArrayList<Weapon>();
	private ArrayList<Armour> armour = new ArrayList<Armour>();
	
	public TreasureWithinTreasure(TreasureWithinTreasureName n, ArrayList<Treasure> t){
		super(10);
		this.name = n;
		this.treasures = t;
		setUpTreasure();
	}
	
	public void setUpTreasure(){
		switch(name){
		case CHEST:
			this.gold = 50;
			break;
		case CRYPT_OF_THE_KNIGHT:
			this.armour.add(new Armour(ArmourName.TREMENDOUS_ARMOR));
			this.weapon.add(new Weapon(WeaponName.BANE_SWORD));
			break;
		case ENCHANTED_MEADOW:
			this.weapon.add(new Weapon(WeaponName.TRUESTEEL_SWORD));
			break;
		case MOULDY_SKELETON:
			this.armour.add(new Armour(ArmourName.GOLD_HELMET));
			this.armour.add(new Armour(ArmourName.SILVER_BREASTPLATE));
			this.armour.add(new Armour(ArmourName.JADE_SHIELD));
			break;
		case REMAINS_OF_THIEF:
			this.weapon.add(new Weapon(WeaponName.LIVING_SWORD));
			this.gold = 20;
			break;
		case TOADSTOOL_CIRCLE:
			this.weapon.add(new Weapon(WeaponName.DEVIL_SWORD));
			break;
		default:
			break;
	}
	}
	
	public TreasureWithinTreasureName getName(){
		return this.name;
	}
	
	public ArrayList<Treasure> getTreasures(){
		return this.treasures;
	}
	
	public void setTreasures(ArrayList<Treasure> t){
		this.treasures = t;
	}
	
	@Override
	public String toString(){
		System.out.println(name.toString()+", Value:"+gold+", Contains: ");
		for(int i=0;i<treasures.size();i++){					//prints treasures it contains
			if(treasures.get(i) != null){
				System.out.println("	"+treasures.get(i));
			}
		}
		for(int i=0;i<armour.size();i++){					//prints weapons it contains
			if(armour.get(i) != null){
				System.out.println("	"+armour.get(i));
			}
		}
		for(int i=0;i<weapon.size();i++){					//prints armour it contains
			if(weapon.get(i) != null){
				System.out.println("	"+weapon.get(i));
			}
		}
		
		return "";
	}

}
