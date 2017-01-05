/**
* Classe EndSimulationEvent.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.components;

import enstabretagne.simulation.core.SimEvent;

public class EndSimulationEvent extends SimEvent {

	@Override
	public void Process() {
		Owner().interruptEngineByDate();
	}

}

