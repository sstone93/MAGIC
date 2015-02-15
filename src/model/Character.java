package model;

public class Character {
    String name;
    Clearing startingLocation;
    Weapon[] startingWeapons;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartingLocation(Clearing location) {
        startingLocation = location;
    }

    public Clearing getStartingLocation() {
        return startingLocation;
    }

    public Weapon[] getStartingWeapons() {
        return startingWeapons;
    }
}