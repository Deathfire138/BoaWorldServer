package boa.update;

import java.util.logging.Logger;

import java.io.File;

public class Cache {

	private static final Logger logger = Logger.getLogger(Cache.class.getName());

	private static CacheIndex index255;
	private static CacheIndex[] indexes;

	public static void init(String path) throws Exception {
		logger.info("Loading cache...");
		int total = 0;
		for (File file : new File(path).listFiles()) {
			String name = file.getName();
			if (name.startsWith("main_file_cache.idx")) {
				int idx = Integer.parseInt(name.split(".idx")[1]);
				if (idx != 255 && idx > total) {
					total = idx;
				}
			}
		}
		index255 = new CacheIndex(path, 255, null);
		indexes = new CacheIndex[total + 1];
		for (int file = 0; file < indexes.length; file++) {
			indexes[file] = new CacheIndex(path, file, index255);
		}
		logger.info("Cache loaded ["+ indexes.length +"].");
	}
	
	public static CacheIndex getIndex(int idx) {
		if (idx == 255) {
			return index255;
		}
		return indexes[idx];
	}
	
    public static byte[] getByName(int idx, String name) throws Exception {
		int file = findName(idx, name);
		return indexes[idx].getFile(file);
    }

    private static int findName(int idx, String name) {
		int hash = calcJaghash(name);
		for (int i = 0; i < indexes[idx].getCacheFIT().getEntrySize(); i++) {
			if (indexes[idx].getCacheFIT().getEntries()[i].getHashName() == hash) {
				return i;
			}
		}
        return -1;
    }

	private static int calcJaghash(String n) {
        int count = 0;
        byte[] characters = n.getBytes();
        for (int i = 0; i < n.length(); i++) {
            count = (characters[i] & 0xff) + ((count << -1325077051) + -count);
		}
        return count;
    }

	public static byte[] getCacheFile(int idx, int file) {
		try {
			if (idx == 255) {
				return index255.getFile(file);
			}
			return indexes[idx].getFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}