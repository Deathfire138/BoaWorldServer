package model;

public class Container<T extends Item> {
	
	private Item[] data;
	private boolean alwaysStackable;
	
	public Container(int size, boolean alwaysStackable) {
		this.data = new Item[size];
		this.alwaysStackable = alwaysStackable;
	}

	public void shift() {
		Item[] oldData = data;
		data = new Item[oldData.length];
		int ptr = 0;
		for(int i = 0; i < data.length; i++) {
			if(oldData[i] != null) {
				data[ptr++] = oldData[i];
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public T get(int slot) {
		if(slot < 0 || slot >= data.length) {
			return null;
		}
		return (T) data[slot];
	}
	
	public void set(int slot, T item) {
		if(slot < 0 || slot >= data.length) {
			return;
		}
		data[slot] = item;
	}
	
	public int forceAdd(T item) {
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				data[i] = item;
				return i;
			}
		}
		return -1;
	}
	
	public int add(T item) {
		//if(alwaysStackable || ItemDefinition.get(item.getId()) == null ||ItemDefinition.get(item.getId()).isStackable()) {
			for(int i = 0; i < data.length; i++) {
				if(data[i] != null) {
					if(data[i].getId() == item.getId()) {
						data[i] = new Item(data[i].getId(), data[i].getAmount() + item.getAmount());
						return i;
					}
				}
			}
		//}
		int index = freeSlot();
		if(index == -1) {
			return -1;
		}
		data[index] = item;
		return index;
	}
	
	public int freeSlots() {
		int j = 0;
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				j++;
			}
		}
		return j;
	}
	
	public void remove(T item) {
		int removed = 0, toRemove = item.getAmount();
		for(int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				if(data[i].getId() == item.getId()) {
					int amt = data[i].getAmount();
					if(amt > toRemove) {
						removed += toRemove;
						amt -= toRemove;
						toRemove = 0;
						data[i] = new Item(data[i].getId(), amt);
						return;
					} else {
						removed += amt;
						toRemove -= amt;
						data[i] = null;
					}
				}
			}
		}
	}
	
	public void removeAll(T item) {
		for(int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				if(data[i].getId() == item.getId()) {
					data[i] = null;
				}
			}
		}
	}
	
	public boolean containsOne(T item) {
		for(int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				if(data[i].getId() == item.getId()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean contains(T item) {
		int amtOf = -1;
		for(int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				if(data[i].getId() == item.getId()) {
					if (amtOf < 0) {
						amtOf = data[i].getAmount();
					} else {
						amtOf += data[i].getAmount();
					}
				}
			}
		}
		return amtOf >= item.getAmount();
	}
	
	public int freeSlot() {
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				return i;
			}
		}
		return -1;
	}

	public void clear() {
		for(int i = 0; i < data.length; i++) {
			data[i] = null;
		}
	}

	public int getSize() {
		return data.length;
	}

	public int getFreeSlots() {
		int s = 0;
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				s++;
			}
		}
		return s;
	}

	public int getNumberOf(Item item) {
		int count = 0;
		for(int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				if(data[i].getId() == item.getId()) {
					count += data[i].getAmount();
				}
			}
		}
		return count;
	}

	public Item[] getItems() {
		return data;
	}

	public Container<Item> asItemContainer() {
		Container<Item> c = new Container<Item>(data.length, alwaysStackable);
		for(int i = 0; i < data.length; i++) {
			data[i] = data[i];
		}
		return c;
	}

	public int getFreeSlot() {
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				return i;
			}
		}
		return 0;
	}

	public Item lookup(int id) {
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				continue;
			}
			if(data[i].getId() == id) {
				return data[i];
			}
		}
		return null;
	}
	
	public int lookupSlot(int id) {
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				continue;
			}
			if(data[i].getId() == id) {
				return i;
			}
		}
		return -1;
	}

	public void remove(int preferredSlot, Item item) {
		int removed = 0, toRemove = item.getAmount();
		if(data[preferredSlot] != null) {
			if(data[preferredSlot].getId() == item.getId()) {
				int amt = data[preferredSlot].getAmount();
				if(amt > toRemove) {
					removed += toRemove;
					amt -= toRemove;
					toRemove = 0;
					data[preferredSlot] = new Item(data[preferredSlot].getId(), amt);
					return;
				} else {
					removed += amt;
					toRemove -= amt;
					data[preferredSlot] = null;
				}
			}
		}
		for(int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				if(data[i].getId() == item.getId()) {
					int amt = data[i].getAmount();
					if(amt > toRemove) {
						removed += toRemove;
						amt -= toRemove;
						toRemove = 0;
						data[i] = new Item(data[i].getId(), amt);
						return;
					} else {
						removed += amt;
						toRemove -= amt;
						data[i] = null;
					}
				}
			}
		}
	}

	public void addAllItem(Container<T> container) {
		for(int i = 0; i < container.getSize(); i++) {
			data[i] = container.get(i);
		}
	}
	
	public void addAll(Container<T> container) {
		for(int i = 0; i < container.getSize(); i++) {
			T item = container.get(i);
			if(item != null) {
				this.add(item);
			}
		}
	}

	public boolean hasSpaceFor(Container<T> container) {
		for(int i = 0; i < container.getSize(); i++) {
			T item = container.get(i);
			if(item != null) {
				if(!this.hasSpaceForItem(item)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean hasSpaceForItem(T item) {
		//if(alwaysStackable || ItemDefinition.get(item.getId()) == null || ItemDefinition.get(item.getId()).isStackable()) {
			for(int i = 0; i < data.length; i++) {
				if(data[i] != null) {
					if(data[i].getId() == item.getId()) {
						return true;
					}
				}
			}
		/*} else {
			if(item.getAmount() > 1) {
				if(freeSlots() >= item.getAmount()) {
					return true;
				} else {
					return false;
				}
			}
		}*/
		int index = freeSlot();
		if(index == -1) {
			return false;
		}
		return true;
	}
}