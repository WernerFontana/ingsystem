/**
* Classe SimEngineEvent.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

import enstabretagne.base.time.LogicalDateTime;

@FunctionalInterface
public interface SimEngineEvent {
	void simEngineEvent(LogicalDateTime t);
}

