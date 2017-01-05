/**
* Classe SimEntity.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.components;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import enstabretagne.base.math.Randomizer;
import enstabretagne.base.messages.MessagesEntity;
import enstabretagne.base.utility.Logger;
import enstabretagne.simulation.core.SimEngine;
import enstabretagne.simulation.core.SimObject;

public abstract class SimEntity extends SimObject implements IEntity {

	static {
		OnCreated=new ArrayList<CreationNotification>();
		OnCreating=new ArrayList<CreationNotification>();
	}
	
	SimFeatures features;
	protected SimFeatures getFeatures(){
		return features;
	}
	
	SimInitParameters initParameters;
	protected SimInitParameters getInitParameters(){
		return initParameters;
	}
	
	public void getSubEntitiesImplementing(List<SimEntity> result,Class i)
	{
		for(SimEntity se : getChildren())
		{
			se.getSubEntitiesImplementing(result, i);
		}
		if(i.isAssignableFrom(this.getClass())) result.add(this);
	}
	
	public SimEntity(SimEngine engine, String name,SimFeatures features){

		status = EntityStatus.NONE;
		OnActivated=new ArrayList<ActivationNotification>();
		OnActivating=new ArrayList<ActivationNotification>();
		OnDeactivated=new ArrayList<DeactivationNotification>();
		OnDeactivating=new ArrayList<DeactivationNotification>();
		OnInitialized=new ArrayList<InitializationNotification>();
		OnInitializing=new ArrayList<InitializationNotification>();
		OnTerminating= new ArrayList<TerminatingNotification>();
		OnTerminated= new ArrayList<TerminatingNotification>();
		parent=null;
		children=new ArrayList<SimEntity>();
		
		this.setName(name);
		this.features=features;
		
		setEngine(engine);
		OnActivated.add(this::AfterActivate);
		OnDeactivating.add(this::BeforeDeactivating);
		OnTerminating.add(this::BeforeTerminating);
		OnTerminated.add(this::AfterTerminated);
		
	}

	
	// / <summary>Entity life cycle status</summary>
	protected static final class EntityStatus {
		public EntityStatus(int status) {
			entityStatus = status;
		}

		private int entityStatus;

		public int entityStatus() {
			return entityStatus;
		}

		// /// <summary>unknownStatus</summary>
		public static final EntityStatus NONE = new EntityStatus(0x0000);
		// / <summary>created or recycled but not yet ready to enter in the
		// simulation</summary>
		public static final EntityStatus BORN = new EntityStatus(0x0001);
		// / <summary>initialized and ready to be started</summary>
		public static final EntityStatus IDLE = new EntityStatus(0x0002);
		// / <summary>started and active as soon as its manager being
		// active</summary>
		public static final EntityStatus ACTIVE = new EntityStatus(0x0004);
		public static final EntityStatus HELD = new EntityStatus(0x0008);
		// / <summary>definitively exited of the simulation ready to garbage
		// collection</summary>
		public static final EntityStatus DEAD = new EntityStatus(0x00032);

		public boolean isBUSY() {
			return (entityStatus == EntityStatus.ACTIVE.entityStatus())
					| (entityStatus == EntityStatus.HELD.entityStatus());
		}

		public boolean isALIVE() {
			return (entityStatus == EntityStatus.IDLE.entityStatus() | isBUSY());
		}

		public boolean exist() {
			return (isALIVE() | (entityStatus == EntityStatus.BORN
					.entityStatus()));
		}

		@Override
		public boolean equals(Object arg0) {
			if (EntityStatus.class.isAssignableFrom(arg0.getClass()))
				return entityStatus == ((EntityStatus) arg0).entityStatus;
			else
				return false;
		}
	}

	private EntityStatus status;

	public EntityStatus getStatus() {
		return status;
	}

	protected void setStatus(EntityStatus s) {
		status = s;
	}

	private static final class EntityTransition {
		public EntityTransition(int transition) {
			entityTransition = transition;
		}

		private int entityTransition;

		public int entityTransition() {
			return entityTransition;
		}

		// / <summary>No transition in progress</summary>
		public static final EntityTransition NONE = new EntityTransition(0x0000);
		// / <summary>Initialize (from NONE to BORN)</summary>
		public static final EntityTransition CREATE = new EntityTransition(0x0001);
		// / <summary>Initialize (from BORN to ALIVE_IDLE)</summary>
		public static final EntityTransition INIT = new EntityTransition(0x0002);
		// / <summary>Start from ALIVE_IDLE to ALIVE_BUSY)</summary>
		public static final EntityTransition ACTIVATE = new EntityTransition(
				0x0004);
		// / <summary>Pause (from ALIVE_BUSY to ALIVE_IDLE)</summary>
		public static final EntityTransition PAUSE = new EntityTransition(
				0x0008);
		// / <summary>Stop (from ALIVE to BORN)</summary>
		public static final EntityTransition DEACTIVATE = new EntityTransition(0x0016);
		// / <summary>CleanUp (from BORN to DEAD)</summary>
		public static final EntityTransition TERMINATE = new EntityTransition(0x0032);
	}

	private EntityTransition currentTransition;

	public EntityTransition getCurrentTransition() {
		return currentTransition;
	}

	protected void setCurrentTransition(EntityTransition t) {
		currentTransition = t;
	}

	// ----------------------------------------------------------
	// From/To| INIT | START | PAUSE | STOP | CLEAN
	// ----------------------------------------------------------
	// INIT | Error StartAt PauseAt StopAt Error
	// START | Error Warning PauseAt StopAt Error
	// PAUSE | Error StartAt Warning StopAt Error
	// STOP | Error Error Error Warning Error
	// CLEAN | Error Error Error Error Warning
	// ----------------------------------------------------------


	@Override
	public void setName(String value) {
		if (getStatus().equals(EntityStatus.NONE))
			super.setName(value);
	}

	@Override
	public String getFullName() {
		return super.getName();
	}
	
	IEntity parent;
	public IEntity getParent(){
		return parent;
	}
	public void setParent(IEntity e){
		parent =e;
		onParentSet();
	}
	
	abstract public void onParentSet();
	
	List<SimEntity> children;
	public List<SimEntity> getChildren(){
		return children;
	}

	Randomizer randomGenerator;
	@Override
	public Randomizer RandomGenerator() {
		return randomGenerator;
	}

	static List<CreationNotification> OnCreating; 
	@Override
	public List<CreationNotification> OnCreating() {
		return OnCreating;
	}

	static List<CreationNotification> OnCreated; 
	@Override
	public List<CreationNotification> OnCreated() {
		return OnCreated;
	}

	private List<InitializationNotification> OnInitializing;
	@Override
	public List<InitializationNotification> OnInitializing() {
		return OnInitializing;
	}

	private List<InitializationNotification> OnInitialized;
	@Override
	public List<InitializationNotification> OnInitialized() {
		return OnInitialized;
	}

	// / <summary>Event fired when the entity is beeing activated</summary>
	private List<ActivationNotification> OnActivating;

	@Override
	public List<ActivationNotification> OnActivating() {
		return OnActivating;
	}

	// / <summary>Event fired when the entity has been activated</summary>
	private List<ActivationNotification> OnActivated;

	@Override
	public List<ActivationNotification> OnActivated() {
		return OnActivated;

	}

	// / <summary>Event fired when the entity has completed its
	// mission</summary>
	private List<TerminatingNotification> OnTerminating;
	private List<TerminatingNotification> OnTerminated;

	@Override
	public List<TerminatingNotification> OnTerminating() {
		return OnTerminating;
	}

	// / <summary> Event fired when the entity is beeing desactivated</summary>
	private List<DeactivationNotification> OnDeactivating;

	@Override
	public List<DeactivationNotification> OnDeactivating() {
		return OnDeactivating;
	}

	// / <summary>Event fired when the entity has been desactivated</summary>
	private List<DeactivationNotification> OnDeactivated;

	@Override
	public List<DeactivationNotification> OnDeactivated() {
		return OnDeactivated;
	}

	@Override
	public boolean IsActive() {
		return status.equals(EntityStatus.ACTIVE);
	}

	@Override
	public boolean IsAlive() {
		return status.isALIVE();
	}
	
	protected final SimEntity createChild(SimEngine engine,Class<? extends SimEntity> type, String name, SimFeatures features) {
	 
		
		if (OnCreating.size() > 0)
			OnCreating.forEach((creationListener) -> creationListener
					.NotifyCreation(this,name,features));
		
		SimEntity e=null;
		
		try {
			Constructor<? extends SimEntity> c = type.getConstructor(SimEngine.class,String.class,SimFeatures.class);
			e=c.newInstance(engine,name,features);

			e.setCurrentTransition(EntityTransition.CREATE);
			e.setParent(this);
			children.add(e);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			Logger.Error(this, "createChild", MessagesEntity.InstanceCreationError, ex);
			return null;
		}
		
				
		e.setCurrentTransition(EntityTransition.NONE);
		e.setStatus(EntityStatus.BORN);
		for(CreationNotification creationListener:OnCreated)
			creationListener.NotifyCreation(e,name,features);
		
		
		return e;
	}

	protected abstract void InitializeSimEntity(SimInitParameters init);
	
	// / <summary>
	// / Activation of the object (pending events in past are fired)
	// / </summary>
	public final void Initialize(SimInitParameters init) {
		if(getStatus().equals(EntityStatus.IDLE)) return;
		
		if (!getStatus().equals(EntityStatus.BORN))
		{
			Logger.Error(this, "initialize", MessagesEntity.InitializeWhenNotBORN);
			return;
		}

		// Activation might be differed (if engine is not yet known)
		if (getEngine() == null)
			return;

		setCurrentTransition(EntityTransition.INIT);
		if (OnInitializing.size() > 0)
			OnInitializing.forEach((initializationListener) -> initializationListener
					.NotifyInitialization(this, init));
		initParameters = init;
		InitializeSimEntity(init);
		
		setCurrentTransition(EntityTransition.NONE);
		setStatus(EntityStatus.IDLE);
		if (OnInitialized.size() > 0)
			OnInitialized.forEach((initializationListener) -> initializationListener
					.NotifyInitialization(this, init));
	}

	// / <summary>
	// / Activation of the object (pending events in past are fired)
	// / </summary>
	@Override
	public final void activate() {
		if(getStatus().equals(EntityStatus.ACTIVE)) return;
		
		if (!getStatus().equals(EntityStatus.IDLE))
		{
			Logger.Error(this, "activate", MessagesEntity.ActivateWhenNotIDLE);
			return;
		}

		// Activation might be differed (if engine is not yet known)
		if (getEngine() == null)
			return;

		setCurrentTransition(EntityTransition.ACTIVATE);
		if (OnActivating.size() > 0)
			OnActivating.forEach((activationListener) -> activationListener
					.NotifyActivation(this, true));
		
		super.activate();
		
		setCurrentTransition(EntityTransition.NONE);
		setStatus(EntityStatus.ACTIVE);
		if (OnActivated.size() > 0)
			OnActivated.forEach((activationListener) -> activationListener
					.NotifyActivation(this, true));
	}
	
	protected abstract void AfterActivate(IEntity sender, boolean starting) ;

	// / <summary>
	// / Desactivation of the object
	// / </summary>
	@Override
	public final void deactivate() {
		
		if (getStatus().equals(EntityStatus.BORN)) return;

		if (!IsAlive())
		{
			IsAlive();
			Logger.Error(this, "deactivate", MessagesEntity.DeactivateWhenNotAlive);
			return;
		}

		// deactivation might be ignored
		if (getEngine() == null)
			return;

		setCurrentTransition(EntityTransition.DEACTIVATE);
		if (OnDeactivating.size() > 0)
			OnDeactivating.forEach((activationListener) -> activationListener
					.NotifyDeactivation(this, true));
		
		UnPostAllEvents();
		super.deactivate();

		setCurrentTransition(EntityTransition.NONE);
		setStatus(EntityStatus.BORN);
		if (OnDeactivated.size() > 0)
			OnDeactivated.forEach((activationListener) -> activationListener
					.NotifyDeactivation(this, true));
	}

	protected abstract void BeforeDeactivating(IEntity sender, boolean starting) ;
	protected  void BeforeTerminating(IEntity sender,boolean restart) {
		for(SimEntity e:children)
		{
			e.BeforeTerminating(this,restart);			
		}
		parent=null;
		children.clear();
	}
	
	protected void AfterTerminated(IEntity sender,boolean restart) {
	}

	@Override
	public final void terminate(boolean restart) {
		if (getStatus().equals(EntityStatus.DEAD)) return;
        if (!getStatus().equals(EntityStatus.BORN))
        {
			Logger.Error(this, "terminate", MessagesEntity.TerminateWhenNotBorn);
			return;
        }

		setCurrentTransition(EntityTransition.TERMINATE);
        if (OnTerminating.size()>0)
            OnTerminating.forEach((terminationListener)->terminationListener.NotifyTerminating(this,restart));

        super.terminate(restart);
		setCurrentTransition(EntityTransition.NONE);

        if (OnTerminated.size()>0)
            OnTerminated.forEach((terminationListener)->terminationListener.NotifyTerminating(this,restart));

		if(restart)
			setStatus(EntityStatus.BORN);
		else
			setStatus(EntityStatus.DEAD);
		
	}

}

