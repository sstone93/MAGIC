package model;

public class Character {
    String name;
    Clearing startingLocation;
    Weapon   startingWeapon;

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

    public Weapon getStartingWeapon() {
        return startingWeapon;
    }

    public void setStartingWeapon(Weapon weapon) {
        startingWeapon = weapon;
    }
}