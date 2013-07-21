package packet.handlers;

import model.Player;
import packet.Packet;
import packet.PacketHandler;

public class InterfacePackets extends PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] getPacketOpcodes() {
		return new int[] {95};
	}

}
