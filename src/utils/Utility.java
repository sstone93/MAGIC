package utils;
import java.util.Random;

import model.Player;

public class Utility {
	
	
    public static int roll(int max) {
        Random r = new Random();
        return r.nextInt(max) + 1;
    }
    
    public static int randomInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt(max-min) + min;
    }

    // checks to see if the player has a treasure that allows them to roll only one die on the table
    // returns true if they can
    // returns false if they don't have a roll reducing treasure
    public static boolean checkRollOneDie(Player player, String table) {

    	boolean onlyOne = false;

    	if (player.hasTreasure(LargeTreasureName.LUCKY_CHARM.toString()))
    		onlyOne = true;
    	else if (player.hasTreasure(SmallTreasureName.SHOES_OF_STEALTH.toString()) && table == "hide")
    		onlyOne = true;
    	else if (player.hasTreasure(SmallTreasureName.DEFT_GLOVES.toString()) && table == "loot")
    		onlyOne = true;
    	else if (player.getCharacter().getName() == CharacterName.ELF && table == "missile")
    		onlyOne = true;
    	else if (player.getCharacter().getName() == CharacterName.DWARF &&( table == "hide" || table == "locate"))
    		onlyOne = true;
    	return onlyOne;
    	
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
    	
    	int c=0;
    	int a=0;
    	
    	if(checking == ItemWeight.LIGHT)
    		c=1;
    	if(checking == ItemWeight.MEDIUM)
    		c=2;
    	if(checking == ItemWeight.HEAVY)
    		c=3;
    	if(checking == ItemWeight.TREMENDOUS)
    		c=4;
    	
    	if(against == ItemWeight.LIGHT)
    		a=1;
    	if(against == ItemWeight.MEDIUM)
    		a=2;
    	if(against == ItemWeight.HEAVY)
    		a=3;
    	if(against == ItemWeight.TREMENDOUS)
    		a=4;
    	
    	if(c > a){
    		return true;
    	}else{
    		return false;
    	}

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
    
    public static String getCharacterDetailImage(CharacterName name) {
    	switch(name){
	    	case AMAZON: return "/images/amazonDetail.jpg";
	    	case BERSERKER: return "/images/berserkerDetail.jpg";
	    	case BLACK_KNIGHT: return "/images/black_knightDetail.jpg";
	    	case CAPTAIN: return "/images/captainDetail.jpg";
	    	case DWARF: return "/images/dwarfDetail.jpg";
	    	case ELF: return "/images/elfDetail.jpg";
	    	case SWORDSMAN: return "/images/swordsmanDetail.jpg";
	    	case WHITE_KNIGHT: return "/images/white_knightDetail.jpg";
	    	default: return null;
    	}
    }

    public static GarrisonName[] getCharacterStartingLocations(CharacterName name) {
    	switch(name){
	    	case AMAZON: return new GarrisonName []{GarrisonName.INN};
	    	case BERSERKER: return new GarrisonName []{GarrisonName.INN};
	    	case BLACK_KNIGHT: return new GarrisonName []{GarrisonName.INN};
	    	case CAPTAIN: return new GarrisonName []{GarrisonName.INN, GarrisonName.GUARD};
	    	case DWARF: return new GarrisonName []{GarrisonName.INN, GarrisonName.GUARD};
	    	case ELF: return new GarrisonName []{GarrisonName.INN};
	    	case SWORDSMAN: return new GarrisonName []{GarrisonName.INN};
	    	case WHITE_KNIGHT: return new GarrisonName []{GarrisonName.INN, GarrisonName.CHAPEL};
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
    
    public static String getMonsterImage (MonsterName name){
    	switch(name){
    		case GHOST: return "/images/ghost.gif";
    		case GIANT: return "/images/giant.gif";
    		case HEAVY_DRAGON: return "/images/dragon.gif";
    		case HEAVY_TROLL: return "/images/troll.gif";
    		case VIPER: return "/images/serpent.gif";
    		case WOLF: return "/images/wolf.gif";
    		case HEAVY_SPIDER: return "/images/spider.gif";
    		case GIANT_BAT: return "/images/bat.gif";
    		default: return null;
    	}
    }
    
    public static String getSoundImage(SoundChits name){
    	switch(name){
    		case HOWL_4: return "/images/howl4.jpg";
    		case HOWL_5: return "/images/howl5.jpg";
    		case FLUTTER_1: return "/images/flutter1.jpg";
    		case FLUTTER_2: return "/images/flutter2.jpg";
    		case PATTER_2: return "/images/patter2.jpg";
    		case PATTER_5: return "/images/patter5.jpg";
    		case ROAR_4: return "/images/roar4.jpg";
    		case ROAR_6: return "/images/roar6.jpg";
    		case SLITHER_3: return "/images/slither3.jpg";
    		case SLITHER_6: return "/images/slither6.jpg";
    		default: return null;
    	}
    }
    
    public static String getSiteImage(TreasureLocations name){
    	switch(name){
    		case HOARD: return "/images/hoard.jpg";
    		case LAIR: return "/images/lair.jpg";
    		case ALTAR: return "/images/altar.jpg";
    		case SHRINE: return "/images/shrine.jpg";
    		case POOL: return "/images/pool.jpg";
    		case VAULT: return "/images/vault.jpg";
    		case CAIRNS: return "/images/cairns.jpg";
    		case STATUE: return "/images/statue.jpg";
    		default: return null;
    	}
    }
    
    public static String getWarningImage(WarningChits name, TileType type){
    	String img = "";
    	
    	switch(name){
	    	case SMOKE:
	    		img += "/images/smoke";
	    		break;
	    	case DANK:
	    		img += "/images/dank";
	    		break;
	    	case RUINS:
	    		img += "/images/ruins";
	    		break;
	    	case STINK:
	    		img += "/images/stink";
	    		break;
	    	case BONES:
	    		img += "/images/bones";
	    		break;
	    	default:
	    		return null;
    	}
    	
    	switch(type){
    		case VALLEY:
    			img += "v.jpg";
    			break;
    		case MOUNTAINS:
    			img += "m.jpg";
    			break;
    		case CAVES:
    			img += "c.jpg";
    			break;
    		case WOODS:
    			img += "w.jpg";
    			break;
    		default:
    			return null;
    	}
    	return img;
    }
    
    public enum PhaseType {BASIC, SUNLIGHT, SPECIAL, TREASURE}

    public enum LostName {LOST_CITY, LOST_CASTLE};
    
    public enum ClearingType {CAVE, MOUNTAIN, WOODS}

    public enum TileType {VALLEY, MOUNTAINS, CAVES, WOODS}

    public enum PathType {TUNNEL, OPEN_ROAD, HIDDEN_PATH, SECRET_PASSAGEWAY}

    public enum WarningChits {SMOKE, DANK, RUINS, STINK, BONES}

    public enum SoundChits {HOWL_4, HOWL_5, FLUTTER_1, FLUTTER_2, PATTER_2, PATTER_5, ROAR_4, ROAR_6, SLITHER_3, SLITHER_6, TREASURE_SITE}

    public enum MessageType {COMBAT_TARGET, COMBAT_MOVES, CHARACTER_SELECT, ACTIVITIES, BOARD, PLAYER, BLOCK}

    public enum ItemWeight {NEGLIGIBLE, LIGHT, MEDIUM, HEAVY, TREMENDOUS}

    public enum TileName {AWFULVALLEY, BADVALLEY, BORDERLAND, CAVERN, CAVES, CLIFF, CRAG, CURSTVALLEY,
    	DARKVALLEY, DEEPWOODS, EVILVALLEY, HIGHPASS, LEDGES, LINDENWOODS, MAPLEWOODS, MOUNTAIN, NUTWOODS,
    	OAKWOODS, PINEWOODS, RUINS
    }

    public enum GarrisonName {CHAPEL, HOUSE, INN, GUARD}

    public enum WeaponName {MEDIUM_BOW, LIGHT_BOW, CROSSBOW, SPEAR, STAFF, GREAT_SWORD, BANE_SWORD, BROADSWORD,
    	DEVIL_SWORD, TRUESTEEL_SWORD, MORNING_STAR, GREAT_AXE, THRUSTING_SWORD, LIVING_SWORD, SHORT_SWORD,
    	AXE, MACE, DAGGER, TOOTH_AND_CLAW, FIST}

    public enum ArmourName {SUIT_OF_ARMOR, BREASTPLATE, HELMET, SHIELD, TREMENDOUS_ARMOR, SILVER_BREASTPLATE,
    	GOLD_HELMET, JADE_SHIELD}

    public enum CharacterName {AMAZON, BERSERKER, BLACK_KNIGHT, CAPTAIN, DWARF, ELF, SWORDSMAN, WHITE_KNIGHT}

    public enum MonsterName {TREMENDOUS_FLYING_DRAGON, T_FLYING_DRAGON_HEAD, TREMENDOUS_DRAGON, T_DRAGON_HEAD,
    	GIANT, GIANT_CLUB, WINGED_DEMON, DEMON, TREMENDOUS_TROLL, OCTOPUS, TREMENDOUS_SERPENT, TREMENDOUS_SPIDER,
    	HEAVY_FLYING_DRAGON, HEAVY_DRAGON, HEAVY_TROLL, HEAVY_SERPENT, GIANT_BAT, HEAVY_SPIDER, IMP, GOBLIN_WITH_SPEAR,
    	GOBLIN_WITH_GREATSWORD, GOBLIN_WITH_AXE, VIPER, GHOST, OGRE, WOLF}

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

    public enum Actions {MOVE, HIDE, ALERT, REST, SEARCH, TRADE, PASS}

    public enum SearchTables {LOCATE, LOOT}

    public enum Attacks {THRUST, SWING, SMASH}

    public enum Maneuvers {CHARGE, DODGE, DUCK, RUN}

    public enum Defenses {AHEAD, SIDE, ABOVE}

    public enum GameState {CHOOSE_CHARACTER, CHOOSE_PLAYS, CHOOSE_COMBATTARGET, CHOOSE_COMBATMOVES, NULL}
}
