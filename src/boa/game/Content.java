package boa.game;


import java.io.File;
import java.util.LinkedList;
import java.util.logging.Logger;

import boa.game.handlers.StandardHandler;
import boa.game.handlers.MinigameHandler;
import boa.game.handlers.QuestHandler;
import boa.game.handlers.SkillHandler;

import model.Player;

public class Content {

	private static final Logger logger = Logger.getLogger(Content.class.getName());
	
	public static void init() {
		logger.info("Loading content...");
		long started = System.nanoTime();
		LinkedList<String> classDirectorys = new LinkedList<String>();
		LinkedList<String> directorys = new LinkedList<String>();
		for (File file : new File("./bin/").listFiles()) {
			if (file.isFile()) {
				classDirectorys.add(file.getPath().replace(".class", "").replace(".\\bin\\", "").replace("\\", "."));
			} else if (file.isDirectory()) {
				directorys.add(file.getPath().replace("\\", "/"));
			}
		}
		while (!directorys.isEmpty()) {
			String directory = directorys.poll();
			for (File file : new File(directory).listFiles()) {
				if (file.isFile()) {
					classDirectorys.add(file.getPath().replace(".class", "").replace(".\\bin\\", "").replace("\\", "."));
				} else if (file.isDirectory()) {
					directorys.add(file.getPath().replace("\\", "/"));
				}
			}
		}
		for (String directory : classDirectorys) {
			try {
				if (directory.contains("$")) {
					continue;
				}
				Class.forName(directory.replace("./bin/", "").replace("/", ".")).newInstance();
			} catch (Exception e) {
				continue;
			}
		}
		long taken = System.nanoTime() - started;
		logger.info("Time taken to load content: "+((double) taken / 1000000000.0)+" seconds.");
	}

	
/*	public static boolean examineObject(Player player, int objectId) {
		if (!QuestHandler.objectExamine(player, objectId)) {
			if (!MinigameHandler.objectExamine(player, objectId)) {
				if (!SkillHandler.objectExamine(player, objectId)) {
					return Handler.objectExamine(player, objectId);
				}
			}
		}
		return true;
	}*/
	
	public static boolean button(Player player, int opcode, int interfaceId, int button, int button2) {
		if (!QuestHandler.button(player, opcode, interfaceId, button, button2)) {
			if (!MinigameHandler.button(player, opcode, interfaceId, button, button2)) {
				if (!SkillHandler.button(player, opcode, interfaceId, button, button2)) {
					return StandardHandler.button(player, opcode, interfaceId, button, button2);
				}
			}
		}
		return true;
	}
	
}
