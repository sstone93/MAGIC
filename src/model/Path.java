package model;

import java.io.Serializable;
import utils.Utility.PathType;

public class Path implements Serializable {

	private static final long serialVersionUID = 7438868920261944520L;
	PathType type;
	Clearing clearingA;
	Clearing clearingB;
	
	public Path(Clearing c1, Clearing c2, PathType t){
		this.type = t;
		this.clearingA = c1;
		this.clearingB = c2;
	}
	
	public PathType getType(){
		return this.type;
	}
	
	/*
	 * Returns null if the clearing is not actually part of the path
	 */
	public Clearing getDestination(Clearing start){
		if(start == clearingA){
			return clearingB;
		}else if(start == clearingB){
			return clearingA;
		}else{
			return null;
		}
	}

}
