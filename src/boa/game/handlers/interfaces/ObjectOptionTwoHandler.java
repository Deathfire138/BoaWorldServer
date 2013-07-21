package boa.game.handlers.interfaces;

import model.Location;
import model.Player;

public abstract interface ObjectOptionTwoHandler {
	
	public abstract boolean handleObjectOptionTwo(Player player, int objectId, Location location);

}