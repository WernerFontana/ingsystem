/**
* Classe ICopyable.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

/// <summary>Common minimum interface to be state or rate using Runge-Kutta.</summary>
public interface ICopyable {
	    /// <summary>Copy the content of its argument in this ICopyable</summary>
	    /// <param name="item">copied item</param>
	    void CopyFrom(Object item);
}

