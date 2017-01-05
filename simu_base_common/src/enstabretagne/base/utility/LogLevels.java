/**
* Classe LogLevels.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.utility;

public enum LogLevels {
	warning("Warning"),
	information("information"),
	error("Error"),
	detail("Detail"),
	fatal("Fatal"),
	data("Data");
	
	String s;
	private LogLevels(String s)
	{
		this.s=s;
	}
	@Override
	public String toString() {
		return s;
	}
}

