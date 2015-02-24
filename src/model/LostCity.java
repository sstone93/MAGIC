package model;

import java.io.Serializable;

public class LostCity extends LostPlace implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3406046049468158180L;
	
	public LostCity(MapChit[] ch){
		super(ch);
	}
	
	public String toString(){
		String s = ("The Lost City contains: ");
		for(int i=0; i<this.chits.length;i++){					//prints sounds it contains
			if(chits[i] != null){
				System.out.println("			"+this.chits[i]+", ");
			}
		}
		return s;
	}

}
