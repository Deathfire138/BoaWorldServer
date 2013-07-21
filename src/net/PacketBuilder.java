package net;

import java.nio.ByteBuffer;

public class PacketBuilder {

	/**
	 * The current bit position.
	 */
	private int bitPosition;
	
	private final static int BIT_MASKS[] = {
		0, 0x1, 0x3, 0x7,
		0xf, 0x1f, 0x3f, 0x7f,
		0xff, 0x1ff, 0x3ff, 0x7ff,
		0xfff, 0x1fff, 0x3fff, 0x7fff,
		0xffff, 0x1ffff, 0x3ffff, 0x7ffff,
		0xfffff, 0x1fffff, 0x3fffff, 0x7fffff,
		0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff,
		0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff,
		-1
	};
	
	/**
	 * Bit mask array.
	 */
	public static final int[] BIT_MASK_OUT = new int[32];
	
	/**
	 * Creates the bit mask array.
	 */
	static {
		for(int i = 0; i < BIT_MASK_OUT.length; i++) {
			BIT_MASK_OUT[i] = (1 << i) - 1;
		}
	}
	
	int opcode;
	ByteBuffer buffer;
	Size size;
	
	public int getOpcode() {
		return opcode;
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public Size getSize() {
		return size;
	}
	
	public PacketBuilder(int opcode, int allocate) {
		this.opcode = opcode;
		buffer = ByteBuffer.allocate(allocate);
		this.size = Size.VAR;
	}
	
	public PacketBuilder(int opcode, int allocate, Size size) {
		this.opcode = opcode;
		buffer = ByteBuffer.allocate(allocate);
		this.size = size;
	}
	
	public enum Size {
		VAR,
		VAR_BYTE,
		VAR_SHORT;
	}
	
	/**
	 * Writes a RuneScape string.
	 * @param string The string to write.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putRS2String(String string) {
		buffer.put(string.getBytes());
		buffer.put((byte) 0);
		return this;
	}
	
	public PacketBuilder put(int val) {
		buffer.put((byte) val);
		return this;
	}
	
	public PacketBuilder putShort(int val) {
		buffer.putShort((short) val);
		return this;
	}
	
	/**
	 * Writes a type-A short.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putShortA(int val) {
		buffer.put((byte) (val >> 8));
		buffer.put((byte) (val + 128));
		return this;
	}

	/**
	 * Writes a type-A byte.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putByteA(int val) {
		buffer.put((byte) (val + 128));
		return this;
	}

	/**
	 * Writes a little endian type-A short.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putLEShortA(int val) {
		buffer.put((byte) (val + 128));
		buffer.put((byte) (val >> 8));
		return this;
	}

	/**
	 * Checks if this packet builder is empty.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isEmpty() {
		return buffer.position() == 0;
	}

	/**
	 * Starts bit access.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder startBitAccess() {
		bitPosition = buffer.position() * 8;
		return this;
	}
	
	/**
	 * Finishes bit access.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder finishBitAccess() {
		buffer.position((bitPosition + 7) / 8);
		return this;
	}

	/**
	 * Writes some bits.
	 * @param numBits The number of bits to write.
	 * @param value The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putBits(int numBits, int value) {
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;
		int pos = (bitPosition + 7) / 8;
		buffer.position(pos);
		for (; numBits > bitOffset; bitOffset = 8) {
			int tmp = buffer.get(bytePos);
			tmp &= ~BIT_MASKS[bitOffset];
			tmp |= (value >> (numBits-bitOffset)) & BIT_MASKS[bitOffset];
			buffer.put(bytePos++,(byte) tmp);
			numBits -= bitOffset;
		}
		if (numBits == bitOffset) {
			int tmp = buffer.get(bytePos);
			tmp &= ~BIT_MASKS[bitOffset];
			tmp |= value & BIT_MASKS[bitOffset];
			buffer.put(bytePos,(byte) tmp);
		} else {
			int tmp = buffer.get(bytePos);
			tmp &= ~(BIT_MASKS[numBits] << (bitOffset - numBits));
			tmp |= (value & BIT_MASKS[numBits]) << (bitOffset - numBits);
			buffer.put(bytePos,(byte) tmp);
		}
		return this;
	}
	
	/**
	 * Puts an <code>IoBuffer</code>.
	 * @param buf The buffer.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder put(ByteBuffer buf) {
		buffer.put(buf);
		return this;
	}
	
	/**
	 * Writes a type-A byte.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putByteA(ByteBuffer buf) {
		int iter = 0;
		while(buf.hasRemaining()) {
			iter++;
			buffer.put((byte) (buf.get() - 128));
		}
		return this;
	}

	/**
	 * Writes a type-C byte.
	 * @param val The value to write.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putByteC(int val) {
		buffer.put((byte) (-val));
		return this;
	}

	/**
	 * Writes a little-endian short.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putLEShort(int val) {
		buffer.put((byte) (val));
		buffer.put((byte) (val >> 8));
		return this;
	}

	/**
	 * Writes a type-1 integer.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putInt1(int val) {
		buffer.put((byte) (val >> 8));
		buffer.put((byte) val);
		buffer.put((byte) (val >> 24));
		buffer.put((byte) (val >> 16));
		return this;
	}
	
	/**
	 * Writes a type-2 integer.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putInt2(int val) {
		buffer.put((byte) (val >> 16));
		buffer.put((byte) (val >> 24));
		buffer.put((byte) val);
		buffer.put((byte) (val >> 8));
		return this;
	}
	
	/**
	 * Writes a little-endian integer.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putLEInt(int val) {
		buffer.put((byte) (val));
		buffer.put((byte) (val >> 8));
		buffer.put((byte) (val >> 16));
		buffer.put((byte) (val >> 24));
		return this;
	}

	/**
	 * Puts a sequence of bytes in the buffer.
	 * @param data The bytes.
	 * @param offset The offset.
	 * @param length The length.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder put(byte[] data, int offset, int length) {
		try {
			buffer.put(data, offset, length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	/**
	 * Puts a type-A byte in the buffer.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putByteA(byte val) {
		buffer.put((byte) (val + 128));
		return this;
	}
	
	/**
	 * Puts a type-C byte in the buffer.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putByteC(byte val) {
		buffer.put((byte) -val);
		return this;
	}
	
	/**
	 * Puts a type-S byte in the buffer.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putByteS(byte val) {
		buffer.put((byte) (128-val));
		return this;
	}
	
	/**
	 * Puts a series of reversed bytes in the buffer.
	 * @param is The source byte array.
	 * @param offset The offset.
	 * @param length The length.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putReverse(byte[] is, int offset, int length) {
		for(int i = (offset + length - 1); i >= offset; i--) {
			buffer.put(is[i]);
		}
		return this;
	}
	
	/**
	 * Puts a series of reversed type-A bytes in the buffer.
	 * @param is The source byte array.
	 * @param offset The offset.
	 * @param length The length.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putReverseA(byte[] is, int offset, int length) {
		for(int i = (offset + length - 1); i >= offset; i--) {
			putByteA(is[i]);
		}
		return this;
	}
	
	/**
	 * Puts a 3-byte integer.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putTriByte(int val) {
		buffer.put((byte) (val >> 16));
		buffer.put((byte) (val >> 8));
		buffer.put((byte) val);
		return this;
	}

	/**
	 * Puts a byte or short.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putSmart(int val) {
		if(val >= 128) {
			buffer.putShort((short)(val + 32768));
		} else {
			buffer.put((byte) val);
		}
		return this;
	}
	
	/**
	 * Puts a byte or short for signed use.
	 * @param val The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public PacketBuilder putSignedSmart(int val) {
		if(val >= 128) {
			buffer.putShort((short)(val + 49152));
		} else {
			buffer.put((byte) (val + 64));
		}
		return this;
	}

	public PacketBuilder putLong(long val) {
		buffer.putLong(val);
		return this;
	}

	public PacketBuilder putLELong(long l) {
		putLEInt((int)(l & -1L));
		putLEInt((int)(l >> 32));
		return this;
	}
	
	public PacketBuilder putInt(int val) {
		buffer.putInt(val);
		return this;		
	}

	public void putBytesReverseA(byte[] aByte, int i, int j) {
		 for(int k = (j + i) - 1; k >= j; k--)
	            putByteA(aByte[k]);
	}

	public void putRS2Int(int val) {
		put((byte) (val >> 24));
		put((byte) (val >> 16));
		put((byte) (val >> 8));
		put((byte) val);
	}
	
	public PacketBuilder putRS2long(long val) {
		putRS2Int((int) (val >> 32));
		putRS2Int((int) val);
		return this;
	}
	
	public PacketBuilder put(byte[] data) {
		buffer.put(data);
		return this;
	}
	
}