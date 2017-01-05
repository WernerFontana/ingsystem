/**
* Classe KineticRate.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.expertise.cinematique;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import enstabretagne.simulation.core.IContinuousRate;

public class KineticRate implements IContinuousRate {
	
	public KineticRate()
	{
		setCurvRate(0);
		setPosRate(Vector3D.ZERO);
		setVelRate(Vector3D.ZERO);
	}
	
    /// <summary>Position of the mobile</summary>
    private Vector3D posRate;

    /// <summary>Velocity of the mobile</summary>
    private Vector3D velRate;

	/// <summary>Curvilinear coordinate of the mobile (number of meters run from the begining)</summary>
    private double curvRate = 0;
    public Vector3D getPosRate() {
		return posRate;
	}

	public void setPosRate(Vector3D posRate) {
		this.posRate = posRate;
	}

	public Vector3D getVelRate() {
		return velRate;
	}

	public void setVelRate(Vector3D velRate) {
		this.velRate = velRate;
	}

	public double getCurvRate() {
		return curvRate;
	}

	public void setCurvRate(double curvRate) {
		this.curvRate = curvRate;
	}

	@Override
	public void CopyFrom(Object item) {
	      KineticRate kr = (KineticRate) item;
	      curvRate = kr.getCurvRate();
	      posRate = kr.getPosRate();
	      velRate = kr.getVelRate();
	}

	@Override
	public Object clone() {
		KineticRate r = new KineticRate();
		r.CopyFrom(this);
		return r;
	}

	@Override
	public void Affine(IContinuousRate rate, double k) {
		KineticRate v = ( KineticRate) rate ;
		posRate=posRate.add(v.getPosRate().scalarMultiply(k));
		velRate=velRate.add(v.getVelRate().scalarMultiply(k));
	      curvRate = v.getCurvRate() *k;
	}

	@Override
	public void Mult(double k) {
		posRate=posRate.scalarMultiply(k);
		velRate=velRate.scalarMultiply(k);
		curvRate *= k;
		
	}

	

}

