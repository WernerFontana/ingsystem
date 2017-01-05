/**
* Classe LoggerParamsNames.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.utility;

public enum LoggerParamsNames {
	FileName("FileName"),
	DirectoryName("DirectoryName");
	
	private String paramName;
	
	private LoggerParamsNames(String paramName){
		this.paramName = paramName;
	}
	@Override
	public String toString() {
		return paramName;
	}

}

