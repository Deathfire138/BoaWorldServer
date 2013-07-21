package packet.handlers;

import boa.central.Connect;
import boa.util.Utils;
import model.ChatMessage;
import model.Player;
import packet.Packet;
import packet.PacketHandler;

public class ChatPackets extends PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getId()) {
		case 104://public chatting
			chat(player, packet);
			break;
		case 54://chat options
			updateOptions(player, packet);
			break;
		}
	}

	private void chat(Player player, Packet packet) {
		int colour = packet.get();
        int effects = packet.get();
        int size = packet.get();
        String text = Utils.decryptPlayerChat(packet, size);
        System.out.println("chat");
        if (player.getIsInClanChat() && text.startsWith("/")) {
        	System.out.println("clannchat");
        	text.replaceFirst("/", "");
        	Connect.sendClanMessage(player, text);
        } else {
        	System.out.println("Text = "+text);
        	player.addChatMessage(new ChatMessage(colour, effects, text));
        }
	}
	
	public void updateOptions(Player player, Packet packet) {
		byte publicChat = packet.get();
		byte privateChat = packet.get();
		byte trade = packet.get();
		player.getSettings().setPublicChat(publicChat);
		player.getSettings().setPrivateChat(privateChat);
		player.getSettings().setTrade(trade);
		Connect.updateChatSettings(player, publicChat, privateChat, trade);
		System.out.println("lolol ==== "+privateChat);
	}
	
	@Override
	public int[] getPacketOpcodes() {
		return new int[] {104, 54};
	}

}
