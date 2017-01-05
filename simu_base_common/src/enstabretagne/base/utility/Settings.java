/**
* Classe Settings.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Settings {

	public static final String controleurSuffixe = "_controler";
	
	public static final String date_time_separator = " ";

	public static final List<Class> classToFilter;
	static {
		classToFilter = new ArrayList<Class>();
		
	}
	
	public static char sep(){
		return ';';
	}
	public static boolean UsePortableRandomGenerator() {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean UseOneRandomGeneratorPerAgent() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public static boolean UseBinaryTreeForEventList() {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean IsEngineIntegrityChecked() {
		return false;
	}

	public static int DefaultSynchroOrder() {
		return 4;
	}
	
	public static double TickValue(){
		return 0.0001;
	}
	
	public static LocalDateTime timeOrigin(){
		return LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
	}
	
	public static boolean filterEngineLogs() {
		return true;
	}
}

