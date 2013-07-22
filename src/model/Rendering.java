package model;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

import boa.io.ActionSender;
import boa.io.PacketBuilder;
import boa.io.PacketBuilder.Size;
import boa.util.Utils;

import model.UpdateFlags.UpdateFlag;
import model.region.RegionManager;


public final class Rendering {
	
	public static void sendRendering(Player player) {
		if (player.getTemporary("MAP_REGION_CHANGING") != null) {
			ActionSender.sendMapRegion(player);
		}
		PacketBuilder updateBlock = new PacketBuilder(-1, 8192);
		PacketBuilder updatePacket = new PacketBuilder(50, 8192, Size.VAR_SHORT);
		updatePacket.startBitAccess();
		updateThisPlayerMovement(player, updatePacket);
		updatePlayer(updateBlock, player, player, false);
		updatePacket.putBits(8, player.getLocalPlayers().size());
		List<Player> localPlayers = RegionManager.getLocalPlayers(player);
		for (final Iterator<Player> it$ = player.getLocalPlayers().iterator(); it$.hasNext();) {
			final Player otherPlayer = it$.next();
			if (localPlayers.contains(otherPlayer) && otherPlayer.getTemporary("TELEPORT_TARGET") == null && !otherPlayer.isHidden()) {
				updatePlayerMovement(updatePacket, otherPlayer);
				if (otherPlayer.getUpdateFlags().isUpdateRequired()) {
					updatePlayer(updateBlock, player, otherPlayer, false);
				}
			} else {
				it$.remove();
				updatePacket.putBits(1, 1);
				updatePacket.putBits(2, 3);
			}
		}
		for (final Player otherPlayer : localPlayers) {
			if (player.getLocalPlayers().size() >= 255) {
				break;
			}
			if (otherPlayer == player || player.getLocalPlayers().contains(otherPlayer) || player.isHidden()) {
				continue;
			}
			player.getLocalPlayers().add(otherPlayer);
			addNewPlayer(updatePacket, player, otherPlayer);
			updatePlayer(updateBlock, player, otherPlayer, true);
		}
		if (!updateBlock.isEmpty()) {
			updatePacket.putBits(11, 2047);
			updatePacket.finishBitAccess();
			updatePacket.put((ByteBuffer) updateBlock.getBuffer().flip());
		} else {
			updatePacket.finishBitAccess();
		}
		player.send(updatePacket);
		/*updateBlock = new PacketBuilder(-1, 8192);
		updatePacket = new PacketBuilder(87, 8192, Size.VAR_SHORT);
		updatePacket.startBitAccess();
		updatePacket.putBits(8, player.getLocalNpcs().size());
		List<Npc> localNpcs = RegionManager.getLocalNpcs(player);
		for (final Iterator<Npc> it$ = player.getLocalNpcs().iterator(); it$.hasNext();) {
			final Npc npc = it$.next();
			if (localNpcs.contains(npc) && npc.getTemporary("TELEPORT_TARGET") == null && !npc.isHidden()) {
				updateNPCMovement(updatePacket, npc);
				if (npc.getUpdateFlags().isUpdateRequired()) {
					updateNPC(updateBlock, npc);
				}
			} else {
				it$.remove();
				updatePacket.putBits(1, 1);
				updatePacket.putBits(2, 3);
			}
		}
		for (final Npc npc : localNpcs) {
			if (player.getLocalNpcs().size() >= 255) {
				break;
			}
			if (player.getLocalNpcs().contains(npc) || npc.isHidden()) {
				continue;
			}
			player.getLocalNpcs().add(npc);
			addNewNPC(updatePacket, player, npc);
			if (npc.getUpdateFlags().isUpdateRequired()) {
				updateNPC(updateBlock, npc);
			}
		}
		if (!updateBlock.isEmpty()) {
			updatePacket.putBits(15, 32767);
			updatePacket.finishBitAccess();
			updatePacket.put((ByteBuffer) updateBlock.getBuffer().flip());
		} else {
			updatePacket.finishBitAccess();
		}
		player.send(updatePacket);*/
	}

	private static void addNewPlayer(PacketBuilder packet, Player player, Player otherPlayer) {
		packet.putBits(11, otherPlayer.getIndex());
		int yPos = otherPlayer.getLocation().getY() - player.getLocation().getY();
		int xPos = otherPlayer.getLocation().getX() - player.getLocation().getX();
		/*if (xPos < 0) {
			xPos += 32;
		}
		if (yPos < 0) {
			yPos += 32;
		}*/
		packet.putBits(1, 1);
		packet.putBits(5, yPos);
		packet.putBits(1, 1);
		packet.putBits(3, 1);//this is set to 1 in Hyperion. Strange, we should find out what it does.
		packet.putBits(5, xPos);

	} // done

	private static void updatePlayerMovement(PacketBuilder packet, Player otherPlayer) {
		if (otherPlayer.getSprites().getPrimarySprite() == -1) {
			if (otherPlayer.getUpdateFlags().isUpdateRequired()) {
				packet.putBits(1, 1);
				packet.putBits(2, 0);
			} else {
				packet.putBits(1, 0);
			}
		} else if (otherPlayer.getSprites().getSecondarySprite() == -1) {
			packet.putBits(1, 1);
			packet.putBits(2, 1);
			packet.putBits(3, otherPlayer.getSprites().getPrimarySprite());
			packet.putBits(1, otherPlayer.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		} else {
			packet.putBits(1, 1);
			packet.putBits(2, 2);
			packet.putBits(3, otherPlayer.getSprites().getPrimarySprite());
			packet.putBits(3, otherPlayer.getSprites().getSecondarySprite());
			packet.putBits(1, otherPlayer.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		}
	} // done

	private static void updatePlayer(PacketBuilder packet, Player player, Player otherPlayer, boolean forceAppearance) {
		if (!otherPlayer.getUpdateFlags().isUpdateRequired() && !forceAppearance) {
			return;
		}
		synchronized (otherPlayer) {
			int mask = 0x0;
			final UpdateFlags flags = otherPlayer.getUpdateFlags();
			if (flags.get(UpdateFlag.GRAPHICS)) {
				mask |= 0x400;
			}
			if (flags.get(UpdateFlag.CHAT)) {
				mask |= 0x40;
			}
			if (flags.get(UpdateFlag.APPEARANCE) || forceAppearance) {
				mask |= 2;
			}
			if (flags.get(UpdateFlag.ANIMATION)) {
				mask |= 0x1;
			}
			if (flags.get(UpdateFlag.FACE_ENTITY)) {
				mask |= 0x20;
			}
			if (flags.get(UpdateFlag.HIT)) {
				mask |= 0x80;
			}
			if (flags.get(UpdateFlag.HIT_2)) {
				mask |= 0x200;
			}
			if (flags.get(UpdateFlag.FORCED_CHAT)) {
				mask |= 0x10;
			}
			if (flags.get(UpdateFlag.FACE_COORDINATE)) {
				mask |= 0x4;
			}
			/* todo
			if (flags.get(UpdateFlag.FORCED_WALKING)) {
				mask |= 0x400;
			}*/

			if (mask >= 0x80) {
				mask |= 0x8;
				packet.put((byte) (mask & 0xFF));
				packet.put((byte) (mask >> 8));
			} else {
				packet.put((byte) mask);
			}
			if (flags.get(UpdateFlag.GRAPHICS)) {
				appendGraphicsUpdate(packet, otherPlayer);
			}
			if (flags.get(UpdateFlag.CHAT)) {
				appendChatUpdate(packet, otherPlayer);
			}
			if (flags.get(UpdateFlag.APPEARANCE) || forceAppearance) {
				appendPlayerAppearanceUpdate(packet, otherPlayer);
			}
			if (flags.get(UpdateFlag.ANIMATION)) {
				appendAnimationUpdate(packet, otherPlayer);
			}
			if (flags.get(UpdateFlag.FACE_ENTITY)) {
				final Entity entity = ((Entity)otherPlayer.getTemporary("FACE_ENTITY"));;
				packet.putShortA((short)(entity == null ? -1 : entity.getClientIndex()));
			}
			if (flags.get(UpdateFlag.HIT)) {
				appendHitUpdate(otherPlayer, packet);
			}
			if (flags.get(UpdateFlag.HIT_2)) {
				appendHit2Update(otherPlayer, packet);
			}
			if (flags.get(UpdateFlag.FORCED_CHAT)) {
				packet.putRS2String((String)otherPlayer.getTemporary("FORCED_CHAT"));
			}
			if (flags.get(UpdateFlag.FACE_COORDINATE)) {
				appendFaceLocationUpdate(packet, otherPlayer);
			}
			/* Todo
			if (flags.get(UpdateFlag.FORCED_WALKING)){
				appendForcedWalkingUpdate(packet, otherPlayer);
			}*/
		}
	} // done

	private static void appendChatUpdate(PacketBuilder packet, Player otherPlayer) {
//		ChatMessage cm = otherPlayer.getCurrentChatMessage();
//		if (cm != null) {
//			byte[] chatData = new byte[256];
//			packet.putLEShortA((short) ((cm.getColor() & 0xFF) << 8 | cm.getEffects() & 0xFF));
//			packet.put((byte) otherPlayer.getRight());
//			/*chatData[0] = (byte) cm.getText().length();*/
//			int offset = 1 + Utils.encryptPlayerChat(chatData, 0, 1, cm.getText().length(), cm.getText().getBytes());
//			//packet.putByteS((byte) offset);
//			packet.putByteS((byte) cm.getText().getBytes().length);
//			packet.putBytesReverseA(chatData, cm.getText().getBytes().length, 0);
//			System.out.println(chatData.toString());
			ChatMessage cm = otherPlayer.getCurrentChatMessage();
			if (cm != null) {
				byte[] chatData = new byte[256];
				packet.putLEShortA((short) ((cm.getColor() & 0xFF) << 8 | cm.getEffects() & 0xFF));
				packet.put((byte) otherPlayer.getRight());
				chatData[0] = (byte) cm.getText().length();
				int offset = 1 + Utils.encryptPlayerChat(chatData, 0, 1, cm.getText().length(), cm.getText().getBytes());
				packet.putByteS((byte) offset);
				//packet.put(chatData, 0, offset);
				packet.putBytesReverseA(chatData, offset, 0);
		}
	}

	private static void appendAnimationUpdate(final PacketBuilder updateBlock, final Player otherPlayer) {
		Animation animation = (Animation) otherPlayer.getTemporary("ANIMATION");
		if (animation != null) {
			updateBlock.putShort((short)animation.getId());
			updateBlock.putByteC((byte)animation.getDelay());
		}
	}

	private static void appendGraphicsUpdate(final PacketBuilder updateBlock, final Player otherPlayer) {
		Graphic graphic = (Graphic) otherPlayer.getTemporary("GRAPHICS");
		if (graphic != null) {
			updateBlock.putLEShort((short)graphic.getId());
			updateBlock.putInt2(graphic.getDelay());
		}
	}
	
	private static void appendFaceLocationUpdate(final PacketBuilder updateBlock, final Player otherPlayer) {
		Location location = (Location) otherPlayer.getTemporary("FACE_LOCATION");
		if (location != null) {
			updateBlock.putShortA((short) location.getX());
			updateBlock.putShort((short) location.getY());
		} else {
			updateBlock.putShortA((short) 0);
			updateBlock.putShort((short) 0);
		}
	}

	public static void appendForcedWalkingUpdate(PacketBuilder updateBlock, Player player) { // todo
	}
	
	private static void appendHit2Update(Player player, PacketBuilder updateBlock) { // todo the system the bytes are right
		updateBlock.put((byte) player.getDamage().getHitDamage2());
		updateBlock.put((byte) player.getDamage().getHitType2());
	}

	private static void appendHitUpdate(Player player, PacketBuilder updateBlock) {  // todo the system the bytes are right
		updateBlock.putByteC((byte) player.getDamage().getHitDamage1());
		updateBlock.putByteA((byte) player.getDamage().getHitType1());
		//updateBlock.put((byte) ((player.getSkills().getLevel(3) * 30) / player.getSkills().getLevelForExperience(3)));
		updateBlock.put((byte) player.getSkills().getLevelForExperience(3));
	}
	
	private static void updateThisPlayerMovement(Player player, final PacketBuilder packet) {
		if (player.getTemporary("TELEPORT_TARGET") != null || player.getTemporary("MAP_REGION_CHANGING") != null) {
			System.out.println("teleport or change map");
			packet.putBits(1, 1);
			packet.putBits(2, 3);
			packet.putBits(1, 1);
			//System.out.println("hue hue hue = "+player.getTemporary("LAST_KNOWN_REGION"));
			//packet.putBits(7, player.getLocation().getLocalX((Location)player.getTemporary("LAST_KNOWN_REGION")));
			packet.putBits(7, player.getLocation().getLocalX());
			//System.out.println("last known region x");
			packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
			packet.putBits(2, player.getLocation().getZ());
			//packet.putBits(7, player.getLocation().getLocalY((Location)player.getTemporary("LAST_KNOWN_REGION")));
			packet.putBits(7, player.getLocation().getLocalY());
		} else {
			if (player.getSprites().getPrimarySprite() == -1) {
				packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
				if (player.getUpdateFlags().isUpdateRequired()) {
					packet.putBits(2, 0);
				}
			} else {
				if (player.getSprites().getSecondarySprite() != -1) {
					//Music.playMusic(player);
					//System.out.println("run?");
					packet.putBits(1, 1);
					packet.putBits(2, 2);
					packet.putBits(3, player.getSprites().getPrimarySprite());
					packet.putBits(3, player.getSprites().getSecondarySprite());
					packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
				} else {
					//System.out.println("walk?");
					//Music.playMusic(player);
					packet.putBits(1, 1);
					packet.putBits(2, 1);
					packet.putBits(3, player.getSprites().getPrimarySprite());
					packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
				}
			}
		}
	} // done

	private static void appendPlayerAppearanceUpdate(PacketBuilder packet, Player otherPlayer) {
		final Appearance app = otherPlayer.getAppearance();
		final PacketBuilder playerProps = new PacketBuilder(-1, 8192);
		playerProps.put((byte) app.getGender()); // gender

		playerProps.put((byte) -1); // icons
		playerProps.put((byte) -1); // icons
		
		if(app.isNpc()) {
			playerProps.putShort((short) -1);
			playerProps.putShort((short) app.getNpcId());
		} else {
			for (int i = 0; i < 4; i++) {
				if (otherPlayer.getEquipment().get(i) != null) {
					playerProps.putShort((short) (32768 + EquipmentDefinition.get(otherPlayer.getEquipment().get(i).getId()).getEquipmentId()));
				} else {
					playerProps.put((byte) 0);
				}
			}
			if (otherPlayer.getEquipment().get(Equipment.SLOT_CHEST) != null) {
				playerProps.putShort((short) (32768 + EquipmentDefinition.get(otherPlayer.getEquipment().get(Equipment.SLOT_CHEST).getId()).getEquipmentId()));
			} else {
				playerProps.putShort((short) (256 + app.getChest()));
			}
			if (otherPlayer.getEquipment().get(Equipment.SLOT_SHIELD) != null) {
				playerProps.putShort((short) (32768 + EquipmentDefinition.get(otherPlayer.getEquipment().get(Equipment.SLOT_SHIELD).getId()).getEquipmentId()));
			} else {
				playerProps.put((byte) 0);
			}
			if (otherPlayer.getEquipment().get(Equipment.SLOT_CHEST) != null) {
				if (!EquipmentDefinition.get(otherPlayer.getEquipment().get(Equipment.SLOT_CHEST).getId()).isFullBody()) {
					playerProps.putShort((short) (256 + app.getArms()));
				} else {
					playerProps.putShort((short) (32768 + EquipmentDefinition.get(otherPlayer.getEquipment().get(Equipment.SLOT_CHEST).getId()).getEquipmentId()));
				}
			} else {
				playerProps.putShort((short) (256 + app.getArms()));
			}
			if (otherPlayer.getEquipment().get(Equipment.SLOT_LEGS) != null) {
				playerProps.putShort((short) (32768 + EquipmentDefinition.get(otherPlayer.getEquipment().get(Equipment.SLOT_LEGS).getId()).getEquipmentId()));
			} else {
				playerProps.putShort((short) (256 + app.getLegs()));
			}
	
			Item helm = otherPlayer.getEquipment().get(Equipment.SLOT_HELM);
			if(helm != null) {
				if(!EquipmentDefinition.get(helm.getId()).isFullHelm() && !EquipmentDefinition.get(helm.getId()).isFullMask()) {
					playerProps.putShort((short) (256 + app.getHead()));
				} else {
					playerProps.put((byte) 0);
				}
			} else {
				playerProps.putShort((short) (256 + app.getHead()));
			}
	
			if(otherPlayer.getEquipment().get(Equipment.SLOT_GLOVES) != null) {
				playerProps.putShort((short) (32768 + EquipmentDefinition.get(otherPlayer.getEquipment().get(Equipment.SLOT_GLOVES).getId()).getEquipmentId()));
			} else {
				playerProps.putShort((short) (256 + app.getHands()));
			}
	
			if(otherPlayer.getEquipment().get(Equipment.SLOT_BOOTS) != null) {
				playerProps.putShort((short) (32768 + EquipmentDefinition.get(otherPlayer.getEquipment().get(Equipment.SLOT_BOOTS).getId()).getEquipmentId()));
			} else {
				playerProps.putShort((short) (256 + app.getFeet()));
			}
	
			boolean fullHelm = false;
			if(helm != null) {
				fullHelm = !EquipmentDefinition.get(helm.getId()).isFullHelm();
			}
			if(fullHelm || app.getGender() == 1) {
				playerProps.put((byte) 0);
			} else {
				playerProps.putShort((short) (256 + app.getBeard()));
			}
		}
		playerProps.put((byte) app.getHairColour()); // hairc
		playerProps.put((byte) app.getTorsoColour()); // torsoc
		playerProps.put((byte) app.getLegColour()); // legc
		playerProps.put((byte) app.getFeetColour()); // feetc
		playerProps.put((byte) app.getSkinColour()); // skinc
		
		if(app.isNpc()) {
			/*playerProps.putShort((short) NpcDefinition.get(app.getNpcId()).getWalkEmote());

			playerProps.putShort((short) 0x337);
			
			playerProps.putShort((short) NpcDefinition.get(app.getNpcId()).getStandEmote());
				
			playerProps.putShort((short) 5793);
			playerProps.putShort((short) 5792);
			playerProps.putShort((short) 5791);
			
			playerProps.putShort((short) NpcDefinition.get(app.getNpcId()).getRunEmote());*/
		} else {
			if (otherPlayer.getEquipment().get(3) == null || EquipmentDefinition.get(otherPlayer.getEquipment().get(3).getId()) == null) {
				playerProps.putShort((short) 0x328);
			} else {
				playerProps.putShort((short) EquipmentDefinition.get(otherPlayer.getEquipment().get(3).getId()).getStandEmote());
			}
			playerProps.putShort((short) 0x337);
			if (otherPlayer.getEquipment().get(3) == null || EquipmentDefinition.get(otherPlayer.getEquipment().get(3).getId()) == null) {
				playerProps.putShort((short) 0x333);
			} else {
				playerProps.putShort((short) EquipmentDefinition.get(otherPlayer.getEquipment().get(3).getId()).getWalkEmote());
			}
			playerProps.putShort((short) 0x334);
			playerProps.putShort((short) 0x335);
			playerProps.putShort((short) 0x336);
			if (otherPlayer.getEquipment().get(3) == null || EquipmentDefinition.get(otherPlayer.getEquipment().get(3).getId()) == null) {
				playerProps.putShort((short) 0x338);
			} else {
				playerProps.putShort((short) EquipmentDefinition.get(otherPlayer.getEquipment().get(3).getId()).getRunEmote());
			}
		}
		playerProps.putLong(otherPlayer.getName()); // player name
		playerProps.put((byte) otherPlayer.getSkills().getCombatLevel()); // combat level
		playerProps.putShort((short)0); // number shows up side of there names??
		packet.put((byte) (playerProps.getBuffer().position() & 0xff));
		packet.putByteA((ByteBuffer)playerProps.getBuffer().flip());
	}
	
	private static void addNewNPC(PacketBuilder packet, Player player, Npc npc) {
		packet.putBits(15, npc.getIndex());
		packet.putBits(1, 1);
		packet.putBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		int xPos = npc.getLocation().getX() - player.getLocation().getX();
		int yPos = npc.getLocation().getY() - player.getLocation().getY();
		if(xPos < 0) {
			xPos += 32;
		}
		if(yPos < 0) {
			yPos += 32;
		}
		packet.putBits(5, yPos);
		packet.putBits(3, (Integer) npc.getTemporary("FACING"));
		packet.putBits(5, xPos);
		packet.putBits(14, npc.getId());
	}
	
	private static void updateNPCMovement(PacketBuilder packet, Npc npc) {
		if(npc.getSprites().getSecondarySprite() == -1) {
			if(npc.getSprites().getPrimarySprite() == -1) {
				if(npc.getUpdateFlags().isUpdateRequired()) {
					packet.putBits(1, 1);
					packet.putBits(2, 0);
				} else {
					packet.putBits(1, 0);
				}
			} else {
				packet.putBits(1, 1);
				packet.putBits(2, 1);
				packet.putBits(3, npc.getSprites().getPrimarySprite());
				packet.putBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
			}
		} else {
			packet.putBits(1, 1);
			packet.putBits(2, 2);
			packet.putBits(3, npc.getSprites().getPrimarySprite());
			packet.putBits(3, npc.getSprites().getSecondarySprite());
			packet.putBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		}
	}
	
	private static void updateNPC(PacketBuilder packet, Npc npc) {
		int mask = 0x0;
		final UpdateFlags flags = npc.getUpdateFlags();
		
		if(flags.get(UpdateFlag.ANIMATION)) {
			mask |= 8;
		}
		if(flags.get(UpdateFlag.HIT_2)) {
			mask |= 2;
		}
		if(flags.get(UpdateFlag.FACE_ENTITY)) {
			mask |= 0x10;
		}
		if(flags.get(UpdateFlag.HIT)) {
			mask |= 0x80;
		}
		if(flags.get(UpdateFlag.GRAPHICS)) {
			mask |= 4;
		}
		
		/*if(flags.get(UpdateFlag.FORCED_CHAT)) {
			mask |= 0x80;
		}
		if(flags.get(UpdateFlag.FACE_COORDINATE)) {
			mask |= 0x1;
		}
		if(flags.get(UpdateFlag.TRANSFORM)) {
			mask |= 0x2;
		}*/
		/*
		 * And write the mask.
		 */
		packet.put((byte) mask);

		if(flags.get(UpdateFlag.ANIMATION)) {
			Animation animation = (Animation) npc.getTemporary("ANIMATION");
			packet.putLEShort((short) animation.getId());
			packet.put((byte) animation.getDelay());
		}
		if(flags.get(UpdateFlag.HIT_2)) {
			appendHit2Update(npc, packet);
		}
		if(flags.get(UpdateFlag.FACE_ENTITY)) {
			Entity entity = ((Entity) npc.getTemporary("FACE_ENTITY"));
			packet.putShort((short)(entity == null ? -1 : entity.getClientIndex()));
		}
		if(flags.get(UpdateFlag.HIT)) {
			appendHitUpdate(npc, packet);
		}
		if(flags.get(UpdateFlag.GRAPHICS)) {
			Graphic graphic = (Graphic) npc.getTemporary("GRAPHICS");
			packet.putLEShort((short)graphic.getId());
			packet.putInt1(graphic.getDelay());
		}
		
		/*if(flags.get(UpdateFlag.FORCED_CHAT)) {
			packet.putString((String)npc.getTemporary("FORCED_CHAT"));
		}
		if(flags.get(UpdateFlag.FACE_COORDINATE)) {
			Location location = ((Location)npc.getTemporary("FACE_LOCATION"));
			packet.putLEShort((short)(location.getY() * 2 + 1));
			packet.putLEShort((short)(location.getX() * 2 + 1));
		}
		if(flags.get(UpdateFlag.TRANSFORM)) {
			int id = (Integer)npc.getTemporary("TRANSFORM_ID");
			packet.putShort((short) id);
		}*/
	}
	
	private static void appendHit2Update(Npc npc, PacketBuilder updateBlock) {
		updateBlock.putByteA((byte) npc.getDamage().getHitDamage2());
		updateBlock.putByteC((byte) npc.getDamage().getHitType2());
	}

	private static void appendHitUpdate(Npc npc, PacketBuilder updateBlock) {
		/*updateBlock.putByteC((byte) npc.getDamage().getHitDamage1());
		updateBlock.put((byte) npc.getDamage().getHitType1());
		int hpRatio = (npc.getHp() * 30) / NpcDefinition.get(npc.getId()).getHitpoints();
		updateBlock.putByteA((byte) hpRatio);*/
	}
}
