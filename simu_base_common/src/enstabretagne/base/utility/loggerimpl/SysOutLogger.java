/**
* Classe SysOutLogger.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.utility.loggerimpl;

import java.time.temporal.Temporal;
import java.util.HashMap;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.utility.ILogger;
import enstabretagne.base.utility.IRecordable;
import enstabretagne.base.utility.LogLevels;
import enstabretagne.base.utility.Settings;
import enstabretagne.simulation.components.ScenarioId;

public class SysOutLogger implements ILogger {

	char sep = Settings.sep();
	@Override
	public boolean open(HashMap<String,Object> initParams) {
		
		return true;
	}
	

	@Override
	public void log(StackTraceElement el,ScenarioId scenarioId,Temporal t, LogicalDateTime d, LogLevels level, Object obj,
			String function, String message, Object... args) {		
//		StackTraceElement[]sts = Thread.currentThread().getStackTrace();
//		StackTraceElement el= sts[8];
	
		String elTxt = "("+el.getFileName()+":"+el.getLineNumber()+")>"+el.getMethodName();
		
		if(args!=null) {
			for(Object arg : args){
				if(Exception.class.isAssignableFrom(arg.getClass()))
					((Exception) arg).printStackTrace(System.err);;
			}
		}
		
		if(obj!=null)
		{
			if(level.equals(LogLevels.data)){
				IRecordable r = (IRecordable) obj;
				String s="";
				for(int i=0;i<r.getTitles().length;i++){
					s=s+r.getTitles()[i]+"="+r.getRecords()[i]+";";
					
				}
				System.out.println(String.format(elTxt+sep+ scenarioId.getScenarioId()+sep+Long.toString(scenarioId.getRepliqueNumber())+sep+d + sep + level + sep + s + sep+message,args));
			}
			else
				System.out.println(String.format(elTxt+sep+ scenarioId.getScenarioId()+sep+Long.toString(scenarioId.getRepliqueNumber())+sep+d + sep + level + sep + obj.toString() + sep+message,args));
		}
		else
			System.out.println(String.format(elTxt+sep+ scenarioId.getScenarioId()+sep+Long.toString(scenarioId.getRepliqueNumber())+sep+d + sep + level + sep + sep+message,args));			
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear(HashMap<String,Object> initParams) {
		System.out.flush();
		System.err.flush();
		
	}

}

