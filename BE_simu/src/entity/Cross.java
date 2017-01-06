package entity;

import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Cross extends Node implements ISimEntity {

	Car isOccupied[] = new Car[4];
	boolean rule;
	public final boolean STOP = true, FEU = false;
	
	public Cross (int id, BasicSimEngine engine, Environment env, boolean rule) {
		super(id, engine, env);
		isOccupied[0] = null;
		isOccupied[1] = null;
		isOccupied[2] = null;
		isOccupied[3] = null;
		this.rule = rule;
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
