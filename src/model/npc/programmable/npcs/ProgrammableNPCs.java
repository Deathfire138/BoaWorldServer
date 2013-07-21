package model.npc.programmable.npcs;

import java.util.ArrayList;

import model.World;


public class ProgrammableNPCs {

	static ArrayList<ProgrammableNPC> programmableNPCs;
	
	public ProgrammableNPCs() {
		init();
	}
	
	public void init() {
		load();
	}
	
	public void load() {
		
	}
	
	public static void register(ProgrammableNPC pnpc) {
		programmableNPCs.add(pnpc);
		World.register(pnpc);
	}
	
}