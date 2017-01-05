/**
* Classe InterruptionRequest.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

@FunctionalInterface
public interface InterruptionRequest {
	boolean needInterruption();
}

