package model;

import java.io.Serializable;

public class LostCastle extends LostPlace implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1116222759723378173L;
	
	public LostCastle(MapChit[] ch){
		super(ch);
	}
	
	public String toString(){
		String s = ("The Lost Castle contains: ");
		for(int i=0; i<this.chits.length;i++){					//prints sounds it contains
			if(chits[i] != null){
				s+=this.chits[i]+", ";
			}
		}
		return s;
	}

}
