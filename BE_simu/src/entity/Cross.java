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
		isOccupied[0] = null;
		isOccupied[1] = null;
		isOccupied[2] = null;
		isOccupied[3] = null;
		this.rule = rule;
		
		
	}
	
	public Car getIsOccupied(int i) {
		return this.isOccupied[i];
	}
	
	public Car getIsOccupied(Node begin, Node end) {
		if(begin.getID()==topLane)
		{
			if(end.getID()==bottomLane)
			{
				
			}
			else if(end.getID()==rightLane)
			{

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
		return this.isOccupied[0];
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
