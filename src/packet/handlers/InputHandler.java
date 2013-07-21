package packet.handlers;

import event.InputEvent;
import miscellaneous.Utils;
import model.Player;
import packet.Packet;
import packet.PacketHandler;

public class InputHandler extends PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getId()) {
		case 179:
			System.out.println("INPUT EVENT BEIN ALL CRAZY");
			String string = Utils.longToPlayerName(packet.getLong());
			((InputEvent) player.getTemporary("INPUT_EVENT")).input(string);
			player.removeTemporary("INPUT_EVENT");
			break;
		}
	}

	@Override
	public int[] getPacketOpcodes() {
		return new int[] {179};
	}

}
