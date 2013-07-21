package content.handlers.interfaces;

import model.Player;

public abstract interface LoginHandler {
	
	public abstract boolean handleLogin(Player player);

}