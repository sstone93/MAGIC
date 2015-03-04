package model;

import java.io.Serializable;
import java.util.ArrayList;

public class LostCity extends LostPlace implements Serializable{

	private static final long serialVersionUID = -3406046049468158180L;
	
	public LostCity(ArrayList<MapChit> ch){
		super(ch);
	}
	
	public String toString(){
		String s = ("The Lost City contains: ");
		for(int i=0; i<this.chits.size();i++){					//prints sounds it contains
			if(chits.get(i) != null){
				System.out.println("			"+this.chits.get(i)+", ");
			}
		}
		return s;
	}

}
