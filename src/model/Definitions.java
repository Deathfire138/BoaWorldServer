package model;

//statically create ArrayLists for all of the itemDefs, npcDefs, objectDefs, equipmentDefs, etc.

public class Definitions {

	/**
	 * Loads up the file holding all of the update dates for the definitions and checks them against the central server's files. If any of them are out of date, then we download the central server's data for that type of definition and update our files accordingly, subsequently updating the file holding the update dates so in the future we know that our definitions are up-to-date. The ones (if any) that are not out of date are read normally and nothing is downloaded from the central server. We then read each type of definition and save them in their respective ArrayLists so that classes can access them and use them to define themselves. Lastly we return so the server can finish loading.
 	 */
	public void load() {
		
	}
	
}
