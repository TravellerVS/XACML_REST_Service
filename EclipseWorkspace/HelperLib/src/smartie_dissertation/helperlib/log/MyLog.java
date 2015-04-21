package smartie_dissertation.helperlib.log;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import smartie_dissertation.helperlib.generic.*;

public class MyLog {
	private static MyLog instance = null;
	private static final String MAIN_LOG_FILE_LOCATION = "C:\\Users\\Korisnik\\Google disk\\UA\\Dissertation\\ProjectWorkspace\\HelperLib\\log\\myLogFile.txt";
//	private static final String MAIN_LOG_FILE_LOCATION = "\\log\\myLogFile.txt";
	private static final boolean debug = true;
	private File logFile;
	
	public static enum logMessageType {
	    INFO, DEBUG, ERROR, DEFAULT
	}
	
	private MyLog(){
		super();
		this.logFile = FileIOHandler.getFile(MAIN_LOG_FILE_LOCATION);
		FileIOHandler.emptyFile(this.logFile);
	}
	
	private static MyLog getInstance(){
		if(instance == null){
			instance = new MyLog();
		}
		return instance;
	}
	
	/**
	 * @param message - message to be logged
	 */
	public static void log(String message){
		log(message, logMessageType.DEFAULT);
	}

	/**
	 * @param message - message to be logged
	 * @param type - type of message
	 */
	public static void log(String message, logMessageType type){
		message = " :"+message;
		Level loggerLevel;
		switch(type){
			case INFO:
				message = "INFO"+message;
				loggerLevel = Level.INFO;
				break;
			case DEBUG:
				if(!debug){
					return;
				}
				message = "DEBUG"+message;
				loggerLevel = Level.INFO;
				break;
			case ERROR:
				message = "ERROR"+message;
				loggerLevel = Level.SEVERE;
				break;
			case DEFAULT:
				loggerLevel = Level.ALL;
				break;
			default:
				loggerLevel = Level.ALL;
				break;
		}
		System.out.println(message);		
//		Logger.getLogger(getInstance().getClass().getName()).log(loggerLevel, message);
		FileIOHandler.outputToFile(getInstance().logFile, message, true);
	}
	
	/**
	 * @param message - message to be logged
	 * @param type - type of message
	 * @param source - source of message. Best practice to put the source class. 
	 */
	public static void log(String message, logMessageType type, String source){
		message = "(source: "+source+") :"+message;
		log(message, type);
	}
	
	/**
	 * @param message - message to be logged
	 * @param type - type of message
	 * @param source - source of message. Best practice to put the source class.
	 * @param e - exception that can be forwarded to the log
	 */
	public static void log(String message, logMessageType type, String source, Exception e){
		e.printStackTrace();
		log(message, type, source);
	}
	
}
