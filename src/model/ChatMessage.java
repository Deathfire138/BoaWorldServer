package model;

public final class ChatMessage {

	private final int color;

	private final int effects;

	private final String text;

	public ChatMessage(int color, int effects, String text) {
		this.color = color;
		this.effects = effects;
		this.text = text;
	}

	public int getColor() {
		return color;
	}

	public int getEffects() {
		return effects;
	}

	public String getText() {
		return text;
	}
}