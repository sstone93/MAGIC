package model;

import java.io.Serializable;

public class LostPlace implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3255228650386288129L;
	@SuppressWarnings("unused")
	private MapChit[] chits;
	
	public LostPlace(MapChit[] ch){
		this.chits = ch;
	}
	
	
}
