package boa.game.handlers.types;

import boa.game.handlers.Handler;
import boa.log.Logger;
import boa.log.Logger.Type;

public abstract class StandardHandler extends Handler {
	
	public StandardHandler() {
		Logger.log(Type.DEBUG, "This is called.", this.getClass());
		Object object = getObject();
		Handler.registerHandler(0, object);
	}
	
	public abstract Object getObject();
	
}
