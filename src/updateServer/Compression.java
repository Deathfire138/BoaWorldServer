package updateServer;

public enum Compression {
	
	/**
	 * If there is no compression.
	 */
	NONE(0),
	/**
	 * If the compression is GZIP
	 */
	GZIP(2),
	/**
	 * If the cache compression is BZIP
	 */
	BZIP(1);
	
	/**
	 * The compression id.
	 */
	private int compression;
	
	/**
	 * Creates a new compression with a certain id;
	 * @param compression The compression id.
	 */
	Compression(int compression) {
		this.compression = compression;
	}

	/**
	 * Gets the compression id.
	 * @return the compression.
	 */
	public int toInteger() {
		return compression;
	}
}