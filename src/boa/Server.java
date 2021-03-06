package boa;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import boa.central.Connect;
import boa.game.handlers.Handler;
import boa.io.central.CentralConnectionManager;
import boa.io.rs2client.RS2ConnectionManager;
import boa.update.Cache;
import boa.update.UpdateServer;


import packet.PacketManager;


import model.World;


public class Server {

	private static final ExecutorService pool = Executors.newCachedThreadPool();
	
	public static byte worldId, location;
	public static boolean members;
	public static String activity;
	public static boolean isOnline = false;
	public static boolean isDebugEnabled;
	
	public Server(byte worldId, boolean members, byte location, String activity, boolean debug) throws Exception {
		this.worldId = worldId;
		this.members = members;
		this.location = location;
		this.activity = activity;
		isDebugEnabled = debug;
		
		init();
	}
	
	public void init() throws Exception {
		//pool.submit(new Connect());
		try {
			CentralConnectionManager.init();
			Cache.init("./data/cache/");
			UpdateServer.init();
			PacketManager.init();
			Handler.load();
			RS2ConnectionManager.init();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		World.init();
	}
	
}
