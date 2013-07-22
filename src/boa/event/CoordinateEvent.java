package boa.event;

import model.Entity;
import model.Location;

public abstract class CoordinateEvent {

	private Entity entity;
	
	private Location target;
	
	private int distance;
	
	private Event event;

	public CoordinateEvent(final Entity entity, final Location target, final int distance) {
		this.entity = entity;
		this.target = target;
		this.distance = distance;
	}
	
	// todo following target i guess?
	
	public void check() {
		if (entity.getLocation().withinDistance(target, distance)) {
			if (entity.getLocation().withinDistance(target, distance)) {
				run();
			}
			this.stop();
		}
		entity.removeTemporary("COORDINATE_EVENT");
	}
	
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

	public abstract void run();
}