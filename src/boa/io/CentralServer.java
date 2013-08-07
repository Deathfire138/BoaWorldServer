package boa.io;

import org.jboss.netty.channel.Channel;

import boa.Server;
import packet.Packet;

public class CentralServer {

	private static Channel channel;
	
	public static void authorizeConnection() {
		String loginPassword = "yvutGYGFkugyg%j8757ffu(HIt7f%$&c665c&86go8O*hi7Gt765dD%Yfgop9h";
		Packet packet = new Packet(loginPassword.getBytes().length + 5 + Server.activity.getBytes().length);
		//TODO make putString method to replace these two lines. :P
		packet.putByteArray(loginPassword.getBytes());
		packet.putByte((byte) 0);
		packet.putByte((byte) Server.worldId);
		packet.putByte((byte) (Server.members ? 1 : 0));
		packet.putByte((byte) Server.location);
		packet.putByteArray(Server.activity.getBytes());
		packet.putByte((byte) 0);
	}

	public static Channel getChannel() {
		return channel;
	}

	public static void setChannel(Channel channel) {
		CentralServer.channel = channel;
	}
	
}
