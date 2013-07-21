package boa.game.handlers.interfaces;

import model.Player;

public abstract interface TakeItemHandler {
	
	public abstract boolean handleTakeItem(Player player, int x, int y, int itemId);

}
