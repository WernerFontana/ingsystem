/**
* Classe InitializationNotification.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.components;

import enstabretagne.simulation.components.SimInitParameters;

@FunctionalInterface
public interface InitializationNotification {
	void NotifyInitialization(IEntity sender,SimInitParameters init);
}

