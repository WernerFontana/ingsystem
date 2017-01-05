/**
* Classe KineticContinuous.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.expertise.cinematique;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import enstabretagne.base.time.LogicalDuration;
import enstabretagne.simulation.core.IContinuousState;
import enstabretagne.simulation.core.SimContinuous;

public abstract class KineticContinuous extends SimContinuous{

	public KineticContinuous(IContinuousState state, LogicalDuration step) {
		super(state, step);
		setIsSynchro(true);

	}

	public Vector3D getPosition() {
		return ((KineticState) getUntypedROState()).getPos();
	}
	
	public Vector3D getVitesse() {
		return ((KineticState) getUntypedROState()).getVel();
	}
	
	public Vector3D getAcceleration() {
		return ((KineticRate) getUntypedRORate()).getVelRate();		
	}
	
	public double getAbscisseCurviligne() {
		return ((KineticState) getUntypedROState()).getCurv();
	}
	

}

