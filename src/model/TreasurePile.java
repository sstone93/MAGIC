package model;

import java.io.Serializable;
import java.util.ArrayList;

import utils.Utility.SoundChits;

public class TreasurePile extends MapChit implements Serializable{
	
	private static final long serialVersionUID = -1975754197391952827L;
	private ArrayList<Treasure> treasures = new ArrayList<Treasure>();
	private ArrayList<Armour> armour = new ArrayList<Armour>();
	private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	
	public TreasurePile(ArrayList<Treasure> t, ArrayList<Armour> a, ArrayList<Weapon> w){
		super(SoundChits.FLUTTER_1);
		this.treasures = t;
		this.armour = a;
		this.weapons = w;
	}
	
	public void setTreasures(ArrayList<Treasure> t){
		this.treasures = t;
	}
	
	public void setArmour(ArrayList<Armour> a) {
		this.armour = a;
	}
	
	public void setWeapons(ArrayList<Weapon> w) {
		this.weapons = w;
	}
	
	public ArrayList<Treasure> getTreasures(){
		return treasures;
	}
	
	public ArrayList<Armour> getArmour(){
		return armour;
	}
	
	public ArrayList<Weapon> getWeapons(){
		return weapons;
	}
	
	public void takeTreasure(Treasure t){
		this.treasures.remove(t);
	}
	
	public void takeWeapon(Weapon w){
		this.weapons.remove(w);
	}
	
	public void takeArmour(Armour a){
		this.armour.remove(a);
	}

	public String toString(){
		String t = "";
		for(int i=0;i<this.treasures.size();i++){					//prints treasures it contains
			if(treasures.get(i) != null){
				t+=treasures.get(i)+", ";
			}
		}
		System.out.println("			Contains Treasures: "+t);
		String a = "";
		for(int i=0;i<this.armour.size();i++){					//prints armour it contains
			if(armour.get(i) != null){
				t+=armour.get(i)+", ";
				a+=armour.get(i)+", ";
			}
		}
		System.out.println("			Contains Armour: "+a);
		String w = "";
		for(int i=0;i<this.weapons.size();i++){					//prints weapons it contains
			if(weapons.get(i) != null){
				t+=weapons.get(i)+", ";
				w+=weapons.get(i)+", ";
			}
		}
		System.out.println("			Contains Weapons: "+w);
		return t;
	}
}