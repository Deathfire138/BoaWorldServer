package model;

import java.util.LinkedList;
import java.util.List;

public final class Tile {

	private final int clipping;
	
	//private List<GroundItem> items = new LinkedList<GroundItem>();
	
	public Tile(int clipping) {
		this.clipping = clipping;
	}

	public int getClipping() {
		return clipping;
	}
	
	/*public List<GroundItem> getItems() {
		return items;
	}*/
}
