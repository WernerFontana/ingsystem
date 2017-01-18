package entity;

import java.time.Duration;
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
	private final int AVG_SPEED = 14;
	
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
		
		currentLine = path.getFirst();
		currentLine.addCar(this);
		
		if(!currentLine.getCars().isEmpty())
		{
			currentLine.getCars().getLast().addObserver(this);
			this.addObserver(this);			
		}
		engine.scheduleEventIn(this, Duration.ofSeconds(0), this::linePassing);
		
	}
	
	public String toString(){
		return "Car("+this.getID()+") : "+begin.getID()+"->"+end.getID();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		try
		{
			Car car = (Car) arg0;
		}
		catch(ClassCastException e)
		{
			e.printStackTrace();
			//TODO : trouver la bonne exception
		}
		
	}
	
	public void linePassing(ISimEngine engine)
	{
		engine.log(this, "parcours de la voie");
		int travelTime = (int) (currentLine.getLongueur()-currentLine.getCars().size()*longueur*distSecu*2)/AVG_SPEED;
		this.setChanged();
		this.notifyObservers();
		engine.scheduleEventIn(this, Duration.ofSeconds(travelTime), this::checkNode);
		
	}
	
	public void checkNode(ISimEngine engine)
	{
		switch(currentLine.getEndType())
		{
		case Line.FEU:
			engine.log(this, "feu===========================================================");
			break;
		case Line.STOP:
			engine.log(this, "stop==========================================================");
			break;
		case Line.END:
			engine.scheduleEventIn(this, Duration.ofSeconds(0), this::endTrip);
			break;
		case Line.FREE:
			engine.scheduleEventIn(this, Duration.ofSeconds(0), this::crossCrossing);
			break;
			default:
			engine.log(this, "situation inconnue");
			break;
		}
		
	}
	
	public void crossCrossing(ISimEngine engine)
	{
		engine.log(this, "Crossing the cross, from : "+currentLine.getID());
		Cross c= (Cross) currentLine.getEnd();
		if(c.getIsOccupied(currentLine, path.get(1)))
		{
		path.getFirst().getCars().remove(this);
		path.removeFirst();
		currentLine=path.getFirst();
		path.getFirst().getCars().add(this);
		engine.scheduleEventIn(this, Duration.ofSeconds(0), this::linePassing);
		}
		else
		{
			engine.scheduleEventIn(this, Duration.ofSeconds(10), this::crossCrossing);
		}

	}
	
	public void endTrip(ISimEngine engine)
	{
		engine.log(this, "End of travel at : "+currentLine.getID());
	}

}
