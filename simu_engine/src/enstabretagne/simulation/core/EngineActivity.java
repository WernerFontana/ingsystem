/**
* Classe EngineActivity.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

public final class EngineActivity {
    // ----------------------------------------------------------------
    // States where engine is stopped
    // ----------------------------------------------------------------

    public EngineActivity(int activity,String name) {
		engineActivityValue = activity;
		this.name=name;
	}
    
    private String name;
    public String getName(){
    	return name;
    }
    
    private int engineActivityValue;
    public int engineActivityValue()
    {
    	return engineActivityValue;
    }

	/// <summary>
    /// Unknown value used in journal message when no engine is attached to
    /// </summary>
    public static final EngineActivity Unknown = new EngineActivity(0x0000,"Inconnu");        //None

    /// <summary>
    /// The engine is stopped and not even initialized. No operations
    /// are permitted (permission is <c>None</c>)
    /// </summary>
    public static final EngineActivity NotInitialized = new EngineActivity(0x0001,"Not Initialized");       //None

    /// <summary>
    /// The engine is initialized. Any operation is permitted (permission is <c>ReadWrite</c>)
    /// </summary>
    public static final EngineActivity Initializing=new EngineActivity(0x0002,"Initializing");       //ReadWrite

    /// <summary>
    /// The engine is Finalizing. Any operation is permitted (permission is <c>ReadWrite</c>)
    /// </summary>
    public static final EngineActivity Finalizing=new EngineActivity(0x0004,"Finalizing");       //ReadWrite

    // ----------------------------------------------------------------
    // States where engine is paused
    // ----------------------------------------------------------------
    /// <summary>
    /// The engine is paused. Any operation is permitted (permission is <c>ReadWrite</c>)
    /// </summary>
    public static final EngineActivity PausedWaiting=new EngineActivity(0x0008,"Pause");       //ReadWrite

    // ----------------------------------------------------------------
    // States where engine is running
    // ----------------------------------------------------------------
    /// <summary>
    /// The engine is running and processing a <see cref="T:DirectSim.Simulation.Core.SimTimeEvent"/>.
    /// Any operation is permitted (permission is <c>ReadWrite</c>)
    /// </summary>
    public static final EngineActivity  TimeEvent=new EngineActivity(0x0010,"Time Events");       //ReadWrite

    /// <summary>
    /// The engine is running and integrating the synchronized <see cref="T:DirectSim.DirectSim.Simulation.Core.SimContinuous"/>.
    ///       /// Only operation that do not change the state of the simulation (no Post, nor activation of
    /// continuous variables, ...) are permitted (permission is <c>ReadOnly</c>)
    /// </summary>
    public static final EngineActivity  SyncContinuous =new EngineActivity( 0x0020,"Integration Variables Continues");       //ReadOnly

    /// <summary>
    /// The engine is running and processing a non synchronized <see cref="T:DirectSim.Simulation.Core.SimContinuous"/>.
    /// Any operation accesses the state of the simulation is <c>not</c> permitted because the unsynchronized continuous
    /// variable being integrated has its own time (it is unsynchronized). Thus permission is <c>None</c>.
    /// </summary>
    public static final EngineActivity  LazyContinuous =new EngineActivity( 0x0040,"Intégration Variables Paresseuses");      //None
    
    @Override
    public String toString() {
    	return name;
    }

}

