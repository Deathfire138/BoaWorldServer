package boa;

public class Main {

	/**
	 * 
	 * @param args[] [0] = worldId, [1] = members boolean, [2] = location, [3] = activity, [4] = debug boolean
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, Exception {
		new Server(Byte.parseByte(args[0]), Boolean.parseBoolean(args[1]), Byte.parseByte(args[2]), args[3], Boolean.parseBoolean(args[4]));
	}
	
}
