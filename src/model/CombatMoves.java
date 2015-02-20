import utils.Utility.*;

public class CombatMoves {
	Player target;
	Attacks attack;
	int attackFatigue;
	Maneuvers maneuver;
	int maneuverFatigue;
	Defenses defense;
	
	public CombatMoves (Player target, Attacks attack, int attackFatigue, Maneuvers maneuver, int maneuverFatigue, Defenses defense) {
		this.target = target;
		this.attack = attack;
		this.attackFatigue = attackFatigue;
		this.maneuver = maneuver;
		this.maneuverFatigue = maneuverFatigue;
		this.defense = defense;
	}
	
	public Player getTarget() {
		return target;
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
