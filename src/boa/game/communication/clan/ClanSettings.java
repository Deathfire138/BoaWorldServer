package boa.game.communication.clan;

import boa.central.Connect;
import boa.game.handlers.StandardHandler;
import boa.game.handlers.interfaces.ButtonHandler;
import net.ActionSender;
import model.Player;
import event.InputEvent;

public class ClanSettings extends StandardHandler implements ButtonHandler {

	@Override
	public boolean handleButton(final Player player, int opcode, int interfaceId,	int buttonId, int buttonId2) {
		
		if (interfaceId == 590) {
			if (buttonId == 31) {
				switch(opcode) {
				case 230:
					Connect.setClanSetting(player, (byte)0, (byte)0);
					break;
				case 205:
					Connect.setClanSetting(player, (byte)0, (byte)1);
					break;
				case 127:
					Connect.setClanSetting(player, (byte)0, (byte)2);
					break;
				case 211:
					Connect.setClanSetting(player, (byte)0, (byte)3);
					break;
				case 203:
					Connect.setClanSetting(player, (byte)0, (byte)4);
					break;
				case 39:
					Connect.setClanSetting(player, (byte)0, (byte)5);
					break;
				case 187:
					Connect.setClanSetting(player, (byte)0, (byte)6);
					break;
				case 156:
					Connect.setClanSetting(player, (byte)0, (byte)7);
					break;
				case 128:
					Connect.setClanSetting(player, (byte)0, (byte)8);
					break;
				}
			} else if (buttonId == 32) {
				switch(opcode) {
				case 230:
					Connect.setClanSetting(player, (byte)1, (byte)0);
					break;
				case 205:
					Connect.setClanSetting(player, (byte)1, (byte)1);
					break;
				case 127:
					Connect.setClanSetting(player, (byte)1, (byte)2);
					break;
				case 211:
					Connect.setClanSetting(player, (byte)1, (byte)3);
					break;
				case 203:
					Connect.setClanSetting(player, (byte)1, (byte)4);
					break;
				case 39:
					Connect.setClanSetting(player, (byte)1, (byte)5);
					break;
				case 187:
					Connect.setClanSetting(player, (byte)1, (byte)6);
					break;
				case 156:
					Connect.setClanSetting(player, (byte)1, (byte)7);
					break;
				case 128:
					Connect.setClanSetting(player, (byte)1, (byte)8);
					break;
				}
			} else if (buttonId == 33) {
				switch(opcode) {
				case 211:
					Connect.setClanSetting(player, (byte)2, (byte)0);
					break;
				case 203:
					Connect.setClanSetting(player, (byte)2, (byte)1);
					break;
				case 39:
					Connect.setClanSetting(player, (byte)2, (byte)2);
					break;
				case 187:
					Connect.setClanSetting(player, (byte)2, (byte)3);
					break;
				case 156:
					Connect.setClanSetting(player, (byte)2, (byte)4);
					break;
				case 128:
					Connect.setClanSetting(player, (byte)2, (byte)5);
					break;
				}
			} else if (buttonId == 34) {
				switch(opcode) {
				case 230:
					Connect.setClanSetting(player, (byte)3, (byte)0);
					break;
				case 205:
					Connect.setClanSetting(player, (byte)3, (byte)1);
					break;
				case 127:
					Connect.setClanSetting(player, (byte)3, (byte)2);
					break;
				case 211:
					Connect.setClanSetting(player, (byte)3, (byte)3);
					break;
				case 203:
					Connect.setClanSetting(player, (byte)3, (byte)4);
					break;
				case 39:
					Connect.setClanSetting(player, (byte)3, (byte)5);
					break;
				case 187:
					Connect.setClanSetting(player, (byte)3, (byte)6);
					break;
				case 156:
					Connect.setClanSetting(player, (byte)3, (byte)7);
					break;
				}
			} else if (buttonId == 41) {
				Connect.setClanSetting(player, (byte)4, (byte)0);
			} else if (buttonId == 30) {
				if (opcode == 230) {
				player.addTemporary("INPUT_EVENT", new InputEvent() {

					@Override
					public void run() {
						ActionSender.sendEnterText(player, "Set clan prefix:");
					}

					@Override
					public void close() {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void input(int amount) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void input(String string) {
						System.out.println("clan name being set to "+string);
						Connect.setClanName(player, string);
					}
					
				});
				((InputEvent) player.getTemporary("INPUT_EVENT")).run();
				}
			}
			return true;
		} else if (interfaceId == 589) {
			
			return true;
		} else {
			return false;
		}
	}

	public static void updateSettingsInterface(Player player, boolean coinShare, byte enter, byte talk, byte kick, byte loot, String prefix) {
		//22
		ActionSender.sendString(player, 590, 30, prefix);
		ActionSender.sendString(player, 590, 31, ENTER_AND_TALK_PERMISSIONS_TEXTS[enter]);
		ActionSender.sendString(player, 590, 32, ENTER_AND_TALK_PERMISSIONS_TEXTS[talk]);
		ActionSender.sendString(player, 590, 33, KICK_PERMISSIONS_TEXTS[kick]);
		ActionSender.sendString(player, 590, 34, LOOT_PERMISSIONS_TEXTS[loot]);
		ActionSender.sendConfig(player, 1083, (coinShare ? 262144 : 0));		
	}
	
	@Override
	public Object getObject() {
		return this;
	}
	
		static String[] ENTER_AND_TALK_PERMISSIONS_TEXTS = {"Anyone", "Any friends", "Recruit+", "Corporal+", "Sergeant+", "Lieutenant+", "Captain+", "General+", "Only me"};
		static String[] KICK_PERMISSIONS_TEXTS = {"Corporal+", "Sergeant+", "Lieutenant+", "Captain+", "General+", "Only me"};
		static String[] LOOT_PERMISSIONS_TEXTS = {"No-one", "Any friends", "Recruit+", "Corporal+", "Sergeant+", "Lieutenant+", "Captain+", "General+"};
	
}