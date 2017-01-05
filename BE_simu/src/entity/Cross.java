package entity;

import java.util.HashMap;

import engine.ISimEntity;

public class Cross extends Node implements ISimEntity {

	Car isOccupied[] = new Car[4];
	boolean rule;
	public final boolean STOP = true, FEU = false;
	
	public Cross (boolean r, int i) {
		super(i);
		isOccupied[0] = null;
		isOccupied[1] = null;
		isOccupied[2] = null;
		isOccupied[3] = null;
		rule = r;
	}
	
	public Car getIsOccupied(int i) {
		return this.isOccupied[i];
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
	
}
