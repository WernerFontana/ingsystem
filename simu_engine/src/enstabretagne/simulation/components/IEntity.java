/**
* Classe IEntity.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.components;

import java.util.List;

import enstabretagne.base.math.Randomizer;

public interface IEntity {

		/// <summary>Event fired when the entity is being created</summary>
	    List<CreationNotification> OnCreating();

		/// <summary>Event fired when the entity is created</summary>
	    List<CreationNotification> OnCreated();

	    /// <summary>Event fired when the entity is being initialized</summary>
	    List<InitializationNotification> OnInitializing();

		/// <summary>Event fired when the entity is initialized</summary>
	    List<InitializationNotification> OnInitialized();

	    List<ActivationNotification> OnActivating();

	    /// <summary>Event fired when the entity has been activated</summary>
	    List<ActivationNotification> OnActivated();

	    /// <summary>Event fired when the entity has completed its mission</summary>
	    List<TerminatingNotification> OnTerminating();

	    /// <summary>Event fired when the entity is beeing desactivated</summary>
	    List<DeactivationNotification> OnDeactivating();

	    /// <summary>Event fired when the entity has been desactivated</summary>
	    List<DeactivationNotification> OnDeactivated();

	    /// <summary>Is the entity alive or not ?</summary>
	    boolean IsAlive();

	    /// <summary>Is the entity active or not ?</summary>
	    boolean IsActive ();

	    /// <summary>Simple Name of the entity</summary>
	    String getName();

	    /// <summary>Full Name of the entity</summary>
	    String getFullName();

	    /// <summary>Simple Name of the entity</summary>
	    Randomizer RandomGenerator();
}

