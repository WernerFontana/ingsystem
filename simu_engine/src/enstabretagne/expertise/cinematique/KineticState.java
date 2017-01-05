/**
* Classe KineticState.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.expertise.cinematique;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import enstabretagne.simulation.core.IContinuousRate;
import enstabretagne.simulation.core.IContinuousState;

public class KineticState implements IContinuousState  {

    /// <summary>Position of the mobile</summary>
    private Vector3D pos;

    public Vector3D getPos() {
		return pos;
	}

	public void setPos(Vector3D pos) {
		this.pos = pos;
	}

	public Vector3D getVel() {
		return vel;
	}

	public void setVel(Vector3D vel) {
		this.vel = vel;
	}

	public double getCurv() {
		return curv;
	}

	public void setCurv(double curv) {
		this.curv = curv;
	}

	/// <summary>Velocity of the mobile</summary>
    private Vector3D vel;

    /// <summary>Curvilinear coordinate of the mobile (number of meters run from the begining)</summary>
    private double curv;
    
  /// <summary>Constructor from a location</summary>
    public KineticState(Vector3D pos) {
    	this (pos, Vector3D.ZERO, 0);
    }

    /// <summary>Constructor from a location and velocity</summary>
    public KineticState(Vector3D pos, Vector3D vel){
    	this(pos, vel, 0);
    }

    /// <summary>Constructor from a location, velocity, and curvilinear coordinate</summary>
    public KineticState(Vector3D pos, Vector3D vel, double curv)
    {
      this.pos = pos;
      this.vel = vel;
      this.curv = curv;
    }
    
	@Override
	public Object clone() {
		return new KineticState(getPos(), getVel(), getCurv());
	}

	@Override
	public void CopyFrom(Object item) {
		KineticState ks = (KineticState) item;
		this.pos = ks.getPos();
		this.vel = ks.getVel();
		this.curv = ks.getCurv();
	}

	@Override
	public void Update(IContinuousRate rate, double dt) {
	      KineticRate v = (KineticRate) rate;
	      pos  = pos.add(v.getPosRate().scalarMultiply(dt));
	      vel  = vel.add(v.getVelRate().scalarMultiply(dt));
	      curv += v.getCurvRate() * dt;
		
	}

	@Override
	public int Dimension() {
		return pos.getSpace().getDimension()+vel.getSpace().getDimension()+1;//+1 dû à l'abscisse curviligne 
	}

	@Override
	public IContinuousRate ZeroRate() {
		return new KineticRate();
	}

}

