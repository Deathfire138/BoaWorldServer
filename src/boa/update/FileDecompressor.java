package boa.update;

import java.io.IOException;
import java.nio.ByteBuffer;

import boa.update.util.CacheUtil;


public class FileDecompressor {

	private final CacheFile file;

	/**
	 * Creates a new <code>CacheContainer</code>
	 * @param data The data being read.
	 * @throws IOException The exception that may be thrown.
	 */
	public FileDecompressor(byte[] data) throws IOException {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		int compression = buffer.get();
		int uncompressedSize = buffer.getInt();
		int cacheSize = compression != 0 ? buffer.getInt() : uncompressedSize;
		this.file = new CacheFile(buffer, compression, uncompressedSize, cacheSize);
	}

	/**
	 * Decompressers a certain type of CacheFile.
	 * @return The uncompressed data.
	 * @throws IOException The Exception that may be thrown.
	 */
	public byte[] decompress() throws IOException {
		byte[] newData = new byte[file.getCacheSize()];
		switch (file.getCompression()) {
		case NONE:
			System.arraycopy(file.toByteArray(), 5, newData, 0, file.getCacheSize());
			break;
		case BZIP:
			newData = CacheUtil.bzipDecompress(file.toByteArray(), 9, file.getUncompressedSize());
			break;
		case GZIP:
			newData = CacheUtil.gzipDecompress(file.toByteArray(), 9, file.getUncompressedSize());
			break;
		}
		return newData;
	}

	/**
	 * Gets the decompressed file.
	 * @return the file
	 */
	public CacheFile getFile() {
		return file;
	}
}