package entity;

import java.time.Duration;
import java.util.LinkedList;

import container.CrossPathContainer;
import engine.ISimEngine;
import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Cross extends Node implements ISimEntity {

	private Car isOccupied[] = new Car[4];
	private final CrossEntry top,right,bottom,left;

	private LinkedList<Car> updateList = new LinkedList<>();

	public final int NONE = 0, STOP = 1, FEU = 2;

	public Cross (int id, BasicSimEngine engine, Environment env, CrossEntry top, CrossEntry right, CrossEntry bottom, CrossEntry left) {
		super(id, engine, env);
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
		isOccupied[0] = null;//top left
		isOccupied[1] = null;// top right
		isOccupied[2] = null;//bottom left
		isOccupied[3] = null;//bottom right
	}

	public void tryPassing(int i, Car c, int pathSize, int rightID)
	{
		if((pathSize==3) && !(getTypeFromID(rightID) == STOP))
		{
			if(this.isLineEmpty(rightID))
			{
				setIsOccupied(i,c);
			}
			else
			{
				this.addLineObserver(rightID, c);
			}
		}
		else
		{
			setIsOccupied(i,c);
		}
		
	}
	
	public boolean setIsOccupied(int i, Car c) {
		if(c != null){
			if(isOccupied[i] == null){
				for(int j = 0;j<isOccupied.length;j++){
					if(isOccupied[j] != null && isOccupied[j].equals(c))
						isOccupied[j] = null;
				}
				isOccupied[i] = c;
				updateList.add(c);
				engine.scheduleEventIn(this, Duration.ZERO, this::updateEvent);
				return true;
			}
			else{
				return false;
			}
		}
		else{
			Car car = isOccupied[i];
			isOccupied[i] = c;
			updateList.add(car);
			engine.scheduleEventIn(this, Duration.ZERO, this::updateEvent);
			return true;
		}
	}
	
	public void updateEvent(ISimEngine engine){
		setChanged();
		notifyObservers(updateList.getFirst());
		updateList.removeFirst();
	}

	private boolean isAvailable(){
		int c = 0;
		for(int i = 0;i<isOccupied.length;i++){
			if(isOccupied[i] == null)
				c++;
		}
		if(c > 1)
			return true;
		else
			return false;
	}
	
	public String toString(){
		String s = "Cross("+getID()+")";
		for(int i = 0;i<isOccupied.length;i++){
			s+=isOccupied[i]+"|";
		}
		return s;
	}

	public Car[] getIsOccupied() {
		return isOccupied;
	}
	

	public LinkedList<Integer> dealWithIt(Car c)
	{
		int pathType;
		LinkedList<Integer> path = calculate(c.getPath().get(c.getCurrentIndex()).getID(),c.getPath().get(c.getCurrentIndex()+1).getID());
		pathType = getTypeFromID(path.getFirst());
		switch(pathType)
		{
		case 0://rien : methode getWay on applique la priorité
			if(!setIsOccupied(path.getFirst(),c))
			{
				return null;
			}
			return path;
		case 1://stop : les stops sont par deux
			if(!stop(path.size(),path.getFirst(),c))
			{
				return null;
			}
			return path;
		case 2://feu : les intersections sont toujours pleines de feux
			//le cas ou l'on tourne a droite est géré au moment ou la voiture doit tourner et non au debut
			isOccupied[path.getFirst()]=c;
			return path;
		}
		return path;
	}
	
	
	private int getTypeFromID(int id) {
		switch(id)
		{
		case 0:
			return top.getType();
		case 1:
			return left.getType();
		case 2 :
			return bottom.getType();
		case 3: 
			return right.getType();
		}
		return 0;
	}

	public boolean isLineEmpty(int ID)
	{
		try
		{
			return env.getLine(convertCrossNode((ID+1)%3)).isOutFree();
		}
		catch(NullPointerException e)
		{
			return true;//si il n'y a pas de voie existante pas de priorité a laisser
		}
		
	}
	
	public void addLineObserver(int ID, Car c)
	{
		try
		{
			env.getLine(convertCrossNode((ID+1)%3)).addObserver(c);
		}
		catch(NullPointerException e)
		{
			//rien besoin de faire, il n'y a rien a observer
		}
		
	}
	public boolean stop(int pathSize,int ID, Car c)
	{
		switch(pathSize)
		{
		case 1:
			//on cede le passage a notre gauche 
			if(isLineEmpty((ID+3)%3) && isOccupied[ID]==null && isOccupied[(ID+3)%3]==null)
			{
				System.out.println("true");
				isOccupied[ID]=c;
				return true;
			}
			else
			{
				System.out.println("false");
				addLineObserver((ID+3)%3,c);
				return false;
			}
		default:
			//on cede le passage a gauche et droite (cas ou l'on va tout droit ou si on tourne a droite)
			if( isLineEmpty((ID+3)%3)  && isLineEmpty((ID+1)%3)  && isOccupied[ID]==null && isOccupied[(ID+3)%3]==null)
			{
				System.out.println("true");
				isOccupied[ID]=c;
				return true;
			}
			else
			{
				System.out.println("false");
				addLineObserver((ID+3)%3,c);
				addLineObserver((ID+1)%3,c);
				return false;
			}
		}
	}

	public void Light()
	{
		
	}
	
	public void Priority()
	{
		
	}
	public int convertNodeCross(int nodeID)
	{
		if(nodeID==top.in() || nodeID == top.out())
		{
			return 0;
		}
		else if(nodeID==left.in() || nodeID == left.out())
		{
			return 1;
		}		
		else if(nodeID==bottom.in() || nodeID == bottom.out())
		{
			return 2;
		}		
		else if(nodeID==right.in() || nodeID == right.out())
		{
			return 3;
		}
		return -1;
	}
	
	public int convertCrossNode(int ID)
	{
		switch(ID)
		{
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
	
	public LinkedList<Integer> calculate(int beginID, int endID)
	{
		int begin = convertNodeCross(beginID);
		int end = convertNodeCross(endID);
		LinkedList<Integer> crossPath=new LinkedList<>();
		int result =  begin - end;
		if(result <0)
		{
			result+=4;
		}
		switch(result)
		{
		case 1:
			crossPath.add(begin);
			break;
		case 2:
			crossPath.add(begin);
			crossPath.add((begin+1)%3);
			break;
		case 3:
			crossPath.add(begin);
			crossPath.add((begin+1)%3);
			crossPath.add((begin+2)%3);
			break;
		}
		return crossPath;
	}
}
