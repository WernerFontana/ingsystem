package entity;

import java.time.Duration;
import java.time.LocalDateTime;

import engine.ISimEngine;
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
		{long time =(convertLocalDateTimeToInt(engine.getCurrentTime())-startTime)%140;
			switch(id)
			{
			case 0:
				if(time<=30)
				{
					return GREEN;
				}
				else if(time>30 && time<=35)
				{
					return ORANGE;
				}
				else
				{	
					c.setAtLight(true);
					engine.scheduleEventIn(this, Duration.ofSeconds(140-time), c::checkNode);
					return RED;
				}
			case 1:
				if(time>35 && time<=65)
				{
					return GREEN;
				}
				else if(time>65 && time<=70)
				{
					return ORANGE;
				}
				else
				{
					c.setAtLight(true);
					engine.scheduleEventIn(this, Duration.ofSeconds(140-time+36), c::checkNode);
					return RED;
				}
			case 2:
				if(time>70 && time<=100)
				{
					return GREEN;
				}
				else if(time>100 && time<=105)
				{
					return ORANGE;
				}
				else
				{
					c.setAtLight(true);
					engine.scheduleEventIn(this, Duration.ofSeconds(140-time+71), c::checkNode);
					return RED;
				}
			case 3:
				if(time>105 && time<=135)
				{
					return GREEN;
				}
				else if(time>135 && time<=140)
				{
					return ORANGE;
				}
				else
				{
					c.setAtLight(true);
					engine.scheduleEventIn(this, Duration.ofSeconds(140-time+106), c::checkNode);
					return RED;
				}
			}
			System.err.println("id de feu inconnu : "+ id);
			System.exit(-1);
			return -1;
		}
	
	private void isGreen(ISimEngine engine)
	{
		this.setChanged();
		this.notifyObservers();
	}
}
