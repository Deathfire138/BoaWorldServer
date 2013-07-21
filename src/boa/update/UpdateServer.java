package boa.update;

import java.util.logging.Logger;

import java.util.zip.CRC32;

import java.nio.ByteBuffer;

import boa.update.Cache;
import boa.update.FileDecompressor;


public class UpdateServer {

	private static final Logger logger = Logger.getLogger(UpdateServer.class.getName());
	
	private static byte[] crc;

	public static void init() throws Exception {
		logger.info("Loading crc tables...");
		byte[] data = new byte[4048];
		ByteBuffer buffer = ByteBuffer.wrap(data);
		buffer.put((byte)0).putInt(Cache.getIndex(255).getLength() * 8);
		CRC32 crc32 = new CRC32();
		for (int file = 0; file < Cache.getIndex(255).getLength(); file++) {
			crc32.update(Cache.getIndex(255).getFile(file));
			buffer.putInt((int) crc32.getValue());
			ByteBuffer crc = ByteBuffer.wrap(new FileDecompressor(Cache.getIndex(255).getFile(file)).decompress());
			int version = crc.get() & 0xFF;
			int revision = version >= 6 ? crc.getInt() : 0;
			buffer.putInt((int) revision);
			crc32.reset();
		}
		crc = (byte[]) buffer.flip().array();
		logger.info("Crc tables loaded.");
	}
	
	private static byte[] getCacheFile(int idx, int file) throws Exception {
		if (idx == 255 && file == 255) {
			return crc;
		}
		return Cache.getCacheFile(idx, file);
	}
	
    public static ByteBuffer getRequest(int idx, int file) throws Exception {
    	try {
			byte[] cache = getCacheFile(idx, file);
			ByteBuffer buffer = ByteBuffer.allocateDirect((cache.length - 2) + ((cache.length - 2) / 511) + 8).put((byte) idx).putShort((short)file);
			int len = (((cache[1] & 0xff) << 24) + ((cache[2] & 0xff) << 16) + ((cache[3] & 0xff) << 8) + (cache[4] & 0xff)) + 9;
			if (cache[0] == 0) {
				len -= 4;
			}
			int c = 3;
			for (int i = 0; i < len; i++) {
				if (c == 512) {
					buffer.put((byte) 0xFF);
					c = 1;
				}
				buffer.put(cache[i]);
				c++;
			}
			return (ByteBuffer) buffer.flip();
    	} catch(ArrayIndexOutOfBoundsException e) {
    		throw new ArrayIndexOutOfBoundsException("File isn't in the cache.");
    	}
	}
}