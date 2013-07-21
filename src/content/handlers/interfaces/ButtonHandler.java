package content.handlers.interfaces;

import model.Player;

public abstract interface ButtonHandler {

	public abstract boolean handleButton(Player player, int opcode, int interfaceId, int buttonId, int buttonId2);

}