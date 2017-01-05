/**
* Classe CreationNotification.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.components;

@FunctionalInterface
public interface CreationNotification {
	void NotifyCreation(IEntity sender,String name, SimFeatures params);
}

