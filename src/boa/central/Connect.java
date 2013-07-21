package boa.central;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import boa.Server;
import boa.game.communication.clan.ClanSettings;
import boa.util.Utils;


import net.ActionSender;

import model.Player;
import model.World;

public class Connect implements Runnable {

	private static final Logger logger = Logger.getLogger(Connect.class.getName());

	private static final String LOGIN_SERVER_IP = "127.0.0.1";
	private static final int LOGIN_SERVER_PORT = 43595;

	private static final String LOGIN_SERVER_PASSWORD = "yvutGYGFkugyg%j8757ffu(HIt7f%$&c665c&86go8O*hi7Gt765dD%Yfgop9h";

	private static Socket socket;
	private static DataInputStream in;
	private static DataOutputStream out;

	@SuppressWarnings("unused")
	private static boolean isOnline = false;

	@SuppressWarnings("unused")
	private static boolean isAuthorized = false;
	
	//TODO Holy shit, please fix this. Oh my god. (try netty if you can. That'd be cool. If not, just organize. x.x)
	
	@Override
	public void run() {
		logger.info("Connecting to login server...");
		while (connect());
		logger.info("Connected to login server.");
		while (true) {
			try {
				//if (!isAuthorized) {
					//System.out.println("is not authorized");
					//if (in.read() == 0) {
					//System.out.println(in.available());
					//if (in.available() == 0) {
					//	continue;
					//}
						//System.out.println("read was 0");
					if (in.available() < 1) {
						//System.out.println("lol");
						continue;
					} else {
						int opcode = in.read();
						System.out.println("opcode = "+opcode);
						if (opcode == 0) {
							switch(in.read()) {
							case 0:
								logger.info("Incorrect password. System shutting down.");
								System.exit(0);
								break;
							case 1:
								isAuthorized = true;
								logger.info("Central Server connection authorized!");
								Server.isOnline = true;
								break;
							case 2:
								updateFriends(in);
								break;
							}
							continue;
						} else if (opcode == 1) {//Authenticate login
							System.out.println("Login authenticated!");
							Player player = World.getPlayerFromName(Utils.getRS2String(in));
							int returncode = in.read();
							player.addTemporary("RETURNCODE", returncode);
							if (returncode == 2) {
								player.setRight(in.read());
								player.setMembers(in.read() == 1 ? true : false);
							}
							player.setIsAuthenticated(true);
						} else if (opcode == 2) {//Friends List
							//Player player = World.getPlayerFromName(Utils.getRS2String(in));
							updateFriends(in);
						
						} else if (opcode == 4) {//Friend logged-in status changes.
							ActionSender.sendFriend(World.getPlayerFromName(Utils.longToPlayerName(in.readLong())), in.readLong(), in.read(), in.readByte());
						} else if (opcode == 5) {//Friend Server
							System.out.println("lolwuts?");
							ActionSender.sendFriendServerStatus(World.getPlayerFromName(Utils.longToPlayerName(in.readLong())), in.read());
						} else if (opcode == 6) {//receive private message
							Player receiver = World.getPlayerFromName(Utils.longToPlayerName(in.readLong()));
							long from = in.readLong();
							byte rights = in.readByte();
							byte[] data = new byte[in.available()];
							in.read(data);
							ActionSender.sendReceivedPrivateMessage(receiver, from, rights, data);
							//ActionSender.sendSentPrivateMessage(from, receiver.getName(), data);
						} else if (opcode == 7) {//
							Player receiver = World.getPlayerFromName(Utils.longToPlayerName(in.readLong()));
							ActionSender.sendMessage(receiver, Utils.getRS2String(in));
						} else if (opcode == 9) {//update clan settings interface
							System.out.println("9");
							ClanSettings.updateSettingsInterface(World.getPlayerFromName(Utils.longToPlayerName(in.readLong())), in.readByte() == 1 ? true : false, in.readByte(), in.readByte(), in.readByte(), in.readByte(), Utils.getRS2String(in));
						} else if (opcode == 10) {//send sent private message
							Player sender = World.getPlayerFromName(Utils.longToPlayerName(in.readLong()));
							long to = in.readLong();
							byte[] data = new byte[in.available()];
							in.read(data);
							ActionSender.sendSentPrivateMessage(sender, to, data);
						} else if (opcode == 11) {//update clan
							System.out.println("opcode 11");
							Player player = World.getPlayerFromName(Utils.longToPlayerName(in.readLong()));
							long clanOwner = in.readLong();
							long clanPrefix = in.readLong();
							byte kick = in.readByte();
							byte numOfUsers = in.readByte();
							long[] usernames = new long[numOfUsers];
							byte[][] ranksAndWorlds = new byte[numOfUsers][2];
							for (int i = 0; i < numOfUsers; i++) {
								usernames[i] = in.readLong();
								ranksAndWorlds[i][0] = in.readByte();//world
								ranksAndWorlds[i][1] = in.readByte();//rank
							}
							ActionSender.sendClanChat(player, clanPrefix, kick, clanOwner, usernames, ranksAndWorlds);
							player.setIsInClanChat(true);
						} else if (opcode == 12) {
							System.out.println("12");
							Player player = World.getPlayerFromName(Utils.longToPlayerName(in.readLong()));
							long sender = in.readLong();
							long prefix = in.readLong();
							String message = Utils.getRS2String(in);
							System.out.println("message = "+message);
							ActionSender.sendClanMessage(player, sender, prefix, message);
						}
					//} else {
						//System.out.println("read was not 0");
					//}
					}
				//}
				Thread.sleep(250);
			} catch (Exception e) {
				logger.info("Lost connection with login sever trying to reconnect...");
				while (reconnect());
				logger.info("Connected to login server.");
			}
		}
	}
					/*if (in.available() > 0) {
						if (in.readBoolean()) {
							isAuthorized = true;
							isOnline = true;
						}
						logger.info((isAuthorized ? "Login Server Connection is authorized." : "Retrying to authoriz the login server connection..."));
					}
				} else {
					if (isOnline) {
						if (in.available() > 0) {
							byte[] data = new byte[in.available()];
							in.read(data);
							ByteBuffer in = ByteBuffer.wrap(data);
							while (in.hasRemaining()) {
								int id = in.get();
								System.out.println("Id = "+id);*/
//								if (id == 0) { // Updates the world info on this server
//									int loop = in.getShort();
//									for (int i = 0; i < loop; i++) {
//										int worldId = in.getShort();
//										WorldManager.register(worldId);
//										int count = in.getShort();
//										for (int l = 0; l < count; l++) {
//											String name = Utils.getRS2String(in);
//											int right = in.get();
//											int status = in.get();
//											int lenght = in.get();
//											List<String> friends = new ArrayList<String>(200);
//											List<String> ignores = new ArrayList<String>(100);
//											for (int j = 0; j < lenght; j++) {
//												friends.add(Utils.getRS2String(in));
//											}
//											lenght = in.get();
//											for (int j = 0; j < lenght; j++) {
//												ignores.add(Utils.getRS2String(in));
//											}
//											WorldManager.registerPlayer(worldId, name, right, status, friends, ignores);
//										}
//									}
//								} else if (id == 1) {
//									int worldId = in.getShort();
//									WorldManager.register(worldId);
//								} else if (id == 2) {
//									int worldId = in.getShort();
//									WorldManager.unregister(worldId);
//								} else if (id == 3) {
//									int worldId = in.getShort();
//									String username = Utils.getRS2String(in);
//									int right = in.get();
//									int status = in.get();
//									int lenght = in.get();
//									List<String> friends = new ArrayList<String>(200);
//									List<String> ignores = new ArrayList<String>(100);
//									for (int j = 0; j < lenght; j++) {
//										friends.add(Utils.getRS2String(in));
//									}
//									lenght = in.get();
//									for (int j = 0; j < lenght; j++) {
//										ignores.add(Utils.getRS2String(in));
//									}
//									WorldManager.registerPlayer(worldId, username, right, status, friends, ignores);
//								} else if (id == 4) {
//									int worldId = in.getShort();
//									String username = Utils.getRS2String(in);
//									WorldManager.unregisterPlayer(worldId, username);
//								} else if (id == 5) {
//									int worldId = in.getShort();
//									String username = Utils.getRS2String(in);
//									int right = in.get();
//									WorldManager.setPlayerRight(worldId, username, right);
//								} else if (id == 6) {
//									int worldId = in.getShort();
//									String username = Utils.getRS2String(in);
//									int status = in.get();
//									WorldManager.setPlayerStatus(worldId, username, status);
//								} else if (id == 7) {
//									int worldId = in.getShort();
//									String username = Utils.getRS2String(in);
//									int otherId = in.getShort();
//									String other = Utils.getRS2String(in);
//									WorldManager.addFriend(worldId, username, otherId, other);
//								} else if (id == 8) {
//									int worldId = in.getShort();
//									String username = Utils.getRS2String(in);
//									int otherId = in.getShort();
//									String other = Utils.getRS2String(in);
//									WorldManager.removeFirend(worldId, username, otherId, other);
//								} else if (id == 9) {
//									int worldId = in.getShort();
//									String username = Utils.getRS2String(in);
//									int otherId = in.getShort();
//									String other = Utils.getRS2String(in);
//									WorldManager.addIgnore(worldId, username, otherId, other);
//								} else if (id == 10) {
//									int worldId = in.getShort();
//									String username = Utils.getRS2String(in);
//									int otherId = in.getShort();
//									String other = Utils.getRS2String(in);
//									WorldManager.removeIgnore(worldId, username, otherId, other);
//								} else if (id == 11) {
//									String username = Utils.getRS2String(in);
//									int right = in.get();
//									String other = Utils.getRS2String(in);
//									byte[] packed = new byte[in.remaining()];
//									in.get(packed);
//									WorldManager.sendPrivateMessage(username, right, other, packed);
//								}

	public static boolean connect() {
		try {
			socket = new Socket(LOGIN_SERVER_IP, LOGIN_SERVER_PORT);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			ByteBuffer out = ByteBuffer.allocate(5 + 
					(LOGIN_SERVER_PASSWORD.getBytes().length) + 
					(Server.activity.getBytes().length));
			out.put(LOGIN_SERVER_PASSWORD.getBytes());
			out.put((byte) 0);
			out.put((byte) Server.worldId);
			out.put((byte) (Server.members ? 1 : 0));
			out.put((byte) Server.location);
			out.put(Server.activity.getBytes());
			out.put((byte) 0);
			send(out);
			return false;
		} catch (Exception e) {
			isOnline = false;
			e.printStackTrace();
			try {
				Thread.sleep(500);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logger.info("Connecting failed, retrying...");
			return true;
		}
	}

	public static void send(ByteBuffer buffer) {
		try {
			if (buffer.hasArray()) {
				byte[] data = (byte[]) buffer.flip().array();
				out.write(data);
				out.flush();
			} else {
				int size = buffer.position();
				byte[] data = new byte[size + 1];
				for (int i = 1; i < size + 1; i++) {
					data[i] = buffer.get();
				}
				send(ByteBuffer.wrap(data));
			}
		} catch (Exception e) {
			logger.info("Lost connection with login sever trying to reconnect...");
			while (reconnect());
			logger.info("Connected to login server.");
			send(buffer);
		}
	}

	public static boolean reconnect() {
		try {
			socket = new Socket(LOGIN_SERVER_IP, LOGIN_SERVER_PORT);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			ByteBuffer out = ByteBuffer.allocate(5 + (LOGIN_SERVER_PASSWORD.getBytes().length) + (Server.activity.getBytes().length));
			out.put(LOGIN_SERVER_PASSWORD.getBytes());
			out.put((byte) 0);
			out.put((byte) Server.worldId);
			out.put((byte) (Server.members ? 1 : 0));
			out.put((byte) Server.location);
			out.put(Server.activity.getBytes());
			out.put((byte) 0);
			send(out);
			return false;
		} catch (Exception e) {
			isOnline = false;
			try {
				Thread.sleep(500);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logger.info("Connecting failed, retrying...");
			return true;
		}
	}

	public void checkFileContinuity() {
		
	}

	public static void loginRequest(Player player) {
		ByteBuffer out = ByteBuffer.allocate(3 + (player.getUsername().getBytes().length) + (player.getPassword().getBytes().length));
		out.put((byte) 1); //opcode
		out.put(player.getUsername().getBytes());
		out.put((byte) 0);
		out.put(player.getPassword().getBytes());
		out.put((byte) 0);
		send(out);
	}
	
	public void updateFriends(DataInputStream buffer) throws IOException {
		long regardingPlayer = buffer.readLong();
		int world = buffer.read();
		String[] affectedPlayers = new String[buffer.readShort()];
		for(int i = 0; i < affectedPlayers.length; i++) {
			Player affectedPlayer = null;//World.getPlayerFromName(Utils.longToPlayerName(buffer.readLong()));
			byte clanRank = buffer.readByte();
			ActionSender.sendFriend(affectedPlayer, regardingPlayer, world, clanRank);
		}
		return;
	}
	
	public static void requestFriendServer(Player player) {
		ByteBuffer buffer = ByteBuffer.allocate(9);
		buffer.put((byte) 3);
		buffer.putLong(Utils.playerNameToLong(player.getUsername()));
		send(buffer);
	}
	
	public static void addFriend(Player player, long name) {
		ByteBuffer buffer = ByteBuffer.allocate(17);
		buffer.put((byte) 4);
		buffer.putLong(Utils.playerNameToLong(player.getUsername()));
		buffer.putLong(name);
		send(buffer);
	}
	
	public static void removeFriend(Player player, long name) {
		ByteBuffer buffer = ByteBuffer.allocate(17);
		buffer.put((byte) 5);
		buffer.putLong(Utils.playerNameToLong(player.getUsername()));
		buffer.putLong(name);
		send(buffer);
	}

	public static void sendMessage(Player player, long to, byte[] data) {
		ByteBuffer buffer = ByteBuffer.allocate(17 + data.length);
		buffer.put((byte) 6);
		buffer.putLong(Utils.playerNameToLong(player.getUsername()));
		buffer.putLong(to);
		buffer.put(data);
		send(buffer);
	}

	public static void updateChatSettings(Player player, byte publicChat, byte privateChat, byte trade) {
		ByteBuffer buffer = ByteBuffer.allocate(12);
		buffer.put((byte) 7);
		buffer.putLong(Utils.playerNameToLong(player.getUsername()));
		buffer.put(publicChat);
		buffer.put(privateChat);
		buffer.put(trade);
		send(buffer);
	}

	public static void setClanName(Player player, String string) {
		ByteBuffer buffer = ByteBuffer.allocate(10 + string.getBytes().length);
		buffer.put((byte) 8);
		buffer.putLong(Utils.playerNameToLong(player.getUsername()));
		buffer.put(string.getBytes());
		buffer.put((byte) 0);
		send(buffer);
	}

	/**
	 * 
	 * @param player
	 * @param setting 0 = enter, 1 = talk, 2 = kick, 3 = loot, 4 = CoinShare
	 * @param value 
	 */
	public static void setClanSetting(Player player, byte setting, byte value) {
		ByteBuffer buffer = ByteBuffer.allocate(11);
		buffer.put((byte) 9);
		buffer.putLong(Utils.playerNameToLong(player.getUsername()));
		buffer.put(setting);
		buffer.put(value);
		send(buffer);
	}

	public static void logoutRequest(Player player) {//IN FUTURE, WILL BE USED TO SEND DATA FOR PLAYER SAVING
		ByteBuffer buffer = ByteBuffer.allocate(9);
		buffer.put((byte) 10);
		buffer.putLong(Utils.playerNameToLong(player.getUsername()));
		send(buffer);
	}

	public static void requestToJoinClan(Player player, long clanOwner) {
		ByteBuffer buffer = ByteBuffer.allocate(17);
		buffer.put((byte) 11);
		buffer.putLong(Utils.playerNameToLong(player.getUsername()));
		buffer.putLong(clanOwner);
		send(buffer);
	}

	public static void adjustRank(Player player, long member, byte rank) {
		ByteBuffer buffer = ByteBuffer.allocate(18);
		buffer.put((byte) 12);
		buffer.putLong(Utils.playerNameToLong(player.getUsername()));
		buffer.putLong(member);
		buffer.put(rank);
		send(buffer);
	}

	public static void sendClanMessage(Player player, String message) {
		System.out.println("lolololololol");
		ByteBuffer buffer = ByteBuffer.allocate(10 + message.getBytes().length);
		buffer.put((byte) 13);
		buffer.putLong(Utils.playerNameToLong(player.getUsername()));
		buffer.put(message.getBytes());
		buffer.put((byte) 0);
		send(buffer);
	}
	
}