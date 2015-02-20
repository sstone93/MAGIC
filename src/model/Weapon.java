package model;

import utils.Utility.*;

public class Weapon {
    WeaponName  type;   // it's name
    boolean ranged;
    boolean active;
    String  attack; // the method of attack
    int     length; // on a scale of 0 - 18
    int     speed;
    int     sharpness; // higher = more harm inflicted
    ItemWeight weight;
    ItemWeight damage;

    Weapon(WeaponName n) {
       this.type = n;
       this.weight = ItemWeight.NEGLIGIBLE;
       this.active = true;
    }

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
    public void setAttack(String attack) {
        this.attack = attack;
    }
    public String getAttack() {
        return attack;
    }
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
      this.length = length;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void setSharpness(int sharp) {
        sharpness = sharp;
    }
    public int getSharpness() {
        return sharpness;
    }
    public ItemWeight getWeight() {
        return weight;
    }
    public void setWeight(ItemWeight weight) {
        this.weight = weight;
    }
    public ItemWeight getDamage() {
        return damage;
    }
    public void setDamage(ItemWeight damage) {
        this.damage = damage;
    }
    public String toString(){
    	return this.type.toString();
    }
}