package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;

import engine.ISimEngine;
import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Car extends Entity implements ISimEntity, Observer {

	public double longueur;
	public double distSecu;
	private Frontier begin, end;
	private LinkedList<Line> path;
	private Line currentLine;
	private int currentIndex;
	private int behaviorSize;
	private LocalDateTime arrivalTime;
	private LocalDateTime debutDuTemps;
	private boolean atLight = false, isCrossing = false, atStop = false, isInCross=false;

	private LinkedList<Integer> behavior;

	public final int vitesse = 50;
	private final int timeCross = 3;

	public Car(int id, BasicSimEngine engine, Environment env, double l, double d, Frontier begin, Frontier end) {
		super(id, engine, env);
		this.longueur = l;
		this.distSecu = d;
		this.begin = begin;
		this.end = end;
		path = new LinkedList<>();
		for (int pathID : env.getPathFinder().execute(begin.getID() - 1, end.getID() - 1)) {
			path.add(env.getLine(pathID));
		}

		currentLine = path.getFirst();
		currentIndex = 0;
		currentLine.addCar(this);
		//currentLine.notifyStop();
		this.setArrivalTime(engine.getCurrentTime());
		check();

		env.carGen();
		// engine.log(this, "generation Car");
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (!isCrossing) {
			if (arg0 instanceof Cross) {
				if (arg1 instanceof Car) {
					if (((Car) arg1).getID() != this.getID()) {
						if (!atLight)
							checkNode(engine);
					}
				}
			} else if (arg0 instanceof Car) {
				if (!atLight && !atStop) {
					arrivalTime=engine.getCurrentTime();
					engine.scheduleEventIn(this, Duration.ofSeconds((long) (longueur + distSecu) / 14),
							this::checkNode);
				}
			} else if (arg0 instanceof Line) {
				if (!atLight && atStop)
					checkNode(engine);
			} else if (arg0 instanceof Light) {
				if (atLight && !atStop)
					checkNode(engine);
			}
		}
	}

	public boolean isAtStop() {
		return atStop;
	}

	public void setAtStop(boolean atStop) {
		this.atStop = atStop;
	}

	private void addToNextLine() {
		isInCross=false;
		currentLine.removeCar(this);
		currentIndex++;
		currentLine = path.get(currentIndex);
		currentLine.addCar(this);
		this.setArrivalTime(engine.getCurrentTime());
		check();
	}

	public void check() {
		if (currentLine.getCars().size() == 1 && currentLine.getCars().getFirst().getID() == this.getID()) {
			engine.scheduleEventIn(this, Duration.ofSeconds(currentLine.getLongueur() / 14), this::checkNode);
		} else if (currentLine.getCars().size() == 2 && currentLine.getCars().getFirst().behavior != null) {
			engine.scheduleEventIn(this, Duration.ofSeconds(currentLine.getLongueur() / 14), this::checkNode);
		} else {
			int l = currentLine.getCars().size();
			((Car) currentLine.getCars().get(l - 2)).addObserver(this);
		}
	}

	public void checkNode(ISimEngine engine) {
		currentLine.setOutFree(false);
		if (currentLine.getEnd() instanceof Cross) {
			isInCross=true;
			nextIterationOfCross(engine);
		} else if (currentLine.getEnd() instanceof Frontier) {
			endTrip();
		}
	}

	private void nextIterationOfCross(ISimEngine engine) {
		setIsCrossing(false);

		try
		{
			Cross c = ((Cross) currentLine.getEnd());
			Car isOccupied[] = c.getIsOccupied();
			if (behavior == null) {
					behavior = c.dealWithIt(this);
					// si le behavior est different de null, on est entrer dans la cross
					if (behavior != null) {
						env.freqCross[c.getID()-8]++;
						if(currentLine.getCars().size()<=1)
						{
							currentLine.setOutFree(true);
						}
						setIsCrossing(true);
						engine.scheduleEventIn(this, Duration.ofSeconds(timeCross), this::nextIterationOfCross);
						c.deleteObserver(this);
						setChanged();
						notifyObservers();
						deleteObservers();
						behaviorSize = behavior.size();
					} else {
						c.addObserver(this);
					}
			}
			// la voiture est d�j� dans la cross
			else {
				// si la voiture doit encore parcourir l'intersection
				if (!behavior.isEmpty() && behavior.size() > 1) {
					if (isOccupied[behavior.get(1)] == null) {
						setIsCrossing(true);
						c.deleteObserver(this);
						c.tryPassing(behavior.get(1), this, behaviorSize);
						behavior.removeFirst();
						engine.scheduleEventIn(this, Duration.ofSeconds(timeCross), this::nextIterationOfCross);
					} else {
						c.addObserver(this);
					}
				}
				// si la voiture doit sortir
				else {
					//System.out.println("ok");
					c.deleteObserver(this);
					addToNextLine();
					c.setIsOccupied(behavior.get(0), null);
					behavior = null;
				}
			}	
		}
		catch(ClassCastException e)
		{

		}
	}

	public void endTrip() {
		
		env.tpsTrajet[0][begin.getID()-1][end.getID()-1] += Duration.between(this.debutDuTemps, engine.getCurrentTime()).getSeconds();
		env.tpsTrajet[1][begin.getID()-1][end.getID()-1]++;
		
		currentLine.getCars().remove(this);
		setChanged();
		notifyObservers();
		deleteObservers();
		env.carEnd();
		// engine.log(this, "End of travel at : "+currentLine.getID());
	}

	public String toString() {
		String s = "";
		s += "Car(" + this.getID() + ") : ";
		s += begin.getID();
		for (Line l : path) {
			s += "->" + l.getID();
		}
		s += "->" + end.getID();
		s += "   " + behavior;
		return s;
	}

	public Line getCurrentLine() {
		return currentLine;
	}

	public LinkedList<Line> getPath() {
		return path;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	/*
	 * private Duration getDurationToGo(){ int l = 0; for(Car c :
	 * currentLine.getCars()){ l += c.longueur; l += c.distSecu; }
	 * 
	 * }
	 */

	public void setAtLight(boolean b) {
		atLight = b;
	}

	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
		this.debutDuTemps = arrivalTime;
	}

	public void setIsCrossing(boolean b)
	{
		boolean tmp=isCrossing;
		isCrossing=b;
	}

}
