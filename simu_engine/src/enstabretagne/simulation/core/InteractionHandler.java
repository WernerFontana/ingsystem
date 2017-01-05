/**
* Classe InteractionHandler.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

@FunctionalInterface
public interface InteractionHandler {
	void EmitInteraction(Class id, SimObject sender, Object... parms);

}

