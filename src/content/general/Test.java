package content.general;

import net.ActionSender;
import model.Player;
import content.handlers.Handler;
import content.handlers.interfaces.ButtonHandler;

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
