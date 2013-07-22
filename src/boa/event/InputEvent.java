package boa.event;

public abstract class InputEvent {

	private Event event;
	
	public abstract void run();

	public abstract void close();
	
	public abstract void input(int amount);
	
	public abstract void input(String string);
	
	public void submit(Event event) {
		this.event = event;
		EventHandler.submit(event);
	}
	
	public void stop() {
		if (event != null) {
			event.stop();
			event = null;
		}
	}
}