package boa.io;

import java.nio.ByteBuffer;

import model.Player;
import model.World;

import org.jboss.netty.channel.Channel;

import boa.central.Connect;
import boa.io.rs2client.codec.RS2Decoder;


public class Login {

	public static void login(Channel channel, String username, long name, String password, boolean lowMem) {
		try {
		Player player = new Player(channel, username, name, password, lowMem);
		World.register(player);
		Connect.loginRequest(player);
		while (!player.getIsAuthenticated())
			break;
		System.out.println("Login says player is auth!");
		int returncode = 2;//(Integer)player.getTemporary("RETURNCODE");
		if (returncode == 2) {
			ByteBuffer buffer = ByteBuffer.allocate(9);
			buffer.put((byte) returncode);
			buffer.put((byte) 2/*player.getRight()*/);
			buffer.put((byte) 1);
			buffer.put((byte) 0);
			buffer.put((byte) 0);
			buffer.put((byte) 0);//1 does interesting things with buffers.
			buffer.putShort((short) player.getIndex());//index
			buffer.put((byte) (player.getMembers() == true ? 1 : 0));//members
			player.send((ByteBuffer)buffer.flip());
			channel.getPipeline().replace("decoder", "decoder", new RS2Decoder(player));
			
			ActionSender.login(player);
			player.setOnline(true);
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}