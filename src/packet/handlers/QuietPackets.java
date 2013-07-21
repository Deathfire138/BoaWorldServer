package packet.handlers;

import model.Player;
import packet.Packet;
import packet.PacketHandler;

public class QuietPackets extends PacketHandler {

	@Override
	public int[] getPacketOpcodes() {
		// TODO Auto-generated method stub
		return new int[] {56, 192, 183, 32};
	}

	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getId()) {
		case 56://ping packet
			break;
		case 192://clicking packet
			break;
		case 183://camera movement packet
			break;
		case 32://game window selection packet
			break;
			
		}
	}

}
