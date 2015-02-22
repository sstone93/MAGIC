package model;

public class Treasure {
    Clearing location;
    String   type = "gold"; // for now we only have gold
    int      gold = 10; // initial

    Treasure(int gold) {
        this.gold = gold; // can be 10, 20, 30, 40, 50
    }

    public Clearing getLocation() {
        return location;
    }

    public void setLocation(Clearing location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}