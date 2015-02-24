package model;

import java.io.Serializable;

import utils.Utility.ArmourName;
import utils.Utility.GarrisonName;
import utils.Utility.WeaponName;

public class Garrison implements Serializable{


	private static final long serialVersionUID = 7914470711829476000L;
	private GarrisonName name;
	private Clearing location;
	//private Native[] natives;
	//private Visitor[] visitors;
	private Weapon[] weapons;
	private Armour[] armour;
	private Treasure[] treasures = new Treasure[2];
	//private Horses[] horses;
	
	public Garrison(GarrisonName n){
		this.name = n;
		setupGarrison();
	}
	
	public void setLocation(Clearing l){
		this.location = l;
		l.setDwelling(this);
	}
	
	public Clearing getLocation(){
		return location;
	}
	
	/**
	 * Every garrison setups up the same way every game, so I can code it
	 */
	private void setupGarrison(){
		if(name == GarrisonName.CHAPEL){
			
			WeaponName[] n = {WeaponName.GREAT_AXE, WeaponName.GREAT_AXE,
					WeaponName.GREAT_SWORD, WeaponName.MORNING_STAR,
					WeaponName.CROSSBOW};
			this.weapons = generateWeapons(n);
			
			ArmourName[] a = {ArmourName.SUIT_OF_ARMOR, ArmourName.SUIT_OF_ARMOR};
			this.armour = generateArmour(a);
			
		} else if (name == GarrisonName.GUARD){
			
			WeaponName[] n = {WeaponName.MACE, WeaponName.MACE,
					WeaponName.AXE, WeaponName.AXE, WeaponName.BROADSWORD};
			this.weapons = generateWeapons(n);
			
			ArmourName[] a = {ArmourName.HELMET, ArmourName.BREASTPLATE};
			this.armour = generateArmour(a);
			
		}else if(name == GarrisonName.HOUSE){

			WeaponName[] n = {WeaponName.SHORT_SWORD, WeaponName.SHORT_SWORD,
					WeaponName.SHORT_SWORD, WeaponName.THRUSTING_SWORD,
					WeaponName.THRUSTING_SWORD, WeaponName.THRUSTING_SWORD,
					WeaponName.STAFF, WeaponName.STAFF};
			this.weapons = generateWeapons(n);
			
			ArmourName[] a = {ArmourName.HELMET, ArmourName.HELMET, ArmourName.HELMET,
					ArmourName.SHIELD, ArmourName.SHIELD};
			this.armour = generateArmour(a);
			
		} else if (name == GarrisonName.INN){
			
			this.armour = null;
			this.weapons = null;
		}
	}
	
	private Weapon[] generateWeapons(WeaponName[] names){
		Weapon[] w = new Weapon[names.length];
		for(int i=0;i<names.length;i++){
			w[i] = new Weapon(names[i]);
		}
		return w;
	}
	
	private Armour[] generateArmour(ArmourName[] names){
		Armour[] a = new Armour[names.length];
		for(int i=0;i<names.length;i++){
			a[i] = new Armour(names[i]);
		}
		return a;
	}
	
	public GarrisonName getName(){
		return name;
	}
	
	public void setTreasures(Treasure[] t){
		treasures = t;
	}
	
	public String toString(){
		String i = "";
		if(this.weapons != null){
			for(int z=0; z<this.weapons.length-1;z++){
				i+= this.weapons[z]+", ";
			}
			i+= this.weapons[this.weapons.length - 1];
			System.out.println("			-contains weapons: "+ i);
		}
		i = "";
		if(this.armour != null){
			for(int z=0; z<this.armour.length-1;z++){
				i+= this.armour[z]+", ";
			}
			i+= this.armour[this.armour.length - 1];
			System.out.println("			-contains armour: "+ i);
		}
		i = "";
		if(this.treasures != null){
			for(int z=0; z<this.treasures.length;z++){
				i+= this.treasures[z]+", ";
			}
			System.out.println("			-contains treasures: "+i);
		}
		return "";
	}
	
}
