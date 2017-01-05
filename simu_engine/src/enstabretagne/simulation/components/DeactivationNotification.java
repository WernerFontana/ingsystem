/**
* Classe DeactivationNotification.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.components;

@FunctionalInterface
public interface DeactivationNotification {
	void NotifyDeactivation(IEntity sender, boolean stopping);
}

