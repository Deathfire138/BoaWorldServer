package packet.handlers;

import content.Content;
import content.handlers.interfaces.ButtonHandler;
import model.Player;
import packet.Packet;
import packet.PacketHandler;

public class ButtonPackets extends PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		System.out.println("Button, opcode = "+packet.getId());
			int interfaceId = packet.getShort() & 0xFFFF;
			int buttonId = packet.getShort() & 0xFFFF;
			int buttonId2 = 0;
			if(packet.getSize() >= 6) {
				buttonId2 = packet.getShort() & 0xFFFF;
			}
			Content.button(player, packet.getId(), interfaceId, buttonId, buttonId2);
			
			System.out.println("interfaceId = "+interfaceId+", buttonId = "+buttonId+", buttonId2 = "+buttonId2);
	}

	@Override
	public int[] getPacketOpcodes() {
		return new int[] {180, 230, 205, 127, 211, 203, 39, 187, 156, 128};
	}

}
