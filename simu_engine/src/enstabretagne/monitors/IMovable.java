/**
* Classe IMovable.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.monitors;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface IMovable {

	public Vector3D getPosition();
	public Vector3D getVitesse();
	public Vector3D getAcceleration();
	
}

