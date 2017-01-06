package entity;

import engine.impl.BasicSimEngine;
import java.util.Observable;

public abstract class Entity extends Observable{

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
