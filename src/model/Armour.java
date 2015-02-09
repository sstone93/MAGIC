package model;

public class Armour {
    String  weight;
    String  health
    String  type;
    boolean damaged;
    boolean active;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHealth() {
        return health;
    }
    public void setHealth(String health) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}