/**
* Classe IContinuousRate.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

/// <summary>Minimum interface to be a continuous rate used by Runge-Kutta.</summary>
public interface IContinuousRate extends ICopyable,ICloneable{
	
	/// <summary>add a derivative increment (rate * k) the this IContinuousRate</summary>
	    /// <param name="rate">derivative increment</param>
	    /// <param name="k">dimensionless coefficient applied before adding</param>
	    void Affine (IContinuousRate rate, double k);

	    /// <summary>multiply this IContinuousRate by a coefficient</summary>
	    /// <param name="k">dimensionless coefficient applied before adding</param>
	    /// <remarks>Reset this IContinuousRate to zero when called with an argument valued to 0</remarks>
	    void Mult(double k);  
	    

}

