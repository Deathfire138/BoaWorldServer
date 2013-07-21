package boa.game.handlers.interfaces;

import model.Location;
import model.Player;

public abstract interface ObjectOptionThreeHandler {
	
	public abstract boolean handleObjectOptionThree(Player player, int objectId, Location location);

}