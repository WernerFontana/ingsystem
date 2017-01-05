/**
* Classe SimObjectRemovedListener.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

@FunctionalInterface
public interface SimObjectRemovedListener {
	void SimObjectRemoved(SimObject o);
}

