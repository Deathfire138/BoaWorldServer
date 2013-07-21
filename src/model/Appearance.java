package model;

public final class Appearance {

	private int npcId = -1;

	private int gender, chest, arms, legs, head, hands, feet, beard, hairColour, torsoColour, legColour, feetColour, skinColour;

	public Appearance() {
		gender = 0;
		head = 0;
		chest = 18;
		arms = 26;
		hands = 33;
		legs = 36;
		feet = 42;
		beard = 10;
		hairColour = 0;
		torsoColour = 0;
		legColour = 9;
		feetColour = 0;
		skinColour = 0;
	}

	public int[] getLook() {
		return new int[] {gender, hairColour, torsoColour, legColour, feetColour, skinColour, head, chest, arms, hands, legs, feet, beard};
	}

	public void setLook(int[] look) {
		  gender = look[0];
		  head = look[6];
		  chest = look[7];
		  arms = look[8];
		  hands = look[9];
		  legs = look[10];
		  feet = look[11];
		  beard = look[12];
		  hairColour = look[1];
		  torsoColour = look[2];
		  legColour = look[3];
		  feetColour = look[4];
		  skinColour = look[5];
	}

	public boolean isNpc() {
		return npcId > 0;
	}

	public int getNpcId() {
		return npcId;
	}
	
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public int getHairColour() {
		return hairColour;
	}
	
	public void setHairColour(int hairColour) {
		 this.hairColour = hairColour; 
	}

	public int getTorsoColour() {
		return torsoColour;
	}
	
	public void setTorsoColour(int torsoColour) {
		 this.torsoColour = torsoColour; 
	}

	public int getLegColour() {
		return legColour;
	}

	public void setLegColour(int legColour) {
		 this.legColour = legColour;
	}
	
	public int getFeetColour() {
		return feetColour;
	}

	public void setFeetColour(int feetColour) {
		this.feetColour = feetColour;
	}
	
	public int getSkinColour() {
		return skinColour;
	}
	
	public void setSkinColour(int skinColour) {
		this.skinColour = skinColour;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}
	
	public int getChest() {
		return chest;
	}
	
	public void setChest(int chest) {
		this.chest = chest;
	}

	public int getArms() {
		return arms;
	}

	public void setArms(int arms) {
		this.arms = arms;
	}
	
	public int getHead() {
		return head;
	}
	
	public void setHead(int head) {
		this.head = head;
	}

	public int getHands() {
		return hands;
	}
	
	public void setHands(int hands) {
		this.hands = hands;
	}

	public int getLegs() {
		return legs;
	}
	
	public void setLegs(int legs) {
		this.legs = legs;
	}

	public int getFeet() {
		return feet;
	}
	
	public void setFeet(int feet) {
		this.feet = feet;
	}

	public int getBeard() {
		return beard;
	}
	
	public void setBeard(int beard) {
		 this.beard = beard; 
	}
}