package boa.game.handlers.interfaces;

import model.Location;
import model.Player;

public abstract interface ObjectOptionOneHandler {
	
	public abstract boolean handleObjectOptionOne(Player player, int objectId, Location location);

}