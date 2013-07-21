package model;

import java.util.Map;
import java.util.HashMap;

import java.io.RandomAccessFile;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel.MapMode;

public class EquipmentDefinition {

	private static Map<Integer, EquipmentDefinition> definitions = new HashMap<Integer, EquipmentDefinition>();

	public static EquipmentDefinition get(int id) {
		return definitions.get(id);
	}

	private final int equipmentId, walkEmote, runEmote, standEmote, slot;
	
	private final int[] bonus;
	
	private final int[][] requirements;
	
	private final boolean isFullBody, isFullHelm, isFullMask, isMelee, isRange, is2H;

	public EquipmentDefinition(int equipmentId, int walkEmote, int runEmote, int standEmote, int slot, int[] bonus, int[][] requirements, boolean isFullBody, boolean isFullHelm, boolean isFullMask, boolean isMelee, boolean isRange, boolean is2H) {
		this.equipmentId = equipmentId;
		this.walkEmote = walkEmote;
		this.runEmote = runEmote;
		this.standEmote = standEmote;
		this.slot = slot;
		this.bonus = bonus;
		this.requirements = requirements;
		this.isFullBody = isFullBody;
		this.isFullHelm = isFullHelm;
		this.isFullMask = isFullMask;
		this.isMelee = isMelee;
		this.isRange = isRange;
		this.is2H = is2H;
	}
	
	public static void init() {
		try {
			RandomAccessFile raf = new RandomAccessFile("./data/equipmentDefinitions.bin", "r");
			ByteBuffer buffer = raf.getChannel().map(MapMode.READ_ONLY, 0, raf.length());
			int size = buffer.getInt();
			for (int i = 0; i < size; i++) {
				int id = buffer.getShort();
				int equipmentId = buffer.getShort();
				int walkEmote = buffer.getShort();
				int runEmote = buffer.getShort();
				int standEmote = buffer.getShort();
				int slot = buffer.get();
				int[] bonus = new int[13];
				for(int j = 0; j < 13; j++) {
					bonus[j] = buffer.getShort();
				}
				int skills = buffer.get();
				int[][] requirements = null;
				if (skills != -1) {
					requirements = new int[skills][2];
					for (int j = 0; j < skills; j++) {
						requirements[j][0] = buffer.get();
						requirements[j][1] = buffer.get();
					}
				}
				definitions.put(id, new EquipmentDefinition(equipmentId, walkEmote, runEmote, standEmote, slot, bonus, requirements, (buffer.get() == 1), (buffer.get() == 1), (buffer.get() == 1), (buffer.get() == 1), (buffer.get() == 1), (buffer.get() == 1)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getEquipmentId() {
		return equipmentId;
	}

	public int getWalkEmote() {
		return walkEmote;
	}
	
	public int getRunEmote() {
		return runEmote;
	}
	
	public int getStandEmote() {
		return standEmote;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public int[] getBonus() {
		return bonus;
	}
	
	public boolean isFullBody() {
		return isFullBody;
	}
	
	public boolean isFullHelm() {
		return isFullHelm;
	}
	
	public boolean isFullMask() {
		return isFullMask;
	}
	
	public boolean isMelee() {
		return isMelee;
	}
	
	public boolean isRange() {
		return isRange;
	}
	
	public boolean is2H() {
		return is2H;
	}

	public int[][] getRequirements() {
		return requirements;
	}
}