package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Light extends Entity implements ISimEntity {	
	/*
	 * D'après l'énoncé seul 1 feu est allumé a la fois. J'ai choisi
	 * arbitrairement de commencer avec le feu du haut allumé puis d'alterner
	 * dans le sens des aiguilles d'une montre
	 */
	public static final int GREEN=0,ORANGE=1,RED=2;
	private long startTime;
	private int GREEN_DURATION=30, ORANGE_DURATION=5, PAUSE_DURATION=5, 
			ONE_TURN_DURATION = GREEN_DURATION + ORANGE_DURATION+PAUSE_DURATION,
			TOTAL_DURATION =4*ONE_TURN_DURATION;
	public Light(int id, BasicSimEngine engine, Environment env)
	{
		super(id,engine,env);
		startTime = convertLocalDateTimeToInt(engine.getCurrentTime());
	}		
	
	public long convertLocalDateTimeToInt(LocalDateTime t)
	{
		return (t.getDayOfWeek().getValue()-1)*604800+t.getHour()*3600+t.getMinute()*60+t.getSecond();
	}
	
	public int getLightByID(int id, Car c)
		{long time =(convertLocalDateTimeToInt(engine.getCurrentTime())-startTime)%TOTAL_DURATION;
			switch(id)
			{
			case 0:
				if(time>PAUSE_DURATION && time<=PAUSE_DURATION+GREEN_DURATION)
				{
					return GREEN;
				}
				else if(time>PAUSE_DURATION+GREEN_DURATION && time<=ONE_TURN_DURATION)
				{
					return ORANGE;
				}
				else
				{	
					c.setAtLight(true);
					engine.scheduleEventIn(this, Duration.ofSeconds(TOTAL_DURATION-time+PAUSE_DURATION+1), c::checkNode);
					return RED;
				}
			case 1:
				if(time>ONE_TURN_DURATION+PAUSE_DURATION && time<=ONE_TURN_DURATION+PAUSE_DURATION+GREEN_DURATION)
				{
					return GREEN;
				}
				else if(time>ONE_TURN_DURATION+PAUSE_DURATION+GREEN_DURATION && time<=2*ONE_TURN_DURATION)
				{
					return ORANGE;
				}
				else
				{
					c.setAtLight(true);
					engine.scheduleEventIn(this, Duration.ofSeconds(TOTAL_DURATION-time+ONE_TURN_DURATION+PAUSE_DURATION+1), c::checkNode);
					return RED;
				}
			case 2:
				if(time>2*ONE_TURN_DURATION+PAUSE_DURATION && time<=2*ONE_TURN_DURATION+PAUSE_DURATION+GREEN_DURATION)
				{
					return GREEN;
				}
				else if(time>2*ONE_TURN_DURATION+PAUSE_DURATION+GREEN_DURATION && time<=3*ONE_TURN_DURATION)
				{
					return ORANGE;
				}
				else
				{
					c.setAtLight(true);
					engine.scheduleEventIn(this, Duration.ofSeconds(TOTAL_DURATION-time+2*ONE_TURN_DURATION+PAUSE_DURATION+1), c::checkNode);
					return RED;
				}
			case 3:
				if(time>3*ONE_TURN_DURATION+PAUSE_DURATION && time<=3*ONE_TURN_DURATION+PAUSE_DURATION+GREEN_DURATION)
				{
					return GREEN;
				}
				else if(time>3*ONE_TURN_DURATION+PAUSE_DURATION+GREEN_DURATION && time<=4*ONE_TURN_DURATION)
				{
					return ORANGE;
				}
				else
				{
					c.setAtLight(true);
					engine.scheduleEventIn(this, Duration.ofSeconds(TOTAL_DURATION-time+3*ONE_TURN_DURATION+PAUSE_DURATION+1), c::checkNode);
					return RED;
				}
			}
			System.err.println("id de feu inconnu : "+ id);
			System.exit(-1);
			return -1;
		}
}
