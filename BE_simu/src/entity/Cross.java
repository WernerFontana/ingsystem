package entity;

import java.time.Duration;
import java.util.LinkedList;

import container.CrossPathContainer;
import engine.ISimEngine;
import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Cross extends Node implements ISimEntity {

	private Car isOccupied[] = new Car[4];
	private final CrossEntry top, right, bottom, left;

	private LinkedList<Car> updateList = new LinkedList<>();
	public final int NONE = 0, STOP = 1, FEU = 2;
	private Light light;

	public Cross(int id, BasicSimEngine engine, Environment env, CrossEntry top, CrossEntry right, CrossEntry bottom,
			CrossEntry left) {
		super(id, engine, env);
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
		isOccupied[0] = null;// top left
		isOccupied[1] = null;// top right
		isOccupied[2] = null;// bottom left
		isOccupied[3] = null;// bottom right
		light = new Light(id, engine, env);
	}

	public void tryPassing(int i, Car c, int pathSize, int rightID) {
		setIsOccupied(i, c);
		/*if ((pathSize == 3) && (getTypeFromID(i) == NONE)) {
			if (this.isLineEmpty(i)) {
				setIsOccupied(i, c);
			} else {
				//setIsOccupied(i,null);
				this.addLineObserver(i, c);
			}
		} else {
			setIsOccupied(i, c);
		}*/
	}

	public boolean setIsOccupied(int i, Car c) {
		if (c != null) {
			if (isOccupied[i] == null) {
				for (int j = 0; j < isOccupied.length; j++) {
					if (isOccupied[j] != null && isOccupied[j].equals(c))
						isOccupied[j] = null;
				}
				isOccupied[i] = c;
				updateList.add(c);
				engine.scheduleEventIn(this, Duration.ZERO, this::updateEvent);
				return true;
			} else {
				return false;
			}
		} else {
			Car car = isOccupied[i];
			isOccupied[i] = c;
			updateList.add(car);
			engine.scheduleEventIn(this, Duration.ZERO, this::updateEvent);
			return true;
		}
	}

	public void updateEvent(ISimEngine engine) {
		setChanged();
		notifyObservers(updateList.getFirst());
		updateList.removeFirst();
	}

	private boolean isAvailable() {
		int c = 0;
		for (int i = 0; i < isOccupied.length; i++) {
			if (isOccupied[i] == null)
				c++;
		}
		if (c > 1)
			return true;
		else
			return false;
	}

	public String toString() {
		String s = "Cross(" + getID() + ")";
		for (int i = 0; i < isOccupied.length; i++) {
			s += isOccupied[i] + "|";
		}
		return s;
	}

	public Car[] getIsOccupied() {
		return isOccupied;
	}

	public void printlist(LinkedList<Integer> p, LinkedList<Integer> p2) {
		if (p != null) {
			for (int i = 0; i < p.size(); i++) {
				System.out.println(p.get(i) + " -> " + p2.get(i));
			}
		}

	}
public LinkedList<Integer> Go(Car c, LinkedList<Integer> p)
{
	if(setIsOccupied(p.getFirst(), c))
	{
		return p;
	}
	return null;
}

public void removeAllLineObserver(Car c)
{
	removeLineObserver(c,env.getLine(top.in()));
	removeLineObserver(c,env.getLine(top.out()));
	removeLineObserver(c,env.getLine(bottom.in()));
	removeLineObserver(c,env.getLine(bottom.out()));
	removeLineObserver(c,env.getLine(right.in()));
	removeLineObserver(c,env.getLine(right.out()));
	removeLineObserver(c,env.getLine(left.in()));
	removeLineObserver(c,env.getLine(left.out()));
	
}
public void removeLineObserver(Car c, Line l)
{
	try
	{
		l.deleteObserver(c);
	}
	catch(NullPointerException e)
	{
		//pas de ligne pas d'observeur a supprimer
	}
}
	public LinkedList<Integer> dealWithIt(Car c) {
		int pathType;
		int ID = convertNodeCross(c.getCurrentLine().getID());
		LinkedList<Integer> p= getBehavior(c);
		pathType =0;//getTypeFromID(ID);
		if (isAvailable()) {
		switch (pathType) {
		case 0:// rien : methode getWay on applique la priorité
			return Go(c,p);
		case 1:// stop : les stops sont par deux
			c.setWaitLine(true);
			if (!stop(p.size(), ID, c)) {
				return null;
			}
			removeAllLineObserver(c);
			return Go(c,p);
		case 2:// feu : les intersections sont toujours pleines de feux
			// le cas ou l'on tourne a droite est géré au moment ou la
			// voiture doit tourner et non au debut
		/*	if (light.getLightByID(ID) == Light.GREEN) {
				light.deleteObserver(c);
				return Go(c,p);
			}
			light.addObserver(c);*/
			return Go(c,p);
		}
		}
		return null;
	}

	private int getTypeFromID(int id) {
		switch (id) {
		case 0:
			return top.getType();
		case 1:
			return left.getType();
		case 2:
			return bottom.getType();
		case 3:
			return right.getType();
		}
		return 0;
	}

	public boolean isLineEmpty(int ID) {
		try {
			return env.getLine(convertCrossNode(ID)).isOutFree();
		} catch (NullPointerException e) {
			return true;// si il n'y a pas de voie existante pas de priorité a
			// laisser
		}

	}

	public void addLineObserver(int ID, Car c) {
		try {
			env.getLine(convertCrossNode(ID)).addObserver(c);
		} catch (NullPointerException e) {
			// rien besoin de faire, il n'y a rien a observer
		}

	}

	public boolean stop(int pathSize, int ID, Car c) {
		switch (pathSize) {
		case 1:
			// on cede le passage a notre gauche
			if (isLineEmpty((ID + 3) %4) && isOccupied[(ID + 3) %4]==null) {
				return true;
			} else {
				addLineObserver((ID + 3) %4, c);
				return false;
			}
		default:
			// on cede le passage a gauche et droite (cas ou l'on va tout droit
			// ou si on tourne a droite)
			if (isLineEmpty((ID + 3) % 4) && isLineEmpty((ID + 1) % 4) && isOccupied[(ID + 3) %4]==null) {
				return true;
			} else {
				addLineObserver((ID + 3) % 4, c);
				addLineObserver((ID + 1) % 4, c);
				return false;
			}
		}
	}

	public int convertNodeCross(int nodeID) {
		if (nodeID == top.in() || nodeID == top.out()) {
			return 0;
		} else if (nodeID == left.in() || nodeID == left.out()) {
			return 1;
		} else if (nodeID == bottom.in() || nodeID == bottom.out()) {
			return 2;
		} else if (nodeID == right.in() || nodeID == right.out()) {
			return 3;
		}
		return -1;
	}

	public int convertCrossNode(int ID) {
		switch (ID) {
		case 0:
			return top.in();
		case 1:
			return left.in();
		case 2:
			return bottom.in();
		case 3:
			return right.in();
		}
		return -1;
	}

	public LinkedList<Integer> getBehavior(Car c) {
		LinkedList<Integer> l = new LinkedList<>();
			if (c.getCurrentLine().getID() == top.in()) {
				if (c.getPath().get(c.getCurrentIndex() + 1).getID() == right.out()) {
					l.add(0);
					l.add(1);
					l.add(2);
				} else if (c.getPath().get(c.getCurrentIndex() + 1).getID() == bottom.out()) {
					l.add(0);
					l.add(1);
				} else if (c.getPath().get(c.getCurrentIndex() + 1).getID() == left.out()) {
					l.add(0);
				} else {
					System.out.println("------------------------------------------------>Problème : " + top.in() + "|"
							+ c.getPath().get(c.getCurrentIndex()).getID());
				}
			} else if (c.getCurrentLine().getID() == right.in()) {
				if (c.getPath().get(c.getCurrentIndex() + 1).getID() == top.out()) {
					l.add(3);
				} else if (c.getPath().get(c.getCurrentIndex() + 1).getID() == bottom.out()) {
					l.add(3);
					l.add(0);
					l.add(1);
				} else if (c.getPath().get(c.getCurrentIndex() + 1).getID() == left.out()) {
					l.add(3);
					l.add(0);
				} else {
					System.out.println("------------------------------------------------>Problème : " + right.in() + "|"
							+ c.getPath().get(c.getCurrentIndex()).getID());
				}
			} else if (c.getCurrentLine().getID() == left.in()) {
				if (c.getPath().get(c.getCurrentIndex() + 1).getID() == top.out()) {
					l.add(1);
					l.add(2);
					l.add(3);
				} else if (c.getPath().get(c.getCurrentIndex() + 1).getID() == bottom.out()) {
					l.add(1);
				} else if (c.getPath().get(c.getCurrentIndex() + 1).getID() == right.out()) {
					l.add(1);
					l.add(2);
				} else {
					System.out.println("------------------------------------------------>Problème : " + left.in() + "|"
							+ c.getPath().get(c.getCurrentIndex()).getID());
				}
			} else if (c.getCurrentLine().getID() == bottom.in()) {
				if (c.getPath().get(c.getCurrentIndex() + 1).getID() == top.out()) {
					l.add(2);
					l.add(3);
				} else if (c.getPath().get(c.getCurrentIndex() + 1).getID() == left.out()) {
					l.add(2);
					l.add(3);
					l.add(0);
				} else if (c.getPath().get(c.getCurrentIndex() + 1).getID() == right.out()) {
					l.add(2);
				} else {
					System.out.println("------------------------------------------------>Problème : " + bottom.in()
					+ "|" + c.getPath().get(c.getCurrentIndex()).getID());
				}
			}
			return l;
	}

}
