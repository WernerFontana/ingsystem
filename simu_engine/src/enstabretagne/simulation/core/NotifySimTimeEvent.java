/**
* Classe NotifySimTimeEvent.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

import enstabretagne.base.time.LogicalDateTime;

@FunctionalInterface
public interface NotifySimTimeEvent {
	void notifySimTimeEvent(LogicalDateTime t);
}

