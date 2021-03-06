package model;

import java.io.Serializable;
import java.util.ArrayList;

import utils.Utility.ArmourName;
import utils.Utility.GarrisonName;
import utils.Utility.LargeTreasureName;
import utils.Utility.SmallTreasureName;
import utils.Utility.TreasureWithinTreasureName;
import utils.Utility.WeaponName;

public class Garrison implements Serializable{


	private static final long serialVersionUID = 7914470711829476000L;
	private GarrisonName name;
	private Clearing location;
	private ArrayList<Weapon> weapons  = new ArrayList<Weapon>();
	private ArrayList<Armour> armour = new ArrayList<Armour>();
	private ArrayList<Treasure> treasures = new ArrayList<Treasure>();

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

			this.armour.clear();
			this.weapons.clear();
			
			//SMALL TREASURES ADDED FOR TESTING
			this.treasures.add(new SmallTreasure(SmallTreasureName.CLOAK_OF_MIST));
			this.treasures.add(new SmallTreasure(SmallTreasureName.DEFT_GLOVES));
			this.treasures.add(new SmallTreasure(SmallTreasureName.SHOES_OF_STEALTH));
			this.treasures.add(new SmallTreasure(SmallTreasureName.MAGIC_SPECTACLES));
			this.treasures.add(new SmallTreasure(SmallTreasureName.HANDY_GLOVES));
			this.treasures.add(new SmallTreasure(SmallTreasureName.LEAGUE_BOOTS_7));
			this.treasures.add(new SmallTreasure(SmallTreasureName.ELVEN_SLIPPERS));
			this.treasures.add(new SmallTreasure(SmallTreasureName.QUICK_BOOTS));
			this.treasures.add(new SmallTreasure(SmallTreasureName.GLOVES_OF_STRENGTH));
			this.treasures.add(new SmallTreasure(SmallTreasureName.POWER_BOOTS));
			this.treasures.add(new SmallTreasure(SmallTreasureName.POWER_GAUNTLETS));
			this.treasures.add(new SmallTreasure(SmallTreasureName.POULTICE_OF_HEALTH));
			this.treasures.add(new SmallTreasure(SmallTreasureName.REFLECTION_GREASE));
			this.treasures.add(new SmallTreasure(SmallTreasureName.SHIELDED_LANTERN));
			
			//LARGE TREASURES FOR TESTING
			this.treasures.add(new LargeTreasure(LargeTreasureName.REGENT_OF_JEWELS));
			this.treasures.add(new LargeTreasure(LargeTreasureName.LUCKY_CHARM));
			
			//treasure within treasures for testing
			this.treasures.add(new TreasureWithinTreasure(TreasureWithinTreasureName.MOULDY_SKELETON, new ArrayList<Treasure>()));
			
		}
	}

	private ArrayList<Weapon> generateWeapons(WeaponName[] names){
		ArrayList<Weapon> w  = new ArrayList<Weapon>();
		for(int i=0;i<names.length;i++){
			w.add(new Weapon(names[i]));
		}
		return w;
	}

	private ArrayList<Armour> generateArmour(ArmourName[] names){
		ArrayList<Armour> a = new ArrayList<Armour>();
		for(int i=0;i<names.length;i++){
			a.add(new Armour(names[i]));
		}
		return a;
	}

	public GarrisonName getName(){
		return name;
	}

	public void addTreasures(ArrayList<Treasure> t){
		for(Treasure tr : t){
			treasures.add(tr);
		}
	}

	public ArrayList<Treasure> getTreasures() {
		return treasures;
	}
	public void removeTreasure(Treasure t) {
		treasures.remove(t);
	}
	public void addTreasure(Treasure t) {
		treasures.add(t);
	}
	
	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}
	public void removeWeapon(Weapon w) {
		weapons.remove(w);
	}
	public void addWeapon(Weapon w) {
		weapons.add(w);
	}
	public ArrayList<Armour> getArmour() {
		return armour;
	}
	public void removeArmour(Armour a) {
		armour.remove(a);
	}
	public void addArmour(Armour a) {
		armour.add(a);
	}

	public String toString(){
		String i = "";
		if(this.weapons != null){
			for(int z=0; z<this.weapons.size();z++){
				i+= this.weapons.get(z)+", ";
			}
			System.out.println("			-contains weapons: "+ i);
		}
		i = "";
		if(this.armour != null){
			for(int z=0; z<this.armour.size();z++){
				i+= this.armour.get(z)+", ";
			}
			System.out.println("			-contains armour: "+ i);
		}
		i = "";
		if(this.treasures != null){
			for(int z=0; z<this.treasures.size();z++){
				i+= this.treasures.get(z)+", ";
			}
			System.out.println("			-contains treasures: "+i);
		}
		return "";
	}

}
