/**
* Classe Logger.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.utility;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.simulation.components.IScenarioIdProvider;
import enstabretagne.simulation.components.ScenarioId;
import enstabretagne.simulation.core.ISimulationDateProvider;


/*
 * La classe Logger permet d'enregistrer les journaux et les données produites au cours de la simulation.
 * Les méthodes publique Detail, Error, Fatal, Information ajoutent d'elle même un timestamp lié au temps réel
 * Il délivre plusieurs services:
 * -
 */
public class Logger {
	
	private static Logger log = new Logger();
	
	private ISimulationDateProvider engine;
	 
	//--------------------------- Journaling -----------------------------------
    /// <summary>
    /// Link between simulation application overridable settings for trace level and the 'old' TraceSwitch
    /// </summary>
    private List<ILogger> loggers;
    
    public static void Data(IRecordable obj) {
    	if(obj!=null)
    		log.log(LogLevels.data, obj, "", "");
    }
    
    public static void Fatal(Object obj,String function,String message,Object... args)
    {
    	log.log(LogLevels.fatal, obj, function, message, args);
    }

    public static void Error(Object obj,String function,String message,Object... args)
    {
    	log.log(LogLevels.error, obj, function, message, args);
    }

    public static void Detail(Object obj,String function,String message,Object... args)
    {
    	log.log(LogLevels.detail, obj, function, message, args);    	
    }

    public static void Information(Object obj,String function,String message,Object... args)
    {
    	log.log(LogLevels.information, obj, function, message, args);    	
    }
    
    public static void Warning(Object obj,String function,String message,Object... args)
    {
    	log.log(LogLevels.warning, obj, function, message, args);    	
    }
    
    private void log(LogLevels level, Object obj,String function,String message,Object... args) {
    	if(engine!=null) {
    		if(engine instanceof IScenarioIdProvider)
    			log(((IScenarioIdProvider) engine).getScenarioId(),Instant.now(),engine.SimulationDate(),level,obj,function, message,args);
    		else
    			log(ScenarioId.ScenarioID_NULL,Instant.now(),engine.SimulationDate(),level,obj,function, message,args);
    	}
    	else
    		log(ScenarioId.ScenarioID_NULL,Instant.now(),LogicalDateTime.Zero,level,obj,function, message,args);
    		
	}
	protected void log(ScenarioId scenarioId, Temporal t, LogicalDateTime d, LogLevels level, Object obj,String function,String message,Object... args)
	{
		boolean toBeLogged=true;
		StackTraceElement[] st= Thread.currentThread().getStackTrace();
		StackTraceElement el= Thread.currentThread().getStackTrace()[4];

		if(Settings.filterEngineLogs())
		{
			String s = el.getClassName();

			boolean hasTBeFiltered =false;
			for(Class c : Settings.classToFilter)
				hasTBeFiltered = hasTBeFiltered | s.startsWith(c.getName());
			if(hasTBeFiltered)
				if(level.equals(LogLevels.detail)||level.equals(LogLevels.information))
					toBeLogged=false;
			}
		
		
		if(toBeLogged) loggers.forEach((log) -> log.log(el,scenarioId,t, d, level, obj, function, message,args));
	}
	
	public void addLogger(ILogger log) {
		loggers.add(log);
	}
	
	public void clearLoggers(){
		loggers.clear();
	}
	
	public Logger() {
		loggers = new ArrayList<ILogger>();
	}
	
	public static boolean isInitialized()
	{
		return log.engine!=null & log.loggers.size()>0;
	}
	@Override
	public void finalize() throws Throwable {
		super.finalize();
		loggers.forEach((log) -> log.close());
	}
	
	public static void setDateProvider(ISimulationDateProvider e){
		log.engine = e;
	}
	
	public static void Init(HashMap<String,HashMap<String,Object>> loggers,boolean clear)
	{
		log.clearLoggers();
		if(log.engine==null)
			log.engine = new ISimulationDateProvider() {
			
			@Override
			public LogicalDateTime SimulationDate() {
					
				return new LogicalDateTime("01/01/2015 00:00:00");
			}
		};
		if(loggers !=null) {
			for(Map.Entry<String,HashMap<String,Object>> m:loggers.entrySet()){
				
				String s = m.getKey();
				HashMap<String,Object> params = m.getValue();
				try {
					
					Class c = Class.forName(s);
					if(ILogger.class.isAssignableFrom(c))
					{
						try {
							ILogger logger=(ILogger) c.newInstance();
							boolean success=logger.open(params);
							if(success)
								log.addLogger(logger);
							
							if(clear) logger.clear(params);
						} catch (InstantiationException | IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
					}
				} catch (ClassNotFoundException e1) {
					System.err.println("Attention la classe de logger '" + e1.getMessage() + "' n'a pas été trouvée. Logger non pris en charge ");
				}
			}
			
		}
	}
	
	public static void Init(ISimulationDateProvider e,HashMap<String,HashMap<String,Object>> loggers,boolean clear)
	{
		log.engine = e;
		Init(loggers,clear);
		
	}

	public static void Terminate() {
		// TODO Auto-generated method stub
		try {
			log.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

