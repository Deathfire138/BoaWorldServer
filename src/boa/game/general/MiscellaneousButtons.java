package boa.game.general;

import boa.game.handlers.interfaces.ButtonHandler;
import boa.game.handlers.types.StandardHandler;
import boa.io.ActionSender;
import model.Player;

public class MiscellaneousButtons extends StandardHandler implements ButtonHandler {

	@Override
	public boolean handleButton(Player player, int opcode, int interfaceId, int buttonId, int buttonId2) {
		if (interfaceId == 182 && buttonId == 6) {
			ActionSender.sendLogout(player);
			return true;
		}
		return false;
	}

	@Override
	public Object getObject() {
		return this;
	}

}
