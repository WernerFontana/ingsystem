/**
* Classe LogicalDateTime.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.time;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;

import enstabretagne.base.messages.MessagesSimEngine;
import enstabretagne.base.utility.Logger;
import enstabretagne.base.utility.Settings;

public class LogicalDateTime implements Comparable<LogicalDateTime>{
	
	LocalDateTime logicalDate;
	public static final DateTimeFormatter logicalDateTimeFormatter;
	public static final DateTimeFormatter logicalTimeFormatter;
	public static final DateTimeFormatter logicalDateFormatter;
	static {
		logicalTimeFormatter=DateTimeFormatter.ISO_TIME;
		logicalDateFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		DateTimeFormatterBuilder dtfb = new DateTimeFormatterBuilder();
		dtfb.parseCaseInsensitive();
		dtfb.append(logicalDateFormatter);
		dtfb.appendLiteral(Settings.date_time_separator);		
		dtfb.append(logicalTimeFormatter);
		logicalDateTimeFormatter = dtfb.toFormatter();
	}
	
	public static final LogicalDateTime Zero= new LogicalDateTime(Settings.timeOrigin()); 
	public static final LogicalDateTime MaxValue = new LogicalDateTime(LocalDateTime.MAX);
	public static final LogicalDateTime MinValue = new LogicalDateTime(LocalDateTime.MIN);

	public static final LogicalDateTime UNDEFINED = new LogicalDateTime(false);
	
	boolean isDefined=true;
	
	private LogicalDateTime(boolean isDefined){
		logicalDate = null;
		isDefined=false;
	}
	
	public LogicalDateTime(String dateTimeFrenchFormat) {
		logicalDate =LocalDateTime.parse(dateTimeFrenchFormat,logicalDateTimeFormatter);
	}
	private LogicalDateTime(LocalDateTime ldt){
		logicalDate = ldt;
	}
	
	public LogicalDuration soustract(LogicalDateTime d){
		return LogicalDuration.soustract(this, d);
	}
	
	public void replaceBy(LogicalDateTime d){
		logicalDate = d.logicalDate;
	}

	public LogicalDateTime add(LogicalDuration offset) {
		if(!isDefined)
			Logger.Fatal(this, "add", MessagesSimEngine.LogicalDateIsNotDefined);
		return new LogicalDateTime(logicalDate.plus(offset.logicalDuration));
	}
	
	public static LogicalDateTime add(LogicalDateTime date,LogicalDuration dt) {
		if(!date.isDefined)
			Logger.Fatal(null, "add", MessagesSimEngine.LogicalDateIsNotDefined);

		return new LogicalDateTime(date.logicalDate.plus(dt.logicalDuration));
	}
	
	
	public LogicalDateTime truncateToYears() {
		return new LogicalDateTime(logicalDate.truncatedTo(ChronoUnit.YEARS));
	}

	public LogicalDateTime truncateToDays() {
		return new LogicalDateTime(logicalDate.truncatedTo(ChronoUnit.DAYS));
	}
	
	public LogicalDateTime truncateToHours() {
		return new LogicalDateTime(logicalDate.truncatedTo(ChronoUnit.HOURS));
	}

	public LogicalDateTime truncateToMinutes() {
		return new LogicalDateTime(logicalDate.truncatedTo(ChronoUnit.MINUTES));
	}
	
	public DayOfWeek getDayOfWeek() {
		return logicalDate.getDayOfWeek();
	}

	@Override
	public int compareTo(LogicalDateTime o) {		
		return logicalDate.compareTo(o.logicalDate);
	}
	@Override
	public String toString() {
		return logicalDateTimeFormatter.format(logicalDate);
	}

	public LogicalDateTime getCopy(){
		return new LogicalDateTime(logicalDate);
	}
}

