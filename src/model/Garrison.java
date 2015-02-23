package model;

import java.io.Serializable;

import utils.Utility.GarrisonName;
import utils.Utility.WeaponName;

public class Garrison implements Serializable{


	private static final long serialVersionUID = 7914470711829476000L;
	private GarrisonName name;
	private Clearing location;
	//private Native[] natives;
	//private Visitor[] visitors;
	private Weapon[] weapons;
	//private Armour[] armour;
	//private Treasure[] treasures;
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
			
		} else if (name == GarrisonName.GUARD){
			
			WeaponName[] n = {WeaponName.MACE, WeaponName.MACE,
					WeaponName.AXE, WeaponName.AXE, WeaponName.BROADSWORD};
			this.weapons = generateWeapons(n);
			
		}else if(name == GarrisonName.HOUSE){

			WeaponName[] n = {WeaponName.SHORT_SWORD, WeaponName.SHORT_SWORD,
					WeaponName.SHORT_SWORD, WeaponName.THRUSTING_SWORD,
					WeaponName.THRUSTING_SWORD, WeaponName.THRUSTING_SWORD,
					WeaponName.STAFF, WeaponName.STAFF};
			this.weapons = generateWeapons(n);
			
		} else if (name == GarrisonName.INN){
			
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
	
	public GarrisonName getName(){
		return name;
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
		//System.out.println("			-contains weapons: "+this.weapons);
		return "";
	}
	
}
