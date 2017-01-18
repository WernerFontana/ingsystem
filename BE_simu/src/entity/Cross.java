package entity;

import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Cross extends Node implements ISimEntity {

	Car isOccupied[] = new Car[4];
	private final int rightLane,topLane,leftLane,bottomLane;

	boolean rule;
	public final boolean STOP = true, FEU = false;
	
	public Cross (int id, BasicSimEngine engine, Environment env, boolean rule, int rightLane, int topLane,int leftLane, int bottomLane) {
		super(id, engine, env);
		this.rightLane=rightLane;
		this.topLane=topLane;
		this.leftLane=leftLane;
		this.bottomLane=bottomLane;
		isOccupied[0] = null;//top left
		isOccupied[1] = null;// top right
		isOccupied[2] = null;//bottom left
		isOccupied[3] = null;//bottom right
		this.rule = rule;
		
		
	}
	
	public Car getIsOccupied(int i) {
		return this.isOccupied[i];
	}
	
	public Boolean getIsOccupied(Line begin, Line end) {
		if(begin.getID()==topLane)
		{
			if(end.getID()==bottomLane)
			{
				return getIsOccupied(0)==null && getIsOccupied(2)==null; //On check si les voies ne sont pas déja
			}
			else if(end.getID()==rightLane)
			{
				return getIsOccupied(0)==null && getIsOccupied(2)==null && getIsOccupied(3)==null; 
			}
			else if(end.getID()==leftLane)
			{
				return getIsOccupied(0)==null; 
			}
		}
		else if(begin.getID()==bottomLane)
		{
			if(end.getID()==topLane)
			{
				return getIsOccupied(3)==null && getIsOccupied(1)==null; 
			}
			else if(end.getID()==rightLane)
			{
				return getIsOccupied(3)==null; 
			}
			else if(end.getID()==leftLane)
			{
				return getIsOccupied(3)==null && getIsOccupied(1)==null && getIsOccupied(0)==null; 
			}
		}
		else if(begin.getID()==rightLane)
		{
			if(end.getID()==topLane)
			{
				return getIsOccupied(1)==null; 
			}
			else if(end.getID()==bottomLane)
			{
				return getIsOccupied(1)==null&& getIsOccupied(0)==null && getIsOccupied(2)==null; 
			}
			else if(end.getID()==leftLane)
			{
				return getIsOccupied(0)==null && getIsOccupied(1)==null; 
			}
		}
		else if(begin.getID()==leftLane)
		{
			if(end.getID()==topLane)
			{
				return getIsOccupied(1)==null && getIsOccupied(2)==null && getIsOccupied(3)==null; 
			}
			else if(end.getID()==bottomLane)
			{
				return getIsOccupied(2)==null; 
			}
			else if(end.getID()==rightLane)
			{
				return getIsOccupied(2)==null && getIsOccupied(3)==null; 
			}
		}
		return false;
	}
	
	public void setIsOccupied(Line begin, Line end,Car c) {
		if(begin.getID()==topLane)
		{
			if(end.getID()==bottomLane)
			{
				 setIsOccupied(0,c);
				 setIsOccupied(2,c); //On check si les voies ne sont pas déja
			}
			else if(end.getID()==rightLane)
			{
				 setIsOccupied(0,c);
				 setIsOccupied(2,c);
				 setIsOccupied(0,c);
			}
			else if(end.getID()==leftLane)
			{
			}
		}
		else if(begin.getID()==bottomLane)
		{
			if(end.getID()==topLane)
			{
			}
			else if(end.getID()==rightLane)
			{
			}
			else if(end.getID()==leftLane)
			{
			}
		}
		else if(begin.getID()==rightLane)
		{
			if(end.getID()==topLane)
			{
			}
			else if(end.getID()==bottomLane)
			{
			}
			else if(end.getID()==leftLane)
			{
			}
		}
		else if(begin.getID()==leftLane)
		{
			if(end.getID()==topLane)
			{
			}
			else if(end.getID()==bottomLane)
			{
			}
			else if(end.getID()==rightLane)
			{
			}
		}
	}
	
	
	public void setIsOccupied(int i, Car c) {
		this.isOccupied[i] = c;
	}
	
	public boolean getRule() {
		return this.rule;
	}
	
	public void setRule(boolean r) {
		this.rule = r;
	}
	
	public final int getRightLane() {
		return rightLane;
	}

	public final int getTopLane() {
		return topLane;
	}

	public final int getLeftLane() {
		return leftLane;
	}

	public final int getBottomLane() {
		return bottomLane;
	}
	
}
