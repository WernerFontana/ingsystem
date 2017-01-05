/**
* Classe SimObjectType.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

import java.util.ArrayList;
import java.util.List;

import enstabretagne.base.messages.MessagesSimEngine;
import enstabretagne.base.utility.Logger;

public class SimObjectType {

	Class<? extends SimObject> c;
	List<SimObject> objectInstances;
	
	
	public SimObjectType(Class<? extends SimObject> c) {
		this.c=c;	
		objectInstances = new ArrayList<SimObject>();
	}
	
	public int terminate(boolean restart)
	{
		if (objectInstances == null)
			return 0;
	  int n = objectInstances.size();
	  if (n == 0)
	    return 0;
	
	  for(SimObject o : objectInstances) {
	    Logger.Warning(this,"terminate",MessagesSimEngine.Finalizing, MessagesSimEngine.ZombiObjectFrom0, o.getClass().getName());
	    o.terminate(restart);
	  }
	  objectInstances.clear();;
	  return n;
	
	}
}

