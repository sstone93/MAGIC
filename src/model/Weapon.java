package model;

public class Weapon {
    boolean ranged;
    int damage;
    int reach;

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

    public void getReach() {
        return reach;
    }

    public int setReach(int reach) {
        this.reach = reach;
    }

}