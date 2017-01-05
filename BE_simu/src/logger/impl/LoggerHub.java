package logger.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import logger.ILogger;

public class LoggerHub implements ILogger {
	
	private Set<ILogger> loggers;
	
	public LoggerHub() {
		super();
		loggers = new HashSet<ILogger>();
	}
	
	public void addLogger(ILogger logger) {
		loggers.add(logger);
	}
	
	@Override
	public void setSpace(String space) {
		for (ILogger logger : loggers) logger.setSpace(space);
	}

	@Override
	public void setTime(LocalDateTime time) {
		for (ILogger logger : loggers) logger.setTime(time);
	}

	@Override
	public void setSource(String source) {
		for (ILogger logger : loggers) logger.setSource(source);
	}
	
	@Override
	public void setComment(String comment) {
		for (ILogger logger : loggers) logger.setComment(comment);	
	}

	@Override
	public void addValue(String key, String value) {
		for (ILogger logger : loggers) logger.addValue(key, value);		
	}

	@Override
	public void log() {
		for (ILogger logger : loggers) logger.log();		
	}

	@Override
	public void clear() {
		for (ILogger logger : loggers) logger.clear();
	}

	@Override
	public void terminate() {
		for (ILogger logger : loggers) logger.terminate();
	}

}
