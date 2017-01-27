package entity;

import java.time.Duration;
import java.util.LinkedList;
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
	private boolean atLight = false, isCrossing = false;

	private LinkedList<Integer> behavior;

	public final int vitesse = 50;
	private final int timeCross = 0;

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
				engine.scheduleEventIn(this, Duration.ofSeconds((long) (longueur + distSecu) / 14), this::checkNode);
			} else if (arg0 instanceof Line) {
					checkNode(engine);
			} else if (arg0 instanceof Light) {
				checkNode(engine);
			}
		}
	}

	private void addToNextLine() {
		currentLine.getCars().remove(this);
		currentIndex++;
		currentLine = path.get(currentIndex);
		currentLine.addCar(this);

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
			nextIterationOfCross(engine);
		} else if (currentLine.getEnd() instanceof Frontier) {
			endTrip();
		}
	}

	private void nextIterationOfCross(ISimEngine engine) {
		isCrossing = false;
		Cross c = ((Cross) currentLine.getEnd());
		Car isOccupied[] = c.getIsOccupied();
		if (behavior == null) {
			behavior = c.dealWithIt(this);

			// si le behavior est different de null, on est entrer dans la cross
			if (behavior != null) {
				currentLine.setOutFree(true);
				isCrossing = true;
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
		// la voiture est déjà dans la cross
		else {
			// si la voiture doit encore parcourir l'intersection
			if (!behavior.isEmpty() && behavior.size() > 1) {
				if (isOccupied[behavior.get(1)] == null) {
					c.deleteObserver(this);
					c.tryPassing(behavior.get(1), this, behaviorSize, (behavior.get(1) + 1) % 4);
					behavior.removeFirst();
					isCrossing = true;
					engine.scheduleEventIn(this, Duration.ofSeconds(timeCross), this::nextIterationOfCross);
				} else {
					c.addObserver(this);
				}
			}
			// si la voiture doit sortir
			else {
				c.deleteObserver(this);
				addToNextLine();
				c.setIsOccupied(behavior.get(0), null);
				behavior = null;
			}
		}
	}

	public void endTrip() {
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

}
