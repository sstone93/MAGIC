package model;

public class Chit {
	String name; 
	Clearing location;
	// also want the image, not sure what it should be stored as
	
	public String getName() {
		return name; 
	}
	
	public void setLocation(Clearing newLocation) {
		location = newLocation;
	}
	
}