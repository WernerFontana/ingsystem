package engine.impl;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

import engine.ISimEngine;
import engine.ISimEvent;
import logger.impl.LoggerHub;

public class BasicSimEngine implements ISimEngine {
	
	private PriorityQueue<ISimEvent<?>> eventQueue;
	private LocalDateTime currentTime;
	private LoggerHub loggerHub;
	
	public BasicSimEngine() {
		eventQueue  = new PriorityQueue<>();
		loggerHub = new LoggerHub();
	}
	
	@Override
	public void initialize(LocalDateTime date) {
		eventQueue.clear();
		currentTime = date;
		
	}
	
	@Override
	public LocalDateTime getCurrentTime() {
		return currentTime;
	}
	
	@Override
	public boolean scheduleEvent(ISimEvent<?> event) {
		if (event.getScheduledTime().isBefore(currentTime)) {
			return false;
		}
		eventQueue.add(event);
		return true;
	}
	
	@Override
	public boolean processNextEvent() {
		final ISimEvent<?> nextEvent = eventQueue.poll();
		if (nextEvent == null)
			return false;
		currentTime = nextEvent.getScheduledTime();
		nextEvent.process(this);
		return true;
	}

	@Override
	public LoggerHub getLoggerHub() {
		return loggerHub;
	}

}
