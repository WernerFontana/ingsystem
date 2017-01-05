/**
* Classe SimScenario.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.components;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import enstabretagne.base.messages.MessagesSimEngine;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.base.utility.IRecordable;
import enstabretagne.base.utility.Logger;
import enstabretagne.simulation.core.SimEngine;
import enstabretagne.simulation.core.SimEvent;
import enstabretagne.simulation.core.SimObjectActivationChangedEventHandler;

public class SimScenario extends SimEntity implements IScenario,IScenarioIdProvider,IRecordable{
	LogicalDateTime startDateTime;
	LogicalDateTime endDateTime;
	long seed;
	
	public long getSeed() {
		return seed;
	}


	SimEntity entityToFollow;
	public SimEntity getEntityToFollow() {
		return entityToFollow;
	}
	
	public LogicalDateTime getStartDateTime() {
		return startDateTime;
	}


	public LogicalDateTime getEndDateTime() {
		return endDateTime;
	}


	List<Action_Scenario> actions;
	private ScenarioId scenarioId;
	public SimScenario(SimEngine engine,ScenarioId id, SimFeatures features,LogicalDateTime start, LogicalDateTime end) {
		super(engine,id.getScenarioId(), features);
		scenarioId=id;
		setStatus(EntityStatus.BORN);
		startDateTime=start;
		endDateTime=end;
		actions = new ArrayList<SimScenario.Action_Scenario>();
		InterruptAt(end);
	}

	public void Add(Action_Scenario a) {
		actions.add(a);
	}
	public void Remove(Action_Scenario a) {
		actions.remove(a);
	}
	
	public abstract class Action_Scenario {
		
		LogicalDateTime d;
		
		public Action_Scenario(LogicalDateTime d) {
			this.d=d;
		}
		
		abstract void processAction();
	}
	
	public class Action_EntityCreation extends Action_Scenario {
		Class<? extends SimEntity> c;
		SimFeatures features;
		String name;
		SimInitParameters params;
		boolean toFollow;
		
		public Action_EntityCreation (Class<? extends SimEntity> c,String name,SimFeatures features,SimInitParameters params) {
			super(LogicalDateTime.UNDEFINED);
			this.c=c;
			this.features=features;
			this.params=params;
			this.name=name;
		}

		public Action_EntityCreation (Class<? extends SimEntity> c,String name,SimFeatures features,SimInitParameters params, boolean tofollow) {
			this(c,name,features,params);
			this.toFollow = tofollow;
		}
		@Override
		void processAction() {
				SimEntity e=createChild(getEngine(), c,name, features);
				if(toFollow) entityToFollow=e;
				actions.add(this);
				actions.add(new Action_EntityInitialization(e, params));
			
		}
	}
	
	public class Action_EntityInitialization extends Action_Scenario {

		SimEntity e;
		SimInitParameters params;
		
		public Action_EntityInitialization(SimEntity e,SimInitParameters params){
			super(LogicalDateTime.UNDEFINED);
			this.e=e;
			this.params=params;
			
		}
		@Override
		void processAction() {
			e.Initialize(params);
		}
		
	}

	@Override
	protected void InitializeSimEntity(SimInitParameters init) {
		seed = ((SimScenarioInit) init).getSeed();
		InterruptAt(this.endDateTime);
		getScenarioId().setRepliqueNumber(((SimScenarioInit) init).getRepliqueNum());

		
		List<Action_Scenario> actionsCopy = new ArrayList<Action_Scenario>();
		actionsCopy.addAll(actions);
		for(Action_Scenario a : actionsCopy) {
			if(Action_EntityCreation.class.isAssignableFrom(a.getClass())) {
				a.processAction();
				actions.remove(a);
			}
		}
		actionsCopy.clear();
		actionsCopy.addAll(actions);
		for(Action_Scenario a : actions) {
			if(Action_EntityInitialization.class.isAssignableFrom(a.getClass())) {
				a.processAction();
			}
		}
	}

	public void InterruptAt(LogicalDateTime date){
		Post(new EndSimulationEvent(),date);
	}

	public void InterruptIn(LogicalDuration duration){
		Post(new EndSimulationEvent(),duration);
	}

	@Override
	protected void AfterActivate(IEntity sender, boolean starting) {
		Logger.Data(this);
		for(IEntity e:children)
			((SimEntity) e).activate();
			
	}

	

	@Override
	public void onParentSet() {
		
	}

	@Override
	public ScenarioId getScenarioId() {
		return scenarioId;
	}

	@Override
	public String[] getTitles() {
		String[] titles={"Nom Scenario","Numéro réplique","Germe"};

		return titles;
	}

	@Override
	public String[] getRecords() {
		String[] rec;
			rec=new String[]{getScenarioId().getScenarioId(),Long.toString(getScenarioId().getRepliqueNumber()),Long.toString(getSeed())};

		return rec;
	}

	@Override
	public String getClassement() {
		return "Scenario";
	}

	@Override
	protected void BeforeDeactivating(IEntity sender, boolean starting) {
		for(IEntity e:children)
			((SimEntity) e).deactivate();		
		
	}

	@Override
	protected void AfterTerminated(IEntity sender, boolean restart) {
		super.AfterTerminated(sender, restart);
		if(restart)
		{
			reinitSimObject();
		}
	}	

}

