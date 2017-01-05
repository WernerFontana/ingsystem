package engine.impl;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

import engine.ISimEngine;
import engine.ISimEntity;
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
		if(currentTime == null)
			System.out.println("no");
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
	
	public void log(ISimEntity entity, String message) {
		this.getLoggerHub().setSpace(this.getClass().getPackage().toString());
		this.getLoggerHub().setTime(this.getCurrentTime());
		this.getLoggerHub().setSource(entity.toString());
		this.getLoggerHub().setComment(message);
		this.getLoggerHub().log();
		this.getLoggerHub().clear();
	}

}
