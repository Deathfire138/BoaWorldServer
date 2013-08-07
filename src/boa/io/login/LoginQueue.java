package boa.io.login;

import java.util.LinkedList;
import java.util.Queue;

import org.jboss.netty.channel.Channel;

public class LoginQueue {

	private static final Queue<Channel> loginQueue = new LinkedList<Channel>();
	
	public static Queue<Channel> getQueue() {
		return loginQueue;
	}
	
	public static void authenticateLogin(Channel channel, long name, String password, boolean lowMem) {
		
	}

}
