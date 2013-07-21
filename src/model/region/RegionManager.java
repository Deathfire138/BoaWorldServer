package model.region;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.Entity;
import model.Location;
import model.Npc;
import model.Player;
import model.Tile;

public class RegionManager implements Runnable {

	private static final Map<Integer, Region> regions = new HashMap<Integer, Region>();
	
	private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public static void init() {
		executor.scheduleAtFixedRate(new RegionManager(), 0, 1000, TimeUnit.MILLISECONDS);
	}
	
	public static List<Player> getLocalPlayers(Entity entity) {
		List<Player> players_todo = new LinkedList<Player>();
		List<Region> regions = getRegions(entity.getLocation());
		for (Region r : regions) {
			for (Player p : r.getPlayers()) {
				if (p.getLocation().withinDistance(entity.getLocation())) {
					players_todo.add(p);
				}
			}
		}
		List<Player> players = new LinkedList<Player>();
		if (players_todo.size() > 0) {
			int range = 0;
			while (range != 17 && players_todo.size() != 0 && players.size() != 255) {
				for (final Iterator<Player> it$ = players_todo.iterator(); it$.hasNext();) {
					final Player p = it$.next();
					if (p.getLocation().withinDistance(entity.getLocation(), range)) {
						it$.remove();
						players.add(p);
						if (players_todo.size() == 0 || players.size() == 255) {
							break;
						}
					}
				}
				range++;
			}
		}
		return players;
	}

	public static List<Npc> getLocalNpcs(Entity entity) {
		List<Npc> npcs_todo = new LinkedList<Npc>();
		List<Region> regions = getRegions(entity.getLocation());
		for (Region r : regions) {
			for (Npc n : r.getNpcs()) {
				if (n.getLocation().withinDistance(entity.getLocation())) {
					npcs_todo.add(n);
				}
			}
		}
		List<Npc> npcs = new LinkedList<Npc>();
		if (npcs_todo.size() > 0) {
			int range = 0;
			while (range != 17 && npcs_todo.size() != 0 && npcs.size() != 255) {
				for (final Iterator<Npc> it$ = npcs_todo.iterator(); it$.hasNext();) {
					final Npc n = it$.next();
					if (n.getLocation().withinDistance(entity.getLocation(), range)) {
						it$.remove();
						npcs.add(n);
						if (npcs_todo.size() == 0 || npcs.size() == 255) {
							break;
						}
					}
				}
				range++;
			}
		}
		return npcs;
	}
	
	public static List<Region> getRegions(Location location) {
		return getRegions(location.getX(), location.getY());
	}

	public static List<Region> getRegions(int x, int y) {
		List<Region> regions = new LinkedList<Region>();
		for (int localX = ((x >> 3) - 12) / 8; localX <= ((x >> 3) + 12) / 8; localX++) {
			for (int localY = ((y >> 3) - 12) / 8; localY <= ((y >> 3) + 12) / 8; localY++) {
				regions.add(getRegion((localX << 8) | localY));
			}
		}
		return regions;
	}

	public static Region getRegion(Location location) {
		return getRegion(location.getX(), location.getY());
	}

	public static Region getRegion(int x, int y) {
		return getRegion(((((x >> 3) / 8) << 8) | ((y >> 3) / 8)));
	}

	private static Region getRegion(int key) {
		if (regions.containsKey(key)) {
			return regions.get(key);
		} else {
			Region region = new Region(key);
			regions.put(key, region);
			return region;
		}
	}
	
	public static void update(Player player) {
	/*	for (Region r : getRegions(player.getLocation())) {
			for (int z = 0; z < 4; z++) {
				for (int x = 0; x < 64; x++) {
					for (int y = 0; y < 64; y++) {
						Tile t = r.getTile(x, y, z);
						if (t.getItems().size() > 0) {
							Location location = new Location(r.getBaseX() + x,  r.getBaseY() + y, z);
							for(GroundItem g : t.getItems()) { 
								if (!(g.getUpdate() <= (System.currentTimeMillis() - 150000)) && (g.getOwner().startsWith("WORLD-ITEM:") || g.getOwner() == "" || player.getUsername().equals(g.getOwner()))) {
									ActionSender.sendCreateGroundItem(player, g, location);
								}
							}
						} else {
							continue;
						}
					}
				}
			}
		}*/
	}
	
	/*public static void addGroundItem(GroundItem item, Location location) {
		getRegion(location).getTile(location.getX(), location.getY(), location.getZ()).getItems().add(item);
		for (Region r : getRegions(location)) {
			for (Player p : r.getPlayers()) {
				if (p.getLocation().getZ() == location.getZ() && p.getLocation().withinDistance(location, 18)) {
					ActionSender.sendCreateGroundItem(p, item, location);
				}
			}
		}
	}*/
	
	/*public static Item removeGroundItem(int id, Location location) {
		GroundItem item = null;
		Tile t = getRegion(location).getTile(location.getX(), location.getY(), location.getZ());
		if (t.getItems().size() > 0) {
			for(Iterator<GroundItem> it$ = t.getItems().iterator(); it$.hasNext();) {
				GroundItem g = it$.next();
				if (g.getItem().getId() == id) {
					if (g.getOwner().startsWith("WORLD-ITEM:")) {
						//make the item respawn :)
					}
					item = g;
					it$.remove();
					break;
				}
			}
		} else {
			return null;
		}
		if (item != null) {
			for (Region r : getRegions(location)) {
				for (Player p : r.getPlayers()) {
					ActionSender.sendDestroyGroundItem(p, item, location);
				}
			}
			return item.getItem();
		}
		return null;
	}*/

	public void run() {
		try {
			if (regions.values().size() > 0) {
				for(Iterator<Region> it$ = regions.values().iterator(); it$.hasNext();) {
					Region r = it$.next();
					long time = r.getTimeOut();
					if (time != 0) {
						if (r.getPlayers().size() > 0) {
							r.setTimeOut(0);
						} else if (time < System.currentTimeMillis()) {
							if (r.getPlayers().size() > 0) {
								r.setTimeOut(0);
							} else {
								// shut down region
								it$.remove();
							}
						}
					} else {
						if (r.getPlayers().size() > 0) {
							r.setTimeOut(System.currentTimeMillis() + 600000);
						}
					}
					/*for (int z = 0; z < 4; z++) {
						for (int x = 0; x < 64; x++) {
							for (int y = 0; y < 64; y++) {
								Tile t = r.getTile(r.getBaseX() + x, r.getBaseY() + y, z);
								if (t.getItems().size() > 0) {
									Location location = new Location(r.getBaseX() + x,  r.getBaseY() + y, z);
									for(Iterator<GroundItem> it$2 = t.getItems().iterator(); it$2.hasNext();) { 
										GroundItem g = it$2.next();
										if (g.getOwner().startsWith("WORLD-ITEM:")) {
											continue;
										} else {
											if ((g.getUpdate() <= (System.currentTimeMillis() - 60000)) && g.getOwner() != "") { // 2 min
												for (Region sr : getRegions(location)) {
													for (Player p : sr.getPlayers()) {
														if (p.getUsername().equals(g.getOwner())) {
															continue;
														}
														ActionSender.sendCreateGroundItem(p, g, location);
													}
												}
												g.setOwner("");
											} else if ((g.getUpdate() <= (System.currentTimeMillis() - 150000)) && g.getOwner() == "") {
												for (Region sr : getRegions(location)) {
													for (Player p : sr.getPlayers()) {
														ActionSender.sendDestroyGroundItem(p, g, location);
													}
												}
												it$2.remove();
											}
										}
									}
								} else {
									continue;
								}
							}
						}
					}*/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
