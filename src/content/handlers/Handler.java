package content.handlers;

import java.util.LinkedList;
import java.util.List;

import model.Location;
import model.Player;

import content.handlers.interfaces.ButtonHandler;
import content.handlers.interfaces.EquipHandler;
import content.handlers.interfaces.ItemOnItemHandler;
import content.handlers.interfaces.ItemOptionHandler;
import content.handlers.interfaces.LoginHandler;
import content.handlers.interfaces.ObjectOptionOneHandler;
import content.handlers.interfaces.TakeItemHandler;

public abstract class Handler {
	
	private static final List<LoginHandler> loginHandlers = new LinkedList<LoginHandler>();
	
	private static final List<ObjectOptionOneHandler> objectOptionOneHandlers = new LinkedList<ObjectOptionOneHandler>();

	private static final List<ItemOptionHandler> itemOptionHandlers = new LinkedList<ItemOptionHandler>();
	
	private static final List<ButtonHandler> buttonHandlers = new LinkedList<ButtonHandler>();
	
	private static final List<ItemOnItemHandler> itemOnItemHandlers = new LinkedList<ItemOnItemHandler>();
	
	private static final List<TakeItemHandler> takeItemHandlers = new LinkedList<TakeItemHandler>();
	
	private static final List<EquipHandler> equipmentHandlerHandlers = new LinkedList<EquipHandler>();

	public Handler() {
		Object object = getObject();
		if (object instanceof ObjectOptionOneHandler) {
			objectOptionOneHandlers.add((ObjectOptionOneHandler) object);
		} else if (object instanceof LoginHandler) {
			loginHandlers.add((LoginHandler) object);
		} else if (object instanceof ItemOptionHandler) {
			itemOptionHandlers.add((ItemOptionHandler) object);
		} else if (object instanceof ButtonHandler) {
			buttonHandlers.add((ButtonHandler) object);
		} else if (object instanceof ItemOnItemHandler) {
			itemOnItemHandlers.add((ItemOnItemHandler) object);
		} else if (object instanceof TakeItemHandler) {
			takeItemHandlers.add((TakeItemHandler) object);
		} else if (object instanceof EquipHandler) {
			equipmentHandlerHandlers.add((EquipHandler) object);
		}
	}
	
	public static boolean login(Player player) {
		for (LoginHandler handler : loginHandlers) {
			if (handler.handleLogin(player)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean objectOptionOne(Player player, int objectId, Location location) {
		for (ObjectOptionOneHandler handler : objectOptionOneHandlers) {
			if (handler.handleObjectOptionOne(player, objectId, location)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean itemOption(Player player, int interfaceId, int index, int itemId) {
		for (ItemOptionHandler handler : itemOptionHandlers) {
			if (handler.handleItemOption(player, interfaceId, index, itemId )) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean button(Player player, int opcode, int interfaceId, int buttonId, int buttonId2) {
		for (ButtonHandler handler : buttonHandlers) {
			if (handler.handleButton(player, opcode, interfaceId, buttonId, buttonId2)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean itemOnItem(Player player, int interfaceId, int usedId, int usedSlot, int withId, int withSlot) {
		for (ItemOnItemHandler handler : itemOnItemHandlers) {
			if (handler.handleItemOnItem(player, interfaceId, usedId, usedSlot, withId, withSlot)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean takeItem(Player player, int x, int y, int itemId) {
		for (TakeItemHandler handler : takeItemHandlers) {
			if (handler.handleTakeItem(player, x, y, itemId)) {
				return true;
			}
		}
		return false;		
	}
	
	public static boolean equip(Player player, int interfaceId, int index, int itemId) {
		for (EquipHandler handler : equipmentHandlerHandlers) {
			if (handler.handleEquip(player, interfaceId, index, itemId)) {
				return true;
			}
		}
		return false;		
	}
	
	public abstract Object getObject();
	
}
