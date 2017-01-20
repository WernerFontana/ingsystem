package entity;

import java.util.LinkedList;

import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Cross extends Node implements ISimEntity {

	private Car isOccupied[] = new Car[4];
	private final int rightIn,topIn,leftIn,bottomIn,rightOut,topOut,leftOut,bottomOut;

	private boolean rule;
	public final boolean STOP = true, FEU = false;

	public Cross (int id, BasicSimEngine engine, Environment env, boolean rule, int rightIn, int topIn,int leftIn, int bottomIn, int rightOut, int topOut,int leftOut, int bottomOut) {
		super(id, engine, env);
		this.rightIn=rightIn;
		this.topIn=topIn;
		this.leftIn=leftIn;
		this.bottomIn=bottomIn;
		this.rightOut=rightOut;
		this.topOut=topOut;
		this.leftOut=leftOut;
		this.bottomOut=bottomOut;
		isOccupied[0] = null;//top left
		isOccupied[1] = null;// top right
		isOccupied[2] = null;//bottom left
		isOccupied[3] = null;//bottom right
		this.rule = rule;
	}

	public LinkedList<Integer> getWay(Car c){
		LinkedList<Integer> l = new LinkedList<>();

		if(isAvailable()){
			if(c.getCurrentLine().getID() == topIn){
				if(!setIsOccupied(0, c))
					return null;
				if(c.getPath().get(c.getCurrentIndex()+1).getID() == rightOut){
					l.add(0);
					l.add(2);
					l.add(3);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == bottomOut){
					l.add(0);
					l.add(2);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == leftOut){
					l.add(0);
				}
				else{
					System.out.println("------------------------------------------------>Problème : "+topIn+"|"+c.getPath().get(c.getCurrentIndex()).getID());
				}
			}
			else if(c.getCurrentLine().getID() == rightIn){
				if(!setIsOccupied(1, c))
					return null;
				if(c.getPath().get(c.getCurrentIndex()+1).getID() == topOut){
					l.add(1);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == bottomOut){
					l.add(1);
					l.add(0);
					l.add(2);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == leftOut){
					l.add(1);
					l.add(0);
				}
				else{
					System.out.println("------------------------------------------------>Problème : "+rightIn+"|"+c.getPath().get(c.getCurrentIndex()).getID());
				}
			}
			else if(c.getCurrentLine().getID() == leftIn){
				if(!setIsOccupied(2, c))
					return null;
				if(c.getPath().get(c.getCurrentIndex()+1).getID() == topOut){
					l.add(2);
					l.add(3);
					l.add(1);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == bottomOut){
					l.add(2);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == rightOut){
					l.add(2);
					l.add(3);
				}
				else{
					System.out.println("------------------------------------------------>Problème : "+leftIn+"|"+c.getPath().get(c.getCurrentIndex()).getID());
				}
			}
			else if(c.getCurrentLine().getID() == bottomIn){
				if(!setIsOccupied(3, c))
					return null;
				if(c.getPath().get(c.getCurrentIndex()+1).getID() == topOut){
					l.add(3);
					l.add(1);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == leftOut){
					l.add(3);
					l.add(1);
					l.add(0);
				}
				else if(c.getPath().get(c.getCurrentIndex()+1).getID() == rightOut){
					l.add(3);
				}
				else{
					System.out.println("------------------------------------------------>Problème : "+bottomIn+"|"+c.getPath().get(c.getCurrentIndex()).getID());
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
				notifyObservers();
				return true;
			}
			else{
				//engine.log(this, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Collision");
				return false;
			}
		}
		else{
			isOccupied[i] = c;
			notifyObservers();
			return true;
		}
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


	public Car[] getIsOccupied() {
		return isOccupied;
	}

	public boolean getRule() {
		return this.rule;
	}

	public void setRule(boolean r) {
		this.rule = r;
	}


}
