/**
* Classe SimContinuous.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

import enstabretagne.base.messages.MessagesSimEngine;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.base.utility.Logger;

public abstract class SimContinuous {

	private boolean isActive;

	/// <summary>
    /// embedded Runge Kutta integrator
    /// </summary>
    private AbstractRungeKuttaNG rk;

    
    private SimObject owner;
	public SimObject getOwner() {
		return owner;
	}

	public void setOwner(SimObject _owner) {
		owner=_owner;
	}
    /// <summary>
    /// basic Constructor
    /// </summary>
    /// <param name="state">State of the continous variable</param>
    public SimContinuous(IContinuousState state)
    {
    	step = LogicalDuration.POSITIVE_INFINITY;
      rk = new AbstractRungeKuttaNG(state, state.ZeroRate());
    }

    /// <summary>
    /// Convenient Constructor (Continuous is synchro by default)
    /// </summary>
    /// <param name="state">state fields</param>
    /// <param name="step">required integration step</param>
    public SimContinuous(IContinuousState state, LogicalDuration step)
    {
    	this(state);
    	setStep(step);
    }

    /// <summary>
    /// integrate active unsynchronized continuous variable to engine current date
    /// and re initialize it to prevent state setting or shynchronize switch
    /// </summary>
    public void ResetState()
    {
      if (getIsActive() && !getIsSynchro())
      {
        updateDeferredState();
        resetLocalDateReference(localDateReference);
      }
    }

    /// <summary>Number of dimensions of the continuous</summary>
    public int Dimension()
    {
        if (rk == null)
          return 0;
        else
          return this.rk.CurState().Dimension();
    }

    /// <summary>
    /// return a handle to current state
    /// in order to read its fields (but not modify them) 
    /// Such operation is only allowed  outside of lazy integration
    /// </summary>
    public IContinuousState getUntypedROState()
    {
        getOwner().getEngine().checkReadAllowed("get_ContinuousState");
        if (getIsActive()&& !getIsSynchro())
          updateDeferredState();
        return rk.CurState();
    }

    /// <summary>
    /// return a handle to current state
    /// in order to read or modify its fields. 
    /// Such operation is only allowed  outside integration steps
    /// </summary>
    public IContinuousState getUntypedRWState()
    {
        getOwner().getEngine().checkWriteAllowed("set_ContinuousState");
        if (getIsActive() && !getIsSynchro())
          updateDeferredState();
        return rk.CurState();
    }

    private void updateDeferredState() {
    	//TODO à faire plus tard
		Logger.Error(this, "updateDeferredState", "Ne pas passer par ici");
	}

	/// <summary>
    /// return a handle to current rate
    /// in order to read its fields (but not modify them) 
    /// Such operation is only allowed  outside of lazy integration
    /// </summary>
    /// <remarks> Note that handle contains smoothed rate when it is called outside integration steps
    /// else the last rate evaluation is returned</remarks> 
    public IContinuousRate getUntypedRORate()
    {
        getOwner().getEngine().checkReadAllowed("get_ContinuousRate");
        if (getIsActive() && !getIsSynchro())
          updateDeferredState();
        return rk.CurRate();
    }
    
    public int getDimension(){
        if (rk == null)
            return 0;
          else
            return this.rk.CurState().Dimension();
    }

    int order;
	private LogicalDuration step;
	private boolean isSynchro;
    /// <summary>
    /// Local Runge Kutta order of the continuous variable.
    /// </summary>
    public int getOrder()
    {
		return order;
      }

    public void setOrder(int value)
      {
        if (order == value)
          return ;
        if (getIsActive())
          getOwner().getEngine().checkWriteAllowed("set_ContinuousOrder");
        order = value;
        if (order < 1) order = 1;
        if (order > 4) order = 4;
        Logger.Detail(this,"setOrder",MessagesSimEngine.SetLocalOrderTo0, order);
      }
    

    /// <summary>
    /// required integration step of the continuous variable.
    /// </summary>
    public LogicalDuration getStep()
    {
        return step;
    }

    public void setStep(LogicalDuration value)
    {
        if (step.compareTo(value)==0)
          return;

        LogicalDuration oldStep = step;
        step = LogicalDuration.Max(LogicalDuration.TickValue, LogicalDuration.Quantify(value));
        if (getIsActive() && getIsSynchro())
          getOwner().getEngine().synchronizer.NotifyStep(this, oldStep);
        Logger.Detail(this, "setStep", MessagesSimEngine.SetStepTo0, step);
    
    }

/// <summary>
/// Is the Continuous variable active ?
/// </summary>
	public boolean getIsActive()
	{
    return isActive;
	}
  public void setIsActive(boolean value)
  {
    if (isActive == value)
      return ;

    if (getIsSynchro())
    {
      if (isActive) // deactiving
      {
        getOwner().getEngine().OnSynchroContinuousRemoved(this);
      }
      else          // activing
      {
        getOwner().getEngine().OnSynchroContinuousAdded(this);
      }
    }
    else
    {
      if (isActive) // deactiving
      {
        getOwner().getEngine().OnLazyContinuousRemoved(this);
        ResetState();
      }
      else          // activing
      {
        getOwner().getEngine().OnLazyContinuousAdded(this);
        resetLocalDateReference(getOwner().CurrentDate());
      }
    }
    isActive = value;
  }



    /// <summary>
    /// Is the continuous variable synchronized with respect to simulation time
    /// else it is unsynchronized (or deferred or lazy updated) 
    /// </summary>
    public boolean getIsSynchro()
    {
        return isSynchro;
    }
    public void setIsSynchro(boolean value)
      {
        if (isSynchro == value)
          return;

        if (getIsActive())
        {
          getOwner().getEngine().checkWriteAllowed("set_IsSynchro");

          if (isSynchro) // unSynchronize
          {
            getOwner().getEngine().OnSynchroContinuousRemoved(this);
            resetLocalDateReference(getOwner().CurrentDate());
            getOwner().getEngine().OnLazyContinuousAdded(this);
          }
          else          // reSynchronize
          {
            getOwner().getEngine().OnLazyContinuousRemoved(this);
            ResetState();
            getOwner().getEngine().OnSynchroContinuousAdded(this);
          }
        }
        isSynchro = value;
      }
    

    
    /// <summary>
    /// Current local date of the continous variable. This local date is only defined if the
    /// continous variable is active. In that case, the <c>currentLocalDate</c> values :
    /// <list type="bullet">
    /// <item>
    /// <see cref="P:Simulation.Core.SimObject.CurrentDate"/> if the continous variable is synchronized
    /// (<see cref="P:Simulation.Core.SimContinous.IsSynchro"/> is <c>true</c>)
    /// </item>
    /// <item>
    /// The date of last evaluation of the continous variable if it is not synchronized
    /// (<see cref="P:Simulation.Core.SimContinous.IsSynchro"/> is <c>false</c>)
    /// </item>
    /// </list>
    /// </summary>
    protected LogicalDateTime currentLocalDate()
    {
        if (getIsActive())
          if (getIsSynchro())
            return getOwner().CurrentDate();
          // use it when debugging to avoid CurrentDate invocation
          // while properties are computed and displayed
          else
            return currentLocalDateCache();
        else
          return null;
    }

    /// <summary>
    /// Is Read allowed ?
    /// </summary>
    protected boolean IsReadAllowed()
    {
        return (getOwner() == null) || (getOwner().getEngine() == null) || (getOwner().getEngine().isReadAllowed());
    }

    /// <summary>
    /// Is Write allowed ?
    /// </summary>
    protected boolean IsWriteAllowed()
    {
        return (getOwner() == null) || (getOwner().getEngine() == null) || (getOwner().getEngine().isWriteAllowed());
    }

    /// <summary>
    /// LocalDateCache and LocalDateOffsetCache allow to save date computing when 
    /// the currentLocalDate property is called 
    /// </summary>
    private LogicalDateTime localDateCache;
    private LogicalDuration localDateOffsetCache;
	private boolean deferredIntegrationInProgress;
	private LogicalDuration localDateOffset;

	private LogicalDateTime localDateReference;
	
    private LogicalDateTime currentLocalDateCache()
    {
      if (deferredIntegrationInProgress)
      {
        if (localDateOffsetCache.compareTo(localDateOffset)!=0)
        {
          localDateOffsetCache = localDateOffset;
          localDateCache = localDateReference.add(localDateOffsetCache);
        }
        return localDateCache;
      }
      else
      {
        Logger.Warning(this, "currentLocalDateCache", MessagesSimEngine.LocalDateShouldNotBeUsefullOutsideOfDeferredIntegration, "");
        return localDateReference;
      }
    }

    /// <summary>
    /// local current date is represented as a reference date (localDateReference) + an offset (localDateOffset)
    /// Take care to compute efficienty a date gap as (t - localDateReference) - localDateOffset
    /// instead of t - (localDateReference + localDateOffset) which allocate a LogicalDate instance
    /// </summary>
    private void updateLocalDateReference(LogicalDateTime t)
    {
      if (localDateReference.compareTo(LogicalDateTime.MinValue)==0) // first initialisation
        localDateOffset = LogicalDuration.ZERO;
      else
    	  localDateOffset = localDateOffset.add(localDateReference.soustract(t));
      localDateReference = t;
      localDateOffsetCache = localDateOffset;
      localDateCache = localDateReference;
    }

    private void resetLocalDateReference(LogicalDateTime t)
    {
      localDateOffset = LogicalDuration.ZERO;
      localDateReference = t;
      localDateOffsetCache = localDateOffset;
      localDateCache = localDateReference;
    }
    
/// <summary>
    /// Compute and set rate field 
    /// Current date is provided as 
    ///   a reference date (date when integration cycle begins)
    ///   plus a date offset (Current date = reference date + date offset)
    /// Compute duration to t1 as (t1 - t0) - Dt instead of T1 - (t0 +Dt)
    /// in order to save LagicalDate allocation during integration passes   
    /// </summary>
    /// <param name="ROstate">state fields (ReadOnly)</param>
    /// <param name="WOrate">rate fields (Write Only)</param>
    protected abstract void ComputeUntypedRate(IContinuousState ROstate, IContinuousRate WOrate);

    
    /// <summary>
    /// Observe and modify state between integration steps
    /// </summary>
    /// <param name="RWstate">state fields (ReadWrite)</param>
    /// <param name="ROrate">rate fields (ReadOnly)</param>
    protected void WatchUntypedState(IContinuousState RWstate, IContinuousRate ROrate) {
    	getOwner().watchState();
    }

	protected void ComputeRate() {
	      try
	      {
	        ComputeUntypedRate(rk.CurState(), rk.CurRate());
	      }
	      catch (Exception ex)
	      {
	        Logger.Error(this, "ComputeRate", MessagesSimEngine.UnhandledException0, ex);
	      }		
	}

	public void UpdateState(int order, int pass, LogicalDuration dt) {
	     rk.UpdateState(order, pass, dt.DoubleValue());
		
	}

	public void WatchState() {
	      try
	      {
	        WatchUntypedState(rk.CurState(), rk.CurRate());
	      }
	      catch (Exception ex)
	      {
	        Logger.Error(this,"watchstate",MessagesSimEngine.UnhandledException0, ex);
	      }

	}

}

