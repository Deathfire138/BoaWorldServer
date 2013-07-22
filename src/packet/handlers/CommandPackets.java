package packet.handlers;

import boa.event.InterfaceEvent;
import boa.io.ActionSender;
import model.Animation;
import model.Inventory;
import model.Item;
import model.Location;
import model.Player;
import model.UpdateFlags.UpdateFlag;
import packet.Packet;
import packet.PacketHandler;

public class CommandPackets extends PacketHandler {

	@Override
	public void handle(final Player player, Packet packet) {
		String string = packet.getRS2String();
		final String[] arguments = string.split(" ");
		String command = arguments[0].toLowerCase();
		if (command.equals("interface")) {
			if (player.getTemporary("INTERFACE_EVENT") != null) {
				((InterfaceEvent) player.getTemporary("INTERFACE_EVENT")).close();
			}
			player.addTemporary("INTERFACE_EVENT", new InterfaceEvent() {

				@Override
				public void close() {
					ActionSender.sendCloseInterface(player, 548, 77);
				}

				@Override
				public void open() {
					ActionSender.sendInterface(player, Integer.parseInt(arguments[1]));	
				}
				
			});
			((InterfaceEvent) player.getTemporary("INTERFACE_EVENT")).open();
		} else if (command.equals("tele")) {
			if(arguments.length == 3 || arguments.length == 4) {
				int x = Integer.parseInt(arguments[1]);
				int y = Integer.parseInt(arguments[2]);
				int z = player.getLocation().getZ();
				if(arguments.length == 4) {
					z = Integer.parseInt(arguments[3]);
				}
				player.addTemporary("TELEPORT_TARGET", new Location(x, y, z));
			} else {
				ActionSender.sendMessage(player, "Syntax is ::tele [x] [y] [z].");
			}
		} else if (command.equals("config")) {
			ActionSender.sendConfig(player, Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2]));
			System.out.println("Set config "+arguments[1]+" to "+arguments[2]);
		} else if (command.equals("configtest")) {
			for(int i = 0; i < 1200; i++) {
				ActionSender.sendConfig(player, i, Integer.parseInt(arguments[1]));
			}
		} else if (command.equals("music")) {
			ActionSender.sendMusic(player, Integer.parseInt(arguments[1]));
		} else if (command.equals("anim")) {
			player.addTemporary("ANIMATION", new Animation(Integer.parseInt(arguments[1]), 0));
			player.getUpdateFlags().flag(UpdateFlag.ANIMATION);
		} else if (command.equals("coords")) {
			ActionSender.sendMessage(player, player.getLocation().toString());
		} else if (command.equals("region")) {
			Location l = player.getLocation();
			int x = (l.getX() / 64);
			int y = (l.getY() / 64);
			ActionSender.sendMessage(player, "You are in region "+((x << 8) + y));
		} else if (command.equals("file")) {
			
		} else if(command.equals("fileregion")) {
			ActionSender.sendMessage(player, (player.getLocation().getX()>>6)+"_"+(player.getLocation().getY()>>6));
		} else if (command.equals("telextea")) {//yCalc + (xCalc << 8);
			int x = (short)((Integer.parseInt(arguments[1]) >> 8) * 64);
			int y = (short)((Integer.parseInt(arguments[1]) & 0xff) * 64);
			//player.setTeleportTarget(Location.create(x, y, 0));
			int regionTest = ((x / 64) << 8) + (y / 64);
			ActionSender.sendMessage(player, "Going to x: "+x+", y: "+y+". Region Test = "+regionTest);
			player.addTemporary("TELEPORT_TARGET", new Location(x, y, 0));
		} else if (command.equals("run")) {
			ActionSender.sendEnergy(player, 100);
		} else if (command.equals("item")) {
			player.getInventory().add(new Item(4151, 1));
		} else if (command.equals("string")) {
			ActionSender.sendString(player, Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2]), arguments[3]);
		}
	}

	@Override
	public int[] getPacketOpcodes() {
		return new int[] {46};
	}

}
	
