/**
* Classe SimObject.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import enstabretagne.base.messages.MessagesSimEngine;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.base.utility.Logger;

public abstract class SimObject {

	boolean isLocal=true;
	
	protected SimObject(){
		ObjID++;
	    simObjID=ObjID;

	    isLocal = true;
	    reinitSimObject();
	    Logger.Detail(this,"SimObject",MessagesSimEngine.Creating, MessagesSimEngine.UnnamedUnregistered);
	}
	
	protected void reinitSimObject() {
	      timeEvents = new TreeMap<SimEvent, SimEvent>();
	      simObjectActivationChangedListeners = new ArrayList<SimObjectActivationChangedEventHandler>();
		
	}
	
	
	protected LogicalDateTime getCurrentLogicalDate() {
		return getEngine().SimulationDate();
	}
	
    private SimContinuous continuous;
	protected SimContinuous getContinuous()
    {
        return continuous;
    }
    protected void setContinuous(SimContinuous value)
      {
        if (value == continuous) return;

        // removing continuous
        if (continuous != null)
        {
          if (IsActive())
            continuous.setIsActive(false);
          Logger.Detail(this,"setContinuous",MessagesSimEngine.UnDefined, this);
          continuous.setOwner(null);
        }
        continuous = value;
        // Adding continuous
        if (continuous != null)
        {
          continuous.setOwner (this);
          Logger.Detail(this,"setContinuous",MessagesSimEngine.Defining, this);
          if (IsActive())
            continuous.setIsActive(true);
        }
      }
    

    SimEngine engine;
	public SimEngine getEngine()
	{
		return engine;
	}
	
	public void interruptEngineByDate()
	{
		engine.interrupt(EngineInterruptReason.ByDate);
	}
	
	public void setEngine(SimEngine value)
	{	        // Reaffectation of same engine : nothing to do
        if (value == engine)
            return;

          // Deconnection from engine
          if (engine != null)
          {
            if (IsActive())
              deactivate();
            engine.OnSimObjectRemoved(this);
          }

          // Connection to new engine
          engine = value;
          if (engine != null)
          {
            engine.OnSimObjectAdded(this);
            if (IsActive())
              activate();
          }
	}
	

    public abstract boolean IsActive();

    List<SimObjectActivationChangedEventHandler> simObjectActivationChangedListeners;
	protected void activate() {
	      // add local timeEvent to engine Event list
	      for(SimEvent ev : timeEvents.keySet())
	        engine.OnEventPosted(ev);

	      // add local dynamic to engine Dynamic list if synchronized
	      if (continuous != null)
	        continuous.setIsActive(true);

	      // Fire event
	      if (simObjectActivationChangedListeners.size()>0)
	    	  simObjectActivationChangedListeners.forEach((l)->l.simObjectActivationChanged(this, true));		
	}

    protected void deactivate() {
	      // remove local timeEvents from engine Event list 
	      for(SimEvent ev : timeEvents.keySet())
	        engine.OnEventUnPosted(ev);

	      // remove local dynamic to engine Dynamic list if synchronized
	      if (continuous != null)
	        continuous.setIsActive(false);

	      // Fire event
	      if (simObjectActivationChangedListeners.size()>0)
	    	  simObjectActivationChangedListeners.forEach((l)->l.simObjectActivationChanged(this, false));				
	}

    protected void terminate(boolean restart) {
	      while(!timeEvents.isEmpty())
	    	  UnPost(timeEvents.firstKey());

	      continuous = null;
	      timeEvents = null;
	}
	
	private String name;
	public String getName() {
		if(name!=null && name!="")
			return name;
		else
			return "#" + simObjID;
	}
	protected void setName(String value) {
		 if (IsActive())
	          Logger.Warning(this,"setName",MessagesSimEngine.Naming, MessagesSimEngine.AttemptToSetName0OnActiveObject, value);
	        else
	          name = value;		
	}


	private static int ObjID;
	public static int CreatedObjectCount() {	
		return ObjID;
	}

	private static int deletedObjectCount;
	public static int DeletedObjectCount() {
		return deletedObjectCount;
	}


	private int simObjID;	
	public int getSimObjID(){
		return simObjID;
	}
	
	
	private TreeMap<SimEvent, SimEvent> timeEvents;
	protected void Post (SimEvent ev)
	{
	      Post(ev, CurrentDate());
	}
	protected void Post (SimEvent ev,LogicalDateTime t) {
	      ev.initialize(this, t);
	      Logger.Detail(ev.Owner(),"Post",MessagesSimEngine.PostingAt0, ev.ScheduleDate());
	      timeEvents.put(ev, ev);
	      if (IsActive() && (engine != null))
	        engine.OnEventPosted(ev);
	}

	protected void Post (SimEvent ev,LogicalDuration dt) {
	      Post(ev, CurrentDate().add(dt));
	}

	protected void UnPost(SimEvent ev) {
	      if (IsActive() && (engine != null))
	          engine.OnEventUnPosted(ev);
	        Logger.Detail(this,"UnPost",MessagesSimEngine.UnpostingWhileScheduledAt0, ev.ScheduleDate());
	        timeEvents.remove(ev);
	        ev.terminate();		
	}
	
	protected void UnPostAllEvents(){
		while(timeEvents.size()>0)
			timeEvents.remove(timeEvents.firstKey());
	}


	public LogicalDateTime CurrentDate() {
        if (engine == null)
            return null;
          engine.checkReadAllowed("get_CurrentDate");
          return engine.SimulationDate();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		deletedObjectCount++;
		Logger.Information(this, "finalize", MessagesSimEngine.Deleting, MessagesSimEngine.Id0, simObjID);
	}


	@Override
	public String toString() {
		return this.getName();
	}


	protected void watchState() {
		
	}	

}

