package content.general;

import model.Player;
import content.handlers.Handler;
import content.handlers.interfaces.ButtonHandler;

public class GameFrame extends Handler implements ButtonHandler {

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

}
