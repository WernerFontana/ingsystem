/**
* Classe SimEngine.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.core;

import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import enstabretagne.base.math.PortableRandom;
import enstabretagne.base.math.Randomizer;
import enstabretagne.base.messages.MessagesSimEngine;
import enstabretagne.base.models.environment.Environment;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.base.utility.Logger;
import enstabretagne.base.utility.Settings;
import enstabretagne.simulation.components.IScenarioIdProvider;
import enstabretagne.simulation.components.ScenarioId;
import enstabretagne.simulation.components.SimScenario;

public class SimEngine implements ISimulationDateProvider,IScenarioIdProvider{
        /// <summary>

        private static final EngineActivity Starting = new EngineActivity(EngineActivity.PausedWaiting.engineActivityValue() | EngineActivity.Initializing.engineActivityValue(),"Starting");
        private static final EngineActivity Running = new EngineActivity(EngineActivity.TimeEvent.engineActivityValue() | EngineActivity.SyncContinuous.engineActivityValue()| EngineActivity.LazyContinuous.engineActivityValue(),"Running");
        private static final EngineActivity Stopping = new EngineActivity(EngineActivity.PausedWaiting.engineActivityValue() | EngineActivity.Finalizing.engineActivityValue(),"Stopping");

        private static final EngineActivity AllowReadWrite = new EngineActivity(Starting.engineActivityValue() | Stopping.engineActivityValue() | EngineActivity.TimeEvent.engineActivityValue(),"Allow Read Write");
        private static final EngineActivity AllowRead = new EngineActivity(AllowReadWrite.engineActivityValue() | EngineActivity.SyncContinuous.engineActivityValue(),"Read Only");


        

        //----------------------- Integrity checking -------------------------------
        private final boolean isIntegrityChecked;
		private Class<? extends SimObject> appType;
		private String appName;

		private SimScenario currentScenario;
		
        private SimScenario getCurrentScenario() {
			return currentScenario;
		}
        
		/// <summary>
        /// set engine state
        /// </summary>
        /// <param name="state"></param>
        protected void setActivity(EngineActivity state)
        {
            if (activity == state)
                return;
            EngineActivity old = activity;
            activity = state;
            Logger.Detail(this,"setActivity",MessagesSimEngine.SwitchedFrom0, old);

            if (onStatusChangedListeners.size()>0)
                if (((old.engineActivityValue() == EngineActivity.NotInitialized.engineActivityValue()) && (activity.engineActivityValue() == EngineActivity.Initializing.engineActivityValue()))
                  || ((old.engineActivityValue() & Starting.engineActivityValue()) != 0 && (activity.engineActivityValue() & Running.engineActivityValue()) != 0)
                  || ((old.engineActivityValue() & Running.engineActivityValue()) != 0 && (activity.engineActivityValue() & Stopping.engineActivityValue()) != 0))
                    onStatusChangedListeners.forEach((listener) -> listener.notifyLogicalDateChange(simulationDate));
        }

        void checkWriteAllowed(String operation)
        {
            if (isIntegrityChecked && (activity.engineActivityValue() & AllowReadWrite.engineActivityValue()) == 0)
                Logger.Error(this, "CheckWriteAllowed",MessagesSimEngine.Operation0RequiresWRITEPermissionAvailableWhile1, operation, AllowReadWrite);
        }

        void checkReadAllowed(String operation)
        {
            if (isIntegrityChecked && (activity.engineActivityValue() & AllowRead.engineActivityValue()) == 0)
            	Logger.Error(this,MessagesSimEngine.Operation0RequiresREADPermissionAvailableWhereas1, operation, AllowRead);
        }

        boolean isReadAllowed()
        {
            return !(isIntegrityChecked && (activity.engineActivityValue() & AllowRead.engineActivityValue()) == 0);
        }

        boolean isWriteAllowed()
        {
            return !(isIntegrityChecked && (activity.engineActivityValue() & AllowReadWrite.engineActivityValue()) == 0);
        }

        
		
         //---------------------- Provided Interface --------------------------------
        /// <summary>
        /// Default constructor (for designers)
        /// </summary>
        /// <param name="monitor">Monitor of the engine</param>
        /// <param name="replica">Index of the current replica</param>
        /// <param name="replicaSeed">Seed for the replica.</param>
        /// <param name="appType">Simulation application (derives from or is <see cref="T:DirectSim.Simulation.Components.SimApp">SimApp</see>)</param>
        /// <param name="appName">Name of the simulation application</param>
        /// <param name="scenario">Scenario</param>
        /// <remarks>
        /// The construction of the engine accesses the following configuration variables (private, and
        /// then global to the monitor) :
        /// <list type="bullet">
        /// <item>SimulationEngine.IsEngineIntegrityChecked (default is true)</item>
        /// <item>SimulationEngine.DefaultSynchroOrder (default is 2)</item>
        /// <item>SimulationEngine.UsePortableRandomGenerator (default is false)</item>
        /// <item>SimulationEngine.UseOneRandomGeneratorPerAgent</item>
        /// </list>
        /// </remarks>
        public SimEngine(){
        	onStatusChangedListeners = new ArrayList<NotifyLogicalDateChange>();
        	onBeforeTimeEventProcessedListeners = new ArrayList<NotifySimTimeEvent>();
        	onBeforeStrongTimeEventProcessed = new ArrayList<NotifySimEvent>();
        	onDateChanged = new ArrayList<SimEngineEvent>();
        	onDateChangedListeners = new ArrayList<NotifyLogicalDateChange>();
        	onTimeEventProcessed = new ArrayList<NotifySimEngineEvent>();
        	onStrongTimeEventProcessed = new ArrayList<NotifySimTimeEvent>();

        	objectDictionary.initialize(this);
        	
            // Initialise fields
            this.appType = appType;
		    this.appName = appName;
 
            
//            this.initialCalendarDate = monitor.InitialCalendarDate();
            this.isIntegrityChecked = Settings.IsEngineIntegrityChecked();
            
            Logger.setDateProvider(this);
        }
        
        public void Init(SimScenario currentScenario)
        {
//        	if(!Logger.isInitialized()) return;
        	
        	this.currentScenario=currentScenario;
        	currentScenario.setEngine(this);

            InitialDate=getCurrentScenario().getStartDateTime();
            FinalDate=getCurrentScenario().getEndDateTime();

            this.simulationDate = InitialDate;

            this.synchronizer = new SimContinuousSynchronizer(Settings.DefaultSynchroOrder());

            this.allTimeEvents = new SortedList<SimEvent>();

            // initialize statistic
            initialCreatedObjectCount = SimObject.CreatedObjectCount();
            initialDeletedObjectCount = SimObject.CreatedObjectCount();
            initialMemoryLevel = (int)Runtime.getRuntime().totalMemory();
            
            elapsedRealTime = Duration.ZERO;
            stepCount = eventCount = strongEventCount = weakEventCount = bundleCount = 0;
            continuousElapsed = 0.0;
            continuousStartAt = null;


            // initialize randomfactory
            this.bundleCount = 0;
            this.generator = createGenerator((int) getCurrentScenario().getSeed());
            Logger.Information(getCurrentScenario(),"SimEngine - Generator", MessagesSimEngine.CreateGenerator0Seed1BurnCount2, generator.RandomGeneratorType(), (int) getCurrentScenario().getSeed(), burnCount);

            // initialize engine 
            getCurrentScenario().setEngine(this);
            OnDateChanged(); // $CS Notify date change due to date (re)initialization
            setActivity(EngineActivity.Initializing);
            synchronizer.initialize();
        } // SimEngine() 

        /// <summary>Activity of the engine (from an external point of view)</summary>
        public EngineActivity Activity() { return activity;  }

        /// <summary>Is the engine initialized ?</summary>
        public boolean IsInitialized() { return (activity.engineActivityValue() & EngineActivity.NotInitialized.engineActivityValue()) == 0; }

        /// <summary>Is the engine running ?</summary>
        public boolean IsRunning() { return (activity.engineActivityValue() & Running.engineActivityValue()) != 0; }

        private void CleanUp(boolean restart)
        {
            setActivity(EngineActivity.Finalizing);
            try
            {
            	getCurrentScenario().terminate(restart);
            }
            catch (Exception ex)
            {
            	Logger.Detail(this,"CleanUp",MessagesSimEngine.UnhandledException0);// App.Error("CleanUp"ex);
            }


            int n;
            n = eventListFinalize();
            if (n > 0)
                Logger.Error(this,"CleanUp",MessagesSimEngine.ZombiEventsWhenFinalizing, n);

            n = lazyContinuousFinalize();
            if (n > 0)
            	Logger.Error(this,"CleanUp",MessagesSimEngine.ZombiLazyContinuousWhenFinalizing, n);

            n = synchronizer.simContinuousSynchronizeFinalize();
            if (n > 0)
            	Logger.Error(this,"CleanUp",MessagesSimEngine.ZombiSynchroContinuousWhenFinalizing, n);

            n = objectDictionary.terminate(restart);
            if (n > 0)
            	Logger.Error(this,"CleanUp",MessagesSimEngine.ZombiObjectsWhenFinalizing, n);

            int memoryLevelInReplica = (int) Runtime.getRuntime().totalMemory() - initialMemoryLevel;
            Runtime.getRuntime().gc();

            int createdObjectInReplica = SimObject.CreatedObjectCount() - initialCreatedObjectCount;
            int deletedObjectInReplica = SimObject.DeletedObjectCount() - initialDeletedObjectCount;

            Logger.Information(this,"CleanUp",MessagesSimEngine.CreatedObjects0Remain1, createdObjectInReplica, createdObjectInReplica - deletedObjectInReplica);
            Logger.Information(this,"CleanUp",MessagesSimEngine.MemoryBeforeGC0AfterGC1, memoryLevelInReplica, Runtime.getRuntime().totalMemory() - initialMemoryLevel);
            
            setActivity(EngineActivity.NotInitialized);
        }

        private int eventListFinalize()
        {
            if (allTimeEvents == null)
                return 0;
            int n = allTimeEvents.size();
            if (n > 0)
            {
                for(SimEvent ev : allTimeEvents)
                	Logger.Information(ev,"enventListFinalize",MessagesSimEngine.ZombiEventFrom0, ev.Owner().getClass().getName());
                allTimeEvents.clear();
            }
            return n;
        }

        private int lazyContinuousFinalize()
        {
            if (allLazyContinuous == null)
                return 0;
            int n = allLazyContinuous.size();
            if (n > 0)
            {
                for(SimContinuous lc :allLazyContinuous)
                    Logger.Warning(lc,"lazyContinuousFinalize",MessagesSimEngine.ZombiLazyContinuousFrom0, lc.getOwner().getClass().getName());
                allLazyContinuous.clear();
            }
            return n;
        }

        /// <summary>
        /// Random number generator
        /// </summary>
        public Randomizer Generator()
        {
                //return randomfactory.Generator;
                return generator;
        }

        /// <summary>
        /// Simulation time : any SimObject is at most at that time in the simulation.
        /// </summary>
        public LogicalDateTime SimulationDate()
        {
                return simulationDate;
        }

        /// <summary>Initial date</summary>
        public static LogicalDateTime InitialDate ;

        /// <summary>Initial date</summary>
        public static LogicalDateTime FinalDate = LogicalDateTime.MaxValue;

        /// <summary>
        /// DateTime representing the day/hour of beginning of the simulated scenario. The 
        /// time in the scenario is <c>simulatedWorldOriginDate</c> when <c>simulationDate</c> is 
        /// <c>LogicalDate.Zero</c>
        /// 
        /// Unless otherwise specified, this date is set to 
        /// <code>System.DateTime.MinValue</code> so that it is not taken into account.
        /// 
        /// This date is not used by the simulation engine, but is provided for the User 
        /// Interfaces.
        /// </summary>
        public Temporal SimulatedWorldOriginDateTime()
        {
                return Environment.SimulatedWorldOriginDateTime;
        }
        // private System.DateTime simulatedWorldOriginDateTime;

        //private DateTime initialCalendarTime;



        /// <summary>
        /// Event fired when the simulation is started.
        /// </summary>
        private List<NotifyLogicalDateChange> onStatusChangedListeners;

        /// <summary>
        /// Event fired each time the simulation date has changed
        /// </summary>
        private List<NotifyLogicalDateChange> onDateChangedListeners;

        /// <summary>
        /// Event fired before a strong Time Event is being processed
        /// </summary>
        private List<NotifySimTimeEvent> onBeforeTimeEventProcessedListeners;
        
        private List<NotifySimEvent> onBeforeStrongTimeEventProcessed;

        /// <summary>
        /// Event fired each time a strong Time Event has been processed
        /// </summary>
        private List<NotifySimEngineEvent> onTimeEventProcessed;
        private List<NotifySimTimeEvent> onStrongTimeEventProcessed;

        /// <summary>
        /// Label for the engine
        /// </summary>
        /// <returns></returns>
        public String ToString()
        {
            return getCurrentScenario() == null ? this.getClass().getName()  : getCurrentScenario().getName();
        }
        
        //------------------------ private members ---------------------------------


        private EngineInterruptReason interrupter;
        private int stepCount, eventCount, strongEventCount, weakEventCount, bundleCount;
        private double continuousElapsed;
        private LogicalDateTime continuousStartAt;
        private int initialCreatedObjectCount, initialDeletedObjectCount;
        private int initialMemoryLevel;
        private Duration elapsedRealTime;
        private Temporal lastStartDate;

        private EngineInterruptReason LastInterruptReason() { return interrupter; }

        private int StrongEventCount() { return strongEventCount; }
        private int WeakEventCount() { return weakEventCount; }
        private int EventCount() { return eventCount; }
        private int BundleCount() { return bundleCount; }
        private LogicalDuration ElapsedTime() { return LogicalDuration.soustract(simulationDate, InitialDate); }

        private double SynchContinuousElapsedTime()
        {
                double result = continuousElapsed;
                if (continuousStartAt != null)
                    result = result + LogicalDuration.soustract(simulationDate , continuousStartAt).DoubleValue();
                return result;
        }

        private double WeakEventRatio()
        {
                if (eventCount > 0)
                    return weakEventCount / (double)eventCount;
                else
                    return 0;
        }

        private double BundleSize()
        {
                if (bundleCount > 0)
                    return eventCount / (double)bundleCount;
                else
                    return 1;
        }

        private double EventRate()
        {
                double elapsed = LogicalDuration.soustract(simulationDate , InitialDate).DoubleValue();
                if (elapsed > 0)
                    return eventCount / elapsed;
                else
                    return 0;
        }

        private double BundleRate()
        {
                double elapsed = ElapsedTime().DoubleValue();
                if (elapsed > 0)
                    return bundleCount / elapsed;
                else
                    return 0;
        }

        private double SynchContinuousTimeRatio()
        {
                double elapsed = ElapsedTime().DoubleValue();
                if (elapsed > 0)
                    return SynchContinuousElapsedTime() / elapsed;
                else
                    return 0;
        }

        private double StepSize()
        {
                if (stepCount > 0)
                    return SynchContinuousElapsedTime() / stepCount;
                else
                    return 0;
        }

        private double StepRate()
        {
                double elapsed = SynchContinuousElapsedTime();
                if (elapsed > 0)
                    return stepCount / elapsed;
                else
                    return 0;
        }

        private double StepBetweenBundle()
        {
                if (bundleCount > 0)
                    return stepCount / (double)bundleCount;
                else
                    return 0;
        }

        private int PendingEventsCount() { return allTimeEvents.size(); }
        private int SynchContinuousCount() { return synchronizer.Count(); }
        private int SynchContinuousDimension() { return synchronizer.Dimension(); }

        private int AllocatedMemory()
        {
                int memoryLevelInReplica = (int)Runtime.getRuntime().totalMemory() - initialMemoryLevel;
                return memoryLevelInReplica;
        }

        private double ElapsedRealTime()
        {
                Duration result = elapsedRealTime;
                if (IsRunning())
                {
                	result=result.plus(Duration.between(Instant.now(),lastStartDate));
                }
                return result.getSeconds();
        }


        /// <summary>Random number generator</summary>
        private Randomizer generator;
        private int burnCount = 0;
        private final boolean usePortableRandomGenerator = Settings.UsePortableRandomGenerator();
        private final boolean isGeneratorShared = !Settings.UseOneRandomGeneratorPerAgent();
        private final boolean useBinaryTreeForEventList = Settings.UseBinaryTreeForEventList();

        private Randomizer createGenerator(int seed)
        {
            Random gen;

            if (usePortableRandomGenerator)
                gen = new PortableRandom(seed);
            else
            {
                if (seed == Integer.MIN_VALUE) // prevent .NET Random constructor against private bug when passing int.MinValue as seed
                    seed = Integer.MAX_VALUE;
                gen = new Random(seed);
            }

            if (burnCount > 0)
                for (int i = 0; i < burnCount; i++)
                    gen.nextInt();

            return new Randomizer(gen);
        }

        private Randomizer GetGenerator(SimObject o)
        {
            if (isGeneratorShared)
                return generator;
            else
            {
                int seed = Randomizer.GetSeed((int) getCurrentScenario().getSeed(), o.getName());
                return createGenerator(seed);
            }
        }

        public List<SimObject> requestAllSimObject() {
        	return objectDictionary.getKnownSimObjects();
        }
        public List<SimObject> requestSimObject(SimObjectRequest r){
        	return objectDictionary.requestSimObject(r);
        }
        
        /// <summary>
        /// current activity of the engine which constraints application permission
        /// </summary>
        private EngineActivity activity = EngineActivity.NotInitialized;

        /// <summary>
        /// Collection of all time events, sorted by Date
        /// </summary>
        private SortedList<SimEvent> allTimeEvents;

        /// <summary>
        /// Collection of all defered active Continuous
        /// </summary>
        private List<SimContinuous> allLazyContinuous = new ArrayList<SimContinuous>();

        /// <summary>
        /// Abstract Runge Kutta integration
        /// </summary>
        SimContinuousSynchronizer synchronizer;

        /// <summary>
        /// Current Simulation date
        /// </summary>
        private LogicalDateTime simulationDate;
		private List<SimEngineEvent> onDateChanged;

        /// <summary>
        /// Method called at each end of quantum
        /// </summary>
        private void OnDateChanged()
        {
            if (onDateChanged.size()>0)
                onDateChanged.forEach((see)->see.simEngineEvent(simulationDate));
            DoTimeManagement(simulationDate);
        }

        /// <summary>
        /// Method to be redefined by engines wanting to get synchronized with
        /// something else (real time or accelerated time or simulation federation)
        /// By default this method does nothing.
        /// </summary>
        protected void DoTimeManagement(LogicalDateTime simulationDate)
        {
        }

        /// <summary>
        /// Method called when a SimObject controled by this engine has an event posted
        /// </summary>
        void OnEventPosted(SimEvent ev)
        {
            if (ev.ScheduleDate().compareTo(simulationDate)<0)
                if (!ev.IsWeak())
                {
                    Logger.Warning(this,"OnEvnetPosted",MessagesSimEngine.ReScheduledAtNowFrom0, ev.ScheduleDate());
                    ev.resetProcessDate(simulationDate);
                }
            if (ev.IsWeak())
                checkReadAllowed("PostWeakSimEvent");
            else
                checkWriteAllowed("PostStrongSimEvent");
            allTimeEvents.add(ev);
        } // OnEventPosted() 

        /// <summary>
        /// Method called when a SimObject controled by this engine has an event unposted
        /// </summary>
        void OnEventUnPosted(SimEvent ev)
        {
            if (ev.IsWeak())
                checkReadAllowed("UnpostWeakTimeEvent");
            else
                checkWriteAllowed("UnpostStrongTimeEvent");
            allTimeEvents.remove(ev);
        } // OnEventUnPosted() 

        void OnSynchroContinuousAdded(SimContinuous con)
        {
            Logger.Detail(this,"OnSynchroContinuousAdded",MessagesSimEngine.Synchronizing);
            checkWriteAllowed("SynchroContinuousAdded");
            synchronizer.add(con);
        }

        void OnSynchroContinuousRemoved(SimContinuous con)
        {
            Logger.Detail(this,"OnSynchroContinuousRemoved",MessagesSimEngine.UnSynchronizing);
            checkWriteAllowed("SynchroContinuousRemoved");
            synchronizer.remove(con);
        }

        void OnLazyContinuousAdded(SimContinuous con)
        {
        	Logger.Detail(this,"OnLazyContinuousAdded", MessagesSimEngine.Defering);
            checkWriteAllowed("DeferedContinuousAdded");
            allLazyContinuous.add(con);
        }

        void OnLazyContinuousRemoved(SimContinuous con)
        {
            Logger.Detail(this,"OnLazyContinuousRemoved",MessagesSimEngine.UnDefering);
            checkWriteAllowed("DeferedContinuousRemoved");
            allLazyContinuous.remove(con);
        }

        /// <summary>
        /// Find next time event to be processed (if any). Returns null if no more to process.
        /// </summary>
        /// <returns></returns>
        private SimEvent FindNextTimeEvent()
        {
        	if(allTimeEvents.size()>0)
        		return allTimeEvents.first();
        	else
        		return null;
        }

        /// <summary>
        /// Find next strong time event to be processed (if any). Returns null if no more to process.
        /// </summary>
        /// <param name="offset">don't find beyond currentDate+offset</param>
        /// <returns>next stong time event</returns>
        private SimEvent FindNextStrongTimeEvent(LogicalDuration offset)
        {
            if (allTimeEvents.size() > 0)
                for(SimEvent ev : allTimeEvents)
                {
                    if (ev.ScheduleDate().soustract(simulationDate).compareTo(offset)>0)
                        break;
                    if (!ev.IsWeak())
                        return ev;
                }
            return null;
        }

        /// <summary>
        /// Processes all events already posted at call time, at the same date than the next event
        /// </summary>
        /// <param name="upTo">Maximum search date</param>
        /// <returns>The number of actually processed events</returns>
        private int processNextTimeEventsPacket(LogicalDateTime upTo)
        {
            SimEvent firstEvent = FindNextTimeEvent();
            if (firstEvent == null) return 0;
            if (firstEvent.ScheduleDate().compareTo(upTo)>0) return 0;
            LogicalDateTime packetDate = firstEvent.ScheduleDate();

            SortedList<SimEvent> evtsToProcess = new SortedList<SimEvent>();

            for(SimEvent ev :allTimeEvents)
            {
                if (ev.ScheduleDate().compareTo(packetDate)>0) break;
                evtsToProcess.add(ev);
            }

            setActivity(EngineActivity.TimeEvent);
            for (SimEvent ev :evtsToProcess)
            {
                if (ev.ScheduleDate().compareTo(packetDate)>0) break;
                // Check whether event is still scheduled and at same date
                // (event may has been unposted or resceduled, or owner may be dead, due to previous events processing)
                if (!allTimeEvents.contains(ev) || !ev.ScheduleDate().equals(packetDate)) continue;
                //if (!ev.owner.IsActive) continue;
                // Remove the event from event list
                ev.Owner().UnPost(ev);
                // journalize event processing
                //
                // and then process event
                if ((!ev.IsWeak()) && (onBeforeStrongTimeEventProcessed.size()>0))
                    onBeforeStrongTimeEventProcessed.forEach((notif)->notif.notifySimEvent(ev));
                try
                {
                    ev.Process();
                }
                catch (Exception ex)
                {
                    Logger.Error(ev,"SimEngine.processNextTimeEventsPacket",MessagesSimEngine.UnhandledException0, ex);
                }
                // update event counter and notify strong event processing
                if (ev.IsWeak())
                    weakEventCount++;
                else
                {
                    strongEventCount++;
                    if (onStrongTimeEventProcessed.size() > 0)
                        onStrongTimeEventProcessed.forEach((strongTimeEventListener)->strongTimeEventListener.notifySimTimeEvent(simulationDate));
                }
            }
            return evtsToProcess.size();
        }

		private void processEventsUntilCurrentDate()
        {
            int count = this.processNextTimeEventsPacket(simulationDate);
            eventCount += count;
            if (count > 0) bundleCount++;
        }

        private LogicalDateTime requestTimeAdvance(LogicalDateTime requested, LogicalDateTime weak)
        {
            //return monitor.RequestTimeAdvance(requested, weak);
        	return requested;
        }

        public void simulate()
        {
            SimEvent ev;
            LogicalDateTime upTo, weakUpTo;
            interrupter = EngineInterruptReason.ByNone;
            //int count;
            lastStartDate = Instant.now();

            
            // process events at current date
            processEventsUntilCurrentDate();

            while (true)
            {
                // Find local target date
            	LogicalDuration offset = this.synchronizer.Count() > 0 ? this.synchronizer.SynchroStep() : LogicalDuration.MAX_VALUE;
                ev = FindNextStrongTimeEvent(offset);
                upTo = ev != null ? ev.ScheduleDate() : offset.compareTo(LogicalDuration.MAX_VALUE)==0 ? LogicalDateTime.MaxValue : LogicalDateTime.add(simulationDate,offset);
                ev = FindNextTimeEvent();
                weakUpTo = ev != null ? ev.ScheduleDate() : offset.compareTo(LogicalDuration.MAX_VALUE)==0 ? LogicalDateTime.MaxValue : LogicalDateTime.add(simulationDate,offset);
                if (weakUpTo.compareTo(upTo)>0) weakUpTo = upTo;
                // Since requestTimeAdvance may post strong events, we have to set a proper activity
                EngineActivity oldActivity = this.activity;
                this.setActivity(EngineActivity.TimeEvent);
                // Request advance (may block, upTo may be decreased)
                upTo = requestTimeAdvance(upTo, weakUpTo);
                // Restore previous activity
                this.setActivity(oldActivity);

                // Process simulation quantum 
                if (this.synchronizer.Count() > 0)
                {
                    // enter in a continuous period
                    if (continuousStartAt == null)
                        continuousStartAt = simulationDate;

                    // integrate next stepwithout overlapping upTo
                    setActivity(EngineActivity.SyncContinuous);
                    LogicalDuration dt = synchronizer.integrateNextStep(simulationDate, upTo);

                    // Update current date and counter
                    OnDateChanged();
                    stepCount++;
                }
                else
                {
                    // leave a continuous period
                    if (continuousStartAt != null)
                    {
                        continuousElapsed += (simulationDate.soustract(continuousStartAt)).DoubleValue();
                        continuousStartAt = null;
                    }

                    // Update current date
                    if (simulationDate != upTo)
                    {
                        simulationDate = upTo;
                        OnDateChanged();
                    }
                }

                // Process time events up to current date 
                // processed events are normaly weak if current date < upTo
                processEventsUntilCurrentDate();

                // Look at any user interruption request pending from Console
                if (ShouldInterrupt())
                    interrupt(EngineInterruptReason.ByUser);

                // check if any interruption is set
                if (interrupter != EngineInterruptReason.ByNone)
                {
                    setActivity(EngineActivity.PausedWaiting);
                    break;
                }
            }
            // exiting engine execution
            elapsedRealTime= elapsedRealTime.plus(Duration.between(Instant.now(),lastStartDate));
            Logger.Information(this,"simulate",MessagesSimEngine.InterruptReason0, interrupter);
        }

        /// <summary>
        /// Should we interrupt the simulation
        /// </summary>
        /// <returns></returns>
        private boolean ShouldInterrupt()
        {
            //return monitor.ShouldInterrupt;
        	return false;
        }

        /// <summary>
        /// Interrupts the engine
        /// </summary>
        /// <param name="interrupter"></param>
        void interrupt(EngineInterruptReason interrupter)
        {
            this.interrupter = interrupter;
        }
        

        //--------------------- Object class database ------------------------------
        //private final SimObjectType RootOfUsedObjectTypeHierarchy;
        //private final SimObjectType RootOfDerivedObjectTypeHierarchy;

        private SimObjectType RootOfUsedObjectTypeHierarchy()
        {
            return objectDictionary.getSimObjectTypeFrom(appType);
        }

        private SimObjectType RootOfDerivedObjectTypeHierarchy()
        {
            return objectDictionary.getSimObjectTypeFrom(SimObject.class);
        }


        private SimObject RetreiveSimObject(int liveId)
        {
            return objectDictionary.RetreiveSimObject(liveId);
        }

        /// <summary>
        /// Write object type dictionary (for debugging or analysis purposes)
        /// </summary>
        public void WriteObjectTypeDictionary(PrintWriter Out)
        {
            objectDictionary.WriteObjectTypeDictionary(Out);
        }


        /// <summary>
        /// Writes filtered pending time event list
        /// </summary>
        /// <param name="Out">Text stream to be used</param>
        /// <param name="maxCount">maximum printed time events</param>
        /// <param name="upTo">maximum occurence date of printed time events</param>
        public void WriteTimeEventList(PrintWriter Out, int maxCount, LogicalDateTime upTo)
        {
            int r = 0;
            for(SimEvent tev : allTimeEvents)
            {
                if (++r > maxCount || tev.ScheduleDate().compareTo(upTo)>0)
                    break;
                Out.println(tev.TimeEventLine(r));
            }
        }

        /// <summary>
        /// Get SimObjectType associated with Reflection type
        /// </summary>
        /// <param name="t">MetaType</param>
        /// <returns>SimObjectType</returns>
        private SimObjectType GetSimObjectType(Class c)
        {
            return objectDictionary.getSimObjectTypeFrom(c);
        }
        

        //-------------------- Object instance database ----------------------------
        /// <summary>
        /// simulated objects managed by the simulation engine.
        /// </summary>
        private SimObjectDictionary objectDictionary = new SimObjectDictionary();

        public void AddSimObjectAddedListener(SimObjectAddedListener listener) {
			objectDictionary.AddSimObjectAddedListener(listener);
		}

		public void RemoveSimObjectAddedListener(SimObjectAddedListener listener) {
			objectDictionary.RemoveSimObjectAddedListener(listener);
		}

		public void AddSimObjectRemovedListener(
				SimObjectRemovedListener listener) {
			objectDictionary.AddSimObjectRemovedListener(listener);
		}

		public void RemoveSimObjectRemovedListener(
				SimObjectRemovedListener listener) {
			objectDictionary.RemoveSimObjectRemovedListener(listener);
		}

		private SimObjectDictionary ObjectDictionary()
        {
                return objectDictionary;
        }
        
        

        /// <summary>
        /// Called by SimObject when it is added to engine
        /// </summary>
        public void OnSimObjectAdded(SimObject o)
        {
            // Add the object to the collection of simulated objects that are managed by the engine
            objectDictionary.Add(o);
        }

        /// <summary>
        /// Called by SimObject when it is removed from engine
        /// </summary>
        public void OnSimObjectRemoved(SimObject o)
        {
            // Remove the object to the collection of simulated objects that are managed by the engine
            objectDictionary.Remove(o);
        }
        

        //Multi purpose serial number
        private Long _mpSerial = Long.MIN_VALUE;
        private Long GetNextSerialNumber()
        {
            return _mpSerial++;
        }
       
        

        private List<InteractionHandler> _InteractionEmitted;
        private void NotifyInteractionEmitted(Class id, SimObject sender, Object... parms)
        {
            if (this._InteractionEmitted.size()>0)
                _InteractionEmitted.forEach((interactionListener)-> interactionListener.EmitInteraction(id, sender, parms));
        }

		@Override
		public ScenarioId getScenarioId() {
			if(getCurrentScenario()!=null)
				return getCurrentScenario().getScenarioId();
			else
				return ScenarioId.ScenarioID_NULL;
		}

}

