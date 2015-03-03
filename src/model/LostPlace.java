package model;

import java.io.Serializable;
import java.util.ArrayList;

public class LostPlace implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3255228650386288129L;
	public ArrayList<MapChit> chits;
	
	public LostPlace(ArrayList<MapChit> ch){
		this.chits = ch;
	}
	
	
}
