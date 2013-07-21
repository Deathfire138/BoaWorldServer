package updateServer.util;

import java.nio.ByteBuffer;

public class RsInput {

	private final ByteBuffer buffer;
	
	public RsInput(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	public byte get() {
		return buffer.get();
	}
	
	public short getShort() {
		return buffer.getShort();
	}
	
	public int getInt() {
		return buffer.getInt();
	}
	
	public long getLong() {
		return buffer.getLong();
	}
	
	public static int readSmart(ByteBuffer in) {
		int val = in.get() & 0xff;
		if (val < 128) {
			return (in.get() & 0xff);
		}
		return -32768 + (in.getShort() & 0xffff);
	}

}