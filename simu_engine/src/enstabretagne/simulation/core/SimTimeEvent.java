/**
* Classe SimTimeEvent.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

import enstabretagne.base.time.LogicalDateTime;

public abstract class SimTimeEvent extends SimEvent{
	
	public LogicalDateTime getProcessDate(){
		return null;
	}

	
}

