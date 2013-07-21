package model;

import java.util.HashMap;
import java.util.LinkedList;

import model.UpdateFlags.UpdateFlag;

public abstract class Entity {

	private Location location;
	
	private int index;
	
	private HashMap<String, Object> temporary;
	
	private LinkedList<Player> localPlayers;

	private LinkedList<Npc> localNpcs;
	
	private UpdateFlags updateFlags;
	
	private Sprites sprites;
	
	private Damage damage;
	
	private WalkingQueue walkingQueue;
	
	private boolean hidden;
	
	private boolean destroyed;

	public Entity(Location location) {
		this.location = location;
		this.temporary = new HashMap<String, Object>();
		this.temporary.put("FACING", 1);
		this.localPlayers = new LinkedList<Player>();
		this.localNpcs = new LinkedList<Npc>();
		this.updateFlags = new UpdateFlags();
		this.updateFlags.set(UpdateFlags.UpdateFlag.APPEARANCE, true);
		this.sprites = new Sprites();
		this.damage = new Damage();
		this.walkingQueue = new WalkingQueue(this);
		this.hidden = false;
		this.destroyed = false;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		Location old = this.location;
		this.location = location;
		if (old.getId() != location.getId()) {
			changeRegion(location, old);
		}
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void addTemporary(String name, Object value) {
		temporary.put(name, value);
	}
	
	public Object getTemporary(String name) {
		return temporary.get(name);
	}
	
	public void removeTemporary(String name) {
		temporary.remove(name);
	}
	
	public LinkedList<Player> getLocalPlayers() {
		return localPlayers;
	}

	public LinkedList<Npc> getLocalNpcs() {
		return localNpcs;
	}
	
	public UpdateFlags getUpdateFlags() {
		return updateFlags;
	}
	
	public Sprites getSprites() {
		return sprites;
	}

	public Damage getDamage() {
		return damage;
	}
	
	public WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}
	
	public void setHidden() {
		hidden = !hidden;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setDestroyed() {
		destroyed = !destroyed;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public boolean isAnimating() {
		return this.getUpdateFlags().get(UpdateFlag.ANIMATION);
	}
	
	public abstract void tick();
	
	public abstract void reset();
	
	public abstract void changeRegion(Location to, Location from);
	
	public abstract int getClientIndex();
	
	public abstract void turnTo(Entity entity);
	
	public abstract void turnTemporarilyTo(Entity entity);
	
	public abstract void playGraphics(Graphic graphic);
	
	public abstract void playAnimation(Animation animation);
	
	public abstract void heal(int amount);
	
	public abstract void hit(int damage, Damage.HitType type);
}
