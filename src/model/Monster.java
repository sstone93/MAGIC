package model;

public class Monster {
    Clearing location;
    int      length = 0; // represents length of the tooth/claw
    // todo: put weight, in enum
    
    public Clearing getLocation() {
    	return location;
    }
    
    public void setLocation(Clearing location) {
    	this.location = location;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}