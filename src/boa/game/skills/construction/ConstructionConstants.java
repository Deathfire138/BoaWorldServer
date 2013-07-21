package boa.game.skills.construction;

import model.Location;

public class ConstructionConstants {

	enum Room {
		GRASS(new Location(1928, 5056, 0)),
		NORMAL_GARDEN(new Location(1920, 5064, 0)),
		STRAIGHT_ROOF(new Location(1928, 5072, 0)),
		PORTAL_ROOM(new Location(1928, 5088, 0));
		
		
		Room(Location location) {
			
		}
	}
	
}