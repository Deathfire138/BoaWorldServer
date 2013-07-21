package boa.update.util;

public class RSInputStream {

    public byte[] buffer;
    public int pos;

    public RSInputStream(byte[] data) {
        buffer = data;
        pos = 0;
    }

    public int readSpaceSaver() {
		int i_6_ = buffer[pos] & 255;
		if (i_6_ < 128) {
			return readUByte();
		}
		return -32768 + readUShort();
    }

    public int readUShort() {
		pos += 2;
		return (((buffer[pos - 2] & 0xff) << 8) + (buffer[pos - 1] & 0xff));
    }
    public int readUByte() {
		return 0xff & buffer[pos++];
    }
}