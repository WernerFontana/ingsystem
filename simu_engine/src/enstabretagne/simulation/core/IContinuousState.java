/**
* Classe IContinuousState.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

public interface IContinuousState extends ICopyable,ICloneable{
	    /// <summary>Update continuous state</summary>
	    /// <param name="rate">state derivative</param>
	    /// <param name="dt">time increment</param>
	    void Update (IContinuousRate rate, double dt);

	    /// <summary>Number of underlying numerical sclar field</summary>
	    int Dimension();

	    /// <summary>Associated zero rate</summary>
	    IContinuousRate ZeroRate();

}

