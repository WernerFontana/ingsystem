/**
* Classe ActivationNotification.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.components;

@FunctionalInterface
public interface ActivationNotification {
	void NotifyActivation(IEntity sender, boolean starting);
}

