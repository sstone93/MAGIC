package model;

import java.io.Serializable;

import utils.Utility.PhaseType;
import utils.Utility.Actions;

public class Phase implements Serializable {
	
	private static final long serialVersionUID = 1039169659229492652L;
	PhaseType type;
	Actions[] action;
	Object extraInfo;

	public Phase(PhaseType t) {
		// TODO used for basic/sunlight phases
		type = t;
	}

	public Phase(PhaseType t, Actions[] a) {
		// TODO Handles special phases?
		type = t;
		action = a;
	}
	
	public Phase(PhaseType t, Actions[] a, Object o) {
		type = t;
		action = a;
		extraInfo = o;
	}

	public PhaseType getType(){
		return type;
	}
	
	public Actions[] getAction(){
		return action;
	}
	
	public Object getExtraInfo(){
		return extraInfo;
	}
	
    public boolean equals(Phase p) {
    	if(this.type == p.type && p.type == PhaseType.BASIC){
    		return true;
    	}
    	if(this.type == p.type && p.type == PhaseType.SUNLIGHT){
    		return true;
    	}
    	if(this.type == p.type && p.type == PhaseType.SPECIAL){
    		return true;
    	}
    	if(this.type == p.type && p.type == PhaseType.TREASURE){
    		if(this.action == p.action){
    			return true;
    		}else{
    			for(Actions a : this.action){
    				if(a == p.action[0]){
    					return true;
    				}
    			}
    			return false;
    		}
    	}else{
    		return false;
    	}
    }
    
    public String toString(){
    	String mes = type.toString()+" ";
    	if(action != null){
    		for(Actions a : action)
    			mes+=a.toString()+" ";
    	}
    	if(extraInfo != null){
    		mes+=extraInfo;
    	}
    	return mes;
    }
	
}
