package boa.update;

import java.nio.ByteBuffer;

public final class CacheFile {

	/**
	 * A <code>ByteBuffer</code> containing the file data.
	 */
	private final ByteBuffer data;
	/**
	 * The uncompressed size of the file.
	 */
	private final int uncompressedSize;
	/**
	 * What compression the file uses.
	 */
	private final Compression compression;
	/**
	 * The cache size.
	 */
	private final int cacheSize;
	    
	/**
	 * Creates a new <code>CacheFile</code with the data needed to decompress certain files
	 * @param data The cache file data
	 * @param compression The compression of the file.
	 * @param uncompressedSize The uncompressed size of the file.
	 * @param cacheSize The cacheSize of the file, uncompressed or compressed.
	 */
	public CacheFile(ByteBuffer data, int compression, int uncompressedSize, int cacheSize) {
		this.data = data;
		this.uncompressedSize = uncompressedSize;
		this.cacheSize = cacheSize;
		this.compression = configureCompression(compression);
	}

	/**
	 * Gets the data as a <code>ByteBuffer</code>.
	 * @return the data.
	 */
	public ByteBuffer getData() {
		return data;
	}

	/**
	 * Gets the uncompressedSize.
	 * @return the uncompressedSize.
	 */
	public int getUncompressedSize() {
		return uncompressedSize;
	}

	/**
	 * Gets the compression of the cacheFile.
	 * @return the compression.
	 */
	public Compression getCompression() {
		return compression;
	}

	/**
	 * Gets the cacheSize.
	 * @return the cacheSize.
	 */
	public int getCacheSize() {
		return cacheSize;
	}
	
	/**
	 * Configures the compression of the cache file.
	 * @param compression The compression as an integer.
	 * @return The compression of the file.
	 */
	public Compression configureCompression(int compression) {
		if(compression == 0) {
			return Compression.NONE;
		} else if(compression == 1) {
			return Compression.BZIP;
		} else {
			return Compression.GZIP;
		}
	}
	
	/**
	 * Gets the data as a ByteArray.
	 * @return The data as ByteArray.
	 */
	public byte[] toByteArray() {
		return data.array();
	}
}
