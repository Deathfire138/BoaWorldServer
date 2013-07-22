package boa.game.handlers;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

import model.Player;

import boa.game.handlers.interfaces.ButtonHandler;
import boa.game.handlers.types.MinigameHandler;
import boa.game.handlers.types.QuestHandler;
import boa.game.handlers.types.SkillHandler;
import boa.game.handlers.types.StandardHandler;

public class Handler {

	private static final Logger logger = Logger.getLogger(Handler.class.getName());
	
	private static final Collection<StandardHandler> standardHandlers = new LinkedList<StandardHandler>();
	
	private static final Collection<SkillHandler> skillHandlers = new LinkedList<SkillHandler>();
	
	private static final Collection<MinigameHandler> minigameHandlers = new LinkedList<MinigameHandler>();
	
	private static final Collection<QuestHandler> questHandlers = new LinkedList<QuestHandler>();
	
	public static void load() {
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
	
	public static void registerHandler(int priorityType, Object sh) {
		if (priorityType == 0) {
			standardHandlers.add((StandardHandler) sh);
		} else if (priorityType == 1) {
			skillHandlers.add((SkillHandler) sh);
		} else if (priorityType == 2) {
			minigameHandlers.add((MinigameHandler) sh);
		} else if (priorityType == 3) {
			questHandlers.add((QuestHandler) sh);
		}
	}
	
	public static boolean button(Player player, int opcode, int interfaceId, int buttonId, int buttonId2) {
		for (QuestHandler qh : questHandlers)
			if (qh instanceof ButtonHandler)
				if (((ButtonHandler)qh).handleButton(player, opcode, interfaceId, buttonId, buttonId2))
					return true;
		for (MinigameHandler mh : minigameHandlers) 
			if (mh instanceof ButtonHandler) 
				if (((ButtonHandler)mh).handleButton(player, opcode, interfaceId, buttonId, buttonId2)) 
					return true;
		for (SkillHandler skh : skillHandlers)
			if (skh instanceof ButtonHandler)
				if (((ButtonHandler)skh).handleButton(player, opcode, interfaceId, buttonId, buttonId2))
					return true;
		for (StandardHandler sh : standardHandlers) 
			if (sh instanceof ButtonHandler) 
				if (((ButtonHandler)sh).handleButton(player, opcode, interfaceId, buttonId, buttonId2)) 
					return true;
		return false;
	}
	
}
