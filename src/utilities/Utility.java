package utilities;

public int roll(int max) {
    Random r = new Random();
    return r.nextInt(max) + 1;
}

public enum WeaponDamage {LIGHT, MEDIUM, HEAVY}