/**
* Classe LogicalDuration.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.time;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import enstabretagne.base.messages.MessagesSimEngine;
import enstabretagne.base.utility.Logger;
import enstabretagne.base.utility.Settings;

public class LogicalDuration implements Comparable<LogicalDuration> {

	public static final DecimalFormat f = new DecimalFormat();
	static {
		f.setGroupingSize(0);
		f.setMaximumFractionDigits(9);
	}
	//807 voir comment faire mieux l'arrondi en double de LongMaxValue. En retirant 807 ceci permet de faire un arrondi double inférieur
	public static final LogicalDuration MAX_VALUE = LogicalDuration.ofSeconds(Long.MAX_VALUE-807);
	public static final LogicalDuration POSITIVE_INFINITY = new LogicalDuration((Duration)null);
	public static final LogicalDuration ZERO = new LogicalDuration(Duration.ZERO);
	public static final LogicalDuration TickValue = LogicalDuration.ofSeconds(Settings.TickValue());
	
	Duration logicalDuration;

	public int getMinutes(){
		return Math.round(logicalDuration.getSeconds()/60);
	}
	
	public static LogicalDuration Max(LogicalDuration d1,LogicalDuration d2)
	{
		if(d1.logicalDuration==null)//infini
			return d1;
		if(d2.logicalDuration==null)//infini
			return d2;
		
		if(d1.logicalDuration.compareTo(d2.logicalDuration)>=0)
			return d1;
		else
			return d2;
	}

	public static LogicalDuration soustract(LogicalDateTime d1,LogicalDateTime d2){
		if(!d1.isDefined)
			Logger.Fatal(d1, "add", MessagesSimEngine.LogicalDateIsNotDefined);
		if(!d2.isDefined)
			Logger.Fatal(d2, "add", MessagesSimEngine.LogicalDateIsNotDefined);

		return new LogicalDuration(Duration.between(d2.logicalDate,d1.logicalDate));
	}
	
	private LogicalDuration(Duration d) {
		logicalDuration = d;
	}
	
	public static LogicalDuration ofDay(long days){
		return new LogicalDuration(Duration.ofDays(days));
	}

	public static LogicalDuration ofHours(long hours){
		return new LogicalDuration(Duration.ofHours(hours));
	}

	public static LogicalDuration ofMillis(long millis){
		return new LogicalDuration(Duration.ofMillis(millis));
	}
	
	public static LogicalDuration ofMinutes(long minutes){
		return new LogicalDuration(Duration.ofMinutes(minutes));
	}

	public static LogicalDuration ofNanos(long nanos){
		return new LogicalDuration(Duration.ofNanos(nanos));
	}

	public static LogicalDuration ofSeconds(double seconds){
		
		if(seconds>Long.MAX_VALUE) 
			Logger.Fatal(null, "ofSeconds", MessagesSimEngine.DoubleValueTooHigh, Long.MAX_VALUE);
		return new LogicalDuration(Duration.parse("PT"+f.format(seconds)+"s"));
	}

	public double DoubleValue() {
		
		return logicalDuration.getSeconds()+logicalDuration.getNano()*1e-9;
	}

	
	public static LogicalDuration Quantify(LogicalDuration dt) {
	      return TickValue.mult(Math.round(dt.DoubleValue() / TickValue.DoubleValue()));
	}


	private LogicalDuration mult(long round) {
		if(logicalDuration==null) return LogicalDuration.POSITIVE_INFINITY;
		return new LogicalDuration(logicalDuration.multipliedBy(round));
	}



	@Override
	public int compareTo(LogicalDuration o) {
		
		if(logicalDuration==null&&o.logicalDuration==null)
			return 0;
		if(logicalDuration==null)
			return 1;
		if(o.logicalDuration==null)
			return -1;
		return logicalDuration.compareTo(o.logicalDuration);
	}

	public LogicalDuration add(LogicalDuration value) {
		if(logicalDuration==null || value.logicalDuration==null)
			return LogicalDuration.POSITIVE_INFINITY;
		return new LogicalDuration(logicalDuration.plus(value.logicalDuration));
	}

	public static LogicalDuration fromString(String dureeAsISO_Time) {
		LocalTime lt=LocalTime.parse(dureeAsISO_Time,DateTimeFormatter.ISO_TIME);
		return LogicalDuration.ofSeconds(lt.getHour()*3600+lt.getMinute()*60+lt.getSecond()+((double) lt.getNano())/1000000000);
	}
	@Override
	public String toString() {
		return logicalDuration.toString();
	}
}

