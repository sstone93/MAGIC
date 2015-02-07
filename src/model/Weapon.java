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
    public void isRanged() {
        return ranged;
    }

    public int getDamage() {
        return damage;
    }
    public int setDamage(int damage) {
        this.damage = damage;
    }

    public void getLength() {
        return length;
    }

    public int setLength(int length) {
        this.length = length;
    }

    public void getSpeed() {
        return speed;
    }

    public String setSpeed(String speed) {
        this.speed = speed;
    }

}