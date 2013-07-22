package boa.game.handlers.types;

import boa.game.handlers.Handler;

public abstract class SkillHandler {

	public SkillHandler() {
		Object object = getObject();
		Handler.registerHandler(1, object);
	}
	
	public abstract Object getObject();
	
}
