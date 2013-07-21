package model;

import model.UpdateFlags.UpdateFlag;
import net.ActionSender;

public class Skills {

	private final Player player;

	public static final int SKILL_COUNT = 24;

	public static final double MAXIMUM_EXP = 200000000;

	public static final String[] SKILL_NAME = {"Attack", "Defence",
			"Strength", "Hitpoints", "Range", "Prayer", "Magic", "Cooking",
			"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
			"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
			"Farming", "Runecrafting", "Construction", "Hunter", "Summoning"};

	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
			HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6, COOKING = 7,
			WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
			CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15,
			AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19,
			RUNECRAFTING = 20, CONSTRUCTION = 21, HUNTER = 22, SUMMONING = 23;

	private final int[] levels = new int[SKILL_COUNT];

	private final double[] exps = new double[SKILL_COUNT];

	public Skills(Player player) {
		this.player = player;
		for (int i = 0; i < SKILL_COUNT; i++) {
			levels[i] = 1;
			exps[i] = 0;
		}
		levels[3] = 10;
		exps[3] = 1154;
	}

	public int getTotalLevel() {
		int total = 0;
		for (int i = 0; i < levels.length; i++) {
			total += getLevelForExperience(i);
		}
		return total;
	}
	
	public int getTotalXp() {
		double total = 0;
		for (int i = 0; i < levels.length; i++) {
			total += exps[i];
		}
		return (int) total;
	}

	public int getCombatLevel() {
		final int attack = getLevelForExperience(0);
		final int defence = getLevelForExperience(1);
		final int strength = getLevelForExperience(2);
		final int hp = getLevelForExperience(3);
		final int prayer = getLevelForExperience(5);
		final int ranged = getLevelForExperience(4);
		final int magic = getLevelForExperience(6);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.2535) + 1;
		final double melee = (attack + strength) * 0.325;
		final double ranger = Math.floor(ranged * 1.5) * 0.325;
		final double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		if (combatLevel <= 126) {
			return combatLevel;
		} else {
			return 126;
		}
	}

	public void setSkill(int skill, int level, double exp) {
		levels[skill] = level;
		exps[skill] = exp;
	}

	public void setLevel(int skill, int level) {
		levels[skill] = level;
		exps[skill] = getXPForLevel(level);
		ActionSender.sendSkill(player, skill);
	}
	
	public void setExperience(int skill, double exp) {
		final int oldLvl = getLevelForExperience(skill);
		exps[skill] = exp;
		ActionSender.sendSkill(player, skill);
		final int newLvl = getLevelForExperience(skill);
		if (oldLvl != newLvl) {
			player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		}
	}

	public void incrementLevel(int skill) {
		levels[skill]++;
		ActionSender.sendSkill(player, skill);
	}
	
	public void incrementLevel(int skill, int amount) {
		levels[skill] += amount;
		ActionSender.sendSkill(player, skill);
	}
	
	public void heal(int amount) {
		levels[3] += amount;
		if (getLevelForExperience(3) < levels[3]) {
			levels[3] = getLevelForExperience(3);
		}
		ActionSender.sendSkill(player, 3);
	}

	public void decrementLevel(int skill) {
		levels[skill]--;
		ActionSender.sendSkill(player, skill);
	}

	public void detractLevel(int skill, int amount) {
		if (levels[skill] == 0) {
			amount = 0;
		}
		if (amount > levels[skill]) {
			amount = levels[skill];
		}
		levels[skill] = levels[skill] - amount;
		ActionSender.sendSkill(player, skill);
	}

	public void normalizeLevel(int skill) {
		final int norm = getLevelForExperience(skill);
		if (levels[skill] > norm) {
			levels[skill]--;
			ActionSender.sendSkill(player, skill);
		} else if (levels[skill] < norm) {
			levels[skill]++;
			ActionSender.sendSkill(player, skill);
		}
	}

	public void restoreLevel(final int skill) {
		final int norm = getLevelForExperience(skill);
		if (levels[skill] < norm) {
			levels[skill] = norm;
			ActionSender.sendSkill(player, skill);
		}
	}

	public int getLevel(int skill) {
		return levels[skill];
	}

	public int getLevelForExperience(int skill) {
		double exp = exps[skill];
		int points = 0;
		int output = 0;
		for(int lvl = 1; lvl < 100; lvl++) {
			points += Math.floor((double)lvl + 300.0 * Math.pow(2.0, (double)lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if((output - 1) >= exp) {
				return lvl;
			}
		}
		return 99;
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public double getExperience(int skill) {
		return exps[skill];
	}
	
	public void addExperience(int skill, double exp) {
		addExperience(skill, exp, true);
	}

	public void addExperience(int skill, double exp, boolean boost) {
		final int oldLevel = getLevelForExperience(skill);
		if (boost) {
			exps[skill] += ((double)exp * 1.5);
		} else {
			exps[skill] += exp;
		}
		if (exps[skill] > MAXIMUM_EXP) {
			exps[skill] = MAXIMUM_EXP;
		}
		final int newLevel = getLevelForExperience(skill);
		final int levelDiff = newLevel - oldLevel;
		if (levelDiff > 0) {
			levels[skill] += levelDiff;
			//LevelUp.level(player, skill);
			player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		}
		ActionSender.sendSkill(player, skill);
	}
}