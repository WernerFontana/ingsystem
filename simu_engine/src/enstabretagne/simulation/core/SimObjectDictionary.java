/**
* Classe SimObjectDictionary.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import enstabretagne.base.messages.MessagesDictionnary;
import enstabretagne.base.utility.Logger;

public class SimObjectDictionary {

	HashMap<Class, SimObjectType> objectTypesDictionnary;
	List<SimObjectAddedListener> simObjectAddedListener; 
	List<SimObjectRemovedListener> simObjectRemovedListener; 
	SimEngine engine;
	
	public void Add(SimObject o) {
		
		Class oc = o.getClass();
		if(SimObject.class.isAssignableFrom(oc))
		{
			
			SimObjectType simObjectType;
			if(!objectTypesDictionnary.containsKey(o.getClass())) {
				simObjectType = new SimObjectType(o.getClass());
			
				objectTypesDictionnary.put(oc, simObjectType);
			      Logger.Information(this, "Add", MessagesDictionnary.NewClassObjectAdded, oc.getName());
			}
			else
				simObjectType= objectTypesDictionnary.get(o.getClass());
		      
			simObjectType.objectInstances.add(o);
		    simObjectAddedListener.forEach(l -> l.SimObjectAdded(o));  
		      Logger.Information(this, "Add", MessagesDictionnary.NewObjectAdded, o.getName());
			
		}
		
		
	}

	public void Remove(SimObject o) {
		
		SimObjectType simObjectType;
		simObjectType= objectTypesDictionnary.get(o.getClass());
		if(simObjectType!=null) {
	      simObjectType.objectInstances.remove(o);
		    simObjectRemovedListener.forEach(l -> l.SimObjectRemoved(o));  
		}
		else
			Logger.Error(this, "Remove", MessagesDictionnary.ObjectTypeNotReferenced, o.getClass().getName());
		
	}

	public List<SimObject> getKnownSimObjects() {
    	List<SimObject> requestedObjects = new ArrayList<SimObject>();    	
    	for(SimObjectType ts:objectTypesDictionnary.values()){
    		for(SimObject o:ts.objectInstances)
    		{
    			requestedObjects.add(o);
    		}
		}
    	return requestedObjects;
   	}
		
    public List<SimObject> requestSimObject(SimObjectRequest r){
    	List<SimObject> requestedObjects = new ArrayList<SimObject>();    	
    	for(SimObjectType ts:objectTypesDictionnary.values())
    		for(SimObject o:ts.objectInstances)
    		{
    			if(r.filter(o))
    				requestedObjects.add(o);
    		}
    	return requestedObjects;

    }
	public SimObjectType getSimObjectTypeFrom(Class c) {
		
		return objectTypesDictionnary.get(c);
	}

	public SimObject RetreiveSimObject(int liveId) {
		for(SimObjectType sot : objectTypesDictionnary.values())
		{
			for(SimObject so : sot.objectInstances){
				if(so.getSimObjID()==liveId) return so;
			}
		}
		return null;
	}

	public int terminate(boolean restart) {
		int n = 0;
	      if (objectTypesDictionnary == null)
	        return 0;

	      for(SimObjectType ot : objectTypesDictionnary.values())
	        n += ot.terminate(restart);
	      return n;
		
	}

	public void initialize(SimEngine simEngine) {
		objectTypesDictionnary = new HashMap<>();
		engine = simEngine;
		simObjectAddedListener=new ArrayList<SimObjectAddedListener>(); 
		simObjectRemovedListener=new ArrayList<SimObjectRemovedListener>(); 
	}
	
	public void AddSimObjectAddedListener(SimObjectAddedListener listener){
		simObjectAddedListener.add(listener);
	}

	public void RemoveSimObjectAddedListener(SimObjectAddedListener listener){
		simObjectAddedListener.remove(listener);
	}

	public void AddSimObjectRemovedListener(SimObjectRemovedListener listener){
		simObjectRemovedListener.add(listener);
	}

	public void RemoveSimObjectRemovedListener(SimObjectRemovedListener listener){
		simObjectRemovedListener.remove(listener);
	}

	public void WriteObjectTypeDictionary(PrintWriter out) {
		
		
	}

}

