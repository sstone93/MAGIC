package model;

public class Monster {
    Clearing location;
    int      length = 0; // represents length of the tooth/claw
    // todo: put weight, in enum

    public void move(Clearing location) {

    }

    // also includes blocking a player
    public void appear() {

    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}