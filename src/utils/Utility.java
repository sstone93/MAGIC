package utils;
import java.util.Random;

public class Utility {
    public int roll(int max) {
        Random r = new Random();
        return r.nextInt(max) + 1;
    }
    
    public enum ItemWeight {LIGHT, MEDIUM, HEAVY}
    
    //new enum to represent the names of the tiles
    public enum TileName {AWFULVALLEY, BADVALLEY, BORDERLAND, CAVERN, CAVES, CLIFF, CRAG, CURSTVALLEY,
    	DARKVALLEY, DEEPWOODS, EVILVALLEY, HIGHPASS, LEDGES, LINDENWOODS, MAPLEWOODS, MOUNTAIN, NUTWOODS,
    	OAKWOODS, PINEWOODS, RUINS
    }
}