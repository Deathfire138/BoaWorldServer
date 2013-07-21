package model;

import java.util.BitSet;

public class UpdateFlags {

	private final BitSet flags = new BitSet();

	public enum UpdateFlag {

		APPEARANCE,

		CHAT,

		GRAPHICS,

		ANIMATION,

		FORCED_CHAT,

		FACE_ENTITY,

		FACE_COORDINATE,

		HIT,

		HIT_2,

		TRANSFORM,
		
		FORCED_WALKING,
	}

	public boolean isUpdateRequired() {
		return !flags.isEmpty();
	}

	public void flag(UpdateFlag flag) {
		flags.set(flag.ordinal(), true);
	}

	public void set(UpdateFlag flag, boolean value) {
		flags.set(flag.ordinal(), value);
	}

	public boolean get(UpdateFlag flag) {
		return flags.get(flag.ordinal());
	}

	public void reset() {
		flags.clear();
	}
}