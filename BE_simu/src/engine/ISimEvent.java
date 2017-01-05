package engine;

import java.time.LocalDateTime;

public interface ISimEvent <E extends ISimEntity> extends Comparable<ISimEvent<E>> {
	
	public E getOwner();
	public LocalDateTime getScheduledTime();
	public void process(ISimEngine engine);

}
