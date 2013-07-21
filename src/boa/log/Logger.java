package boa.log;

import java.util.Calendar;

import boa.Server;

public class Logger {

	public enum Type {
		INFO,
		DEBUG,
		MILD,
		SEVERE;
	}
	
	public static void log(Type type, String message, Class theClass) {
		//TODO debug mode
		//if (type == Type.DEBUG && !Server.isDebugEnabled()) {
		//	return;
		//}
		System.out.println(Calendar.getInstance().getTime().toString() + " " + type.toString() + " " + theClass.getName() + "\n" + message + "\n");
	}
	
	public static void log(Type type, String message, Throwable throwable, Class theClass) {
		
	}
	
	public static void log(Type type, Throwable throwable, Class theClass) {
		
	}
	
}
