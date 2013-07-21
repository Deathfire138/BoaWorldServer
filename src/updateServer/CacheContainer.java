package updateServer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class CacheContainer {
	/**
	 * The data of the container as an 2d object array.
	 * 
	 * The first part is the Container Id and the second part is the internal file in the Container.
	 */
	private Object[][] containerData;
	/**
	 * We need to grab the index and decompress it so we can read the data.
	 */
	private CacheIndex index;
	/**
	 * Creates a new <code>CacheContainer</code> with the relevant instances.
	 * @param cacheFIT The FileInformationTable
	 * @param index The index of the cache.
	 */
	public CacheContainer(CacheIndex index) {
		this.index = index;
		this.containerData = new Object[index.getCacheFIT().getEntrySize()][];
	}
	/**
	 * Decompressers the container and gets the data from it.
	 * @param containerId The containers id since each container is 256.
	 * @param childId The internal file of the container.
	 * @return The data of the container is compressed to keep it smaller and usually in the GZIP.
	 * @throws IOException The exception that may be thrown.
	 */
	public byte[] getInternalCacheFile(int containerId, int childId) throws IOException {
		if (containerData[containerId] == null) {
			containerData[containerId] = new Object[index.getCacheFIT().getEntries()[containerId].getChildSize()];
		}
		int[] children = index.getCacheFIT().getEntries()[containerId].getChildEntries();
		int size = index.getCacheFIT().getEntries()[containerId].getChildSize();
		Object[] childs = containerData[containerId];
		FileDecompressor decompressor = new FileDecompressor(index.getFile(containerId));
		byte[] uncompressed = decompressor.decompress();
		ByteBuffer buffer = ByteBuffer.wrap(uncompressed);
		if (size > 0) {
			int length = uncompressed.length;
			int verification = uncompressed[--length] & 0xff;
			length -= verification * (size * 4);
			buffer.position(length);
			int[] outOffset = new int[size];
			for (int i = 0; verification > i; i++) {
				int offset = 0;
				for (int count = 0; count < size; count++) {
					offset += buffer.getInt();
					outOffset[count] += offset;
				}
			}
			byte[][] newData = new byte[size][];
			for (int i = 0; i < size; i++) {
				newData[i] = new byte[outOffset[i]];
				outOffset[i] = 0;
			}
			int readPos = 0;
			buffer.position(length);
			for (int i = 0; i < verification; i++) {
				int offset = 0;
				for (int id = 0; id < size; id++) {
					offset += buffer.getInt();
					System.arraycopy(uncompressed, readPos, newData[id], outOffset[id], offset);
					readPos += offset;
					outOffset[id] += offset;
				}
			}
			for (int i = 0; i < size; i++) {
				childs[children != null ? children[i] : i] = newData[i];
			}
		} else {
			childs[children != null ? children[0] : 0] = uncompressed;
		} 
		return (byte[]) containerData[containerId][childId];	
	}
	
	/**
	 * Gets the internal file from a certain container.
	 */
	public byte[] getData(int id) throws IOException {
		return getInternalCacheFile(id >>> 8, id & 0xFF);
	}
}
