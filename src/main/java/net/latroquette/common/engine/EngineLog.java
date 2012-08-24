/**
 * Debug class for logging and output, will output on a terminal if asked.
 */
package net.latroquette.common.engine;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EngineLog{
	
	private Logger logger;
	public static final Level TRACE_LEVEL = Level.ALL; 
	public static String GLOBAL_LOGGER_NAME = "DreamZone-Server";
	public static String CLIENT_LOGGER_NAME = "DreamZone-Client";
	private static final EngineLog ENGINE_SERVER_LOG = new EngineLog(net.latroquette.common.engine.Engine.class.getName(),true);
	public static final Logger SERVER = ENGINE_SERVER_LOG.getLogger();

	protected EngineLog(String name, boolean displayOnConsole)
	{
		logger = Logger.getLogger(name);
		getLogger().setLevel(TRACE_LEVEL);
		if(displayOnConsole)
		{
			ConsoleHandler cLogger = new ConsoleHandler();
			cLogger.setLevel(TRACE_LEVEL);
			getLogger().addHandler(cLogger);
		}
		//TODO Add a static path for log
		//Unique fileLog at the moment
		/**
		 * FileHandler file = null;
		try {
			file = new FileHandler("logfileName.log");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.addHandler(file);
		*/
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}
	

}
