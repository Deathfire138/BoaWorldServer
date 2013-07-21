package boa.game.handlers.interfaces;

import model.Player;

public abstract interface EquipHandler {
	
	public abstract boolean handleEquip(Player player, int interfaceId, int index, int itemId);

}
