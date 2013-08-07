package packet;

import java.io.File;
import java.util.LinkedList;
import java.util.logging.Logger;

import model.Player;

/**
 * TODO yeah... redo the fuck out of this. Not a hard task. ^^
 * @author Tyler
 *
 */
public class PacketManager {

	private static final Logger logger = Logger.getLogger(PacketManager.class.getName());
	
	private static LinkedList<PacketHandler> handlers = new LinkedList<PacketHandler>();
	
	public static void init() throws Exception {
		logger.info("Loading packets...");
		for (File file : new File("./bin/packet/handlers/").listFiles()) {
			if (file.getName().contains("$1")) {
				continue;
			}
			handlers.add((PacketHandler) Class.forName("packet.handlers."+file.getName().replace(".class", "")).newInstance());
		}
		logger.info("Loaded "+handlers.size()+" packets.");
	}

	public static void handle(Player player, Packet packet) {
		boolean handled = false;
		for (PacketHandler handler : handlers) {
			if (handler.handlesOpcode(packet.getOpcode())) {
				handler.handle(player, packet);
				handled = true;
				break;
			}
		}
		if(!handled) {
			logger.info("Unhandled packet: (id = "+packet.getOpcode()+"), (size = "+packet.getSize()+")");
			System.out.println("Unhandled packet! "+packet.getOpcode());
		}
	}
	
}