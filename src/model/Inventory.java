package model;

import boa.io.ActionSender;

public final class Inventory {
	
	public static boolean addItem(Player player, int id, int amount) {
		//if (ItemDefinition.get(id) == null || ItemDefinition.get(id).isStackable()) {
			Item item = new Item(id, amount);
			int slot = player.getInventory().add(item);
			if (slot == -1) {
				ActionSender.sendMessage(player, "Not enough space in your inventory.");
				return false;
			}
			ActionSender.sendUpdateItem(player, 149, 0, 93, slot, player.getInventory().get(slot));
				return true;
		/*} else {
			for (int i = 0; i < amount; i++) {
				Item item = new Item(id, 1);
				int slot = player.getInventory().add(item);
				if (slot == -1) {
					ActionSender.sendMessage(player, "Not enough space in your inventory.");
					return false;
				}
				ActionSender.sendUpdateItem(player, 149, 0, 93, slot, player.getInventory().get(slot));
			}
			return true;
		}*/
	}
	
	public static boolean addItems(Player player, int[] items, int[] amounts) {
		for(int i = 0; i < items.length; i++) {
			if (!addItem(player, items[i], amounts[i])) {
				return false;
			}
		}
		return true;
	}

	public static boolean contains(Player player, int item, int amount) {
		return contains(player, new Item(item, amount));
	}
	
	public static boolean contains(Player player, Item item) {
		return player.getInventory().contains(item);
	}

	public static void deleteItem(Player player, int item, int amount) {
		player.getInventory().remove(new Item(item, amount));
		refresh(player);
	}

	public static void refresh(Player player) {
		ActionSender.sendUpdateItems(player, 149, 0, 93, player.getInventory().getItems());
	}
	
	public static void refresh(Player player, int slot) {
		ActionSender.sendUpdateItem(player, 149, 0, 93, slot, player.getInventory().get(slot));
	}

	public static boolean hasRoomFor(Player player, int id, int amount) {
		//if (ItemDefinition.get(id).isStackable()) {
			return player.getInventory().freeSlots() >= 1 || player.getInventory().contains(new Item(id, amount));
		/*} else {
			return player.getInventory().freeSlots() >= amount;
		}*/
	}

	public static void clear(Player player) {
		player.getInventory().clear();
		refresh(player);
	}
}