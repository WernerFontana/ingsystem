/**
* Classe SolidKineticState.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.expertise.cinematique;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import enstabretagne.simulation.core.IContinuousRate;

public class SolidKineticState extends KineticState {

	Rotation angularRotation;
	Rotation angularSpeedRotation;
		
	public SolidKineticState(Vector3D pos,Rotation r) {
		super(pos);
		this.angularRotation=r;
		this.angularSpeedRotation=new Rotation(r.getAxis(),0);
	}

	public SolidKineticState(Vector3D pos, Vector3D vel,Rotation angularRotation, Rotation angularSpeedRotation) {
		super(pos, vel);
		this.angularRotation=angularRotation;
		this.angularSpeedRotation=angularSpeedRotation;
	}

	public Rotation getAngularSpeedRotation() {
		return angularSpeedRotation;
	}

	public void setAngularSpeedRotation(Rotation angularSpeedRotation) {
		this.angularSpeedRotation = angularSpeedRotation;
	}

	public void setAngularRotation(Rotation angularRotation) {
		this.angularRotation = angularRotation;
	}

	public SolidKineticState(Vector3D pos, Vector3D vel, Rotation angularRotation, Rotation angularSpeed, double curv) {
		super(pos, vel, curv);
		this.angularRotation=angularRotation;
		this.angularSpeedRotation=angularSpeed;
	}

	public Rotation getAngularRotation() {
		return angularRotation;
	}
	
	@Override
	public Object clone() {
		return new SolidKineticState(getPos(), getVel(), angularRotation,angularSpeedRotation, getCurv());
	}

	@Override
	public void CopyFrom(Object item) {
		super.CopyFrom(item);
		SolidKineticState sks = (SolidKineticState)((SolidKineticState) item).clone();
		angularRotation= sks.angularRotation;
		angularSpeedRotation=sks.angularSpeedRotation;
	
	}
	
	@Override
	public int Dimension() {
		return super.Dimension()+6;
	}
	
	@Override
	public void Update(IContinuousRate rate, double dt) {
		super.Update(rate, dt);
		SolidKineticRate skr = (SolidKineticRate) rate;
		String s0;
		String s1;
		String s2;

		s0=toString(angularRotation);
		Rotation tempoR=new Rotation(skr.angularRotationRate.getAxis(),skr.angularRotationRate.getAngle()*dt);
		s1=toString(tempoR);
		angularRotation=(tempoR).applyTo(angularRotation);
		s2=toString(angularRotation);
		angularSpeedRotation=(new Rotation(skr.angularSpeedRotationRate.getAxis(),skr.angularSpeedRotationRate.getAngle()*dt)).applyTo(angularSpeedRotation);
	}
	@Override
	public IContinuousRate ZeroRate() {
		return new SolidKineticRate();
	}

	private String toString(Rotation r){
		String s;
		s=r.getAxis().scalarMultiply(r.getAngle()).toString();
		return s;
	}
	
	@Override
	public String toString() {
		String s = "angularRotation="+angularRotation.getAxis().scalarMultiply(angularRotation.getAngle());
		s += " angularSpeedRotation="+angularSpeedRotation.getAxis().scalarMultiply(angularSpeedRotation.getAngle());
		return s;
	}

}

