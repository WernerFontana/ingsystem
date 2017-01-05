/**
* Classe SimObjectActivationChangedEventHandler.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

@FunctionalInterface
public interface SimObjectActivationChangedEventHandler {
    void simObjectActivationChanged(SimObject o, boolean active);
}

