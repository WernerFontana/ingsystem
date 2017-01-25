package engine;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Supplier;

import engine.impl.FunctionalSimEvent;
import logger.impl.LoggerHub;


public interface ISimEngine {
	
	public void initialize(LocalDateTime date, Duration s);
	public LocalDateTime getCurrentTime();
	public boolean scheduleEvent(ISimEvent<?> event);
	public boolean processNextEvent();
	public LoggerHub getLoggerHub();
	public void log(ISimEntity entity, String message);
	
	// Utilities
	
	public default <E extends ISimEntity> ISimEvent<E> scheduleEventAt(
			E owner,
			LocalDateTime time,
			Consumer<ISimEngine> behavior) {
		final ISimEvent<E> event = new FunctionalSimEvent<E>(owner, time, behavior);
		return scheduleEvent(event) ? event : null;
	}
	
	public default <E extends ISimEntity> ISimEvent<E> scheduleEventIn(
			E owner,
			Duration duration,
			Consumer<ISimEngine> behavior) {
		return scheduleEventAt(owner, getCurrentTime().plus(duration), behavior);
	}
	
	public default <E extends ISimEntity> ISimEvent<E> scheduleEventAt(
			E owner,
			Supplier<LocalDateTime> timeSupplier,
			Consumer<ISimEngine> behavior) {
		return scheduleEventAt(owner, timeSupplier.get(), behavior);
	}
	
	public default <E extends ISimEntity> ISimEvent<E> scheduleEventIn(
			E owner,
			Supplier<Duration> durationSupplier,
			Consumer<ISimEngine> behavior) {
		return scheduleEventIn(owner, durationSupplier.get(), behavior);
	}
	
	public default void processEventsUntil(LocalDateTime endTime) {
		//while (getCurrentTime().isBefore(endTime.plusHours(24)) && processNextEvent()) {}
		while (processNextEvent()) {}
	}
	
}
