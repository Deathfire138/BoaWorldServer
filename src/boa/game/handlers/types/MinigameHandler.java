package boa.game.handlers.types;

import boa.game.handlers.Handler;

public abstract class MinigameHandler {

	public MinigameHandler() {
		Object object = getObject();
		Handler.registerHandler(2, object);
	}
	
	public abstract Object getObject();
	
}
