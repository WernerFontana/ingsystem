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
		{long time =(convertLocalDateTimeToInt(engine.getCurrentTime())-startTime)%160;
			switch(id)
			{
			case 0:
				if(time>5 && time<=35)
				{
					return GREEN;
				}
				else if(time>35 && time<=40)
				{
					return ORANGE;
				}
				else
				{	
					c.setAtLight(true);
					engine.scheduleEventIn(this, Duration.ofSeconds(160-time+6), c::checkNode);
					return RED;
				}
			case 1:
				if(time>45 && time<=75)
				{
					return GREEN;
				}
				else if(time>75 && time<=80)
				{
					return ORANGE;
				}
				else
				{
					c.setAtLight(true);
					engine.scheduleEventIn(this, Duration.ofSeconds(160-time+46), c::checkNode);
					return RED;
				}
			case 2:
				if(time>85 && time<=115)
				{
					return GREEN;
				}
				else if(time>115 && time<=120)
				{
					return ORANGE;
				}
				else
				{
					c.setAtLight(true);
					engine.scheduleEventIn(this, Duration.ofSeconds(160-time+86), c::checkNode);
					return RED;
				}
			case 3:
				if(time>125 && time<=155)
				{
					return GREEN;
				}
				else if(time>155 && time<=160)
				{
					return ORANGE;
				}
				else
				{
					c.setAtLight(true);
					engine.scheduleEventIn(this, Duration.ofSeconds(160-time+126), c::checkNode);
					return RED;
				}
			}
			System.err.println("id de feu inconnu : "+ id);
			System.exit(-1);
			return -1;
		}
}
