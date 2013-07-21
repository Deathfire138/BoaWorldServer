package boa.update;

import java.io.IOException;
import java.nio.ByteBuffer;

public final class FileInformationTable {

	private TableEntry[] entries;

	private int size;
	
	public FileInformationTable(int id, CacheIndex index, Format format) throws IOException {
		ByteBuffer buffer = ByteBuffer.wrap(new FileDecompressor(index.getFile(id)).decompress());
		if(format == Format.OLD) {
			oldFileInformationTable(buffer);
		} else {
			newFileInformationTable(buffer);
		}
	}

	public void oldFileInformationTable(ByteBuffer buffer) throws IOException {
		int version = buffer.get() & 0xFF;
		@SuppressWarnings("unused")
		int revision = version >= 6 ? buffer.getInt() : 0;
		boolean named = buffer.get() == 1;
		size = buffer.getShort() & 0xFFFF;
		entries = new TableEntry[size];
		int offset = 0;
		for (int i = 0; i < size; i++) {
			entries[i] = new TableEntry();
			entries[i].entry = offset += buffer.getShort() & 0xFFFF;
		}
		if (named) {
			for (int i = 0; i < size; i++)
				entries[i].hashName = buffer.getInt();
		}
		for (int i = 0; i < size; i++)
			entries[i].crc = buffer.getInt();
		for (int i = 0; i < size; i++)
			entries[i].revision = buffer.getInt();
		for (int i = 0; i < size; i++)
			entries[i].childSize = buffer.getShort() & 0xFFFF;
		for (int i = 0; i < size; i++) {
			offset = 0;
			entries[i].childEntries = new int[entries[i].childSize];
			for (int child = 0; child < entries[i].childSize; child++) {
				entries[i].childEntries[child] = offset += buffer.getShort();
			}
		}
		if (named)
			for (int i = 0; i < size; i++) {
				entries[i].childHashNames = new int[entries[i].childSize];
				for (int subSize = 0; subSize < entries[i].childSize; subSize++)
					entries[i].childHashNames[subSize] = buffer.getInt();
			}
	}

	
	public void newFileInformationTable(ByteBuffer buffer) throws IOException {
		int version = buffer.get() & 0xFF;
		@SuppressWarnings("unused")
		int revision = version >= 6 ? buffer.getInt() : 0;
		int named = buffer.get() & 0xFF;
		boolean hasName = (0x1 & named) != 0;
		boolean hasDigests = (0x2 & named) != 0;
		size = buffer.getShort() & 0xFFFF;
		entries = new TableEntry[size];
		int offset = 0;
		for (int i = 0; i < size; i++) {
			entries[i] = new TableEntry();
			entries[i].entry = offset += buffer.getShort() & 0xFFFF;
		}
		if (hasName) {
			for (int i = 0; i < size; i++)
				entries[i].hashName = buffer.getInt();
		}
		for (int i = 0; i < size; i++)
			entries[i].crc = buffer.getInt();
		/**
		 * Only different from 604 and below is this part. and the two booleans
		 */
		if (hasDigests) {
			for (int i = 0; i < size; i++) {
				byte[] data = new byte[64];	
				buffer.get(data);
				entries[i].digests = data;
			}
		}
		for (int i = 0; i < size; i++)
			entries[i].revision = buffer.getInt();
		for (int i = 0; i < size; i++)
			entries[i].childSize = buffer.getShort() & 0xFFFF;
		for (int i = 0; i < size; i++) {
			offset = 0;
			entries[i].childEntries = new int[entries[i].childSize];
			for (int child = 0; child < entries[i].childSize; child++) {
				entries[i].childEntries[child] = offset += buffer.getShort();
			}
		}
		if (hasName)
			for (int i = 0; i < size; i++) {
				entries[i].childHashNames = new int[entries[i].childSize];
				for (int child = 0; child < entries[i].childSize; child++) {
					entries[i].childHashNames[child] = buffer.getInt();
				}
			}
	}
	

	/**
	 * Gets the entries.
	 * @return the entries.
	 */
	public TableEntry[] getEntries() {
		return entries;
	}

	/**
	 * Gets the entry size.
	 * @return the size
	 */
	public int getEntrySize() {
		return size;
	}
	
	/**
	 * The TableEntry for the FIT.
	 */
	public final class TableEntry {

		private int revision;
		private int crc;
		private int entry;
		private int childSize;
		private int hashName;
		private int[] childHashNames;
		private int[] childEntries;
		private byte[] digests;
		/**
		 * Gets the revision.
		 * @return the revision
		 */
		public int getRevision() {
			return revision;
		}
		/**
		 * Gets the CRC value.
		 * @return the crc.
		 */
		public int getCrc() {
			return crc;
		}
		/**
		 * Gets the entry id.
		 * @return the entry.
		 */
		public int getEntry() {
			return entry;
		}
		/**
		 * Gets the chile size.
		 * @return the childSize
		 */
		public int getChildSize() {
			return childSize;
		}
		/**
		 * Gets the hash name.
		 * @return the hashName
		 */
		public int getHashName() {
			return hashName;
		}
		/**
		 * Gets the child hash names.
		 * @return the childHashNames
		 */
		public int[] getChildHashNames() {
			return childHashNames;
		}
		/**
		 * Gets the child entires.
		 * @return the childEntries
		 */
		public int[] getChildEntries() {
			return childEntries;
		}
		/**
		 * Gets the digests
		 * @return the digests
		 */
		public byte[] getDigests() {
			return digests;
		}
	}
	
	/**
	 * There are two different FITs that the client use to read the cache.
	 * The 420 - 604 client uses the old one but the 605+ use the new one.
	 */
	public static enum Format {
		NEW,
		OLD;
	}
}