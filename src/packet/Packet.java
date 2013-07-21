package packet;

import java.nio.ByteBuffer;

public final class Packet {

	private int id;

	private final ByteBuffer buffer;
	
	public Packet(int id, ByteBuffer buffer) {
		this.id = id;
		this.buffer = buffer;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSize() {
		return buffer.limit();
	}
	
	public byte get() {
		return buffer.get();
	}
	
	public int getS() {
		return ((get() & 0xff) + 128);
	}

	public int getC() {
		return -(get() & 0xff);
	}

	public int getA() {
		return ((get() & 0xff) - 128);
	}
	
	public int getA2() {
		return (-128 + (get() & 0xff));
	}
	
	public int getShort() {
		return buffer.getShort();
	}
	
	public int getShortA() {
		return (((get() & 0xff) << 8) + (get() - 128 & 0xff));
	}
	
	public int getLEShort() {
		int i = ((get() & 0xff)) + ((get() & 0xff) << 8);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}
	
	public int getLEShortA() {
		int i = ((get() - 128 & 0xff)) + ((get() & 0xff) << 8);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}
	
	public int getTriByte() {
		return (((get() & 0xff) << 8)
				+ ((get() & 0xff) << 16)
				+ (get() & 0xff));
	}
	
	public int getInt() {
		return buffer.getInt();
	}
	
	public int getLEInt() {
		return (get() & 0xff) | ((get() & 0xff) << 8) | ((get() & 0xff) << 16) | ((get() & 0xff) << 24);
	}
	
	public int getInt1() {
		return ((get() & 0xff) << 8) | (get() & 0xff) | ((get() & 0xff) << 24) | ((get() & 0xff) << 16);
	}
	
	public int getInt2() {
		return ((get() & 0xff) << 16) | ((get() & 0xff) << 24) | (get() & 0xff) | ((get() & 0xff) << 8);
	}

	public long getLong() {
		return buffer.getLong();
	}
	
	public long getLong2() {
		long l = 0xffffffffL & (long) getInt2();
        long l_35_ = (long) getInt2() & 0xffffffffL;
        return l_35_ + (l << 32);
	}
	
	public int getSmart() {
		int val = buffer.get() & 0xff;
		buffer.position(buffer.position() - 1);
		if (val < 128) {
			return (buffer.get() & 0xff);
		}
		return -32768 + (buffer.getShort() & 0xffff);
	}
	
	public String getRS2String() {
		StringBuilder s = new StringBuilder();
		byte c;
		while ((c = get()) != 0) {
			s.append((char)c);
		}
		return s.toString();
	}
	
	public String getString() {
		StringBuilder s = new StringBuilder();
		byte c;
		while ((c = get()) != 10) {
			s.append((char)c);
		}
		return s.toString();
	}
	
	public boolean hasRemaining() {
		return buffer.hasRemaining();
	}
	
	public byte[] getRemainingData() {
		byte[] data = new byte[buffer.remaining()];
		for(int i = 0; i < data.length;i++) {
			data[i] = buffer.get();
		}
		return data;
		
	}
}
