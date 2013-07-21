package boa;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import boa.central.Connect;
import boa.game.Content;


import packet.PacketManager;

import updateServer.Cache;
import updateServer.UpdateServer;

import model.World;
import net.ConnectionManager;


public class Server {

	private static final ExecutorService pool = Executors.newCachedThreadPool();
	
	public static byte worldId, location;
	public static boolean members;
	public static String activity;
	
	public static boolean isOnline = false;
	
	public Server(byte worldId, boolean members, byte location, String activity) throws Exception {
		this.worldId = worldId;
		this.members = members;
		this.location = location;
		this.activity = activity;
		init();
	}
	
	public void init() throws Exception {
		pool.submit(new Connect());
		while(!isOnline)
			break;
		Cache.init("./data/cache/");
		UpdateServer.init();
		PacketManager.init();
		Content.init();
		//Handler.load();
		try {
			ConnectionManager.init();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		World.init();
	}
	
}
