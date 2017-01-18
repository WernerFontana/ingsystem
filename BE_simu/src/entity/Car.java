package entity;

import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import engine.ISimEngine;
import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Car extends Entity implements ISimEntity,Observer {
	
	public double longueur;
	public double distSecu;
	private Frontier begin, end;
	private LinkedList<Line> path;
	private Line currentLine;
	private Iterator<Line> iteratorCurrent;
	public final int vitesse = 50;
	
	public Car(int id, BasicSimEngine engine, Environment env, double l, double d, Frontier begin, Frontier end){
		super(id,engine,env);
		this.longueur = l;
		this.distSecu = d;
		this.begin = begin;
		this.end = end;
		path = new LinkedList<>();
		engine.log(this, "generation Car");
		
		for(int pathID:env.getPathFinder().execute(begin.getID()-1, end.getID()-1))
		{
			path.add(env.getLine(pathID));
		}
		iteratorCurrent = path.iterator();
		currentLine = iteratorCurrent.next();
		engine.scheduleEventIn(this, Duration.ofSeconds(currentLine.getLongueur()/14), this::moveToEnd);
	}
	
	public String toString(){
		String s = "";
		s += "Car("+this.getID()+") : ";
		s += begin.getID();
		for(Line l : path){
			s += "->"+l.getID();
		}
		s += "->"+end.getID();
		return s;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void moveToEnd(ISimEngine engine)
	{
		engine.scheduleEventIn(this, Duration.ofSeconds(0), this::checkNode);
	}
	
	public void checkNode(ISimEngine engine)
	{
		if(currentLine.getEnd() instanceof Cross)
		{
			engine.scheduleEventIn(this, Duration.ofSeconds(0), this::crossCrossing);
		}
		else if(currentLine.getEnd() instanceof Frontier)
		{
			engine.scheduleEventIn(this, Duration.ofSeconds(0), this::endTrip);
		}
	}
	
	public void crossCrossing(ISimEngine engine)
	{
		
		engine.log(this, "Crossing the cross, from : "+currentLine.getID());
		Cross cross = (Cross) currentLine.getEnd();
		//cross.
		final int crossTop = cross.getTopLane();
		if(cross.getRule())
		{

		}
		else
		{
		
		}
		path.getFirst().getCars().remove(this);
		currentLine = iteratorCurrent.next();
		currentLine.getCars().add(this);
		engine.scheduleEventIn(this, Duration.ofSeconds(0), this::moveToEnd);
	}
	
	public void endTrip(ISimEngine engine)
	{
		engine.log(this, "End of travel at : "+currentLine.getID());
	}
	
	/*private Duration getDurationToGo(){
		int l = 0;
		for(Car c : currentLine.getCars()){
			l += c.longueur;
			l += c.distSecu;
		}
		
	}*/

}
