package boa.game.handlers.interfaces;

import model.Player;

public abstract interface ItemOnItemHandler {

	public abstract boolean handleItemOnItem(Player player, int interfaceId, int usedId, int usedSlot, int withId, int withSlot);
	
}
