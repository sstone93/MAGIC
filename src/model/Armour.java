package model;

public class Armour {
    int health;
    String type;
    boolean damaged;

    public int getHealth() {
        return health;
    }

    public void addHealth(int health) {
        this.health += health;
    }
    public void removeHealth(int health) {
        this.health -= health;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public void setDamaged(boolean damage) {
        damaged = damage;
    }
}