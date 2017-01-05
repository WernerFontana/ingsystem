/**
* Classe SolidKineticRate.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.expertise.cinematique;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import enstabretagne.simulation.core.IContinuousRate;

public class SolidKineticRate extends KineticRate {

	Rotation angularRotationRate;
	Rotation angularSpeedRotationRate;

	public Rotation getAngularRotationRate() {
		return angularRotationRate;
	}

	public void setAngularRotationRate(Vector3D angularRotationRate) {
		if(angularRotationRate.equals(Vector3D.ZERO))
			this.angularRotationRate = Rotation.IDENTITY;
		else
			this.angularRotationRate = new Rotation(angularRotationRate.normalize(), angularRotationRate.getNorm());
	}

	public void setAngularRotationRate(Rotation angularRotationRate) {
		this.angularRotationRate = angularRotationRate;
	}
	
	public Rotation getAngularSpeedRotationRate() {
		return angularSpeedRotationRate;
	}

	public void setAngularSpeedRotationRate(Vector3D angularSpeedRotationRate) {
		if(angularSpeedRotationRate.equals(Vector3D.ZERO))
			this.angularSpeedRotationRate = Rotation.IDENTITY;
		else
			this.angularSpeedRotationRate = new Rotation(angularSpeedRotationRate.normalize(), angularSpeedRotationRate.getNorm());
	}
	public void setAngularSpeedRotationRate(Rotation angularSpeedRotationRate) {
		this.angularSpeedRotationRate = angularSpeedRotationRate;
	}

	public SolidKineticRate() {
		super();
		angularRotationRate= Rotation.IDENTITY;
		angularSpeedRotationRate= Rotation.IDENTITY;
	}

	@Override
	public void Affine(IContinuousRate rate, double k) {
		super.Affine(rate, k);
		SolidKineticRate skr = (SolidKineticRate)rate.clone();
		skr.Mult(k);
		angularRotationRate=skr.angularRotationRate.applyTo(angularRotationRate);
		angularSpeedRotationRate=skr.angularSpeedRotationRate.applyTo(angularSpeedRotationRate);
		
	}
	
	@Override
	public Object clone() {
		SolidKineticRate skr = new SolidKineticRate();
		skr.CopyFrom(this);
		return skr;
	}
	@Override
	public void CopyFrom(Object item) {
		SolidKineticRate skr = (SolidKineticRate) item;
		super.CopyFrom(item);
		angularRotationRate = skr.angularRotationRate;		
		angularSpeedRotationRate = skr.angularSpeedRotationRate;		
	}
	@Override
	public void Mult(double k) {
		super.Mult(k);
		angularRotationRate = new Rotation(angularRotationRate.getAxis(), angularRotationRate.getAngle()*k);
		angularSpeedRotationRate = new Rotation(angularSpeedRotationRate.getAxis(), angularSpeedRotationRate.getAngle()*k);
	}

	@Override
	public String toString() {
		double[] as = angularRotationRate.getAngles(RotationOrder.XYZ);
		String s = "angularRotationRate="+angularRotationRate.getAxis().scalarMultiply(angularRotationRate.getAngle())+" xyz="+as[0];
//		s += " angularSpeedRotationRate="+angularSpeedRotationRate.getAxis().scalarMultiply(angularSpeedRotationRate.getAngle());
		return s;
	}
}

