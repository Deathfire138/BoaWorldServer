package boa.game.general;

import boa.game.handlers.Handler;
import boa.game.handlers.interfaces.ButtonHandler;
import net.ActionSender;
import model.Player;

public class Test extends Handler implements ButtonHandler {

	@Override
	public boolean handleButton(Player player, int opcode, int interfaceId,
			int buttonId, int buttonId2) {
		System.out.println("lolseventy");
		ActionSender.sendMessage(player, "You pressed button "+buttonId+" on interface "+interfaceId);
		return true;
	}

	@Override
	public Object getObject() {
		return this;
	}

}
