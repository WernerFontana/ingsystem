/**
* Classe SolidKineticContinuous.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.expertise.cinematique;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.simulation.core.IContinuousState;

public abstract class SolidKineticContinuous extends KineticContinuous {

	public SolidKineticContinuous(IContinuousState state, LogicalDuration step) {
		super(state, step);
	}
	
	public Rotation getAngularRotation() {
	
		Rotation r = ((SolidKineticState) getUntypedROState()).getAngularRotation();		
		return r;
	}

	public Rotation getAngularSpeedRotation() {
		Rotation r = ((SolidKineticState) getUntypedROState()).getAngularSpeedRotation();
		return r;
	}
	public Rotation getAngularAccelerationRotation() {
		Rotation r = ((SolidKineticRate) getUntypedRORate()).getAngularSpeedRotationRate();
		return r;
	}
}

