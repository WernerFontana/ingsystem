/**
* Classe NotifySimEngineEvent.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

@FunctionalInterface
public interface NotifySimEngineEvent {
	void notifySimEngineEvent(SimTimeEvent ev);
}

