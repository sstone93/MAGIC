package model;

import java.io.Serializable;

import utils.Utility.PhaseType;
import utils.Utility.Actions;

public class Phase implements Serializable {
	
	private static final long serialVersionUID = 1039169659229492652L;
	PhaseType type;
	Actions action;
	Object extraInfo;

	public Phase(PhaseType t) {
		// TODO used for basic/sunlight phases
		type = t;
	}

	public Phase(PhaseType t, Actions a) {
		// TODO Handles special phases?
		type = t;
		action = a;
	}
	
	public Phase(PhaseType t, Actions a, Object o) {
		type = t;
		action = a;
		extraInfo = o;
	}

	public PhaseType getType(){
		return type;
	}
	
	public Actions getAction(){
		return action;
	}
	
	public Object getExtraInfo(){
		return extraInfo;
	}
	
    public boolean equals(Phase p) {
    	if(this.type == p.type){
    		if(this.action != null && p.action != this.action || p.action == Actions.PASS){
    			return false;
    		}
    		return true;
    	}else{
    		return false;
    	}
    }
	
}
