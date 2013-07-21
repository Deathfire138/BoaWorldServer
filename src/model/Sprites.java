package model;

public final class Sprites {

	private int primary = -1, secondary = -1;

	public int getPrimarySprite() {
		return primary;
	}

	public int getSecondarySprite() {
		return secondary;
	}

	public void setSprites(int primary, int secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}
}