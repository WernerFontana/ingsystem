/**
* Classe SimFeatures.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.components;

public abstract class SimFeatures {
	
	private String id;

	public String getId() {
		return id;
	}

	public SimFeatures(String id) {
		this.id = id;
	}
}

