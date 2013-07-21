package event;

public abstract class Event {

	private long delay;

	private boolean running = true;

	public Event(long delay) {
		this.delay = delay;
	}

	public long getDelay() {
		return delay;
	}

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
	}

	public abstract void execute();

}