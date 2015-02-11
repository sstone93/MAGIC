package model;

public class Weapon {
    boolean ranged;
    int     damage;
    int     length;
    int     weight;
    String  speed;

    public void setRanged(boolean ranged) {
        this.ranged = ranged;
    }
    public boolean isRanged() {
        return ranged;
    }

    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

}