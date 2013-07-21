package content.handlers.interfaces;

import model.Player;

public abstract interface ObjectExamineHandler {

	public abstract boolean examineObject(Player player, int objectId);
	
}
