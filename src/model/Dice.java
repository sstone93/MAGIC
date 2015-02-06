package model;
import java.util.Random;

public class Dice {
    public int roll(int max) {
        Random r = new Random();
        return r.nextInt(max) + 1;
    }
}