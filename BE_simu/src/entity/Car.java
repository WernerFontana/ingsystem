package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import engine.ISimEngine;
import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Car extends Entity implements ISimEntity, Observer {

	public double longueur;
	public double distSecu;
	//La frontière de début et de fin de la voiture
	private Frontier begin, end;
	//La liste de ligne a parcourir pour atteindre la destination
	private LinkedList<Line> path;
	//La ligne sur laquelle se trouve la voiture
	private Line currentLine;
	//L'index courant, correspond au numéro de la ligne sur laquelle est la voiture dans la liste "path"
	private int currentIndex;
	private int behaviorSize;
	private LocalDateTime arrivalTime;
	private LocalDateTime debutDuTemps;
	private boolean atLight = false, isCrossing = false, atStop = false, isInCross = false,waitJamEnd=false;

	//Contient le chemin à effectuer dans une intersection, correspond aux numéro des cases a parcourir dans la Cross
	private LinkedList<Integer> behavior;

	//La vitesse des voitures
	public final int vitesse = 50;
	//Le temps que met une voiture pour traverser une case d'une intersection
	private final int timeCross = 3;

	public Car(int id, BasicSimEngine engine, Environment env, double l, double d, Frontier begin, Frontier end) {
		super(id, engine, env);
		this.longueur = l;
		this.distSecu = d;
		this.begin = begin;
		this.end = end;
		path = new LinkedList<>();
		//Remplissage du path avec les lignes issue de l'algo du plus court chemin
		for (int pathID : env.getPathFinder().execute(begin.getID() - 1, end.getID() - 1)) {
			path.add(env.getLine(pathID));
		}

		currentLine = path.getFirst();
		currentIndex = 0;
		currentLine.addCar(this);
		
		env.freqLine[currentLine.getID()-12]++;
		
		this.setArrivalTime(engine.getCurrentTime());
		check();

		env.carGen();
		// engine.log(this, "generation Car");
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		//Si la voiture n'est pas en train de bouger dans une intersection
		if (!isCrossing) {
			//Si l'update viens d'une Cross et que on ne s'update pas soi-même
			if (arg0 instanceof Cross) {
				if (arg1 instanceof Car) {
					if (((Car) arg1).getID() != this.getID()) {
						if (!atLight)
							checkNode(engine);
					}
				}
				//Update par une voiture, cela veut dire que la voiture peut accéder à l'intersection
			} else if (arg0 instanceof Car) {
				if (!atLight && !atStop) {
					arrivalTime = engine.getCurrentTime();
					engine.scheduleEventIn(this, Duration.ofSeconds((long) (longueur + distSecu) / 14),
							this::checkNode);
				}
				//Update par une ligne, utile pour les stop
			} else if (arg0 instanceof Line) {
				if (!atLight && atStop || waitJamEnd)
					checkNode(engine);
				//Update d'un feu, changement de feu vert
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

	/**
	 * Méthode de transfère d'une voiture d'une ligne à la suivante d'après le path,
	 * actualise toutes les variables en conséquence
	 */
	private void addToNextLine() {
		isInCross = false;
		currentLine.removeCar(this);
		currentIndex++;
		currentLine = path.get(currentIndex);
		currentLine.addCar(this);
		env.freqLine[currentLine.getID()-12]++;
		this.setArrivalTime(engine.getCurrentTime());
		check();
	}

	/**
	 * Méthode appelé en entrée sur une ligne
	 */
	public void check() {
		//Si la voiture est seule sur sa ligne
		if (currentLine.getCars().size() == 1 && currentLine.getCars().getFirst().getID() == this.getID()) {
			engine.scheduleEventIn(this, Duration.ofSeconds(currentLine.getLongueur() / 14), this::checkNode);
		} 
		//Si il y a une voiture dans l'intersection en bout de ligne, celle ci est toujours présente dans la liste de voitures de la ligne également
		//En réalité, la voiture est toute seule sur sa ligne
		else if (currentLine.getCars().size() == 2 && currentLine.getCars().getFirst().behavior != null) {
			engine.scheduleEventIn(this, Duration.ofSeconds(currentLine.getLongueur() / 14), this::checkNode);
		} 
		//Sinon on observe la voiture qui précède
		else {
			int l = currentLine.getCars().size();
			((Car) currentLine.getCars().get(l - 2)).addObserver(this);
		}
	}

	/**
	 * Aiguille la voiture si elle doit traverser une intersection en fin de ligne ou si elle arrive à destination
	 * @param engine
	 */
	public void checkNode(ISimEngine engine) {
		currentLine.setOutFree(false);
		if (currentLine.getEnd() instanceof Cross) {
			isInCross = true;
			nextIterationOfCross(engine);
		} else if (currentLine.getEnd() instanceof Frontier) {
			endTrip();
		}
	}

	/**
	 * Gère la traversée d'une intersection
	 * @param engine
	 */
	private void nextIterationOfCross(ISimEngine engine) {
		setIsCrossing(false);

		try {
			Cross c = ((Cross) currentLine.getEnd());
			//Récupération de l'état actuel de l'intersection
			Car isOccupied[] = c.getIsOccupied();
			if (!path.get(currentIndex + 1).isFull()) {
				waitJamEnd=false;
				//Si le behavior est null, on souhaite entrer dans l'intersection
				if (behavior == null) {
					//Récupération du chemin à suivre si il est possible d'entrer
					behavior = c.dealWithIt(this);
					// si le behavior est different de null, on est entrer dans la cross
					if (behavior != null) {
						env.freqCross[c.getID() - 8]++;
						if (currentLine.getCars().size() <= 1) {
							currentLine.setOutFree(true);
						}
						setIsCrossing(true);
						engine.scheduleEventIn(this, Duration.ofSeconds(timeCross), this::nextIterationOfCross);
						c.deleteObserver(this);
						//On previent la voiture suivante dans la ligne qu'on s'est engagé et qu'elle peut donc avancé
						setChanged();
						notifyObservers();
						deleteObservers();
						behaviorSize = behavior.size();
					} 
					//sinon on attend un mouvement dans l'intersection pour re-tester
					else {
						c.addObserver(this);
					}
				}
				// la voiture est déjà dans la cross
				else {
					// si la voiture doit encore parcourir l'intersection
					if (!behavior.isEmpty() && behavior.size() > 1) {
						if (isOccupied[behavior.get(1)] == null) {
							if(c.tryPassing(behavior.get(1), this)){
								c.deleteObserver(this);
								setIsCrossing(true);
								behavior.removeFirst();
								engine.scheduleEventIn(this, Duration.ofSeconds(timeCross), this::nextIterationOfCross);
							}							
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
			else
			{
				waitJamEnd =true;
				path.get(currentIndex+1).addObserver(this);
				c.addObserver(this);
			}
		} catch (ClassCastException e) {
		}
	}

	/**
	 * Méthode de fin de trajet pour la voiture,
	 * La voiture est supprimé, les observateurs notifiés, et les stats calculés
	 */
	public void endTrip() {

		env.tpsTrajet[0][begin.getID() - 1][end.getID() - 1] += Duration
				.between(this.debutDuTemps, engine.getCurrentTime()).getSeconds();
		env.tpsTrajet[1][begin.getID() - 1][end.getID() - 1]++;

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

	public void setIsCrossing(boolean b) {
		boolean tmp = isCrossing;
		isCrossing = b;
	}

}
