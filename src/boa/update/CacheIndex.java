package boa.update;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import boa.update.FileInformationTable.Format;


public class CacheIndex implements Closeable {
	
	/**
	 * The data random access file.
	 */
	private final RandomAccessFile dataFile;
	/**
	 * The index random access files.
	 */
	private final RandomAccessFile indexFile;
	/**
	 * The FileInformationTable.
	 */
	private FileInformationTable cacheFIT;
	/**
	 * The CacheContainer for the index if it has one.
	 */
	private CacheContainer containers;
	
	/**
	 * Creates the cache.
	 * @param directory The directory where the cache is stored.
	 * @throws IOException 
	 */
	public CacheIndex(String path, int id, CacheIndex index255) throws IOException {
		this.dataFile = new RandomAccessFile(path + "main_file_cache.dat2", "rw");
		this.indexFile = new RandomAccessFile(path + "main_file_cache.idx" + id, "rw");
		if (index255 != null) {
			this.cacheFIT = new FileInformationTable(id, index255, Format.OLD);
			this.containers = new CacheContainer(this);
		}
	}
	
	/**
	 * Gets an Index file that point to a block in the data file, which contains the data.
	 * @param idx The idx file 
	 * @return The containing data.
	 * @throws IOException The exception that may be thrown.
	 */
	@SuppressWarnings("unused")
	public byte[] getFile(int id) throws IOException {
		ByteBuffer index = indexFile.getChannel().map(MapMode.READ_ONLY, 6 * id, 6);
		int fileSize = ((index.get() & 0xFF) << 16) | ((index.get() & 0xFF) << 8) | (index.get() & 0xFF);
		int fileSector = ((index.get() & 0xFF) << 16) | ((index.get() & 0xFF) << 8) | (index.get() & 0xFF);
		int remainingBytes = fileSize;
		int sector = fileSector;
		ByteBuffer finalBuffer = ByteBuffer.allocate(fileSize);
		while(remainingBytes > 0) {
			ByteBuffer mainBlock = dataFile.getChannel().map(MapMode.READ_ONLY, sector * 520, 520);
			int nextFile = mainBlock.getShort() & 0xFFFF;
			int currentSector = mainBlock.getShort() & 0xFFFF;
			int nextSector = ((mainBlock.get() & 0xFF) << 16) | ((mainBlock.get() & 0xFF) << 8) | (mainBlock.get() & 0xFF);
			int nextCache = mainBlock.get() & 0xFF;	
			int remaining = remainingBytes;
			if(remaining > 512) {
				remaining = 512;
			}
			byte[] finalData = new byte[remaining];
			mainBlock.get(finalData);
			finalBuffer.put(finalData, 0, remaining);
			remainingBytes -= remaining;
			sector = nextSector;
		}
		return (byte[]) finalBuffer.flip().array();
	}

	/**
	 * Closes the cache.
	 * @throws IOException The exception that may be thrown.
	 */
	public void close() throws IOException {
		dataFile.close();
		indexFile.close();
	}
	
	/**
	 * Gets the length of the index file.
	 * @return The length of the index file
	 * @throws IOException The exception that may be thrown.
	 */
	public int getLength() throws IOException {
		return (int) (indexFile.length() / 6);

	}
	/**
	 * Gets the cacheFit.
	 * @return the cacheFIT.
	 */
	public FileInformationTable getCacheFIT() {
		return cacheFIT;
	}
	/**
	 * Gets the container.
	 * @return the container
	 */
	public CacheContainer getContainer() {
		return containers;
	}
}