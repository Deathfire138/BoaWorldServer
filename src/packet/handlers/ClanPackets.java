package packet.handlers;

import centralServerConnection.Connect;
import net.ActionSender;
import miscellaneous.Utils;
import model.Player;
import packet.Packet;
import packet.PacketHandler;

public class ClanPackets extends PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getId()) {
		case 244://join clan chat
			long name = packet.getLong();
			ActionSender.sendMessage(player, "Attempting to join channel...:clan:");
			Connect.requestToJoinClan(player, name);
			break;
		case 68:
			adjustRank(player, packet);
		}
	}

	public void adjustRank(Player player, Packet packet) {
		byte rank = packet.get();
		long name = packet.getLong2();
		System.out.println(Utils.longToPlayerName(name)+" set to rank "+rank);
		Connect.adjustRank(player, name, rank);
	}
	
	@Override
	public int[] getPacketOpcodes() {
		return new int[] {244, 68};
	}

}
