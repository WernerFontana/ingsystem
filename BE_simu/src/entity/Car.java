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
	private int currentIndex;
	
	private LinkedList<Integer> behavior;

	private Iterator<Line> iteratorCurrent;
	public final int vitesse = 50;
	public final int timeCross = 5;

	public Car(int id, BasicSimEngine engine, Environment env, double l, double d, Frontier begin, Frontier end){
		super(id,engine,env);
		this.longueur = l;
		this.distSecu = d;
		this.begin = begin;
		this.end = end;
		path = new LinkedList<>();


		for(int pathID:env.getPathFinder().execute(begin.getID()-1, end.getID()-1))
		{
			path.add(env.getLine(pathID));
		}
		iteratorCurrent = path.iterator();
		currentLine = iteratorCurrent.next();
		currentIndex = 0;
		currentLine.addCar(this);
		engine.scheduleEventIn(this, Duration.ofSeconds(currentLine.getLongueur()/14), this::checkNode);

		engine.log(this, "generation Car");
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
		if(arg0 instanceof Cross)
			engine.scheduleEventIn(this, Duration.ZERO, this::nextIterationOfCross);

	}

	private void addToNextLine(){
		path.get(getCurrentIndex()).getCars().remove(this);
		currentLine = iteratorCurrent.next();
		currentLine.addCar(this);
		currentIndex++;
		engine.scheduleEventIn(this, Duration.ofSeconds(currentLine.getLongueur()/14), this::checkNode);
	}

	/*public void moveToEnd(ISimEngine engine)
	{
		engine.scheduleEventIn(this, Duration.ofSeconds(0), this::checkNode);
	}*/

	public void checkNode(ISimEngine engine)
	{
		if(currentLine.getEnd() instanceof Cross)
		{
			currentLine.getEnd().addObserver(this);
			engine.scheduleEventIn(this, Duration.ofSeconds(0), this::nextIterationOfCross);
		}
		else if(currentLine.getEnd() instanceof Frontier)
		{
			engine.scheduleEventIn(this, Duration.ofSeconds(0), this::endTrip);
		}
	}

	private void nextIterationOfCross(ISimEngine engine){
		Cross c = ((Cross)currentLine.getEnd());
		Car isOccupied[] = c.getIsOccupied();
		
		//Si le behavior est null, la voiture n'est pas encore dans la cross
		if(behavior == null){
			behavior = ((Cross)currentLine.getEnd()).getWay(this);
			//si le behavior est different de null, on est entrer dans la cross
			if(behavior != null){
				engine.scheduleEventIn(this, Duration.ofSeconds(timeCross), this::nextIterationOfCross);
			}
		}
		//la voiture est déjà dans la cross
		else{
			//si la voiture doit encore parcourir l'intersection
			if(!behavior.isEmpty() && behavior.size() > 1){
				if(isOccupied[behavior.get(1)] == null){
					c.setIsOccupied(behavior.get(1),this);
					behavior.removeFirst();
					engine.scheduleEventIn(this, Duration.ofSeconds(timeCross), this::nextIterationOfCross);
				}
			}
			//si la voiture doit sortir
			else{
				addToNextLine();
				c.setIsOccupied(behavior.get(0),null);
				c.deleteObserver(this);
				behavior = null;
			}
		}
	}

	public void endTrip(ISimEngine engine)
	{
		path.get(getCurrentIndex()).getCars().remove(this);
		engine.log(this, "End of travel at : "+currentLine.getID());
	}

	public Line getCurrentLine(){
		return currentLine;
	}

	public LinkedList<Line> getPath() {
		return path;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}



	/*private Duration getDurationToGo(){
		int l = 0;
		for(Car c : currentLine.getCars()){
			l += c.longueur;
			l += c.distSecu;
		}

	}*/

}
