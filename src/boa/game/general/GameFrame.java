package boa.game.general;

import boa.game.handlers.interfaces.ButtonHandler;
import boa.game.handlers.interfaces.LoginHandler;
import boa.game.handlers.types.StandardHandler;
import model.Player;

public class GameFrame extends StandardHandler implements ButtonHandler, LoginHandler {

	@Override
	public boolean handleButton(Player player, int opcode, int interfaceId,	int buttonId, int buttonId2) {
		if (interfaceId == 548) {
			return false;//set to true when handled
		} else {
			return false;
		}
	}

	@Override
	public Object getObject() {
		return this;
	}

	@Override
	public void handleLogin(Player player) {

	}

}
