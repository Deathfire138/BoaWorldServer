package model;

import net.ActionSender;

public final class Equipment {
	
	public static final int SLOT_HELM = 0, SLOT_CAPE = 1, SLOT_AMULET = 2, SLOT_WEAPON = 3, SLOT_CHEST = 4, SLOT_SHIELD = 5, SLOT_LEGS = 7, SLOT_GLOVES = 9, SLOT_BOOTS = 10, SLOT_RING = 12, SLOT_ARROWS = 13;

	public static final String[] BONUSES = { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range", 
		"Strength", "Prayer", "Ranged Strength"
	};
	
	public static void refresh(Player player) {
		/*ActionSender.sendUpdateItems(player, 387, 28, 94, player.getEquipment().getItems());
		sendWeapon(player);*/
	}
	
	public static void sendWeapon(Player player) {
		/*if(player.getEquipment().get(3) == null) {
			ActionSender.sendSidebarInterface(player, 92, 128);
			ActionSender.sendString(player, "Unarmed", 92, 0);
			return;
		}*/
		/*int id = player.getEquipment().get(3).getId();
		String weapon = ItemDefinition.get(id).getName();
		
		if(WeaponUtils.DartCheck(id) || WeaponUtils.KnifeCheck(id)|| WeaponUtils.JavelinCheck(id)|| WeaponUtils.ThrownAxeCheck(id)) {
			ActionSender.sendSidebarInterface(player, 91, 128);
			ActionSender.sendString(player, weapon, 91, 0);
		} else if(WeaponUtils.ShortSwordCheck(id) || WeaponUtils.DaggerCheck(id)) {
			ActionSender.sendSidebarInterface(player, 89, 128);
			ActionSender.sendString(player, weapon, 89, 0);
		} else if (WeaponUtils.ScimitarCheck(id) || WeaponUtils.LongswordCheck(id)){
			ActionSender.sendSidebarInterface(player, 81, 128);
			ActionSender.sendString(player, weapon, 81, 0);
		} else if (WeaponUtils.TwoHandersCheck(id) || WeaponUtils.GodSwordCheck(id)){
			ActionSender.sendSidebarInterface(player, 82, 128);
			ActionSender.sendString(player, weapon, 82, 0);
		} else if (WeaponUtils.WhipCheck(id)) {
			ActionSender.sendSidebarInterface(player, 93, 128);
			ActionSender.sendString(player, weapon, 93, 0);
		} else if (WeaponUtils.isWand(id) || WeaponUtils.StavesCheck(id)) {
			ActionSender.sendSidebarInterface(player, 90, 128);
			ActionSender.sendString(player, weapon, 90, 0);
		} else if (WeaponUtils.MaceCheck(id) || WeaponUtils.isVerac(id)) {
			ActionSender.sendSidebarInterface(player, 88, 128);
			ActionSender.sendString(player, weapon, 88, 0);
		} else if (WeaponUtils.CrossbowCheck(id) || WeaponUtils.KarilCheck(id)) {
			ActionSender.sendSidebarInterface(player, 79, 128);
			ActionSender.sendString(player, weapon, 79, 0);
		} else if (WeaponUtils.isTorag(id) || WeaponUtils.GraniteMaulCheck(id) || WeaponUtils.WarHammerCheck(id)) {
			ActionSender.sendSidebarInterface(player, 93, 128);
			ActionSender.sendString(player, weapon, 93, 0);
		} else if (WeaponUtils.BowCheck(id) || WeaponUtils.SeercullCheck(id)) {
			ActionSender.sendSidebarInterface(player, 77, 128);
			ActionSender.sendString(player, weapon, 77, 0);
		} else if (WeaponUtils.HatchetCheck(id) || WeaponUtils.BattleAxeCheck(id) || WeaponUtils.isDharok(id)) {
			ActionSender.sendSidebarInterface(player, 75, 128);
			ActionSender.sendString(player, weapon, 75, 0);
		} else if (WeaponUtils.ClawCheck(id)) {
			ActionSender.sendSidebarInterface(player, 78, 128);
			ActionSender.sendString(player, weapon, 78, 0);
		} else if (WeaponUtils.SpearCheck(id)) {
			ActionSender.sendSidebarInterface(player, 85, 128);
			ActionSender.sendString(player, weapon, 85, 0);
		} else if (WeaponUtils.HalberdCheck(id)) {
			ActionSender.sendSidebarInterface(player, 84, 128);
			ActionSender.sendString(player, weapon, 84, 0);
		} else if (WeaponUtils.PickaxeCheck(id)) {
			ActionSender.sendSidebarInterface(player, 83, 128);
			ActionSender.sendString(player, weapon, 83, 0);
		} else if (WeaponUtils.ScytheCheck(id)) {
			ActionSender.sendSidebarInterface(player, 86, 128);
			ActionSender.sendString(player, weapon, 86, 0);
		} else {	
			ActionSender.sendSidebarInterface(player, 86, -1);
			ActionSender.sendString(player, weapon, -1, 0);
		}
		sendSpecials(player);*/
	}
	
	private static void sendSpecials(Player player) {
		/*int weaponId = player.getEquipment().get(3).getId();
		boolean specialWeapon = false;
		if (weaponId == 4151) {
			ActionSender.sendInterfaceConfig(player, 93, 10, false);
			specialWeapon = true;
		} else if (weaponId == 1215 || weaponId == 1231 || weaponId == 5680
				|| weaponId == 5698 || weaponId == 8872 || weaponId == 8874
				|| weaponId == 8876 || weaponId == 8878) {
			ActionSender.sendInterfaceConfig(player, 89, 12, false);
			specialWeapon = true;
		} else if (weaponId == 35 || weaponId == 1305 || weaponId == 4587
				|| weaponId == 6746 || weaponId == 11037 || weaponId == 11694 ||
				weaponId == 11696 || weaponId == 11698 || weaponId == 11700) {
			ActionSender.sendInterfaceConfig(player, 82, 12, false);
			specialWeapon = true;
		} else if (weaponId == 7158 || weaponId == 11694 || weaponId == 11696
				|| weaponId == 11698 || weaponId == 11700 || weaponId == 11730) {
			ActionSender.sendInterfaceConfig(player, 81, 12, false);
			specialWeapon = true;
		} else if (weaponId == 859 || weaponId == 861 || weaponId == 6724
				|| weaponId == 10284 || weaponId == 859 || weaponId == 11235) {
			ActionSender.sendInterfaceConfig(player, 77, 13, false);
			specialWeapon = true;
		} else if (weaponId == 8880) {
			ActionSender.sendInterfaceConfig(player, 79, 10, false);
			specialWeapon = true;
		} else if (weaponId == 3101) {
			ActionSender.sendInterfaceConfig(player, 78, 12, false);
			specialWeapon = true;
		} else if (weaponId == 1434 || weaponId == 11061 || weaponId == 10887) {
			ActionSender.sendInterfaceConfig(player, 88, 12, false);
			specialWeapon = true;
		} else if (weaponId == 1377 || weaponId == 6739) {
			ActionSender.sendInterfaceConfig(player, 75, 12, false);
			specialWeapon = true;
		} else if (weaponId == 4153) {
			ActionSender.sendInterfaceConfig(player, 76, 10, false);
			specialWeapon = true;
		} else if (weaponId == 3204) {
			ActionSender.sendInterfaceConfig(player, 84, 10, false);
			specialWeapon = true;
		}*/
	}
	
	public static int getBonus(Player player, int bonus) {
		/*int total = 0;
		for (Item item : player.getEquipment().getItems()) {
			if (item != null) {
				total += EquipmentDefinition.get(item.getId()).getBonus()[bonus];
			}
		}
		return total;*/
		return -1;
	}
	
	public static int[] getBonus(Player player) {
		/*int[] bonus = new int[12];
		for (Item item : player.getEquipment().getItems()) {
			if (item != null) {
				for(int j = 0; j < 12; j++) {
					bonus[j] += EquipmentDefinition.get(item.getId()).getBonus()[j];
				}
			}
		}
		return bonus;*/
		return new int[12];
	}
	
	public static int getWeight(Player player) {
		/*double weight = 0.0;
		for (Item item : player.getEquipment().getItems()) {
			if (item != null) {
				weight += ItemDefinition.get(item.getId()).getWeight();
			}
		}
		return (int) weight;*/
		return -1;
	}
	
	public static void sendBonus(final Player player) {
		/*int[] bonus = getBonus(player);
		int id = 108;
		for(int index = 0; index < bonus.length; index++) {
			if(id == 118 || id == 107) {
				id++;
			}
			String name = (Equipment.BONUSES[index] + ": " + (bonus[index] >= 0 ? "+" : "") + bonus[index]);
			ActionSender.sendString(player, name, 465, id++);
		}
		ActionSender.sendString(player, getWeight(player) +" kg", 465, 121);*/
	}
}
