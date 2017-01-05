/**
* Classe TimeUnits.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.time;

public enum TimeUnits {
	Milliseconde("Milliseconde"),
	Seconde("Seconde"),
	Minute("Minute"),
	Heure("Heure"),
	Annee("Annee");
	
	private final String name;
	
	private TimeUnits(String name){
		this.name=name;
	}
		
	@Override
	public String toString() {
		return name;
	}

}

