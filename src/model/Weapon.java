package model;
import Utility;

public class Weapon {
    boolean ranged;
    boolean active;
    String  attack; // the method of attack
    int     length; // on a scale of 0 - 18
    int     speed;
    int     sharpness; // higher = more harm inflicted
    WeaponDamage weight;
    WeaponDamage damage;

    public void setRanged(boolean ranged) {
        this.ranged = ranged;
    }
    public boolean isRanged() {
        return ranged;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean isActive() {
        return active;
    }

    public void setAttack(boolean attack) {
        this.attack = attack;
    }
    public String getAttack() {
        return attack;
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

    public void setSharpness(int sharp) {
        sharpness = sharp;
    }

    public int getSharpness() {
        return sharpness;
    }

    public WeaponDamage getWeight() {
        return weight;
    }
    public WeaponDamage setWeight(WeaponDamage weight) {
        this.weight = weight;
    }

    public WeaponDamage getDamage() {
        return damage;
    }
    public WeaponDamage setDamage(WeaponDamage damage) {
        this.damage = damage;
    }
}