package utils;
import java.util.Random;

public class Utility {
    public static int roll(int max) {
        Random r = new Random();
        return r.nextInt(max) + 1;
    }

    public static ItemWeight getItemWeight(String weight) {
        switch(weight){
            case "NEGLIGIBLE": return Utility.ItemWeight.NEGLIGIBLE;
            case "LIGHT": return Utility.ItemWeight.LIGHT;
            case "MEDIUM": return Utility.ItemWeight.MEDIUM;
            case "HEAVY": return Utility.ItemWeight.HEAVY;
            default: return Utility.ItemWeight.NEGLIGIBLE;
        }
    }

    // checking: the one you're checking to see if it has a heavier weight
    public static boolean isWeightHeavier(ItemWeight checking, ItemWeight against) {
        if (checking == Utility.ItemWeight.NEGLIGIBLE)
            return false;

        if (checking == Utility.ItemWeight.HEAVY)
            return true;

        if (checking == Utility.ItemWeight.LIGHT) {
            if (against == Utility.ItemWeight.NEGLIGIBLE)
                return true;
            else
                return false;
        }
        if (checking == Utility.ItemWeight.MEDIUM) {
            if (against == Utility.ItemWeight.HEAVY)
                return false;
            else
                return true;
        }
        return false;
    }

    public static String getTileImage(TileName name) {
    	switch(name){
	    	case AWFULVALLEY: return "/images/awfulvalley1.gif";
	    	case BADVALLEY: return "/images/badvalley1.gif";
	    	case BORDERLAND: return "/images/borderland1.gif";
	    	case CAVERN: return "/images/cavern1.gif";
	    	case CAVES: return "/images/caves1.gif";
	    	case CLIFF: return "/images/cliff1.gif";
	    	case CRAG: return "/images/crag1.gif";
	    	case CURSTVALLEY: return "/images/curstvalley1.gif";
	    	case DARKVALLEY: return "/images/darkvalley1.gif";
	    	case DEEPWOODS: return "/images/deepwoods1.gif";
	    	case EVILVALLEY: return "/images/evilvalley1.gif";
	    	case HIGHPASS: return "/images/highpass1.gif";
	    	case LEDGES: return "/images/ledges1.gif";
	    	case LINDENWOODS: return "/images/lindenwoods1.gif";
	    	case MAPLEWOODS: return "/images/maplewoods1.gif";
	    	case MOUNTAIN: return "/images/mountain1.gif";
	    	case NUTWOODS: return "/images/nutwoods1.gif";
	    	case OAKWOODS: return "/images/oakwoods1.gif";
	    	case PINEWOODS: return "/images/pinewoods1.gif";
	    	case RUINS: return "/images/ruins1.gif";
	    	default: return null;
    	}
    }
    
    public static String getCharacterImage(CharacterName name) {
    	switch(name){
	    	case AMAZON: return "/images/amazon.gif";
	    	case BERSERKER: return "/images/berserker.gif";
	    	case BLACK_KNIGHT: return "/images/black_knight.gif";
	    	case CAPTAIN: return "/images/captain.gif";
	    	case DWARF: return "/images/dwarf.gif";
	    	case ELF: return "/images/elf.gif";
	    	case SWORDSMAN: return "/images/swordsman.gif";
	    	case WHITE_KNIGHT: return "/images/white_knight.gif";
	    	default: return null;
    	}
    }

    public static String getGarrisonImage(GarrisonName name) {
    	switch(name){
	    	case CHAPEL: return "/images/chapel.gif";
	    	case HOUSE: return "/images/house.gif";
	    	case INN: return "/images/inn.gif";
	    	case GUARD: return "/images/guard.gif";
	    	default: return null;
    	}
    }

    public enum ClearingType {CAVE, MOUNTAIN, WOODS}
    
    public enum TileType {VALLEY, MOUNTAINS, CAVES, WOODS}
    
    public enum PathType {TUNNEL, OPEN_ROAD, HIDDEN_PATH, SECRET_PASSAGEWAY}
    
    public enum WarningChits {SMOKE, DANK, RUINS, STINK, BONES}

    public enum SoundChits {HOWL_4, HOWL_5, FLUTTER_1, FLUTTER_2, PATTER_2, PATTER_5, ROAR_4, ROAR_6, SLITHER_3, SLITHER_6}
    
    public enum MessageType {COMBAT_TARGET, COMBAT_MOVES, CHARACTER_SELECT, ACTIVITIES}

    public enum ItemWeight {NEGLIGIBLE, LIGHT, MEDIUM, HEAVY, TREMENDOUS}

    public enum TileName {AWFULVALLEY, BADVALLEY, BORDERLAND, CAVERN, CAVES, CLIFF, CRAG, CURSTVALLEY,
    	DARKVALLEY, DEEPWOODS, EVILVALLEY, HIGHPASS, LEDGES, LINDENWOODS, MAPLEWOODS, MOUNTAIN, NUTWOODS,
    	OAKWOODS, PINEWOODS, RUINS
    }

    public enum GarrisonName {CHAPEL, HOUSE, INN, GUARD}

    public enum WeaponName {MEDIUM_BOW, LIGHT_BOW, CROSSBOW, SPEAR, STAFF, GREAT_SWORD, BANE_SWORD, BROADSWORD,
    	DEVIL_SWORD, TRUESTEEL_SWORD, MORNING_STAR, GREAT_AXE, THRUSTING_SWORD, LIVING_SWORD, SHORT_SWORD,
    	AXE, MACE, DAGGER, TOOTH_AND_CLAW}

    public enum ArmourName {SUIT_OF_ARMOR, BREASTPLATE, HELMET, SHIELD, TREMENDOUS_ARMOR, SILVER_BREASTPLATE,
    	GOLD_HELMET, JADE_SHIELD}

    public enum CharacterName {AMAZON, BERSERKER, BLACK_KNIGHT, CAPTAIN, DWARF, ELF, SWORDSMAN, WHITE_KNIGHT}

    public enum HorseName {PONY, WORKHORSE, WARHORSE}

    public enum MonsterName {TREMENDOUS_FLYING_DRAGON, T_FLYING_DRAGON_HEAD, TREMENDOUS_DRAGON, T_DRAGON_HEAD,
    	GIANT, GIANT_CLUB, WINGED_DEMON, DEMON, TREMENDOUS_TROLL, OCTOPUS, TREMENDOUS_SERPENT, TREMENDOUS_SPIDER,
    	HEAVY_FLYING_DRAGON, HEAVY_DRAGON, HEAVY_TROLL, HEAVY_SERPENT, GIANT_BAT, HEAVY_SPIDER, IMP, GOBLIN_WITH_SPEAR,
    	GOBLIN_WITH_GREATSWORD, GOBLIN_WITH_AXE, VIPER, GHOST, OGRE, WOLF}

    public enum NativeGroupName {COMPANY, BASHKARS, ROGUES, GUARD, ORDER, LANCERS, WOODFOLK, PATROL, SOLDIERS}

    public enum NativeName {KNIGHT, GREAT_SWORDSMAN, GREAT_AXEMAN, PIKEMAN, SHORT_SWORDSMAN, CROSSBOWMAN, LANCER,
    	RAIDER, ARCHER, SWORDSMAN, ASSASSIN}

    public enum TreasureType{SMALL, LARGE, TREASURE_WITHIN_TREASURE}
    
    public enum TreasureWithinTreasureName {CHEST, CRYPT_OF_THE_KNIGHT, ENCHANTED_MEADOW, MOULDY_SKELETON,
    	REMAINS_OF_THIEF, TOADSTOOL_CIRCLE}

    public enum LargeTreasureName{BATTLE_BRACELETS, BEJEWELED_DWARF_VEST, BELT_OF_STRENGTH, BLASTED_JEWEL,
    	CRYSTAL_BALL, ENCHANTERS_SKULL, EYE_OF_THE_IDOL, EYE_OF_THE_MOON, FLYING_CARPET, GARB_OF_SPEED,
    	GIRTLE_OF_ENERGY, GLIMMERING_RING, GLOWING_GEM, GOLDEN_ARM_BAND, GOLDEN_CROWN, GOLDEN_ICON,
    	HIDDEN_RING, IMPERIAL_TABARD, LUCKY_CHARM, MAGIC_WAND, REGENT_OF_JEWELS, SACRED_GRAIL, TIMELESS_JEWEL}

    public enum SmallTreasureName{LEAGUE_BOOTS_7, ALCHEMISTS_MIXTURE, AMULET, ANCIENT_TELESCOPE, BEAST_PIPES, BLACK_BOOK,
    	BOOK_OF_LORE, CLOAK_OF_MIST, CLOVEN_HOOF, DEFT_GLOVES, DRAGON_ESSENCE, DRAGONFANG_NECKLACE, DRAUGHT_OF_SPEED,
    	ELUSIVE_CLOAK, ELVEN_SLIPPERS, FLOWERS_OF_REST, GLOVES_OF_STRENGTH, GOOD_BOOK, GRIPPING_DUST, HANDY_GLOVES,
    	LOST_KEYS, MAGIC_SPECTACLES, MAP_OF_LOST_CASTLE, MAP_OF_LOST_CITY, MAP_OF_RUINS, OIL_OF_POISON, OINTMENT_OF_BITE,
    	OINTMENT_OF_STEEL, PENETRATING_GREASE, PHANTOM_GLASS, POTION_OF_ENERGY, POULTICE_OF_HEALTH, POWER_BOOTS,
    	POWER_GAUNTLETS, QUICK_BOOTS, REFLECTION_GREASE, ROYAL_SCEPTRE, SACRED_STATUE, SCROLL_OF_ALCHEMY, SCROLL_OF_NATURE,
    	SHIELDED_LANTERN, SHOES_OF_STEALTH, TOADSTOOL_RING, VIAL_OF_HEALING, WITHERED_CLAW}

    public enum TreasureLocations{ HOARD, LAIR, ALTAR, SHRINE, POOL, VAULT, CAIRNS, STATUE}

    public enum Actions {MOVE, HIDE, ALERT, REST, SEARCH, TRADE, FOLLOW}

    public enum Attacks {THRUST, SWING, SMASH}

    public enum Maneuvers {CHARGE, DODGE, DUCK}

    public enum Defenses {AHEAD, SIDE, ABOVE}

    public enum GameState {CHOOSE_CHARACTER, CHOOSE_PLAYS, CHOOSE_COMBATTARGET, CHOOSE_COMBATMOVES, NULL}
}
