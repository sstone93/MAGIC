package model;

import java.io.Serializable;

import utils.Utility.*;

public class CombatMoves implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -442459525150433813L;
	public Attacks attack;
	public int attackFatigue;
	public Maneuvers maneuver;
	public int maneuverFatigue;
	public Defenses defense;
	
	public CombatMoves (Attacks attack, int attackFatigue, Maneuvers maneuver, int maneuverFatigue, Defenses defense) {
		this.attack = attack;
		this.attackFatigue = attackFatigue;
		this.maneuver = maneuver;
		this.maneuverFatigue = maneuverFatigue;
		this.defense = defense;
	}

	public Attacks getAttack() {
		return attack;
	}
	
	public int getAttackFatigue() {
		return attackFatigue;
	}
	
	public Maneuvers getManeuver() {
		return maneuver;
	}
	
	public int getManeuverFatigue() {
		return maneuverFatigue;
	}
	
	public Defenses getDefense() {
		return defense;
	}
}
