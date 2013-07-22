package boa.game.handlers.types;

import boa.game.handlers.Handler;

public abstract class QuestHandler {

	public QuestHandler() {
		Object object = getObject();
		Handler.registerHandler(3, object);
	}
	
	public abstract Object getObject();
	
}