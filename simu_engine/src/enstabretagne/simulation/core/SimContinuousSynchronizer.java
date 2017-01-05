/**
* Classe SimContinuousSynchronizer.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

import java.util.HashMap;

import enstabretagne.base.messages.MessagesSimEngine;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.base.utility.Logger;

public class SimContinuousSynchronizer {

    private LogicalDuration lowerStep   = LogicalDuration.POSITIVE_INFINITY;
    
    private int order = 2;
    HashMap<SimContinuous,SimContinuous> allContinuous;
    
	public SimContinuousSynchronizer(int synchroOrder) {
		order = synchroOrder;
	}
	

    /// <summary>
    /// total number of fields over continuous variable the synchronizer is managing 
    /// </summary>
	private int dim;
	public int Dimension() {
		return dim;
	}
	
	
    /// <summary>
    /// number of continuous varable the synchronizer is managing 
    /// </summary>
	
	public int Count() {
        if (allContinuous == null)
            return 0;
          else
            return allContinuous.size();

    }
    
    public LogicalDuration integrateNextStep(LogicalDateTime t, LogicalDateTime upTo)
    {
        LogicalDuration dt = upTo.soustract(t);
        boolean isLast = dt.compareTo(lowerStep)<=0;
        if (!isLast) 
          dt = lowerStep;
        for (int pass = 1; pass<=order; pass++)
        {
          for(SimContinuous c : allContinuous.keySet()) 
            c.ComputeRate();
          for(SimContinuous c : allContinuous.keySet()) 
            c.UpdateState(order, pass, dt);
          t.add(LogicalDuration.ofSeconds(AbstractRungeKuttaNG.TimeIncrement(order, pass, dt.DoubleValue())));
        }
        if (isLast) // last step done (avoid numeric error)
          t.replaceBy(upTo);
        for(SimContinuous c : allContinuous.keySet()) 
          c.WatchState();
        return dt;
    }
    
    public LogicalDuration SynchroStep()
    {
        return lowerStep;
    }

	public void add(SimContinuous con) {
	      if (allContinuous == null)
	          allContinuous = new HashMap<SimContinuous, SimContinuous>();
	        allContinuous.put(con, con);
	        addStep(con.getStep());
	        dim += con.getDimension();	
	}

	private void addStep(LogicalDuration step) {
	      if (step.compareTo(lowerStep)<0) 
	          lowerStep=step;		
	}


	public void remove(SimContinuous con) {
	      dim -= con.getDimension();
	      allContinuous.remove(con);
	      removeStep(con.getStep());

	}
    /// <summary>
    /// Remove a step. It this step is minimum step value, recomputes the minimum step;
    /// </summary>
    /// <param name="step"></param>
    private void removeStep(LogicalDuration step)
    {
      if (lowerStep.compareTo(LogicalDuration.POSITIVE_INFINITY)==0 || step.compareTo(lowerStep)>0) return;

      LogicalDuration dt = LogicalDuration.POSITIVE_INFINITY;
      for(SimContinuous c : allContinuous.keySet())
        if (c.getStep().compareTo(dt)<0)
        {
          dt = c.getStep();
          if (dt.compareTo(lowerStep)<=0) break;
        }
      lowerStep = dt;
    }


	public void initialize() {
	      lowerStep = LogicalDuration.POSITIVE_INFINITY; 
	      dim = 0;		
	}
    
	public int simContinuousSynchronizeFinalize(){
	      if (allContinuous==null)
	          return 0;
	        int n = allContinuous.size();
	        if (n==0)
	          return 0;

	        for(SimContinuous c :allContinuous.keySet())
	          Logger.Warning(c,"simContinuousSynchronizeFinalize",MessagesSimEngine.ZombiContinuousFrom0, c.getOwner().getClass().getName());
	        allContinuous.clear();
	        return n;
	}


	public void NotifyStep(SimContinuous simContinuous, LogicalDuration oldStep) {
	      if (simContinuous.getStep().compareTo(oldStep)<0) // step decreases : add current constraint
	          addStep(simContinuous.getStep());
	        else                  // step increases : remove previous constraint
	          removeStep(oldStep);
		
	}

}

