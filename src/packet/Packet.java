package packet;

import java.nio.ByteBuffer;

public class Packet {

	ByteBuffer buffer;
	byte opcode;
	
	public Packet(ByteBuffer buffer, boolean hasOpcode) {
		this.buffer = buffer;
		if (hasOpcode)
			opcode = buffer.get();
	}
	
	public Packet(byte opcode, int expectedAllocation) {
		buffer = ByteBuffer.allocate(expectedAllocation + 1);
		putByte(opcode);
		
	}
	
	public Packet(int expectedAllocation) {
		buffer = ByteBuffer.allocate(expectedAllocation);
	}
	
	public byte getByte() {
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
	
	public String getString() {
		StringBuilder stringBuilder = new StringBuilder();
		int currentCharacter;
		while ((currentCharacter = buffer.get()) != 0)
			stringBuilder.append((char) currentCharacter);
		return stringBuilder.toString();
	}
	
	public void putByte(byte b) {
		buffer.put(b);
	}
	
	public void putShort(short s) {
		buffer.putShort(s);
	}
	
	public void putInt(int i) {
		buffer.putInt(i);
	}
	
	public void putLong(long l) {
		buffer.putLong(l);
	}
	
	public void putString(String s) {
		buffer.put(s.getBytes());
		buffer.put((byte) 0);
	}

	public void put(ByteBuffer b) {
		buffer.put(b);
	}
	
	public void putByteArray(byte[] data) {
		buffer.put(data);
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}

	public byte getOpcode() {
		return opcode;
	}
	
}