package enstabretagne.monitors;

import java.util.HashMap;
import java.util.List;

import enstabretagne.base.utility.Logger;
import enstabretagne.simulation.components.SimScenario;
import enstabretagne.simulation.core.IMonitor;
import enstabretagne.simulation.core.SimEngine;

public abstract class AbstractMonitor implements IMonitor {

	private SimScenario currentScenario;
	public SimScenario getCurrentScenario(){
		return currentScenario;
	}

	protected void setCurrentScenario(SimScenario currentScenario) {
		this.currentScenario = currentScenario;
	}

	SimEngine engine;
	HashMap<String,HashMap<String,Object>> loggersNames;

	public SimEngine getEngine() {
		return engine;
	}
	
	public AbstractMonitor(HashMap<String,HashMap<String,Object>> loggersNames)
	{
		this.loggersNames=loggersNames;	
		engine = new SimEngine();
		Logger.Init(loggersNames,true);

	}
	
	public abstract void run(SimScenario s,long repliqueNumber);
	public abstract void run(List<SimScenario> listeScenario,long repliqueNumbers);
	public abstract void terminate(boolean restart);
}
