package updateServer.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.nio.ByteBuffer;

import java.util.zip.GZIPInputStream;

public class CacheUtil {

	/**
	 * Decompressers gzip compressed formats.
	 * @param data The data that needs to decompressed
	 * @param offset The offset
	 * @param length The length of the cacheFile
	 * @return The decompressed data.
	 * @throws IOException The exception that may be thrown.
	 */
	public static byte[] gzipDecompress(byte[] data, int offset, int length) throws IOException {
		byte[] uncompressed = new byte[length];
		System.arraycopy(data, offset, uncompressed, 0, length);
		GZIPInputStream bzip = new GZIPInputStream(new ByteArrayInputStream(uncompressed));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] inBuffer = new byte[length];
			int read;
			while ((read = bzip.read(inBuffer)) > 0) {
				out.write(inBuffer, 0, read);
			}
		} finally {
			bzip.close();
			out.close();
		}
		return out.toByteArray();
	}
	
	/**
	 * Decompressers bzip compressed formats.
	 * @param data The data that needs to decompressed
	 * @param offset The offset
	 * @param length The length of the cacheFile
	 * @return The decompressed data.
	 * @throws IOException The exception that may be thrown.
	 */
	public static byte[] bzipDecompress(byte[] data, int offset, int length) throws IOException {
		byte[] uncompressed = new byte[length];
		System.arraycopy(data, offset, uncompressed, 0, length);
		CBZip2InputStream bzip = new CBZip2InputStream(new ByteArrayInputStream(uncompressed), 0);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] inBuffer = new byte[length];
			int read;
			while ((read = bzip.read(inBuffer)) > 0) {
				out.write(inBuffer, 0, read);
			}
		} finally {
			bzip.close();
			out.close();
		}
		return out.toByteArray();
	}
	
	public static String getString(ByteBuffer buf) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while (buf.remaining() > 0 && (b = buf.get()) != 0) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}
}
