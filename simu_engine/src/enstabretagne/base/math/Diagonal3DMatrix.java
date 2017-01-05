/**
* Classe Diagonal3DMatrix.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.math;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Diagonal3DMatrix {

	private double x,y,z;
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	private Diagonal3DMatrix(){}
	
	public Diagonal3DMatrix(double x,double y,double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Vector3D dot(Vector3D v) {
		return new Vector3D(v.getX()*x,v.getY()*y,v.getZ()*z);
	}

	public Diagonal3DMatrix inverse() {
		
		Diagonal3DMatrix m = new Diagonal3DMatrix();
		if(x!=0) m.x=1/x;
		if(y!=0) m.y=1/y;
		if(z!=0) m.z=1/z;
		
		return m;
	}
}

