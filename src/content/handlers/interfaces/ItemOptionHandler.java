package content.handlers.interfaces;

import model.Player;

public abstract interface ItemOptionHandler {

	public abstract boolean handleItemOption(Player player, int interfaceId, int index, int itemId);

}