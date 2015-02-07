package model;

public class Weapon {
    boolean ranged;
    int damage;
    int length;

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

}