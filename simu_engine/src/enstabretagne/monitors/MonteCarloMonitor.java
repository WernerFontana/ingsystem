package enstabretagne.monitors;

import java.util.HashMap;
import java.util.List;

import enstabretagne.base.math.MoreRandom;
import enstabretagne.base.utility.Logger;
import enstabretagne.simulation.components.SimScenario;
import enstabretagne.simulation.components.SimScenarioInit;
import enstabretagne.simulation.core.IMonitor;
import enstabretagne.simulation.core.SimEngine;

public abstract class  MonteCarloMonitor extends AbstractMonitor implements IMonitor {


public MonteCarloMonitor(HashMap<String,HashMap<String,Object>> loggersNames)
{
	super(loggersNames);

}

@Override
public void run(SimScenario s, long replique) {
	setCurrentScenario(s);
	engine.Init(getCurrentScenario());

	//Initialisation de la graine
	MoreRandom.globalSeed = replique;
	
	//Initialisation du scénario et tracé de la valeur de la graine dans l'Init du scénario
	getCurrentScenario().Initialize(new SimScenarioInit(MoreRandom.globalSeed,replique));
	getCurrentScenario().activate();
	engine.simulate();				
				
	getCurrentScenario().deactivate();
	getCurrentScenario().terminate(true);
}

@Override
public void run(List<SimScenario> listeScenario,long repliqueNumber) {
	for(SimScenario scenario : listeScenario) {
		for(long k=0;k<repliqueNumber;k++) {
			run(scenario,k);
			
		}
	}
}

@Override
public void terminate(boolean restart) {
	Logger.Terminate();
}

}
