package maps;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapData {
	
	private static final Map<Integer, int[]> KEYS = new HashMap<Integer, int[]>();
	
	public static int[] getKeys(int region) {
		System.out.println("Grabbing keys for region "+region);
		if (KEYS.containsKey(region)) {
			return KEYS.get(region);
		} else {
			try {
				FileReader fis = new FileReader("./data/XTEA/"+region+".txt");
				BufferedReader br = new BufferedReader(fis);
				int[] keys = new int[4];
				for (int i = 0; i < 4; i++) {
					try {
						keys[i] = Integer.parseInt(br.readLine());
					} catch (NumberFormatException e) {
						return new int[] {0, 0, 0, 0};
					} catch (IOException e) {
						return new int[] {0, 0, 0, 0};
					}
				}
				KEYS.put(region, keys);
				return keys;
			} catch (FileNotFoundException e) {
				return new int[] {0, 0, 0, 0};
			}
		}
	}
	
}
