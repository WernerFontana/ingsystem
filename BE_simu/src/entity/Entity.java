package entity;

import engine.impl.BasicSimEngine;

public abstract class Entity {

	private int ID;
	protected BasicSimEngine engine;
	protected Environment env;
	
	public Entity(int id, BasicSimEngine engine, Environment env){
		this.ID = id;
		this.engine = engine;
		this.env = env;
	}
	
	public int getID(){
		return ID;
	}
}
