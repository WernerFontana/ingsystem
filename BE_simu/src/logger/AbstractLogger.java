package logger;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractLogger implements ILogger {
	
	private String space;
	private LocalDateTime time;
	private String source;
	private String comment;
	private final List<String> keys;
	private final Map<String, String> values;
	
	public AbstractLogger() {
		keys = new LinkedList<>();
		values = new HashMap<>();
	}
	

	@Override
	public void setSpace(String space) {
		this.space = space;		
	}

	@Override
	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	
	@Override
	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public void addValue(String key, String value) {
		if (keys.contains(key) == false) {
			keys.add(key);
		}
		values.put(key,  value);
	}

	@Override
	public String toString() {
		StringBuilder log = new StringBuilder();
		if (space != null) log.append(space).append(" > ");
		if (time != null) log.append(time).append(" ");
		log.append("[");
		if (source != null) log.append(source);
		log.append("] ");
		if (comment != null) log.append(comment);
		for (String key : keys) {
			log
			.append(" ")
			.append(key)
			.append("=")
			.append(values.get(key));
		}
		return log.toString();
	}

	@Override
	public void log() {}
	
	@Override
	public void clear() {
		space = null;
		time = null;
		source = null;
		comment = null;
		keys.clear();
		values.clear();
	}

	@Override
	public abstract void terminate();

}
