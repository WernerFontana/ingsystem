package entity;

import java.time.Duration;
import java.util.LinkedList;

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

	public LinkedList<Integer> getWay(Car c){
		LinkedList<Integer> l = new LinkedList<>();

		if(isAvailable()){
			if(c.getCurrentLine().getID() == top.in()){
				if(!setIsOccupied(0, c))
					return null;
				if(c.getPath().get(c.getCurrentIndex()+1).getID() == right.out()){
					l.add(0);
					l.add(2);
					l.add(3);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == bottom.out()){
					l.add(0);
					l.add(2);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == left.out()){
					l.add(0);
				}
				else{
					System.out.println("------------------------------------------------>Problème : "+top.in()+"|"+c.getPath().get(c.getCurrentIndex()).getID());
				}
			}
			else if(c.getCurrentLine().getID() == right.in()){
				if(!setIsOccupied(1, c))
					return null;
				if(c.getPath().get(c.getCurrentIndex()+1).getID() == top.out()){
					l.add(1);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == bottom.out()){
					l.add(1);
					l.add(0);
					l.add(2);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == left.out()){
					l.add(1);
					l.add(0);
				}
				else{
					System.out.println("------------------------------------------------>Problème : "+right.in()+"|"+c.getPath().get(c.getCurrentIndex()).getID());
				}
			}
			else if(c.getCurrentLine().getID() == left.in()){
				if(!setIsOccupied(2, c))
					return null;
				if(c.getPath().get(c.getCurrentIndex()+1).getID() == top.out()){
					l.add(2);
					l.add(3);
					l.add(1);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == bottom.out()){
					l.add(2);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == right.out()){
					l.add(2);
					l.add(3);
				}
				else{
					System.out.println("------------------------------------------------>Problème : "+left.in()+"|"+c.getPath().get(c.getCurrentIndex()).getID());
				}
			}
			else if(c.getCurrentLine().getID() == bottom.in()){
				if(!setIsOccupied(3, c))
					return null;
				if(c.getPath().get(c.getCurrentIndex()+1).getID() == top.out()){
					l.add(3);
					l.add(1);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == left.out()){
					l.add(3);
					l.add(1);
					l.add(0);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == right.out()){
					l.add(3);
				}
				else{
					System.out.println("------------------------------------------------>Problème : "+bottom.in()+"|"+c.getPath().get(c.getCurrentIndex()).getID());
				}
			}
			return l;
		}
		else{
			return null;
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

}
