package model;

import java.io.Serializable;

import utils.Utility.WarningChits;

public class WarningChit implements Serializable{

	private WarningChits name;
	private static final long serialVersionUID = -5168229883049997240L;
	
	public WarningChit(WarningChits n){
		this.name = n;
	}
	
	public WarningChits getName(){
		return this.name;
	}
	
	public String toString(){
		System.out.println(name.toString());
		return "";
	}
}
