package engine;

import java.time.LocalDateTime;

public abstract class AbstractSimEvent<E extends ISimEntity> implements ISimEvent<E> {

	private final E owner;
	private final LocalDateTime time;
	
	public AbstractSimEvent(E owner, LocalDateTime time) {
		this.owner = owner;
		this.time = time;
	}
	
	@Override
	public int compareTo(ISimEvent<E> other) {
		return this.getScheduledTime().compareTo(other.getScheduledTime());
	}

	@Override
	public E getOwner() {
		return owner;
	}

	@Override
	public LocalDateTime getScheduledTime() {
		return time;
	}

	@Override
	public abstract void process(ISimEngine engine);

}
