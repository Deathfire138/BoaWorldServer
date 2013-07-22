package model;

import boa.io.ActionSender;

public final class Settings {
	
	private Player player;

	private boolean withdrawAsNotes = false;

	private boolean swapping = true;
	
	private boolean isAutoRetaliate = true;
	
	private boolean isSpecialActive = false;
	
	private boolean twoMouseButtons = true;

	private boolean chatEffects = true;

	private boolean splitPrivateChat = false;

	private boolean acceptAid = false;
	
	private String attackStyle = "Accurate";

	private String attackType = "Slash";
	
	private double energy = 100;
	
	private int publicChat = 0;

	private int privateChat = 0;

	private int trade = 0;
	
	private int brightnessSetting = 2;
	
	private int spellbook = 192;
	
	public Settings(Player player) {
		this.player = player;
	}

	public void setWithdrawAsNotes() {
		this.withdrawAsNotes = !withdrawAsNotes;
		ActionSender.sendConfig(player, 115, withdrawAsNotes ? 1 : 0);
	}

	public void setSwapping() {
		this.swapping = !swapping;
		ActionSender.sendConfig(player, 304, swapping ? 0 : 1);
	}
	
	public void setAutoRetaliate() {
		this.isAutoRetaliate = !isAutoRetaliate;
		ActionSender.sendConfig(player, 172, isAutoRetaliate ? 0 : 1);
	}
	
	public void setSpecialActive() {
		this.isSpecialActive = !isSpecialActive;
	}
	
	public void setTwoMouseButtons() {
		this.twoMouseButtons = !twoMouseButtons;
		/*ActionSender.sendConfig(player, 170, twoMouseButtons ? 0 : 1);
		ActionSender.sendInterfaceConfig(player, 261, 57, !twoMouseButtons);
		ActionSender.sendInterfaceConfig(player, 261, 58, twoMouseButtons);*/
	}

	public void setChatEffects() {
		this.chatEffects = !chatEffects;
		/*ActionSender.sendConfig(player, 171, chatEffects ? 0 : 1);
		ActionSender.sendInterfaceConfig(player, 261, 53, !chatEffects);
		ActionSender.sendInterfaceConfig(player, 261, 54, chatEffects);*/
		
	}

	public void setSplitPrivateChat() {
		this.splitPrivateChat = !splitPrivateChat;
		/*ActionSender.sendRunScript(player, 83, new Object[0], "");
		ActionSender.sendConfig(player, 287, splitPrivateChat ? 1 : 0);
		ActionSender.sendInterfaceConfig(player, 261, 55, !splitPrivateChat);
		ActionSender.sendInterfaceConfig(player, 261, 56, splitPrivateChat);*/
	}

	public void setAcceptAid() {
		this.acceptAid = !acceptAid;
		/*ActionSender.sendConfig(player, 427, acceptAid ? 1 : 0);
		ActionSender.sendInterfaceConfig(player, 261, 59, !acceptAid);
		ActionSender.sendInterfaceConfig(player, 261, 60, acceptAid);*/
	}
	
	public void setAttackType(String i) {
		this.attackType = i;
	}
	
	public void setAttackStyle(String i) {
		this.attackStyle = i;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
		//ActionSender.sendRunEnergy(player);
	}
	
	public void setPublicChat(int publicChat) {
		this.publicChat = publicChat;
	}
	
	public void setPrivateChat(int privateChat) {
		if (this.privateChat != privateChat) {
			//Friends.changeStatus(player, privateChat);
		}
		this.privateChat = privateChat;
	}
	
	public void setTrade(int trade) {
		this.trade = trade;
	}

	public void setBrightnessSetting(int brightnessSetting) {
		this.brightnessSetting = brightnessSetting;
		ActionSender.sendConfig(player, 166, brightnessSetting);
	}

	public boolean isSwapping() {
		return swapping;
	}

	public boolean isWithdrawingAsNotes() {
		return withdrawAsNotes;
	}
	
	public boolean isAutoRetaliate() {
		return isAutoRetaliate;
	}
	
	public boolean isTwoMouseButtons() {
		return twoMouseButtons;
	}

	public boolean isChatEffects() {
		return chatEffects;
	}

	public boolean isSplitPrivateChat() {
		return splitPrivateChat;
	}

	public boolean isAcceptingAid() {
		return acceptAid;
	}
	
	public String getAttackStyle() {
		return attackStyle;
	}
	
	public String getAttackType() {
		return attackType;
	}
	
	public int getEnergy() {
		return (int)energy;
	}
	
	public int getPublicChat() {
		return publicChat;
	}
	
	public int getPrivateChat() {
		return privateChat;
	}
	
	public int getTrade() {
		return trade;
	}
	
	public int getBrightnessSetting() {
		return brightnessSetting;
	}
	

	public void setSpellbook(int spellbook) {
		this.spellbook = spellbook;
	}

	public int getSpellbook() {
		return spellbook;
	}

	public void refresh() {
		/*ActionSender.sendRunEnergy(player);
		ActionSender.sendConfig(player, 173, 0);
		
		ActionSender.sendConfig(player, 166, brightnessSetting);

		
		ActionSender.sendConfig(player, 115, withdrawAsNotes ? 1 : 0);
		
		ActionSender.sendConfig(player, 304, swapping ? 0 : 1);
		
		ActionSender.sendConfig(player, 172, isAutoRetaliate ? 0 : 1);
		
		ActionSender.sendConfig(player, 170, twoMouseButtons ? 0 : 1);
		ActionSender.sendInterfaceConfig(player, 261, 57, !twoMouseButtons);
		ActionSender.sendInterfaceConfig(player, 261, 58, twoMouseButtons);
		
		ActionSender.sendConfig(player, 171, chatEffects ? 0 : 1);
		ActionSender.sendInterfaceConfig(player, 261, 53, !chatEffects);
		ActionSender.sendInterfaceConfig(player, 261, 54, chatEffects);
		
		ActionSender.sendRunScript(player, 83, new Object[0], "");
		ActionSender.sendConfig(player, 287, splitPrivateChat ? 1 : 0);
		ActionSender.sendInterfaceConfig(player, 261, 55, !splitPrivateChat);
		ActionSender.sendInterfaceConfig(player, 261, 56, splitPrivateChat);
		
		ActionSender.sendConfig(player, 427, acceptAid ? 1 : 0);
		ActionSender.sendInterfaceConfig(player, 261, 59, !acceptAid);
		ActionSender.sendInterfaceConfig(player, 261, 60, acceptAid);*/
	}
}