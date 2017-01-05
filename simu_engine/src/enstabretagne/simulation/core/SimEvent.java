/**
* Classe SimEvent.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

import enstabretagne.base.messages.MessagesSimEngine;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.utility.Logger;

public abstract class SimEvent implements Comparable<SimEvent>{

	public SimEvent(){
		
	}
	private boolean isWeak;
	public SimEvent(boolean isWeak)
	{
		this.isWeak = isWeak;
	}
	public abstract void Process();
	
    /// <summary>Logical date of the event occurrence</summary>
    private LogicalDateTime scheduleDate;
	private SimObject owner;
	private LogicalDateTime postDate;
	private static int initCounter;
	private int initRank;

	public LogicalDateTime ScheduleDate() {
		
		return scheduleDate;
	}

	
	public boolean IsWeak() {
		
		return isWeak;
	}

	public void resetProcessDate(LogicalDateTime simulationDate) {
		scheduleDate=simulationDate;
	}
	
	@Override
	public int compareTo(SimEvent e) {
		
	        // Case where not comparable
	        if (e == null){
	        	Logger.Error(this, "compareTo", MessagesSimEngine.ComparisonNotPossible);
//	        	throw new Exception(MessagesSimEngine.ComparisonNotPossible);
	        }
	        if (this == e)
	            return 0;

	        // Comparison of dates
	        int comparison = scheduleDate.compareTo(e.ScheduleDate());
	         return comparison;

	}

	public SimObject Owner() {
		
		return owner;
	}

	public String TimeEventLine(int r) {
        return r +  ";Date = " + ScheduleDate() + " " + (IsWeak() ? "Weak" : "Strong") + ";"+ this.getClass().getName() +";Owner=" + Owner();
	}

	public void initialize(SimObject simObject, LogicalDateTime t) {
        owner = simObject;
        scheduleDate = t;
        
        if(owner!=null)
        	postDate = owner.CurrentDate();
        initRank = ++initCounter;

	}

	public void terminate() {
        initRank = 0;		
	}

}

