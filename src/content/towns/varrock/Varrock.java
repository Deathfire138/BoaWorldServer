package content.towns.varrock;

import content.towns.varrock.npcs.VarrockTownCrier;
import model.Location;
import model.npc.programmable.npcs.ProgrammableNPCs;

public class Varrock {

	public static void init() {
		ProgrammableNPCs.register(new VarrockTownCrier(new Location(3223, 3223, 0)));
	}
	
}
