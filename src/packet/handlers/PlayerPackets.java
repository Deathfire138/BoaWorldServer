package packet.handlers;

import model.Player;
import packet.Packet;
import packet.PacketHandler;

public class PlayerPackets extends PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getId()) {
		case 185:
			break;
		}
	}

	@Override
	public int[] getPacketOpcodes() {
		return new int[] {185};
	}

}
