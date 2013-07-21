package model.npc.programmable.npcs;

import model.Location;
import model.Npc;

public abstract class ProgrammableNPC extends Npc {
	
	public ProgrammableNPC(Location location) {
		super(location);
		// TODO Auto-generated constructor stub
	}

	public abstract void programTick();
	
}
