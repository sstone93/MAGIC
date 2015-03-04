package model;

import java.io.Serializable;
import java.util.ArrayList;

public class LostCastle extends LostPlace implements Serializable{

	private static final long serialVersionUID = 1116222759723378173L;
	
	public LostCastle(ArrayList<MapChit> ch){
		super(ch);
	}
	
	public String toString(){
		String s = ("The Lost Castle contains: ");
		for(int i=0; i<this.chits.size();i++){					//prints sounds it contains
			if(chits.get(i) != null){
				s+=this.chits.get(i)+", ";
			}
		}
		return s;
	}

}
