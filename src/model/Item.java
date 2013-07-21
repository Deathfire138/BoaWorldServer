package model;

public class Item {

	ItemDefinition definition;
	int amount;
	
	public Item(int id, int amount) {
		//get ItemDefinition based on id, set definition to it.
		this.amount = amount;
	}
	
	public int getId() {
		return -1;
	}
	
	public int getAmount() {
		return amount;
	}
	
}
