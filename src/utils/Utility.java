package utils;
import java.util.Random;

public class Utility {
    public int roll(int max) {
        Random r = new Random();
        return r.nextInt(max) + 1;
    }
    
    public enum ItemWeight {LIGHT, MEDIUM, HEAVY}
}