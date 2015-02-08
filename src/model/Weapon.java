package model;
import Utility;

public class Weapon {
    boolean ranged;
    int     length;
    int     weight;
    int     speed;
    WeaponDamage damage;

    public void setRanged(boolean ranged) {
        this.ranged = ranged;
    }
    public void isRanged() {
        return ranged;
    }

    public WeaponDamage getDamage() {
        return damage;
    }
    public WeaponDamage setDamage(WeaponDamage damage) {
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

    public int setSpeed(int speed) {
        this.speed = speed;
    }

}