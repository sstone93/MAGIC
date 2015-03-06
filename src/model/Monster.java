package model;

import java.io.Serializable;

import utils.Utility.ItemWeight;

public class Monster implements Serializable{

	private static final long serialVersionUID = -582083574086670219L;
	Clearing   location;
	ItemWeight weight;
	int        notorietyPts;
	int        famePts;
	boolean    armoured;
    // todo: put weight, in enum
    
    public Clearing getLocation() {
    	return location;
    }
    
    public void setLocation(Clearing location) {
    	this.location = location;
    }
    
    public ItemWeight getWeight() {
    	return weight;
    }
    
    public int getNotorietyPts() {
    	return notorietyPts;
    }
    
    public int getFamePts() {
    	return famePts;
    }
    
    public boolean isArmoured() {
    	return armoured;
    }


}