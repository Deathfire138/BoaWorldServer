package boa.io;

import boa.central.Connect;
import boa.game.maps.MapData;
import boa.io.PacketBuilder.Size;
import boa.util.Utils;
import model.Item;
import model.Player;

/**
 * TODO Rename this to something more relevant? Such as RS2Packets?
 * @author Tyler
 *
 */
public class ActionSender {

	public static void login(Player player) {
		sendMapRegion(player);
		sendWindowPane(player, 548);
		sendSidebarInterface(player, 137, 121);//Chatbox
		sendSidebarInterface(player, 92, 128);//Attack
		sendSidebarInterface(player, 320, 129);//Levels
		sendSidebarInterface(player, 274, 130);//Quest
		sendSidebarInterface(player, 149, 131);//Inventory
		sendSidebarInterface(player, 387, 132);//Equipment
		sendSidebarInterface(player, 271, 133);//Prayer
		sendSidebarInterface(player, 662, 135); //Summoning, disabled for handler. :P
		sendSidebarInterface(player, 192, 134);//Magic
		sendSidebarInterface(player, 550, 136);//Friends
		sendSidebarInterface(player, 551, 137);//Ignore
		sendSidebarInterface(player, 589, 138);//Clanchat
		sendSidebarInterface(player, 261, 139);//Options
		sendSidebarInterface(player, 464, 140);//Emotes
		sendSidebarInterface(player, 187, 141);//Music
		sendSidebarInterface(player, 182, 142);//Logout
		sendMessage(player, "Welcome to RuneScape.");
		sendSkill(player, 0, 99, 14000000);
		sendSkill(player, 1, 99, 14000000);
		sendSkill(player, 2, 99, 14000000);
		sendSkill(player, 3, 99, 14000000);
		sendSkill(player, 4, 99, 14000000);
		sendSkill(player, 5, 99, 14000000);
		sendSkill(player, 6, 99, 14000000);
		sendSkill(player, 7, 99, 14000000);
		sendSkill(player, 8, 99, 14000000);
		sendSkill(player, 9, 99, 14000000);
		sendSkill(player, 10, 99, 14000000);
		sendSkill(player, 11, 99, 14000000);
		sendSkill(player, 12, 99, 14000000);
		sendSkill(player, 13, 99, 14000000);
		sendSkill(player, 14, 99, 14000000);
		sendSkill(player, 15, 99, 14000000);
		sendSkill(player, 16, 99, 14000000);
		sendSkill(player, 17, 99, 14000000);
		sendSkill(player, 18, 99, 14000000);
		sendSkill(player, 19, 99, 14000000);
		sendSkill(player, 20, 99, 14000000);
		sendSkill(player, 21, 99, 14000000);
		sendSkill(player, 22, 99, 14000000);
		sendSkill(player, 23, 99, 14000000);
		sendSkill(player, 24, 99, 14000000);
		sendConfig(player, 1179, 100000000);
		sendConfig(player, 1230, 10);
		//sendInterface(player, 532);
		sendConfig(player, 313, -1);
		sendConfig(player, 465, -1);
		sendConfig(player, 802, -1);
		sendConfig(player, 1085, 249852);
		sendEnergy(player, 100);
		//sendSystemUpdate(player, 300);
		/*sendMusic2(player, 281, 1);
		sendMusic2(player, 282, 1);
		sendMusic2(player, 283, 1);
		sendMusic2(player, 284, 1);
		sendMusic2(player, 285, 1);
		sendMusic2(player, 286, 1);
		sendMusic2(player, 287, 1);
		sendMusic2(player, 288, 1);
		sendMusic2(player, 289, 1);
		sendMusic2(player, 290, 1);
		sendMusic2(player, 291, 1);
		sendMusic2(player, 292, 1);
		sendMusic2(player, 293, 1);
		sendMusic2(player, 294, 1);
		sendMusic2(player, 295, 1);*/
		//sendMusic2(player, 140, 1);
		sendInterface(player, 590);
		sendPlayerOption(player, "ololol", 1, true);
		//sendConfig(player, 1083, 262144);//turns on the toggle
		Connect.requestFriendServer(player);
		//sendFriendServerStatus(player, 2);
		//sendFriend(player, Utils.playerNameToLong("deathfire"), 1);
		//sendSentMessage(player, Utils.playerNameToLong("deathfire"), "lolwut?");
		//sendEnterText(player, "You're gonna find out, I'm about to summon it!");
		sendUpdateItem(player, 149, 0, 93, 5, new Item(4151, 1));
		//sendClanTest(player);
		String message = "Hello, World!";
		byte[] data = new byte[message.length()];
		Utils.textPack(data, message);
		sendClanMessage(player, Utils.playerNameToLong("hue hue"), Utils.playerNameToLong("lololol"), "It's going down like sabotage");
	}
	
	public static void sendWindowPane(Player player, int interfaceId) {
		player.send(new PacketBuilder(222, 3).putShort((short) interfaceId).putByteA(0));
	}
	
	public static void sendMapRegion(Player player) {
		try {
		player.addTemporary("LAST_KNOWN_REGION", player.getLocation());
		PacketBuilder pb = new PacketBuilder(44, 8192, Size.VAR_SHORT);
		boolean forceSend = true;
		for(int xCalc = (player.getLocation().getRegionX() - 6) / 8; xCalc <= ((player.getLocation().getRegionX() + 6) / 8); xCalc++) {
			for(int yCalc = (player.getLocation().getRegionY() - 6) / 8; yCalc <= ((player.getLocation().getRegionY() + 6) / 8); yCalc++) {
				int region = yCalc + (xCalc << 8);
				if(forceSend || ((yCalc != 49) && (yCalc != 149) && (yCalc != 147) && (xCalc != 50) && ((xCalc != 49) || (yCalc != 47)))) {
					int[] keys = MapData.getKeys(region);
					for (int key : keys) {
						//System.out.println("region = "+region+", key = "+key);
						pb.putInt1(key);
					}
				}
		    }
		}
		pb.putShort(player.getLocation().getRegionY());
		pb.putLEShortA(player.getLocation().getRegionX());
		pb.putLEShortA(player.getLocation().getLocalY());
		pb.putByteC((byte) player.getLocation().getZ());
		pb.putShortA(player.getLocation().getLocalX());
		//System.out.println("end?");
		player.send(pb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendSidebarInterface(Player player, int interfaceId, int icon) {
		sendInterface(player, 548, icon, interfaceId, true);
	}
	
	public static void sendInterface(Player player, int windowId, int position, int interfaceId, boolean walkable) {
		//player.getInterfaceState().interfaceOpened(interfaceId);
		PacketBuilder pb = new PacketBuilder(6, 7);
		pb.putInt2(windowId << 16 | position);
		pb.putByteA(walkable ? 1 : 0);
		pb.putLEShort(interfaceId);
		player.send(pb);
	}
	
	public static void sendMessage(Player player, String message) {
		player.send(new PacketBuilder(89, message.getBytes().length + 1, Size.VAR_BYTE).putRS2String(message));
	}
	
	public static void sendConfig(Player player, int id, int value) {
		if(value < 128) {
			sendConfig1(player, id, value);
		} else {
			sendConfig2(player, id, value);
		}
	}
	
 	public static void sendConfig1(Player player, int id, int value) {
 		PacketBuilder bldr = new PacketBuilder(246, 3);
 		bldr.putByteC(value);
 		bldr.putLEShortA(id);
 		player.send(bldr);
 	}
 	
 	public static void sendConfig2(Player player, int id, int value) {
 		PacketBuilder bldr = new PacketBuilder(250, 6);
 		bldr.putLEInt(value);
 		bldr.putLEShortA(id);
 		player.send(bldr);
 	}
	
	public static void sendSkill(Player player, int skill, int level, int exp) {
		PacketBuilder bldr = new PacketBuilder(239, 6);
		bldr.putInt1(exp);
		bldr.putByteS((byte) skill);
		bldr.putByteA((byte) level);
		player.send(bldr);
	}
 	
	public static void sendSkill(Player player, int skill) {
		sendSkill(player, skill, player.getSkills().getLevel(skill), (int)player.getSkills().getExperience(skill));
	}
	
    public static void sendEnergy(Player player, int energy) {
    	player.send(new PacketBuilder(205, 1).put((byte) energy));
    }
	
 	public static void sendSystemUpdate(Player player, int time) {
 		PacketBuilder bldr = new PacketBuilder(139, 2);
 		bldr.putShortA(time);
 		player.send(bldr);
 	}
 	
    public static void sendInterface(Player player, int id) {
		sendInterface(player, 548, 77, id, false);
    }

	public static void sendFriend(Player player, long name, int world, byte clanRank) {
		System.out.println("world = "+world);
		String string = world == 0 ? "Offline" : "RuneScape"+world;
		player.send(new PacketBuilder(191, (12 + string.getBytes().length), Size.VAR_BYTE).putLong(name).putShort((short) world).put((byte) clanRank).putRS2String(string));//the reason we have clan rank is because of the clan set up page. This way it knows. x3 In other words, the clan rank variable is what that player is in YOUR clan.
	}
    
	public static void sendMusic(Player player, int music) {
		player.send(new PacketBuilder(172, 2).putShortA(music));
	}
	
	public static void sendMusic2(Player player, int songId, int unknown) {
		player.send(new PacketBuilder(132, 5).putTriByte(unknown).putShort((short) songId));
	}

	public static void sendPlayerOption(Player player, String option, int slot, boolean top) {
		player.send(new PacketBuilder(247, (3 + option.getBytes().length), Size.VAR_BYTE).putRS2String(option).putByteS((byte) (top ? 1 : 0)).putByteS((byte) slot));
	}
	
	public static void sendFriendServerStatus(Player player, int status) {
		System.out.println("lolwut");
		player.send(new PacketBuilder(137, 1).put(status));
	}

	//public static void sendSentMessage(Player player, long to, String message) {
	//	player.send(new PacketBuilder(11, 9 + message.getBytes().length).putLong(to).putRS2String(message));
	//}

    public static void sendClanChat(Player player, long clanName, byte kick, long clanOwner, long[] usernames, byte[][] ranksAndWorlds) {
    	System.out.println("sendClanChat");
    	PacketBuilder pb = new PacketBuilder(150, 8192, Size.VAR_SHORT);
    	pb.putLong(clanOwner);
    	pb.putLong(clanName);
    	pb.put((byte) 7);//kick limit?
    	pb.put((byte) usernames.length);
    	for (int i = 0; i < usernames.length; i++) {
    		pb.putLong(usernames[i]);
    		pb.putShort(ranksAndWorlds[i][0]);//world
    		pb.put(ranksAndWorlds[i][1]);
    		pb.putRS2String("RuneScape "+ranksAndWorlds[i][0]);
    	}
    	player.send(pb);
    }
    
    public static void sendLeaveClanChat(Player player) {
    	player.send(new PacketBuilder(150, 8, Size.VAR_SHORT).putLong(0));
    }
    
    public static void sendClanTest(Player player) {
    	//the 1 in (1 * 11) is going to be a variable for how many players are in the clan.
    	PacketBuilder pb = new PacketBuilder(150, 18 + (1 * 11),Size.VAR_SHORT);
    	pb.putLong(Utils.playerNameToLong("Herp"));
    	pb.putLong(Utils.playerNameToLong("Derp"));
    	pb.put((byte) 7);
    	pb.put((byte) 1);
    		pb.putLong(Utils.playerNameToLong("Deathfire138"));//long
    		pb.putShort(1);//short
    		pb.put((byte) 3);//byte
    	player.send(pb);
    }

    public static void sendReceivedPrivateMessage(Player player, long from, byte rights, byte[] message) {
    	player.send(new PacketBuilder(232, 14 + message.length, Size.VAR_BYTE).putLong(from).putShort((short) Short.MAX_VALUE).putTriByte(player.getNextUniqueId()).put(rights).put(message));
    }
    
    public static void sendSentPrivateMessage(Player player, long from, byte[] message) {
    	player.send(new PacketBuilder(11, 8 + message.length, Size.VAR_BYTE).putLong(from).put(message));
    }
    
    public static void sendEnterText(Player player, String title) {
    	runScript(player, 109, new Object[]{title}, "s");
    }
    
	public static void runScript(Player player, int value, Object[] objects, String string) {
		if (string.length() != objects.length) {
			throw new IllegalArgumentException("Argument array size mismatch");
		}
		PacketBuilder pb = new PacketBuilder(58, 8192, Size.VAR_SHORT);
		pb.putRS2String(string);
		int j = 0;
		for (int i = (string.length() - 1); i >= 0; i--) {
			if (string.charAt(i) == 115) {
				pb.putRS2String((String) objects[j]);
			} else {
				pb.putInt((Integer) objects[j]);
			}
			j++;
		}
		pb.putInt(value);
		player.send(pb);
	}
 
	public static void sendUpdateItems(Player player, int interfaceId, int childId, int type, Item[] items) {
		PacketBuilder pb = new PacketBuilder(104, 8192, Size.VAR_SHORT).putInt(interfaceId << 16 | childId).putShort((short) type).putShort((short) items.length);
	       for (int i = 0; i < items.length; i++) {
	            int id, amt;
	            if (items[i] == null) {
	                id = -1;
	                amt = 0;
	            } else {
	                id = items[i].getId();
	                amt = items[i].getAmount();
	            }
				pb.putLEShortA((short) (id+1));
			if(amt > 254) {
				pb.put((byte) 255);
				pb.putInt(amt);
			} else {
				pb.put((byte) amt);
			}
		}
		player.send(pb);
	}

	public static void sendUpdateItem(Player player, int interfaceId, int childId, int type, int slot, Item item) {
		PacketBuilder pb = new PacketBuilder(159, 8192, Size.VAR_SHORT).putInt(interfaceId << 16 | childId).putShort((short) type).putSmart(slot);
		if(item != null) {
			int id = item.getId() + 1;
			pb.putShort((short) (id));
			if(id != 0) {
				int count = item.getAmount();
				if(count > 254) {
					pb.put((byte) 255);
					pb.putInt(count);
				} else {
					pb.put((byte) count);
				}
			}
		} else {
			pb.putShort((short) 0);
		}
		player.send(pb);
	}

	public static void sendCloseInterface(Player player, int windowId, int position) {
		player.send(new PacketBuilder(167, 4).putInt(windowId << 16 | position));
	}

	public static void sendLogout(Player player) {
		player.send(new PacketBuilder(207, 0));
	}
	
	public static void sendString(Player player, int interfaceId, int childId, String string) {
		player.send(new PacketBuilder(99, 5 + string.getBytes().length, Size.VAR_SHORT).putLEInt(interfaceId << 16 | childId).putRS2String(string));
	}
	
	public static void sendClanMessage(Player player, long sender, long clanPrefix, String message) {
		System.out.println("va jay jay");
		PacketBuilder packet = new PacketBuilder(183, 8192/*24 + message.length()*/, Size.VAR_BYTE);
		packet.putLong(sender);
		packet.put((byte) 1);//Luke has this as a dummy, I know that obfuscation doesn't randomly add bytes into streams, it must go to something. Trace later.
		packet.putLong(clanPrefix);
		packet.putShort(0);
		packet.putTriByte(player.getNextUniqueId());
		packet.put(player.getRight());
		byte[] bytes = new byte[message.length()];
		//bytes[0] = (byte) message.length();
		Utils.textPack(bytes, message);
		//int offset = 1 + Utils.encryptPlayerChat(bytes, 0, 1, message.length(), message.getBytes());
		//packet.put(bytes);
		//packet.put(bytes, offset, bytes.length - offset);
		packet.put(bytes);
		player.send(packet);
	}
	
}