package logger;

import java.time.LocalDateTime;

public interface ILogger {

	public void setSpace(String space);
	public void setTime(LocalDateTime time);
	public void setSource(String source);
	public void setComment(String comment);
	public void addValue(String key, String value);
	
	public void log();
	public void clear();
	
	public void terminate();
	
}
