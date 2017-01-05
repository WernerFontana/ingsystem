package engine.impl;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import engine.AbstractSimEvent;
import engine.ISimEngine;
import engine.ISimEntity;

public class FunctionalSimEvent<E extends ISimEntity> extends AbstractSimEvent<E> {
	
	private final Consumer<ISimEngine> behavior;

	public FunctionalSimEvent(E owner, LocalDateTime time, Consumer<ISimEngine> behavior) {
		super(owner, time);
		this.behavior = behavior;
	}

	@Override
	public void process(ISimEngine engine) {
		behavior.accept(engine);
	}

}
