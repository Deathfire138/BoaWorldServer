package packet.handlers;

import content.Content;
import model.Player;
import packet.Packet;
import packet.PacketHandler;

public class ObjectPackets extends PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getId()) {
		case 50://Object option 1
			break;
		case 28://Object option 2
			break;
		case 67://Object option 3
			break;
		case 88://Examine object
			examine(player, packet);
			break;
		}
		
	}

	private void examine(Player player, Packet packet) {
		int objectId = packet.getShortA();
		//Content.examine(player, objectId);
	}
	
	@Override
	public int[] getPacketOpcodes() {
		return new int[] {50, 88};
	}

}
