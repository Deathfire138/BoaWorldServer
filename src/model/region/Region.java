package model.region;

import java.util.LinkedList;
import java.util.List;

import model.Npc;
import model.Player;
import model.Tile;

public final class Region {
	
	private final int baseX, baseY;
	
	//private final List<GameObject> objects;
	
	private final List<Player> players;
	
	private final List<Npc> npcs;
	
	private final Tile[][][] tiles;	
	
	private long timeOut = 0;

	public Region(int id) {
		this.baseX = ((id >> 8) * 64);
		this.baseY = ((id & 0xFF) * 64);
		//this.objects = new LinkedList<GameObject>();
		this.players = new LinkedList<Player>();
		this.npcs = new LinkedList<Npc>();
		this.tiles = new Tile[4][64][64];
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++) {
					tiles[z][x][y] = new Tile(0); // todo clipping
				}
			}
		}
	}

	public int getBaseX() {
		return baseX;
	}
	
	public int getBaseY() {
		return baseY;
	}

	public long getTimeOut() {
		return timeOut;
	}
	
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	/*public List<GameObject> getObjects() {
		return objects;
	}*/
	
	public Tile getTile(int x, int y, int z) {
		return tiles[z][x - baseX][y - baseY];
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Npc> getNpcs() {
		return npcs;
	}
}
