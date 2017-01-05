/**
* Classe TerminatingNotification.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.components;


@FunctionalInterface
public interface TerminatingNotification {
	void NotifyTerminating(IEntity sender,boolean restart);
}

