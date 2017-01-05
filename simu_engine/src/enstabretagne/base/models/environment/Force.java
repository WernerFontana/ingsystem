/**
* Classe Force.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.models.environment;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Force {

	private Vector3D pointApplication;
	private Vector3D force;
	public Vector3D getPointApplication() {
		return pointApplication;
	}
	public void setPointApplication(Vector3D pointApplication) {
		this.pointApplication = pointApplication;
	}
	public Vector3D getForce() {
		return force;
	}
	public void setForce(Vector3D force) {
		this.force = force;
	}
	public Force(Vector3D pointApplication, Vector3D force) {
		super();
		this.pointApplication = pointApplication;
		this.force = force;
	}
	public static final double originRadius = 0.3;
}

