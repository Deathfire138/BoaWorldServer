package model;


import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import boa.game.handlers.types.MinigameHandler;
import boa.game.handlers.types.QuestHandler;
import boa.game.handlers.types.SkillHandler;
import boa.game.handlers.types.StandardHandler;


import model.region.RegionManager;

public class World implements Runnable {

	private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
	private static final EntityList<Player> players = new EntityList<Player>(2048);
	
	private static final EntityList<Npc> npcs = new EntityList<Npc>(32767);
	
	
	public static void init() {
		executor.scheduleAtFixedRate(new World(), 0, 600, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void run() {
		for (Player player : players) {
			if (player.isOnline()) {
				player.tick();					
			}
		}
		for (Player player : players) {
			if (player.isOnline()) {
				Rendering.sendRendering(player);
			}
		}
		for(Player player : players) {
			if (player == null) {
				continue;
			}
			player.getUpdateFlags().reset();
			player.reset();
		}
	}

	public ArrayList<Player> getLocalPlayers(Player player) {
		return null;
	}
	
	public static int register(Player player) {
		players.add(player);
		int index = player.getIndex();
		if (index != -1) {
			player.setIndex(index);
			//LoginServerConnection.register(player);
			boolean bool = RegionManager.getRegion(player.getLocation()).getPlayers().add(player);
			System.out.println("bool = "+bool);
			//Friends.register(player);
		}
		return index;
	}
	
	public static int register(Npc npc) {
		npcs.add(npc);
		int index = npc.getIndex();
		if (index != -1) {
			//LoginServerConnection.register(player);
			//RegionManager.getRegion(npc.getLocation()).getPlayers().add(npc);
			//Friends.register(player);
		}
		return index;
	}	
	
	public static Player getPlayerFromName(String name) {
		for (Player player : players)
			if (player.getUsername().equalsIgnoreCase(name))
				return player;
		return null;
	}
	
}
