package packet.handlers;

import net.ActionSender;
import centralServerConnection.Connect;
import miscellaneous.Utils;
import model.Player;
import packet.Packet;
import packet.PacketHandler;

public class FriendsPackets extends PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getId()) {
		case 92:
			addFriend(player, packet);
			break;
		case 47:
			removeFriend(player, packet);
			break;
		case 137:
			break;
		case 13:
			break;
		case 69:
			sendMessage(player, packet);
			break;
		}
	}

	private void addFriend(Player player, Packet packet) {
		//ActionSender.sendFriend(player, packet.getLong(), 5);
		Connect.addFriend(player, packet.getLong());
	}
	
	private void removeFriend(Player player, Packet packet) {
		Connect.removeFriend(player, packet.getLong());
	}
	
	private void sendMessage(Player player, Packet packet) {
		//long to = packet.getLong();
		//byte[] data = packet.getRemainingData();
		//int size = data.length;
		//String text = Utils.textUnpack(data, size);
		Connect.sendMessage(player, packet.getLong(), packet.getRemainingData());
		//byte[] packed = new byte[size];
		//Utils.textPack(packed, text);
		//System.out.println("text = "+text+", to "+Utils.longToPlayerName(to));
	}
	
	@Override
	public int[] getPacketOpcodes() {
		return new int[] {92, 47, 137, 13, 69};
	}

}
