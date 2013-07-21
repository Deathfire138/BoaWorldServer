package packet;

import model.Player;

public abstract class PacketHandler {
	
	public abstract void handle(Player player, Packet packet);
	
	public abstract int[] getPacketOpcodes();
	
	public boolean handlesOpcode(int id) {
		for(int b : getPacketOpcodes()) {
			if(b == id) {
				return true;
			}
		}
		return false;
	}
	
}
