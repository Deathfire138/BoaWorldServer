package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import model.UpdateFlags.UpdateFlag;
import model.region.RegionManager;

import org.jboss.netty.channel.Channel;

public final class Player extends Entity {
	
	private final Channel channel;
	
	private final String username, password;
	
	private int right;
	
	private final boolean lowMemoryVersion;
	
	private final long name;
	
	private final Appearance appearance;
	
	private final Container<Item> inventory;
	
	private final Container<Item> equipment;
	
	private final Skills skills;
	
	private final Settings settings;
	
	private boolean isAuthenticated;
	
	private boolean isOnline;
	
	private final List<Long> friends;

	private final List<Long> ignores;
	
	private final Queue<ChatMessage> chatMessages;
	
	private ChatMessage currentChatMessage;
	
	private int pingIndex;
	
	private boolean isMembers;
	
	private boolean isInClanChat;

	public Player(Channel channel, String username, long name, String password, boolean lowMemoryVersion) {
		super(new Location(3223, 3223, 0));
		//super(new Location(1890, 5090, 0));
		this.channel = channel;
		this.username = username;
		this.name = name;
		this.password = password;
		this.lowMemoryVersion = lowMemoryVersion;
		this.appearance = new Appearance();
		this.inventory = new Container<Item>(28, false);
		this.equipment = new Container<Item>(14, false);
		this.skills = new Skills(this);
		this.settings = new Settings(this);
		//this.quests = new Quests(this);
		this.friends = new ArrayList<Long>(200);
		this.ignores = new ArrayList<Long>(100);
		this.chatMessages = new LinkedList<ChatMessage>();
		this.isAuthenticated = false;
		this.isOnline = false;
		this.isMembers = false;
	}
	
	public Channel getChannel() {
		return channel;
	}

	public long getName() {
		return name;
	}

	public boolean isLowMemoryVersion() {
		return lowMemoryVersion;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public int getRight() {
		return right;
	}
	
	public void setRight(int right) {
		this.right = right;
	}
	
	public Appearance getAppearance() {
		return appearance;
	}
	
	public Container<Item> getInventory() {
		return inventory;
	}
	
	public Container<Item> getEquipment() {
		return equipment;
	}
	
	public Skills getSkills() {
		return skills;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	/*public Quests getQuests() {
		return quests;
	}*/

	public List<Long> getFriends() {
		return friends;
	}

	public List<Long> getIgnores() {
		return ignores;
	}

	public void addChatMessage(ChatMessage message) {
		chatMessages.add(message);
	}
	
	public ChatMessage getCurrentChatMessage() {
		return currentChatMessage;
	}
	
	public int getNextUniqueId() {
		if (pingIndex >= 16000000) {
			pingIndex = 0;
		}
		return pingIndex++;
	}
	
	public void send(Object packet) {
		synchronized (this) {
			channel.write(packet);
		}
	}

	@Override
	public void tick() {
		if (chatMessages.size() > 0) {
			currentChatMessage = chatMessages.poll();
			this.getUpdateFlags().flag(UpdateFlag.CHAT);
		} else {
			currentChatMessage = null;
		}
		getWalkingQueue().processNextMovement();
	}

	@Override
	public void reset() {
		if (getTemporary("HAS_TELEPORTED") != null) {
			removeTemporary("HAS_TELEPORTED");
			removeTemporary("TELEPORT_TARGET");
		}
		removeTemporary("MAP_REGION_CHANGING");
		removeTemporary("GRAPHICS");
		removeTemporary("ANIMATION");
		removeTemporary("FACE_LOCATION");
		removeTemporary("FACE_ENTITY");
		if (getTemporary("FACE_ENTITY_TEMP") != null) {
			removeTemporary("FACE_ENTITY_TEMP");
			getUpdateFlags().flag(UpdateFlag.FACE_ENTITY);
		}
	}

	@Override
	public void changeRegion(Location to, Location from) {
		if (from != null) {
			RegionManager.getRegion(from).getPlayers().remove(this);
		}
		RegionManager.getRegion(to).getPlayers().add(this);
	}

	@Override
	public int getClientIndex() {
		return getIndex() + 32768;
	}
	
	@Override
	public void playAnimation(Animation animation) {
		addTemporary("ANIMATION", animation);
		this.getUpdateFlags().flag(UpdateFlag.ANIMATION);
	}
	
	@Override
	public void playGraphics(Graphic graphic) {
		addTemporary("GRAPHICS", graphic);
		this.getUpdateFlags().flag(UpdateFlag.GRAPHICS);
	}
	
	@Override
	public void turnTo(Entity entity) {
		addTemporary("FACE_ENTITY", entity);
		this.getUpdateFlags().flag(UpdateFlag.FACE_ENTITY);
	}

	@Override
	public void turnTemporarilyTo(Entity entity) {
		addTemporary("FACE_ENTITY", entity);
		addTemporary("FACE_ENTITY_TEMP", true);
		this.getUpdateFlags().flag(UpdateFlag.FACE_ENTITY);
	}
	
	@Override
	public void heal(int amount) {
	}
	
	@Override
	public void hit(int damage, Damage.HitType type) {
	}
	
	public boolean getIsAuthenticated() {
		return isAuthenticated;
	}
	
	public void setIsAuthenticated(boolean bool) {
		isAuthenticated = bool;
	}
	
	public boolean isOnline() {
		return isOnline;
	}
	
	public void setOnline(boolean bool) {
		isOnline = bool;
	}
	
	public boolean getMembers() {
		return isMembers;
	}
	
	public void setMembers(boolean bool) {
		isMembers = bool;
	}

	public boolean getIsInClanChat() {
		return isInClanChat;
	}
	
	public void setIsInClanChat(boolean bool) {
		isInClanChat = bool;
	}
	
}