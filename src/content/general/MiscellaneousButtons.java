package content.general;

import net.ActionSender;
import model.Player;
import content.handlers.Handler;
import content.handlers.interfaces.ButtonHandler;

public class MiscellaneousButtons extends Handler implements ButtonHandler {

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
